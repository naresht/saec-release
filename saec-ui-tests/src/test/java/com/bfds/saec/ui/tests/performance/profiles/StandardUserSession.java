package com.bfds.saec.ui.tests.performance.profiles;

import com.bfds.saec.ui.test.UserUI;
import com.bfds.saec.ui.test.webdriver.UIFactory;

/**
 * A "standard" user logs in, performs 5 searches. Only 30% perform a proper log out
 *
 * @author: Erich Eichinger
 * @date: 25/01/12
 */
public class StandardUserSession implements Runnable {

    @Override
    public void run() {
        final UserUI userUI = UIFactory.getInstance().getUserUI();

        userUI.login("csr", "csr");

        long numberOfSearches = Math.round(Math.random()*3.0) + 3;

        for(long i=0; i<numberOfSearches; i++) {
            userUI.performAccountSearch();
            // THEN an account list table will appear with at least 10 results
            userUI.check().canSeeText("(1 of");
        }

        if (Math.random() <= 0.3) {
            userUI.logout();
        }
    }
}
