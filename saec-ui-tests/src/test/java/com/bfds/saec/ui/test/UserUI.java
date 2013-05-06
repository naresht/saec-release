package com.bfds.saec.ui.test;

/**
 * English language driver for user operation (e.g. a CSR using a browser).
 * 
 * Operations provided here should be readable against user tests.
 * 
 * @author neale.upstone@opencredo.com
 */
public interface UserUI {

	Checker check();

	void quit();

	void navigateToHome();

	/**
	 * Login as a per-test-instance user based on the supplied username.
	 * This will log in using a username and password based on the conventions
	 * used defined in {@link UniqueDataService}, which allows for the same
	 * test cases to be run in parallel against one server for load testing.
	 */
	void login(String username);

	/**
	 * Login as a specific pre-created user (e.g. administrator).
	 */
	void login(String username, String password);

	void logout();

	void performAccountSearch(String... args);

}
