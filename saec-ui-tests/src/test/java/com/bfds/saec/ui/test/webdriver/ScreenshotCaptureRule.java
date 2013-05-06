package com.bfds.saec.ui.test.webdriver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfds.saec.ui.test.Settings;

/**
 * Capture screenshots on test failure and save them to 
 * System.getProperty("user.dir") + "/target/surefire-reports"
 * 
 * @author neale.upstone@opencredo.com
 */
final class ScreenshotCaptureRule extends TestWatchman {

	private static Logger log = LoggerFactory.getLogger(ScreenshotCaptureRule.class);
	
	private final String screenshotPath = System.getProperty("user.dir") + "/target/surefire-reports";
	private final WebDriver driver;


	public ScreenshotCaptureRule(WebDriver webDriver) {
		this.driver = webDriver;
	}
	
	public void failed(Throwable e, FrameworkMethod method) {
		String methodName = method.getMethod().getName();
		log.error("EXCEPTION in {}.{}", method.getMethod().getDeclaringClass().getName(), methodName);
		log.debug("Exception is:", e);
		if (Settings.TAKE_ERROR_SCREENSHOTS) {
			captureScreen(screenshotPath, methodName);
		}
	}

	public void captureScreen(String screenshotPath, String methodName) {
		captureEntirePageScreenshot(screenshotPath + "/" + methodName + "_"
				+ "top.png");

		// Depending on what test server's maximised window size is, this might
		// be necessary to capture what's off the bottom
		// if (isElementPresent("link=Home Page")) {
		// focus("link=Home Page");
		// keyPressNative(java.awt.event.KeyEvent.VK_END + "");
		// captureEntirePageScreenshot(screenshotPath + "/" + methodName + "_" +
		// "bottom.png");
		// }
	}

	public void captureEntirePageScreenshot(String fileName) {
		if (!(driver instanceof TakesScreenshot)){
			log.warn("No screenshot taken as driver {} doesn't support this capability", driver.getClass().getName());
			return;
		}
		File screenshotAs = null;
		// Sometimes the interfaces is supported but throws an UOE as underlying driver doesn't support it
		try {
			screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		} catch (UnsupportedOperationException e) {
			log.warn("No screenshot taken as driver {} didn't let us", driver.getClass().getName());
			return;
		}

		try {
			FileUtils.copyFile(screenshotAs, new File(fileName));
			log.info("Screenshot saved to {}", fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}