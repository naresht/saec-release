package com.bfds.saec.ui.test;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class Settings {
    
    public static final String APPLICATION_URL;
    public static final String TEST_HARNESS_URL;
    public static final boolean RUN_HEADLESS;
	    
    public static final String TARGET_BROWSER = "firefox";
	public static final boolean TAKE_ERROR_SCREENSHOTS = true;
    
    static {
    	// Read properties file.
        Properties properties = new Properties();
        try {
            ClassPathResource res = new ClassPathResource("test-environment.properties");
            properties.load(res.getInputStream());
            properties.putAll(System.getProperties()); // merge -Dblah=x in
        } catch (IOException e) {
        	throw new RuntimeException(e); // can't do much else
        }
        
        APPLICATION_URL = trimTrailingSlash(properties.getProperty("target.host.url"));
        TEST_HARNESS_URL = trimTrailingSlash(properties.getProperty("testharness.host.url"));
        RUN_HEADLESS = Boolean.parseBoolean(properties.getProperty("tests.headless"));
    }

    
	private static String trimTrailingSlash(String url) {
		if (url.endsWith("/")){
        	url = url.substring(0, url.length() - 1); 
        }
		return url;
	}
}