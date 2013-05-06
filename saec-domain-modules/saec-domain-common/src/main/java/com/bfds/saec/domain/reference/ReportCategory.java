	package com.bfds.saec.domain.reference;

public enum ReportCategory {
	OUTREACH("OUTREACH"), 
	CHECK_DISTRIBUTION("Check Distribution"), 
	CHECK_STATUS("Check Status"), 
	RPO("RPO"), 
	CLASS_ACTION("Class Action"),
	ROF("ROF"),
	TAX("Tax"),
	MISC("Misc.")
	;
	
	private ReportCategory(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
