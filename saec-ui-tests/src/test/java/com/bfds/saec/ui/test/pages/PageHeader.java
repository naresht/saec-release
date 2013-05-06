package com.bfds.saec.ui.test.pages;


public class PageHeader {

	private static final String LINK_HOME = "home";
	private static final String LINK_LOGIN = "login";
	private static final String LINK_LOGOUT = "logout";

	private BasePageObject parent;
	

	public PageHeader(BasePageObject parent) {
		this.parent = parent;
	}

	public void logout() {
		gotoPage(LINK_LOGOUT);
	}

	public void gotoHome() {
		gotoPage(LINK_HOME);
	}

	public void gotoLogin() {
		gotoPage(LINK_LOGIN);
	}


	private void gotoPage(String linkText) {
		parent.clickLink(linkText);
	}
}
