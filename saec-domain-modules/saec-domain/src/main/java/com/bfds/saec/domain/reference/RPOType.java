package com.bfds.saec.domain.reference;

public enum RPOType {
	FORWARDABLE("Forwardable"), NONFORWARDABLE("NonForwardable");
	
	private RPOType(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
