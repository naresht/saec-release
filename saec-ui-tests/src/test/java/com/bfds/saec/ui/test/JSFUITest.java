package com.bfds.saec.ui.test;


/**
 * The operations here should match the wording you would use if giving someone a script
 */
public abstract class JSFUITest extends BaseSeleniumTest {

	
	/**
	 * Login using given credentials and expect a response within a the specified timeout
	 */
	protected void logIn(String userName, String passWord, String timeOut) {

		userUI.navigateToHome();
		// TODO: Uncomment and implement these
//		setTextBoxTo(Constants.LOGIN_FIELD_USERID,userName);
//		setTextBoxTo(Constants.LOGIN_FIELD_PASSWORD,passWord);
//		clickAndWait(Constants.LOGIN_BUTTON_LOGIN, timeOut);
		userUI.check().canSeeText("Welcome"); 
	}


}
