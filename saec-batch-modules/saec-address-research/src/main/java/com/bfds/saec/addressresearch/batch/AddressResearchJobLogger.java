package com.bfds.saec.addressresearch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Job Listener for logging job states (before and after)
 */
public class AddressResearchJobLogger implements JobExecutionListener {

    final Logger log = LoggerFactory.getLogger(AddressResearchJobLogger.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.debug("before job ...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
         log.debug("after job ...");
    }
}
