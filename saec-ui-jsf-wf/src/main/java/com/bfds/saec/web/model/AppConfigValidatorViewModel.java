package com.bfds.saec.web.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.Severity;

import com.bfds.saec.web.util.JsfUtils;
import com.bfds.validation.execution.ValidatorExecutor;
import com.bfds.validation.execution.ValidatorFinder;
import com.bfds.validation.message.Message;

/**
 * 
 * @see {@link com.bfds.saec.web.filter.AppConfigValidationVerifier}
 * 
 */
public class AppConfigValidatorViewModel implements java.io.Serializable {

	static final private Logger logger = LoggerFactory
			.getLogger(TestBatchViewModel.class);

	@Autowired
	private transient ValidatorExecutor validatorExecutor;

	@Autowired
	private transient ValidatorFinder validatorFinder;

	public boolean runValidation(MessageContext messageContext) {
		// First delete old messages.
		for (Message m : Message.findAllMessages()) {
			m.remove();
		}
		(new Message()).flush();
		(new Message()).clear();
		List<Message> messages = validatorExecutor.execute(validatorFinder
				.find());

		for (Message m : messages) {
			if (Severity.ERROR == m.getSeverity()
					|| Severity.FATAL == m.getSeverity()) {
				JsfUtils.getHttpServletRequest().getSession()
						.getServletContext()
						.setAttribute("APP_CONFIG_INVALID", Boolean.TRUE);
				break;
			}
		}

		return true;
	}
}
