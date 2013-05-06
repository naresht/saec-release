package com.bfds.saec.ui.test.webdriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.rules.MethodRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ClasspathExtension;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfds.saec.ui.test.AdminUI;
import com.bfds.saec.ui.test.Settings;
import com.bfds.saec.ui.test.UserUI;

/**
 * Factory to create application driver instances (i.e. {@link UserUI} and {@link AdminUI})
 * appropriately configured according to settings provided in {@link Settings}
 * 
 * @author neale.upstone@opencredo.com
 */
public class UIFactory {
	
	private static final Logger log = LoggerFactory.getLogger(UIFactory.class);
	
	private static final UIFactory instance = new UIFactory();
	
	/**
	 * To avoid creating unnecessary UI driver instances, we cache them per thread and clean up 
	 * after class.<p>
	 * TODO: If we get to running multiple suites in parallel, then this collection will be being shared
	 * by multiple subclasses having @AfterClass called.  We'll need to remove UserUI entries from the
	 * map when that happens, or look at providing a pool.
	 */
	private Map<Thread,WebDriver> webDriverPerThread = new HashMap<Thread,WebDriver>();

	public static UIFactory getInstance() {
		return instance;
	}

	public AdminUI getAdminUI() {
		return new AdminWebDriverUI(getCurrentWebDriver());
	}
	
	public UserUI getUserUI() {
		return new UserWebDriverUI(getCurrentWebDriver());
	}

	public void closeAllInstances() {
		log.info("Closing WebDriver instances...");
		synchronized (webDriverPerThread) {
			for (Entry<Thread, WebDriver> entry : webDriverPerThread.entrySet()) {
				entry.getValue().quit();
				webDriverPerThread.remove(entry.getKey());
			}
		}
		log.info("Closed WebDriver instances");
	}

    /**
     * Force (re-)initialization of the current thread's webdriver instance.
     * Any previously existing webdriver instance will be quit()ed
     *
     * @param delayMillis if greater 0, adds random delays after each click to emulate more realistic user behaviour
     * @return the newly initialized webdriver instance
     */
    public WebDriver initializeWebDriver(int delayMillis) {
        synchronized (webDriverPerThread) {
            final Thread instanceKey = Thread.currentThread();
            WebDriver webDriver = webDriverPerThread.get(instanceKey);
            if (webDriver != null) {
                webDriver.quit();
            }

            log.debug("Creating WebDriver instance for thread {}", instanceKey.getName());
            webDriver = createNewWebDriver(instanceKey, delayMillis);
            webDriverPerThread.put(instanceKey, webDriver);

            return webDriver;
        }
    }

	public WebDriver getCurrentWebDriver() {
		// reuse UI instance if one found in this thread, otherwise create one and save it, including remembering this thread
		synchronized (webDriverPerThread) {
			final Thread instanceKey = Thread.currentThread();
            WebDriver webDriver = webDriverPerThread.get(instanceKey);
            if (webDriver == null) {
                webDriver = initializeWebDriver(0);
			}
            return webDriver;
		}
	}

    protected WebDriver createNewWebDriver(final Thread instanceKey, final int delayMillis) {
        WebDriver webDriver = Settings.RUN_HEADLESS ? new HtmlUnitDriver(true) : createFirefoxDriver();
        // make sure the driver is removed from the map once quit() or close() are called
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(webDriver) {
            @Override
            public void close() {
                this.quit();
            }

            @Override
            public void quit() {
                synchronized (webDriverPerThread) {
                    webDriverPerThread.remove(instanceKey);
                }
                super.quit();
                log.debug("{} WebDriver instance quitted on thread {}", this.getClass().getSimpleName(), instanceKey.getName());
            }
        };
        eventFiringWebDriver.register(new AbstractWebDriverEventListener() {
            @Override
            public void afterClickOn(WebElement element, WebDriver driver) {
                // put in some delay for performance/load tests
                if (delayMillis > 0) {
                    try {
                        Thread.sleep( Math.round(Math.random()* ((double)delayMillis)) );
                    } catch (InterruptedException e) {
                        // ignore interruptions
                    }
                }
            }
        });
        return eventFiringWebDriver;
    }

    public MethodRule getScreenshotCaptureRule() {
		return new ScreenshotCaptureRule(getCurrentWebDriver());
	}
	
	private FirefoxDriver createFirefoxDriver() {
		
		// Create template directory if doesn't exist, and unzip extension once
		File dir = new File("target/firefox-template");
		if (!dir.exists()) {
			dir.mkdir();
		    ClasspathExtension extension = new ClasspathExtension(FirefoxProfile.class,
		            "/" + FirefoxProfile.class.getPackage().getName().replace(".", "/") + "/webdriver.xpi");
		    try {
				extension.writeTo(new File(dir, "extensions"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		FirefoxProfile profile = new FirefoxProfile(dir){
			@Override
			protected void addWebDriverExtensionIfNeeded() {
				// already in model profile, so doesn't need extracting as part of FirefoxProfile.installExtensions()
			}
			
		};
		FirefoxDriver firefoxDriver = new FirefoxDriver(profile);
		return firefoxDriver;
	}

}
