package com.bfds.saec.addressresearch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.retry.RetryCallback;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.listener.RetryListenerSupport;


public class InfoAgeBatchRetryListener extends RetryListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(InfoAgeBatchRetryListener.class);

    @Override
    public <T> void onError(RetryContext context, RetryCallback<T> callback, Throwable throwable) {
        log.error("retried operation", throwable);
    }
}