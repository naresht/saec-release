package com.bfds.saec.addressresearch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;

import com.bfds.saec.domain.Claimant;

public class OutAddressResearchSkipListener extends BaseElement{
    final Logger log = LoggerFactory.getLogger(OutAddressResearchSkipListener.class);

    @OnSkipInRead
    public void dataReadLog(Throwable t) {
        if (t instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) t;
            log.debug("Error while reading data at line Number:" + ffpe.getLineNumber());
            log.error("Read skip count="+getReadSkipCount());
        }
        log.debug("Error while reading data ");
    }

    @OnSkipInWrite
    public void dataWriteLog(Claimant c, Throwable t) {
        if (t instanceof Exception) {
        	log.error("writing Error for job :"+getJobName());
        	log.error("Writing Error for Step :"+getStepName());
        	log.error("write skip count="+getWriteSkipCount());
            log.error("Error while writing data for Outbound corporate account Prescrub job with reference No: " + c.getReferenceNo(), t);
            Exception e = (Exception) t;
            log.debug("Error in writing data " + e.getMessage());
        }
    }

    @OnSkipInProcess
    public void dataProcessLog(Claimant c, Throwable t) {
        if (t instanceof Exception) {
        	log.error("Processing Error for job :"+getJobName());
        	log.error("Processing Error for step :"+getStepName());
        	log.error("process skip count="+getProcessSkipCount());
            log.error("Error while Processing data for Outbound corporate account Prescrub job with reference No: " +c.getReferenceNo(), t);
            Exception e = (Exception) t;
            log.debug("Error in Processing data " + e.getMessage());
        }
    }
}
