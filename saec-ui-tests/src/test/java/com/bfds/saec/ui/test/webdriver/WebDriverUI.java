package com.bfds.saec.ui.test.webdriver;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfds.saec.ui.test.Checker;
import com.bfds.saec.ui.test.Settings;

/**
 * Base for WebDriver based UI drivers
 * 
 * @author neale.upstone@opencredo.com
 */
abstract class WebDriverUI implements Lifecycle {

	protected static Logger log = LoggerFactory.getLogger(WebDriverUI.class);

	protected volatile WebDriver webDriver;
	private volatile WebDriverBackedSelenium webDriverBackedSelenium;

	
	protected WebDriverUI(WebDriver webDriver) {
		this.webDriver  = webDriver;
		this.webDriverBackedSelenium = new WebDriverBackedSelenium(webDriver, Settings.APPLICATION_URL);
	}
	
	@Override
	final public void quit() {
		webDriverBackedSelenium = null;
		webDriver.quit();
		webDriver = null;
	}

	abstract public Checker check();

	public void canSeeText(String text) {
		Assert.assertTrue("Text [" + text
				+ "] was expected to be visible and is not",
				webDriverBackedSelenium.isTextPresent(text));
	}

	public void cannotSeeText(String text) {
		Assert.assertFalse("Text [" + text
				+ "] should not be visible, but it is",
				webDriverBackedSelenium.isTextPresent(text));
	}

	protected boolean isElementPresent(String id) {
		return webDriver.findElements(By.id(id)).size() == 1;
	}

	protected void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(webDriver, 30);
		try {
			wait.until(expectation);
		} catch (Throwable error) {
			fail("Timeout waiting for Page Load Request to complete.");
		}
	}
	

	/**
	 * Wait until the wait image is no longer visible
	 * 
	 * This works from Firebug console, so should work too.
	 * $x("//img[contains(@src,'ajaxloading.gif')]")
	 */
	protected void waitForPrimeFacesAjaxComplete() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement element = driver.findElement(By.xpath("//img[contains(@src,'ajaxloading.gif')]"));
				return !element.isDisplayed();
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(webDriver, 30);
		try {
			wait.until(expectation);
		} catch (Throwable error) {
			fail("Timeout waiting for JQuery Ajax request to complete.");
		}
	}

	
	/**
	 * BUG prob in PrimeFaces or our use of it: jQuery.active remains 1 after search has completed
	 */
	protected void waitForJQueryAjaxComplete() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				Object result = ((JavascriptExecutor) driver).executeScript(
						"return jQuery.active");
				System.out.println(result);
				return result.toString().equals("0");
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(webDriver, 30);
		try {
			wait.until(expectation);
		} catch (Throwable error) {
			fail("Timeout waiting for JQuery Ajax request to complete.");
		}
	}
	

	protected void maximizeWindow() {
		webDriverBackedSelenium.windowMaximize();
	}

	public void navigateToPage(String pageAddress) {
		webDriver.get(pageAddress);
	}

	public void clickLink(String linkText) {
		webDriver.findElement(By.linkText(linkText)).click();
	}
	
	public void setField(String fieldName, String text){
		webDriver.findElement(By.name(fieldName)).sendKeys(text);
	}
	
	public void clickElement(String name) {
		webDriver.findElement(By.name(name)).click();
	}
	
	public void submitForm() {
		webDriver.findElement(By.xpath("//input[@type='submit']")).click();
	}

}
