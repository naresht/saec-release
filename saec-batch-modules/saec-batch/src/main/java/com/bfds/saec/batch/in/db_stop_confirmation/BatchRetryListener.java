/**
 * 
 */
package com.bfds.saec.batch.in.db_stop_confirmation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.retry.RetryCallback;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.listener.RetryListenerSupport;

/**
 * @author sandeep
 * 
 */
public class BatchRetryListener extends RetryListenerSupport {
	private static final Logger log = LoggerFactory.getLogger(BatchRetryListener.class);

	@Override
	public <T> void onError(RetryContext context, RetryCallback<T> callback, Throwable throwable) {
		log.debug("retried operation", throwable);
	}
}