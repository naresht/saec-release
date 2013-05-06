package com.bfds.saec.domain.reference;

public enum PaymentStatus {
	CREATED ("Created"),
	VOIDED ("Voided"),
	STOP_REQUESTED ("Stop Requested"),
	STOPPED_CONFIRMED ("Stopped Confirmed"),
	STOP_REJECTED ("Stop Rejected"),
	REPLACE_APPROVED ("Replace Approved"),
	REPLACE_COMPLETED ("Replace Completed"),
	OUTSTANDING ("Outstanding"),
	CASHED ("Cashed"),
	REISSUE_COMPLETED ("Reissue Completed"),
	REISSUE_REQUESTED ("Reissue Requested"),
	REISSUE_APPROVED ("Reissue Approved"),
	REISSUE_REJECTED ("Reissue Rejected"),
	WIRE_REQUESTED ("Wire Requested"),
	WIRE_APPROVED ("Wire Approved"),
	WIRE_REJECTED ("Wire Rejected"),
	ROF_PARTIAL ("Rof Partial"),
	ROF_FULL ("Rof Full"),
	ROF_INTEREST ("Rof Interest"),
	REPLACE_REQUESTED ("Replace Requested"),
	REPLACE_REJECTED ("Replace Rejected"),
	RPO_FORWARDABLE("Void Forwardable"),
	RPO_NONFORWARDABLE("Void Non-Forward");
	
	private PaymentStatus(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}

}