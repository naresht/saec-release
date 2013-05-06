package com.bfds.saec.ui.test.pages;


public class PageFooter {


	private static final String LINK_CONTACT_US = "contact us";

	private static final String LINK_TERMS = "terms and conditions";
	private static final String LINK_PRIVACY = "privacy policy";
	private static final String LINK_ACCEPTABLE_USE = "acceptable use policy";

	private BasePageObject parent;


	public PageFooter(BasePageObject parent) {
		this.parent = parent;
	}

	public void gotoContactUs() {
		gotoPage(LINK_CONTACT_US);
	}

	public void gotoTermsAndConditions() {
		gotoPage(LINK_TERMS);
	}

	public void gotoPrivacyPolicy() {
		gotoPage(LINK_PRIVACY);
	}

	public void gotoAcceptableUsePolicy() {
		gotoPage(LINK_ACCEPTABLE_USE);
	}

	private void gotoPage(String linkText) {
		parent.clickLink(linkText);
	}
}
