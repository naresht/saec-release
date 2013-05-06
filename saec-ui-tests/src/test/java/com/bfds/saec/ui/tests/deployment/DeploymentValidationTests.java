package com.bfds.saec.ui.tests.deployment;

import org.junit.Test;

import com.bfds.saec.ui.test.BaseSeleniumTest;

public class DeploymentValidationTests extends BaseSeleniumTest {

	@Test
	public void homePageLooksGood() throws Exception {
        userUI.login("csr","csr");
		userUI.navigateToHome();
		userUI.check().canSeeText("Boston Financial");
		
	}

	// this can be expanded, but with pre-populated data is good enough to do some
	// load testing
	@Test
	public void emptyAccountSearchReturnsSomething() throws Exception {
		
		// GIVEN that I am logged in as an authorised user
		// AND there are at least 10 accounts in the system
		// adminUI.ensureAtLeast10Accounts();
		userUI.login("csr", "csr");
		
		// WHEN I navigate to the account search page
		// AND click Search
		userUI.performAccountSearch();

		// THEN an account list table will appear with at least 10 results
		userUI.check().canSeeText("(1 of");
		
	}

	@Test
	public void csrCanLogOnAndSeeFirstPage() throws Exception {
		
		//adminUI.createUser("csr", Role.CSR);
		
		userUI.navigateToHome();
		userUI.check().canSeeText("Don't ask for my password for two weeks");
		
		userUI.login("csr", "csr");
		
		userUI.check().canSeeText("Admin");

		userUI.logout();
		userUI.check().canSeeText("Don't ask for my password for two weeks");
	}
}
