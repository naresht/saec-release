package com.bfds.saec.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * SessionListener class used to listen to session creation and destruction.<br>
 * Please add in web.xml its definition to enable it.
 *
 * <pre>
 *  &lt;listener&gt;
 *      &lt;listener-class&gt;com.bfds.saec.web.listener.SessionListener&lt;/listener-class&gt;
 *  &lt;/listener&gt;
 * </pre>
 *
 */
public class SessionListener implements HttpSessionListener {

    private Logger logger = LoggerFactory.getLogger(SessionListener.class);

    /**
     * called upon session creation
     */
    public void sessionCreated(HttpSessionEvent se) {
        if (logger.isInfoEnabled()) {
            HttpSession session = se.getSession();
            logger.info("sessionId=" + session.getId() + " maxInactiveInterval=" + session.getMaxInactiveInterval()
                    + " seconds");
        }
    }

    /**
     * called upon session deletion
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        if (logger.isInfoEnabled()) {
            HttpSession session = se.getSession();
            // session has been invalidated and all session data (except Id)is no longer available
            logger.info("sessionId=" + session.getId());
        }
    }
}
