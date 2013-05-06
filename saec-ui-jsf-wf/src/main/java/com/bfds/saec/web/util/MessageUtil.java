/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.web.util;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;

/**
 * Use this bean from your flow to create JSF info/warn/error messsages.
 */
@Component
public class MessageUtil {

    public void info(String code, MessageContext messageContext) {
        messageContext.addMessage(new MessageBuilder().info().code(code).build());
    }

    public void warning(String code, MessageContext messageContext) {
        messageContext.addMessage(new MessageBuilder().warning().code(code).build());
    }

    public void error(String code, MessageContext messageContext) {
        messageContext.addMessage(new MessageBuilder().error().code(code).build());
    }
}