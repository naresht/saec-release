/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.web.util;

import org.springframework.stereotype.Component;

/**
 * Use this bean from your flow to print debug message in the console.
 */
@Component("out")
public class FlowDebugUtil {

    public void print(String msg) {
        System.out.println("[flow debug] " + msg);
    }
}
