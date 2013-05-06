package com.bfds.saec.domain.reference;

public enum RpoEligibleOption {
	 MAIL("MAIL"),
	 PAYMENT("PAYMENT"),
	 MAIL_AND_PAYMENT("Mail and Payment")
	 ;
	
	private RpoEligibleOption(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}

}