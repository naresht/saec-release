package com.bfds.saec.domain.reference;

public enum LetterType {
	CLAIM_FORM("Claim Form"), 
	CLAIM_FORM_CURE_LETTER("Claim Form Cure Letter"), 
	OPTOUT_FORM("Optout Form"), 
	OPTOUT_CURE_LETTER("Optout Form Cure Letter"), 
	GENERAL_CORRESPONDENCE("Correspondence"),
	GENERAL_CORRESPONDENCE_CURE_LETTER("Correspondence Cure Letter");
	
	private LetterType(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
