package com.bfds.saec.domain.reference;

public enum EventType {
	 CLASS_ACTION("Class Action"),
	 SEC_FAIR_FUNDS("SEC Fair Funds"),
	 CALL_CENTER("Call Center"),
	 OTHER("Other")
	 ;
	
	private EventType(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}

}