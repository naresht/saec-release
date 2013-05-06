package com.bfds.saec.ui.test.webdriver;

import org.openqa.selenium.WebDriver;

import com.bfds.saec.ui.test.AdminUI;
import com.bfds.saec.ui.test.Checker;
import com.bfds.saec.ui.test.Role;
import com.bfds.saec.ui.test.Settings;


class AdminWebDriverUI extends WebDriverUI implements AdminUI, Checker {

	public AdminWebDriverUI(WebDriver webDriver) {
		super(webDriver);
	}


	/* (non-Javadoc)
	 * @see com.bfds.saec.ui.test.webdriver.AdminUI#createUser(java.lang.String, com.bfds.saec.ui.test.Role)
	 */
	@Override
	public void createUser(String userName, Role csr) {
		// Create username using unique test session id, so that we can run many tests in parallel.
//		userName = userName + TestSession.getSessionId();
	}

	
	/* (non-Javadoc)
	 * @see com.bfds.saec.ui.test.webdriver.AdminUI#navigateToPage(java.lang.String)
	 */
	@Override
	public void navigateToPage(String pageAddress) {
		webDriver.get(pageAddress);
		maximizeWindow();
	}

	protected void navigateTestHarness(String contextRelativeUrl) {
		navigateToPage(Settings.TEST_HARNESS_URL + contextRelativeUrl);
	}

	@Override
	public Checker check() {
		return this;
	}
}
