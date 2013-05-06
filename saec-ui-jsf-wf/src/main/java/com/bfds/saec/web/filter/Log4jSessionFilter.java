package com.bfds.saec.web.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter that adds user session info in log4j MDC (Mapped Diagnoistic Context)
 */
public class Log4jSessionFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            if (getLoggedUser() != null) {
                MDC.put("user", getLoggedUser());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getLoggedUser() {
        // TODO: Replace with Spring security Auth invocation       
        return "";
    }

    @Override
    public void destroy() {
        // NO OP
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // NO OP
    }

}