package com.bfds.saec.batch.domain;

import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;

public enum InRecordStatus {

	ERROR_SKIPPED("ERROR_SKIPPED"),
	/**
	 * When the Record cannot be matched with a record in the SAEC database. For example there is no {@link Payment} for the given check#.
	 */
	NOT_FOUND_IN_SAEC("NOT_FOUND_IN_SAEC"),
	/**
	 * When the Record cannot be processed as a result of a business constraint violation. For example an {@link Letter} cannot be RPO(ed) if it is already RPO(ed).
	 */
	CANNOT_PROCESS_SKIPPED("CANNOT_PROCESS_SKIPPED"); 
	
	private InRecordStatus(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
