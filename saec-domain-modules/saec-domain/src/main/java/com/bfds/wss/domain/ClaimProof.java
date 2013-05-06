package com.bfds.wss.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.saec.domain.Claimant;
import com.google.common.collect.Lists;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit = "entityManagerFactory")
@Audited
public class ClaimProof implements java.io.Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimant_claim_fk", nullable = true)
	private ClaimantClaim claimantClaim;
	/**
	 * Description of the required proof.
	 */
	private String proofDescription;

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "name")
	@Column(name = "value", nullable = false)
	@CollectionTable(name = "claim_proof_type", joinColumns = @JoinColumn(name = "claim_proof_fk"))
	List<String> proofTypes = new ArrayList<String>();

	/**
	 * Indicates if the proof in question is required for the claim or not.
	 */
	@NotNull
	@Column(nullable = false)
	private boolean proofRequiredInd;

	@Column
	@Enumerated(EnumType.STRING)
	private ProofStatus status;

	@Column
	@Enumerated(EnumType.STRING)
	private Source source;

	/**
	 * The date the proof was received.
	 */
	private Date dateReceived;

	@Column
	private String comments;

	public static enum ProofStatus {
		IGO("IGO"), NIGO("NIGO"), PENDING("Pending"), IGO_OVERRIDE(
				"IGO Override");

		private final String name;

		private ProofStatus(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	public static enum Source {
        WEB("WEB"), MAIL("MAIL"), DATA_INTAKE("Data Intake");

        private final String name;

        private Source(String name){
                this.name = name;
        }

        public String toString() {
                return name;
        }
	}
	
	public String getSourceDesc(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.source == null ? "" : this.source);
		return sb.toString();
	}
	
	public String getProofStatusDesc(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.status == null ? "" : this.status);
		return sb.toString();
	}

	public static ClaimProof newClaimProof() {
		final ClaimProof newClaimProof = new ClaimProof();
		newClaimProof.setStatus(ProofStatus.PENDING);
		return newClaimProof;
	}
	
	public static List<ClaimProof>  getClaimProofs(Long claimantId) {
		Claimant claimant = Claimant.findClaimant(claimantId,
				Claimant.ASSOCIATION_ALL);
		ClaimantClaim claimantClaim = claimant.getSingleClaimantClaim();
		if(claimantClaim==null){
			return Lists.newArrayList();
		}
		
		Set<ClaimProof> claimProofs = new HashSet<ClaimProof>();
		for (ClaimProof proof : claimantClaim.getClaimProofs()) {
			claimProofs.add(proof);
		}
		if (claimantClaim.getClaimUserResponseGroups() != null) {
			for (ClaimUserResponseGroup grp : claimantClaim
					.getClaimUserResponseGroups()) {
				for (ClaimantUserResponses resp : grp.getClaimUserResponses()) {
					if (resp.getProof() != null)
						claimProofs.add(resp.getProof());
				}
				if (grp.getProof() != null)
					claimProofs.add(grp.getProof());
			}
		}
		if (ClaimantTransactions
				.getProofsOfClaimantTransactions(claimantClaim) != null) {
			List<ClaimProof> txnProofs = ClaimantTransactions
					.getProofsOfClaimantTransactions(claimantClaim);
			for (ClaimProof proof : txnProofs) {
				claimProofs.add(proof);
			}
		}
		return new ArrayList<ClaimProof>(claimProofs);
	}
	
	public static List<ClaimProof> getClaimProofs(Long claimantId,
			Collection<ProofStatus> proofStatus) {
		List<ClaimProof> retClaimProofs = Lists.newArrayList();
		List<ClaimProof> claimProofs = getClaimProofs(claimantId);
		for (ClaimProof claimProof : claimProofs) {
			if (proofStatus.contains(claimProof.getStatus())) {
				retClaimProofs.add(claimProof);
			}
		}
		return retClaimProofs;
	}
	
	public static ClaimProof getProofOfClaimantTransactions(Long id) {
		if (id == null) throw new IllegalArgumentException("The Transaction Id argument is required");
		EntityManager em = ClaimantTransactions.entityManager();
        TypedQuery<ClaimProof> q = em.createQuery("SELECT o.proof FROM ClaimantTransactions AS o WHERE o.id = :id ", ClaimProof.class);
        q.setParameter("id", id);
        return q.getSingleResult();
	}
	
	
	
}
