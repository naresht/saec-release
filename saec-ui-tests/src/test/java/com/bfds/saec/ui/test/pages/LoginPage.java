package com.bfds.saec.ui.test.pages;


public class LoginPage extends BasePageObject {

	private static final String URL = "/app/login";

	private static final String FIELD_USERNAME = "j_username";
	private static final String FIELD_PASSWORD = "j_password";


	public LoginPage() {
		super(URL);
	}
	
	public HomePage login(String username, String password) {
		verifyIsCurrentPage();
		setField(FIELD_USERNAME, username);
		setField(FIELD_PASSWORD, password);
		submitForm();
        waitForPageLoaded();
        return new HomePage();
	}

}
