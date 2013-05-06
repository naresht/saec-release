package com.bfds.wss.domain.reference;

/**
 * The type of UI control used to enter a response to an {@link AdditionalQuestions}
 */
public enum ResponseDisplayCode {
		DATE("Date"), 
		SELECTION("Selection"),
		TEXT("Text");
		
		private ResponseDisplayCode(String iText) {
			this.name = iText ;
		}
		
		private final String name;
		
		public String toString() {
			return name;
		}
}
