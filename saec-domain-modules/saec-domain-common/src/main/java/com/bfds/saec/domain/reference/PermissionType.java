package com.bfds.saec.domain.reference;

public enum PermissionType {
	 VIEW("View"),
	 EDIT("Edit"),
	 DELETE("Delete")
	 ;
	
	private PermissionType(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}

}