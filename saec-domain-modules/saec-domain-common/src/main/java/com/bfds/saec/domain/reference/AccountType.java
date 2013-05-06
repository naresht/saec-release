package com.bfds.saec.domain.reference;

public enum AccountType {
	 INDIVIDUAL("Individual"),
	 CORPORATION("CORPORATION"),
	 INDIVIDUAL_AND_CORPORATION("Individual And Corporation")
	 ;
	
	private AccountType(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}

}