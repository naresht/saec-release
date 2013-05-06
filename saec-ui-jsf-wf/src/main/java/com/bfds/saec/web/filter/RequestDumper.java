package com.bfds.saec.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Handy filter to show Request values including headers, request params etc.
 * Useful for debugging. Disable in Prod
 */
public final class RequestDumper implements Filter {

    final Logger log = LoggerFactory.getLogger(RequestDumper.class);

    // value = "true" or "false", configured in filter init-param
    private String showHeaders;

    public void init(FilterConfig filterConfig) {
        this.showHeaders = filterConfig.getInitParameter("showHeaders");
    }

    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (log.isDebugEnabled()) {
            log.debug("------------------------------------------------------------------------------------------------------------------------");
            log.debug("request.getRequestURI=" + request.getRequestURI());
            log.debug("request.getRequestURL=" + request.getRequestURL());
            log.debug("request.getAuthType=" + request.getAuthType());
            log.debug("request.getRemoteUser=" + request.getRemoteUser());
            log.debug("request.getUserPrincipal=" + request.getUserPrincipal());

            if (showHeaders.equals("true")) {
                log.debug("*************Headers************");
                for (Enumeration headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
                    String headerName = (String) headerNames.nextElement();
                    StringBuilder sb = new StringBuilder(headerName).append(" = ");
                    for (Enumeration headerValues = request.getHeaders(headerName); headerValues.hasMoreElements(); ) {
                        sb.append(headerValues.nextElement());
                        if (headerValues.hasMoreElements()) {
                            sb.append(", ");
                        }
                    }
                    log.debug(sb.toString());
                }
            }

            log.debug("************Request Parameters************");
            for (Enumeration parameterNames = request.getParameterNames(); parameterNames.hasMoreElements(); ) {
                String parameterName = (String) parameterNames.nextElement();
                // Get rid of JSF-client-state noise in the output
                if (parameterName.startsWith("jsf_state_64") || parameterName.startsWith("jsf_tree_64")
                        || "javax.faces.ViewState".matches(parameterName)) {
                    final String v = request.getParameter(parameterName);
                    log.info(
                            parameterName + " = " + v.substring(0, Math.min(5, v.length())) + "...(truncated)..."
                    );
                    continue;
                }
                // passwords should never be echoed
                if (parameterName.toLowerCase().contains("password")) {
                    continue;
                }
                StringBuilder sb = new StringBuilder(parameterName).append(" = ");
                String[] pvs = request.getParameterValues(parameterName);
                if (pvs != null) {
                    for (String pv : pvs) {
                        sb.append(" ").append(pv).append(",");
                    }
                    if (sb.charAt(sb.length() - 1) == ',') {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                }
                log.debug(sb.toString());
            }
            log.debug("------------------------------------------------------------------------------------------------------------------------\n");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        // NO OP
    }
}