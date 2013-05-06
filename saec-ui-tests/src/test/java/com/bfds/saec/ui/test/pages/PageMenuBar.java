package com.bfds.saec.ui.test.pages;


public class PageMenuBar {

	@SuppressWarnings("unused")
	private static final String LINK_ACCOUNT_SEARCH = "Account Search";

	private BasePageObject parent;
	

	public PageMenuBar(BasePageObject parent) {
		this.parent = parent;
	}
	
	public AccountSearchPage gotoAccountSearch() {
		
		// NONE OF THESE WORK - Could be PrimeFaces although shouldn't be
//		parent.clickButtonXpath("//a[@href='claimant']");
//		parent.clickButtonXpath("//span[span='Account Search']"); /html/body/table/tbody/tr/td/div/div[3]/form/div/div/div/ul/li[3]/a/span/span[2]
//		parent.clickLinkHref("claimant");
		
		AccountSearchPage page = new AccountSearchPage();
		parent.getWebDriver().get(page.getPageUrl());
		parent.waitForPageLoaded();
		return page;
	}

}
