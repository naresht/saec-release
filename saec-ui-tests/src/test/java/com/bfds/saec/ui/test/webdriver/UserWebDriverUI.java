package com.bfds.saec.ui.test.webdriver;

import org.openqa.selenium.WebDriver;

import com.bfds.saec.ui.test.Checker;
import com.bfds.saec.ui.test.Settings;
import com.bfds.saec.ui.test.UniqueDataService;
import com.bfds.saec.ui.test.UserUI;
import com.bfds.saec.ui.test.pages.AccountSearchPage;
import com.bfds.saec.ui.test.pages.GenericPage;
import com.bfds.saec.ui.test.pages.HomePage;
import com.bfds.saec.ui.test.pages.LoginPage;

class UserWebDriverUI extends WebDriverUI implements Checker, UserUI {

	private final UniqueDataService unique = new UniqueDataService();
	
	public UserWebDriverUI(WebDriver webDriver) {
		super(webDriver);
	}



	/**
	 * Navigate to this path within the target application
	 * 
	 * @param contextRelativeUrl
	 *            url relative to the root of this webapp (e.g. / for home)
	 */
	protected void navigate(String contextRelativeUrl) {
		log.info("Navigating to " + Settings.APPLICATION_URL + contextRelativeUrl);
		navigateToPage(Settings.APPLICATION_URL + contextRelativeUrl);
	}

	@Override
	public void navigateToHome() {
		navigate("/");
	}
	
	@Override
	public void login(String user) {
		login(unique.userNameFor(user), unique.passwordFor(user));
	}

	@Override
	public void login(String username, String password) {
//		navigateToHome();
		LoginPage loginPage = new LoginPage();
        loginPage.navigateTo();
//        loginPage.verifyIsCurrentPage();
		HomePage next = loginPage.login(username, password);
		next.verifyIsCurrentPage();
	}


	@Override
	public void logout() {
		new HomePage().logout();
	}
	
	@Override
	public Checker check() {
		return this;
	}

	@Override
	public void performAccountSearch(String... args) {
		AccountSearchPage page = new GenericPage().getMenuBar().gotoAccountSearch();
		page.doSearch(args);
		waitForPrimeFacesAjaxComplete();
	}

}
