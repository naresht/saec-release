package com.bfds.saec.domain.reference;

public enum LetterStatus {
	IGO("IGO"), NIGO("NIGO"), RPO("RPO"), SUBMITTED("Submitted"), PENDING(
			"Pending"), NO_FURTHER_ACTION("NoFurtherAction"), LETTER_SENT(
			"Letter Sent");

	private LetterStatus(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
