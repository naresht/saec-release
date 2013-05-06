package com.bfds.saec.ui.tests.performance;

import com.bfds.saec.ui.test.support.SchedulingUtils;
import com.bfds.saec.ui.test.webdriver.UIFactory;

/**
 * A BrowserSessionFactory produces on demand a runnable browser session.
 * The session duration is limited to a maxium of maxSessionDuration after which the session aborts
 * with a TimeoutThresholdException
 *
 * @author: Erich Eichinger
 * @date: 25/01/12
 */
public class BrowserSessionFactory<T extends Runnable> {
    private final Class<T> profileClass;
    private final int delayMillis;
    private final int maxSessionDuration;

    public BrowserSessionFactory(Class<T> profileClass, int delayMillis, int maxSessionDuration) {
        this.profileClass = profileClass;
        this.delayMillis = delayMillis;
        this.maxSessionDuration = maxSessionDuration;
    }

    public Class<T> getProfileClass() {
        return profileClass;
    }

    public Runnable newRunnable() {
        return SchedulingUtils.newRunnableWithTimeout(maxSessionDuration, new Runnable() {
            @Override
            public void run() {
                UIFactory.getInstance().initializeWebDriver(delayMillis).manage().deleteAllCookies();
                try {
                    profileClass.newInstance().run();
                } catch (Exception e) {
                    throw new RuntimeException("failed to execute browser session " + profileClass.getName());
                } finally {
                    UIFactory.getInstance().getCurrentWebDriver().quit();
                }
            }
        });
    }
}
