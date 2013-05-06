/**
 * 
 */
package com.bfds.saec.web.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.bfds.saec.claim.service.ClaimService;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantClaimId;

/**
 * @author dt73510
 * 
 */
public class ClaimDetailsViewModel implements Serializable {

	private static final long serialVersionUID = 1L;
	final static Logger log = LoggerFactory.getLogger(ClaimDetailsViewModel.class);	

	@Autowired
	private transient ClaimService claimService;
	
	private ClaimantClaim claimantClaim;
	
	private Claimant claimant;
	
	
	/**    
	 *    Prepares {@Link ClaimantClaim} Details based on below scenarios.
	 *     case 1: If the Claim was Submitted via Web Self Service it fills the Claimant Claim information. 
	 *     case 2: If the Claim Form was submitted by mail without the Claim Member registering in Web Self Service.
	 *     	       In this case, it creates a new Claim details. 
	 *     case 3: If the Class Member opted to Print a personalized Claim Form in WSS and mail it in, then it  
	 *             will look the most recent(largest controlNumber) claimIdentifier and controlNumber in {@link ClaimantClaimId}.        
	 * 
	 * @param claimantId
	 */
	public void prepareViewModel(Long claimantId) {
		claimant  = Claimant.findClaimant(claimantId, Claimant.ASSOCIATION_ALL);
		claimantClaim = claimService.getClaimantClaim(claimantId);
	}

	public void saveClaimDetails(Long claimantId) {
		claimService.saveClaimantClaim(claimantId, claimantClaim);
	}


	public String getAddressText() {
		ClaimantAddress address = claimant.getMailingAddress();
		if (address != null) {
			return address.getAddressAsString("<br/>");
		}
		return null;
	}

	public String getRegistrationText() {
		return this.claimant.getClaimantRegistration()
				.getRegistrationLinesAsString("<br/>");
	}

	public ClaimantClaimHeader getClaimantClaimHeader() {
		return new ClaimantClaimHeader(this.claimant.getId());
	}

	public String getClaimIdentifier() {
		if(StringUtils.hasText(this.claimantClaim.getClaimIdentifier())) {
			return  this.claimantClaim.getEntryMethod()+"-"+this.claimantClaim.getClaimIdentifier();
		}
		return null;
	}
	
	/**
	 * @return the claimantClaim
	 */
	public ClaimantClaim getClaimantClaim() {
		return claimantClaim;
	}

}
