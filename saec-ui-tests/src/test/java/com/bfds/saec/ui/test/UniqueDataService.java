package com.bfds.saec.ui.test;

/**
 * Provides translation to test data (such as username, password, name, surname etc)
 * to a form such as "user-1-password".
 * 
 * @author Neale Upstone
 */
public class UniqueDataService {
	
	private static final String runId = String.valueOf(System.currentTimeMillis()); 
	private ThreadLocal<String> threadUniqueId = new ThreadLocal<String>();

	public String userNameFor(String user) {
		return user + "-" + getUniqueId();
	}

	public String passwordFor(String user) {
		return user + "-" + getUniqueId() + "-password";
	}

	public String firstNameFor(String user) {
		return user + "-" + getUniqueId() + "-firstName";
	}

	public String lastNameFor(String user) {
		return user + "-" + getUniqueId() + "-lastName";
	}
	
	
	private String getUniqueId() {
		String id = threadUniqueId.get();
		if (id == null){
			id = runId + Thread.currentThread().getName(); 
		}
		return id;
	}
}
