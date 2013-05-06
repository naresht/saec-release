package com.bfds.saec.ui.test;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.springframework.util.Assert;

import com.bfds.saec.ui.test.webdriver.Lifecycle;

/**
 * Rule to shutdown driver after good or bad test outcome
 * 
 * @author neale.upstone@opencredo.com
 */
final public class DriverQuitRule extends TestWatchman {

	private final Lifecycle driver;

	public DriverQuitRule(Lifecycle driver) {
		Assert.state(driver != null);
		this.driver = driver;
	}
	
	@Override
	public void finished(FrameworkMethod method) {
		driver.quit();
	}
}
