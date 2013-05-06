package com.bfds.saec.batch.in.ss_cashed_check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

public class SscashedcheckStepExecutionListener implements StepExecutionListener {

	final Logger log = LoggerFactory
			.getLogger(SscashedcheckStepExecutionListener.class);

	@Autowired
	private SsCashedCheckBatchService batchService;

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		batchService.notifyBank();
		return null;
	}

}
