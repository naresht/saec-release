package com.bfds.saec.claim.service;

import com.bfds.saec.domain.Claimant;
import com.bfds.wss.domain.ClaimantClaim;

/**
 * 
 * UI Service for claim form processing.
 *
 */
public interface ClaimService {
	
	/**
	 * Get the active {@link ClaimantClaim} for the given claimantId. If the claimant does not have a ClaimantCalim then 
	 * 
	 * 1. If a claimIdentifier/Control no exists then a new {@link ClaimantClaim} with those values set will be returned.
	 * 2. If a claimIdentifier/Control does not exist then null is returned.
	 * 
	 * @param claimantId - The Id of the {@link Claimant}
	 * @return Existing or new {@link ClaimantClaim}
	 */
	ClaimantClaim getClaimantClaim(Long claimantId);
	
	/**
	 * Creates or updates claimantClaim in repository. If the claimantClaim has to be created( has null id) then 
	 * the claimantClaim is assigned to the {@link Claimant} represented by claimantId. 
	 * 
	 * @param claimantId - The id of the {@link Claimant}
	 * @param claimantClaim - {@link ClaimantClaim} that has to be created or updated.
	 */
	void saveClaimantClaim(Long claimantId, ClaimantClaim claimantClaim);

}
