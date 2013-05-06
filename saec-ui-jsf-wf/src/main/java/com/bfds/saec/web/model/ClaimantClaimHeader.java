package com.bfds.saec.web.model;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.reference.ClaimEntryMethod;

public class ClaimantClaimHeader implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long claimantId;
	private String referenceNo = "";
	private String claimIdentifier = "";
	private String registrationText;
	private String addressText = "";

	public ClaimantClaimHeader(final Long claimantId) {
		this.claimantId = claimantId;
		final Claimant claimant = Claimant.findClaimant(claimantId);
		this.referenceNo = claimant.getReferenceNo();
		final ClaimantClaim claimantClaim = claimant.getSingleClaimantClaim();
		if (claimantClaim != null) {
			this.claimIdentifier =claimantClaim.getEntryMethod().toString()+"-"+ claimantClaim.getClaimIdentifier();
		}else if(claimant.getClaimantClaimIds().size()>0){
			this.claimIdentifier =ClaimEntryMethod.SASEC.toString()+"-"+ claimant.getLatestClaimantClaimId().getClaimIdentifier();
		}
			
		this.registrationText = claimant.getRegistrationLinesAsString("<br/>");
		final ClaimantAddress address = claimant.getMailingAddress();
		if (address != null) {
			this.addressText = address.getAddressAsString("<br/>");
		}
	}

	public Long getClaimantId() {
		return claimantId;
	}

	public void setClaimantId(Long claimantId) {
		this.claimantId = claimantId;
	}

	public String getReferenceNo() {
		return this.referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getClaimIdentifier() {
		return this.claimIdentifier;
	}

	public void setClaimIdentifier(String claimIdentifier) {
		this.claimIdentifier = claimIdentifier;
	}

	public String getRegistrationText() {
		return this.registrationText;
	}

	public void setRegistrationText(String registrationText) {
		this.registrationText = registrationText;
	}

	public String getAddressText() {
		return this.addressText;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

}