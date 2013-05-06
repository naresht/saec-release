package com.bfds.saec.rip.domain;

public enum RipObjectStatus {
		/**
		 * The {@link RipObject} has been sent to AWD. The object can be safely deleted for the database.
		 */		
		SENT("SENT"), 
		/**
		 * The {@link RipObject} is current in the process of being sent to AWD.
		 */		
		SENDING("SENDING"), 		
		/**
		 * the {@link RipObject} in not sent to AWD.
		 */		
		NOT_SENT("NOT_SENT");

	
	private RipObjectStatus(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
