package com.bfds.saec.batch.encorr.util;

public enum EncorrWorkType {

	TLETTER("TLETTER"),

	LETTER("LETTER");

	private EncorrWorkType(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
