package com.bfds.saec.addressresearch.batch;

import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;


/**
 * Listener for logging Step Errors. Acts as a cheap, light-weight logging strategy in order to
 * isolate batch process errors via this class to a custom log4j file appender.
 * <p/>
 * In the future, more sophisticated logging implementations can be used (persistence)
 */
public class CorpAddressResearchStepLogger implements ItemReadListener<CorporateAddressResearch> {

    final Logger log = LoggerFactory.getLogger(CorpAddressResearchStepLogger.class);

    @Override
    public void beforeRead() {
        // TODO
    }

    @Override
    public void afterRead(CorporateAddressResearch corporateAddressResearch) {
        // TODO
    }

    @Override
    public void onReadError(Exception e) {
        e.printStackTrace();
        log.error("Encountered error on read", e);
    }
}
