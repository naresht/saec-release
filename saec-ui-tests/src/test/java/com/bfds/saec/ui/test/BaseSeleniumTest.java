package com.bfds.saec.ui.test;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfds.saec.ui.test.webdriver.UIFactory;

/**
 * Base class for test cases
 */
public class BaseSeleniumTest {

	protected static Logger log = LoggerFactory.getLogger(BaseSeleniumTest.class);


	@Rule
    public final TestLogger loggerRule = new TestLogger();
    
    @Rule
    public MethodRule screenshotRule;

	final protected UserUI userUI;

	final protected AdminUI adminUI;
	
	/**
	 * 
	 * @param singleSession - use one session for all tests in the suite
	 */
	protected BaseSeleniumTest() {
		
		userUI = UIFactory.getInstance().getUserUI();
		adminUI = UIFactory.getInstance().getAdminUI();
		
		screenshotRule = UIFactory.getInstance().getScreenshotCaptureRule();
	}

	
	@Before
	public void beforeTest() {
		resetData(); // want to make this beforeClass, or not at all, and instead allow tests to be independent where possible so they can run together
	}

	@AfterClass
	public static void closeAllInstances() {
		UIFactory.getInstance().closeAllInstances();
	}
	
	
	/**
	 * Use this to make an HTTP call to your test support webapp which can do some simple JdbcTemplate ops to clear tables containing
	 * data that got created during your test.
	 */
	protected void resetData() {
        UIFactory.getInstance().getCurrentWebDriver().manage().deleteAllCookies();
		// TODO: something like
//		adminUI.navigateTestHarness("/clearTestData");
	}

}
