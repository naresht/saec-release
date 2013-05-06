package com.bfds.saec.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bfds.validation.message.Message;

/**
 * 
 * A HandlerInterceptor that verifies that the application configuration has no
 * errors. If there are any errors the application is rendered unusable.
 * 
 * @see {@link com.bfds.saec.web.model.AppConfigValidatorViewModel}
 * 
 */
public class AppConfigValidationVerifier extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (!appConfigValid(request)) {
			writeValidationMessages(response);
			return false;
		}
		return true;

	}

	private void writeValidationMessages(HttpServletResponse response)
			throws IOException {
		List<Message> messages = Message.findAllMessages();
		response.getWriter().write("<html><body>");
		response.getWriter().write(
				"Configuration Errors. Fix and redeploy application.");
		response.getWriter().write("</br></br></br>");
		for (Message message : messages) {
			response.getWriter().write(message.toString() + "</br></br>");
		}
		response.getWriter().write("</body></html>");
	}

	private boolean appConfigValid(HttpServletRequest request) {
		return request.getSession().getServletContext()
				.getAttribute("APP_CONFIG_INVALID") == null;
	}

}
