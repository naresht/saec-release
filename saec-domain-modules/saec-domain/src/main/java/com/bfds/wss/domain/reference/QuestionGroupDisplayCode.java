package com.bfds.wss.domain.reference;

/**
 * Indicates that a {@link QuestionGroup} is single-occuring or multi-occuring.
 */
public enum QuestionGroupDisplayCode {
	ROW("Row"), // Indicates single-occuring
	GROUP("Group") // Indicates multi-occuring
    ;
	
	private QuestionGroupDisplayCode(String iText) {
		this.name = iText ;
	}
	
	private final String name;
	
	public String toString() {
		return name;
	}

}
