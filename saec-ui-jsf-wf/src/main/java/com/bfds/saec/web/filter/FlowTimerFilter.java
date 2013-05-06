/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.web.filter;

import com.bfds.saec.security.spring.SpringSecurityContext;
import com.bfds.saec.util.LogContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.execution.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * Log current flow transition, state, etc.
 * You must also configure this class as an Execution Listener in Spring Web Flow conf.
 */
public final class FlowTimerFilter extends FlowExecutionListenerAdapter implements Filter {

    private static final byte SPACE = 32;
    private static final Logger logger = LoggerFactory.getLogger(FlowTimerFilter.class);
    private static final ThreadLocal<StringBuilder> message = new ThreadLocal<StringBuilder>();

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // reset context.
        message.set(new StringBuilder(""));

        // set up log context for this thread so these information can be used by log4j
        String username = SpringSecurityContext.getUsername();
        LogContext.setLogin(username != null ? username : "no username");
        LogContext.setSessionId(httpRequest.getSession().getId());

        // timer
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long stop = System.currentTimeMillis();
        String duration = Long.toString(stop - start);

        if (duration.length() < 5) {
            byte[] spaces = new byte[5 - duration.length()];
            Arrays.fill(spaces, SPACE);
            duration = new String(spaces) + duration;
        }

        logger.trace("flow only duration: " + duration + " ms - " + httpRequest.getRequestURI() + message.get());

        // reset context
        LogContext.resetLogContext();
    }

    public void destroy() {
    }

    @Override
    public void viewRendered(RequestContext context, View view, StateDefinition viewState) {
        StringBuilder sb = message.get();
        if (sb == null) {
            logger.info("viewRendered strange case: "
                    + ((HttpServletRequest) context.getExternalContext().getNativeRequest()).getRequestURI());
            return;
        }

        sb.append("\n\tActive flow: ")
                .append(context.getActiveFlow() == null ? "N/A" : context.getActiveFlow().getId()).append(
                        " (" + context.getFlowExecutionUrl() + ")");
        sb.append("\n\tCurrent State: ").append(
                (context.getCurrentState() == null ? "N/A" : context.getCurrentState().getId()));
        sb.append("\n\tCurrent Transition: ").append(
                context.getCurrentTransition() == null ? "N/A" : context.getCurrentTransition().getId());
    }

    @Override
    public void eventSignaled(RequestContext context, Event event) {
        StringBuilder sb = message.get();
        if (sb == null) {
            logger.info("eventSignaled strange case: "
                    + ((HttpServletRequest) context.getExternalContext().getNativeRequest()).getRequestURI());
            return;
        }

        sb.append("\n\teventSignaled: ").append(event.getId());
    }

    @Override
    public void transitionExecuting(RequestContext context, TransitionDefinition transition) {
        StringBuilder sb = message.get();
        if (sb == null) {
            logger.info("transitionExecuting strange case: "
                    + ((HttpServletRequest) context.getExternalContext().getNativeRequest()).getRequestURI());
            return;
        }

        sb.append("\n\ttransitionExecuting: ").append(
                "on=" + transition.getId() + " to=" + transition.getTargetStateId());
    }

    @Override
    public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
        req.getSession().setAttribute("lastException", exception);
        req.getSession().setAttribute("lastExceptionUniqueId", exception.hashCode());

        logger.error("EXCEPTION unique id: " + exception.hashCode(), exception); // todo: better

        StringBuilder sb = message.get();
        if (sb == null) {
            logger.info("exceptionThrown strange case: "
                    + ((HttpServletRequest) context.getExternalContext().getNativeRequest()).getRequestURI());
            return;
        }

        sb.append("\n\tException Thrown: ").append(exception);
        if (exception.getCause() != null) {
            sb.append("\n\tException Cause: ").append(exception.getCause());
        }
    }
}