package com.bfds.saec.ui.test.pages;

import org.springframework.util.Assert;

public class AccountSearchPage extends BasePageObject {

	private static final String URL = "/app/claimant";

	protected AccountSearchPage() {
		super(URL);
	}

	public void doSearch(String... args) {
		Assert.isTrue(args.length == 0, "TODO: Implement args -> set HTML form fields");
		
		// TODO: parse args of form ("field : value", field2 : value2") and populate those fields
		
		clickElementWithId("form:search");
	}
}
