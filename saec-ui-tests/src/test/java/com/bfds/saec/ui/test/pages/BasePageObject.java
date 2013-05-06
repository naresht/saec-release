package com.bfds.saec.ui.test.pages;

import com.bfds.saec.ui.test.Settings;
import com.bfds.saec.ui.test.webdriver.UIFactory;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public abstract class BasePageObject {

	private static final String BUTTON_XPATH_SUBMIT = "//input[@type='submit']";

	private final String pageURL;

	private final PageHeader header;
	private final PageFooter footer;
	private final PageMenuBar menuBar;

    protected BasePageObject(String pageURL) {
		this.pageURL = Settings.APPLICATION_URL + pageURL;
		header = new PageHeader(this);
		footer = new PageFooter(this);
		menuBar = new PageMenuBar(this);
	}

    protected WebDriver getWebDriver() {
    	return UIFactory.getInstance().getCurrentWebDriver();
    }

    protected String getPageUrl() {
    	return pageURL;
    }

	protected void setField(String fieldName, String text) {
		if (StringUtils.hasText(text)) {
			getWebDriver().findElement(By.name(fieldName)).sendKeys(text);
		}
	}

	protected void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(getWebDriver(), 30);
		try {
			wait.until(expectation);
		} catch (Throwable error) {
			fail("Timeout waiting for Page Load Request to complete.");
		}
	}

	/**
	 * Set options in a <select ...>
	 */
	protected void setSelection(String fieldName, String... optionsToSelect) {
		List<WebElement> elements = getWebDriver().findElement(By.name(fieldName)).findElements(By.tagName("option"));
		for (WebElement element : elements) {
			if (ArrayUtils.contains(optionsToSelect, element.getText())) {
				element.click();
			}
		}
	}

	protected void setRadio(String fieldName, String option) {
		if (StringUtils.hasText(option)) {
			List<WebElement> options = getWebDriver().findElements(By.xpath("//input[@name='" + fieldName + "']"));
			for (WebElement element : options) {
				if (element.getAttribute("value").equals(option)) {
					element.click();
				}
			}
		}
	}

	protected void setCheckbox(String fieldName, boolean checked) {
		WebElement checkbox = getWebDriver().findElement(By.name(fieldName));
		if (checkbox.isSelected() != checked) {
			checkbox.click();
		}
	}

	protected void uploadFile(String uploadFieldName, String filename) {
		if (StringUtils.hasText(filename)) {
			// TODO needs to be tested, probably need to get a File() object and do file.getAbsolutePath() ? 
			getWebDriver().findElement(By.name(uploadFieldName)).sendKeys(filename);
		}
	}

	/**
	 * Finds an element using linkText, and clicks it
	 */
	protected void clickLink(String linkText) {
		if (StringUtils.hasText(linkText)) {
			getWebDriver().findElement(By.linkText(linkText)).click();
			waitForPageLoaded();
		}
	}

	/**
	 * Finds an element with id id, and clicks it
	 */
	protected void clickElementWithId(String id) {
		if (StringUtils.hasText(id)) {
			getWebDriver().findElement(By.id(id)).click();
			waitForPageLoaded();
		}
	}

	/**
	 * Finds an element named name, and clicks it
	 */
	protected void clickLinkNamed(String name) {
		if (StringUtils.hasText(name)) {
			getWebDriver().findElement(By.name(name)).click();
			waitForPageLoaded();
		}
	}

	/**
	 * Finds a link with the specified href, clicks it, and waits for the next page to load
	 */
	protected void clickLinkHref(String href) {
		if (StringUtils.hasText(href)) {
			getWebDriver().findElement(By.xpath("//a[@href='" + href + "']")).click();
			waitForPageLoaded();
		}
	}

	/**
	 * Finds an element (such as a button or submit) using an input ID, then clicks it
	 */
	protected void clickButton(String buttonId) {
		getWebDriver().findElement(By.id(buttonId)).click();
		waitForPageLoaded();
	}

	/**
	 * Finds an element (such as a button or submit) using xpath, then clicks it
	 */
	protected void clickButtonXpath(String buttonXpath) {
		getWebDriver().findElement(By.xpath(buttonXpath)).click();
		waitForPageLoaded();
	}
	
	protected void submitForm() {
		clickButtonXpath(BUTTON_XPATH_SUBMIT);
	}

    public void navigateTo() {
        getWebDriver().get(getPageUrl());
        waitForPageLoaded();
    }

	public void verifyIsCurrentPage() {
		assertTrue("Unexpected page encountered. URL is " + getWebDriver().getCurrentUrl() +  " was expecting " + getPageUrl(),
                getWebDriver().getCurrentUrl().startsWith(getPageUrl()));
    }

	public PageHeader getPageHeader() {
		return header;
	}

	public PageMenuBar getMenuBar() {
		return menuBar;
	}

	public PageFooter getPageFooter() {
		return footer;
	}
}
