package com.bfds.wss.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.service.DataFieldMaxValueIncrementerService;
import com.bfds.wss.domain.reference.ClaimEntryMethod;
import com.bfds.wss.domain.reference.ClaimStatus;

@RooJavaBean
@Audited
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"claim_identifier", "control_number"})})
public class ClaimantClaim implements java.io.Serializable {
	
	@Autowired
	private transient DataFieldMaxValueIncrementerService dataFieldMaxValueIncrementerService;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "claimant_fk", nullable = true)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Claimant claimant;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "claimantClaim")
	@NotAudited
	private Set<ClaimActivity> claimActivities = new HashSet<ClaimActivity>(0);
	
    @OneToMany(mappedBy = "claimantClaim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClaimUserResponseGroup> claimUserResponseGroups = new ArrayList<ClaimUserResponseGroup>();
    
    @OneToMany(mappedBy = "claimantClaim", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ClaimFileUploaded> claimFilesUploaded = new HashSet<ClaimFileUploaded>();

    @OneToMany(mappedBy = "claimantClaim", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ClaimantDistribution> distributions = new HashSet<ClaimantDistribution>();

    @OneToMany(mappedBy = "claimantClaim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClaimProof> claimProofs = new HashSet<ClaimProof>();


    /**
     * Unique identifier assigned to a claim.  Will most likely be a sequence with the prefix "Web" or "SASEC" to indicate the source of the submission.
     */
    @NotNull
    @Column(name = "claim_identifier", nullable = false)
	private String claimIdentifier ;

    /**
     * Number which provides additional claim identification. Typically means another claim form has been issued for the claim.
     */
    @NotNull
    @Column(name = "control_number", nullable = false)
    private int controlNumber ;

    /**
     * Date on which Claim was entered.
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(length=10, nullable=true)
	@DateTimeFormat(style = "S-")
	private Date dateFiled;

    /**
     * Status of Claim
     */
	@Column(length=50, nullable=true)
    @Enumerated(EnumType.STRING)
	private ClaimStatus status;

    /**
     * Method of submitting Claim
     */
	@Column(length=20, nullable=true)
    @Enumerated(EnumType.STRING)
	private ClaimEntryMethod entryMethod;
	
	@Column(length=200, nullable=true)
	private String electronicSignature;

    /**
     * The date on which {@link #harmAmount} was last calculated.
     */
    private Date harmCalculatedDate;

    private BigDecimal harmAmount;
    
    /**
     * Holds the {@link ClaimActivity#getComments()} of the last ClaimActivity of this ClaimantClaim entity.
     */
    @Transient
    private String comments;
    
    
    @PrePersist
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void generateReferenceNumber() {
		if (!StringUtils.hasText(this.claimIdentifier)) {
			this.claimIdentifier = getNextClaimtIdentifierNumber() ;
			this.controlNumber = 1 ;
		}
    }
    	
	private String getNextClaimtIdentifierNumber() {
		return dataFieldMaxValueIncrementerService.getNextClaimtIdentifierNumber() ;
	}
    
    public void addClaimActivity(final ClaimActivity activity) {
        this.getClaimActivities().add(activity);
        activity.setClaimantClaim(this);
    }
    
    public String getLatestClaimActivityComments() {
    	ClaimActivity activity = getLatestClaimActivity();
    	return activity != null ? activity.getComments() : null;
    } 
    
    /**
     * @return All the latest {@link ClaimActivity} of this {@link ClaimantClaim} 
     */
    public ClaimActivity getLatestClaimActivity() {
        
    	List<ClaimActivity> ret = new ArrayList<ClaimActivity>();
        
        for(ClaimActivity activity : this.getClaimActivities()) {
        	 ret.add(activity);
        }
        Collections.sort(ret, new ClaimActivityComparator());
        return ret.size() > 0 ?  ret.get(0) : null;
    }
    
	  /**
     * A Comparator used to sort {@link ClaimActivity} by activity date.
     *
     */
    public static class ClaimActivityComparator implements
            java.util.Comparator<ClaimActivity> {
        @Override
        public int compare(ClaimActivity o1, ClaimActivity o2) {
            return 0 - o1.getActivityDate().compareTo(o2.getActivityDate());
        }
    }
    public List<ClaimantDistribution> getClaimantDistributionsAsList() {
        final List<ClaimantDistribution> ret = new ArrayList<ClaimantDistribution>();
        ret.addAll(this.getDistributions());
        return ret;
    }

}
