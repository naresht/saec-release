package com.bfds.saec.integration.api;

import org.springframework.integration.annotation.Gateway;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation=Propagation.MANDATORY)
public interface UpdateListener {
	
	/**
	 * Notify of a generic change.
	 */
	@Gateway(requestChannel="updatesFromApp")
	void notifyUpdate(UpdateEvent event);
}
