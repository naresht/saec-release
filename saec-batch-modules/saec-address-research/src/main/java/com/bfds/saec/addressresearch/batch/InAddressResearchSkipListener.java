/**
 *
 */
package com.bfds.saec.addressresearch.batch;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;

import com.bfds.saec.batch.in.infoage.AddressResearchStatus;
import com.bfds.saec.domain.ProcessError;
import com.bfds.saec.domain.ProcessError.ErrorType;
import com.bfds.saec.domain.reference.ProcessName;

/**
 * PreScrub Listener
 */
public class InAddressResearchSkipListener extends BaseElement {
	final Logger log = LoggerFactory
			.getLogger(InAddressResearchSkipListener.class);

	@OnSkipInRead
	public void dataReadLog(Throwable t) {
		if (t instanceof FlatFileParseException) {
			FlatFileParseException ffpe = (FlatFileParseException) t;
			log.error("Error while reading data at line Number:"
					+ ffpe.getLineNumber());
			log.error("Read skip count=" + getReadSkipCount());
			ProcessError.fatal("Exception in job : " + getJobName(),
					ProcessName.INFOAGE, ErrorType.BUSINESS_PROCESS,
					Boolean.TRUE, ffpe);
			log.info("Sent email regarding the error info in INFOAGE");
		}
		log.error("Error while reading data ");
	}

	@OnSkipInWrite
	public void dataWriteLog(
			HashMap<String, AddressResearchStatus> addressResearchStatus,
			Throwable t) {
		if (t instanceof Exception) {
			HashMap<String, AddressResearchStatus> addressStatus = (HashMap<String, AddressResearchStatus>) addressResearchStatus;
			for (AddressResearchStatus researchStatus : addressStatus.values()) {
				log.error("writing Error for job :" + getJobName());
				log.error("Writing Error for Step :" + getStepName());
				log.error("write skip count=" + getWriteSkipCount());
				log.error("Error while writing data for individual address research prescrub job with reference No: "
						+ researchStatus.getAddressResearchUpdate().getUserRef());
			}
			Exception e = (Exception) t;
			log.error("Error in writing data " + e.getMessage());
			ProcessError.fatal("Exception in job : " + getJobName(),
					ProcessName.INFOAGE, ErrorType.BUSINESS_PROCESS,
					Boolean.TRUE, e);

			log.info("Sent email regarding the error info in INFOAGE");
		}
	}

	@OnSkipInProcess
	public void dataProcessLog(
			HashMap<String, AddressResearchStatus> addressResearchStatus,
			Throwable t) {
		if (t instanceof Exception) {
			HashMap<String, AddressResearchStatus> addressStatus = (HashMap<String, AddressResearchStatus>) addressResearchStatus;
			for (AddressResearchStatus researchStatus : addressStatus.values()) {
				log.error("Processing Error for job :" + getJobName());
				log.error("Processing Error for step :" + getStepName());
				log.error("process skip count=" + getProcessSkipCount());
				log.error("Error while Processing data for individual address research prescrub job with reference No: "
						+ researchStatus.getAddressResearchUpdate().getUserRef());
			}
			Exception e = (Exception) t;
			log.error("Error in Processing data " + e.getMessage());
			ProcessError.fatal("Exception in job : " + getJobName(),
					ProcessName.INFOAGE, ErrorType.BUSINESS_PROCESS,
					Boolean.TRUE, e);
			log.info("Sent email regarding the error info in INFOAGE");
		}
	}
}
