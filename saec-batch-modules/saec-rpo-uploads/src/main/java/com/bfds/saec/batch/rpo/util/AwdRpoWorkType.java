package com.bfds.saec.batch.rpo.util;

public enum AwdRpoWorkType {

	NON_FORWARDABLE_LETTER("NONFORWARD"),

	NON_FORWARDABLE_CHECK("NOFORWDCHK"),

	FORWARDABLE_LETTER("FRWRDNOCHK"),

	FORWARDABLE_CHECK("FORWARDCHK");
	
	private AwdRpoWorkType(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
