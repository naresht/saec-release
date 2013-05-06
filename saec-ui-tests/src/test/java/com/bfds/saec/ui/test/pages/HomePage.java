package com.bfds.saec.ui.test.pages;


public class HomePage extends BasePageObject {

	private static final String URL = "/app/home";

	public HomePage() {
		super(URL);
	}

	public LoginPage gotoLoginPage() {
		verifyIsCurrentPage();
		getPageHeader().gotoLogin();
		waitForPageLoaded();
		return new LoginPage();
	}

	public void logout() {
		clickLinkNamed("logout");
		
		// The following works in Firefox but fails in HtmlUnit driver because HtmlUnit doesn't do the transform
		// clickLink("LOGOUT"); // CSS text-transform:uppercase results in LOGOUT being expected
		
	}
	
	public AccountSearchPage gotoAccountSearchPage() {
		getMenuBar().gotoAccountSearch();
		waitForPageLoaded();
		return new AccountSearchPage();
	}
}
