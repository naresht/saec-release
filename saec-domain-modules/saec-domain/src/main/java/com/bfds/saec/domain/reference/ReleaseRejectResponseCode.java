package com.bfds.saec.domain.reference;

public enum ReleaseRejectResponseCode {

	NAME_CORRECTION_NEEDED("Name correction needed"),
	ADDRESS_CORRECTION_NEEDED("Address correction needed"), 
	CHECK_REISSUED_IN_ERROR("Check reissued in error");

	private ReleaseRejectResponseCode(String rejectResponseCode) {
		this.rejectResponseCode = rejectResponseCode;
	}

	private final String rejectResponseCode;

	public String toString() {
		return rejectResponseCode;
	}
}
