package com.bfds.saec.addressresearch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;

/**
 * Listener for logging Errors. Acts as a cheap, light-weight logging strategy in order to
 * isolate batch process errors via this class to a custom log4j file appender.
 * 
 * In the future, more sophisticated logging implementations can be used (persistence)
 * 
 */
public class AddressReasearchJobFailureLogger extends ItemListenerSupport {
    
    final Logger log = LoggerFactory.getLogger(AddressReasearchJobFailureLogger.class);

    public void onReadError(Exception e) {
        e.printStackTrace();
        log.error("Encountered error on read", e);
    }

    public void onWriteError(Exception e, Object item) {
        e.printStackTrace();
        log.error(String.format("Encountered error on item[%s] write: ", item, e));
    }

}
