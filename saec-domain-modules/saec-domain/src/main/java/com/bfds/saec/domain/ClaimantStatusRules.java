package com.bfds.saec.domain;

import java.io.Serializable;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantPosition;
import com.bfds.wss.domain.reference.ClaimStatus;
import com.google.common.collect.Lists;

public class ClaimantStatusRules implements IClaimantStatusRules, Serializable {

	private static final long serialVersionUID = 1L;
	private static final String IN_BALACNE = "In Balance";
	private Long claimantId;
	private Claimant claimant;
	private ClaimantClaim claimantClaim;

	public ClaimantStatusRules(Long claimantId) {
		this.claimantId = claimantId;
		claimant = Claimant.findClaimant(claimantId);
		claimantClaim = claimant.getSingleClaimantClaim();
	}

	public ClaimStatus getStatus() {

		if (isPending()) {
			return ClaimStatus.PENDING;
		} else if (isNotInGoodOrder()) {
			return ClaimStatus.NIGO;
		} else if (isOptOut()) {
			return ClaimStatus.OPT_OUT;
		} else if (isInGoodOrder()) {
			return ClaimStatus.IGO;
		}

		return null;
	}

	/**
	 * All Claim Proofs are flagged as "IGO" or "IGO OVERRIDE" AND All instances
	 * of "Proof Provided" are checked AND Daily Positions are in balance
	 * (Securities / Mutual Fund actions) AND There is no OptOut correspondence
	 * flagged as 'IGO'
	 * 
	 */
	public boolean isInGoodOrder() {
		final boolean isInGoodOrder = this.areAllProofsIGOOrOverride()
				&& this.areDailyPositionsInBalance()
				&& this.hasNoOptOutCorrespondenceWithStatusIGO();
		return isInGoodOrder;
	}

	/**
	 * (None of the Claim Proofs are flagged as "NIGO" and at least one Claim
	 * Proof is flagged as "PENDING", regardless of whether or not all instances
	 * of "Proof Provided" are checked OR All Claim Proofs are "IGO" or "IGO
	 * OVERRIDE" AND at least one instance of "Proof Provided" is unchecked.)
	 * AND There is no OptOut correspondence flagged as "IGO"
	 * 
	 */
	public boolean isPending() {
		boolean hasNoNIGOClaimProofs = ClaimProof.getClaimProofs(claimantId,
				Lists.newArrayList(ProofStatus.NIGO)).size() == 0;
		boolean hasAnyPendingClaimProofs = ClaimProof.getClaimProofs(
				claimantId, Lists.newArrayList(ProofStatus.PENDING)).size() > 0;
		boolean hasNoOptOutCorrespondenceWithStatusIGO = hasNoOptOutCorrespondenceWithStatusIGO();

		boolean isPending = ((hasNoNIGOClaimProofs && hasAnyPendingClaimProofs) || (areAllProofsIGOOrOverrideAndAtleastOnePending()))
				&& hasNoOptOutCorrespondenceWithStatusIGO;

		return isPending;
	}

	/**
	 * (At least one Claim Proof is flagged as "NIGO" OR Daily Position is out
	 * of balance (Securities / Mutual Fund actions)) AND There is no OptOut
	 * correspondence flagged as 'IGO'
	 */
	public boolean isNotInGoodOrder() {
		boolean hasAnyNIGOClaimProofs = ClaimProof.getClaimProofs(claimantId,Lists
				.newArrayList(ProofStatus.NIGO)).size() > 0;

		boolean areDailyPositionsOutBalance = areDailyPositionsOutBalance();

		boolean hasNoOptOutCorrespondenceWithStatusIGO = hasNoOptOutCorrespondenceWithStatusIGO();

		return (hasAnyNIGOClaimProofs || areDailyPositionsOutBalance)
				&& hasNoOptOutCorrespondenceWithStatusIGO;
	}

	/**
	 * There is OptOut correspondence flagged as 'IGO'
	 */
	public boolean isOptOut() {
		return Letter.countLetter(claimantId,"Opt Out", LetterStatus.IGO) > 0;
	}

	public boolean areAllProofsIGOOrOverride() {
		return ClaimProof.getClaimProofs(claimantId,Lists.newArrayList(ProofStatus.IGO,
				ProofStatus.IGO_OVERRIDE)).size() == ClaimProof.getClaimProofs(
				claimantId).size();
	}
	
	public boolean areAllProofsIGOOrOverrideAndAtleastOnePending() {
		int igo_or_igo_override = ClaimProof.getClaimProofs(
				claimantId,
				Lists.newArrayList(ProofStatus.IGO, ProofStatus.IGO_OVERRIDE)).size();

		int pending = ClaimProof.getClaimProofs(claimantId,
				Lists.newArrayList(ProofStatus.PENDING)).size();
		if (pending > 0) {
			return igo_or_igo_override + pending == ClaimProof.getClaimProofs(
					claimantId).size();
		}
		return false;
	}
	

	public boolean areDailyPositionsInBalance() {
		final Long count = ClaimantPosition.countDailyPositionsWhereStatus(
				claimantClaim,
				Lists.newArrayList(IN_BALACNE));
		return count > 0;
	}

	public boolean areDailyPositionsOutBalance() {
		final Long count = ClaimantPosition.countDailyPositionsWhereStatus(claimantClaim,
				Lists.newArrayList(ClaimantPosition.STATUS_OUT_OF_BALANCE));
		return count > 0;
	}

	public boolean hasNoOptOutCorrespondenceWithStatusIGO() {
		return Letter.countLetter(claimantId,"Opt Out", LetterStatus.IGO) == 0;
	}

}
