package com.bfds.saec.web.filter;

import com.bfds.saec.dao.ProcessErrorDao;
import com.bfds.saec.domain.ProcessError;
import com.bfds.saec.domain.ProcessError.ErrorType;
import com.bfds.saec.domain.reference.ProcessName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Log the Error at the outermost context level
 */
public final class ErrorLogger implements Filter {

	private static Logger log = LoggerFactory.getLogger(ErrorLogger.class);

	// TODO: parameterize this in config
	static String ERROR_URL = "/saec/app/error";

	@Autowired
	ProcessErrorDao processErrorDao;

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (Exception e) {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			request.getSession().setAttribute("lastException", e);
			request.getSession().setAttribute("lastExceptionUniqueId",
					e.hashCode());
			log.error("Unhandled " + e.getClass().getSimpleName()
					+ ". EXCEPTION unique id:" + e.hashCode(), e);

			ProcessError error = ProcessError.fatal(
					"Exception in Application Flow",
					ProcessName.ACCOUNT_SEARCH, ErrorType.SYSTEM,
					Boolean.FALSE, e);

			processErrorDao.save(error);

			HttpServletResponse response = (HttpServletResponse) servletResponse;
			// TODO: make it work with ajax request
			log.info("Sent email regarding the error info");
			response.sendRedirect(ERROR_URL);
		}
	}
}
