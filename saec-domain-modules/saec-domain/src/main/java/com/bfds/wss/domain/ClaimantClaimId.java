package com.bfds.wss.domain;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantReferenceNumber;
import com.bfds.saec.service.DataFieldMaxValueIncrementerService;

/**
 * 
 * This entity holds the claimIdentifier and controlNumber of a yet to be created {@link ClaimantClaim} of a {@link Claimant}. Once a {@link ClaimantClaim} is 
 * created for a {@link Claimant} this entity is of no further use. 
 * 
 *  The case where this is possible is when a Class Member prints the Claim form without submitting it online. When a claim form is printed 
 *  a bar code with a newly generated claimIdentifier and controlNumber pair is printed on the claim form. If a Class Member prints the form multiple times then
 *  multiple ClaimantClaimId entities for the  {@link Claimant} are created. 
 *  
 *  At the time of creating the {@link ClaimantClaim} the most recent(largest controlNumber) claimIdentifier and controlNumber are used. 
 *
 */
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit = "entityManagerFactory")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"claim_identifier", "control_number"})})
public class ClaimantClaimId implements java.io.Serializable {
	
	@Autowired
	private transient DataFieldMaxValueIncrementerService dataFieldMaxValueIncrementerService;
	
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
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "claimant_fk", nullable = true)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Claimant claimant;
	
    @PrePersist
    public void generateReferenceNumber() {
    	if (this.claimIdentifier == null) {
    		if (!StringUtils.hasText(this.claimIdentifier)) {
    			this.claimIdentifier = getNextClaimtIdentifierNumber() ;
    			this.controlNumber = 1 ;
    		}
    	}
    }
    
	@Transactional(propagation=Propagation.SUPPORTS)
	public String getNextClaimtIdentifierNumber() {
		return dataFieldMaxValueIncrementerService.getNextClaimtIdentifierNumber() ;
	}


}
