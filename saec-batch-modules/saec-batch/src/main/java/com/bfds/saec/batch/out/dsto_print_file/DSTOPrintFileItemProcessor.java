package com.bfds.saec.batch.out.dsto_print_file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.util.ConverterUtils;

public class DSTOPrintFileItemProcessor implements
		ItemProcessor<Letter, DstoRec>, StepExecutionListener {

	final Logger log = LoggerFactory
			.getLogger(DSTOPrintFileItemProcessor.class);

	@Autowired
	private DSTOPrintFileOutputBatchService dstoprintfileoutputbatchService;

	private StepExecution stepExecution;

	@Override
	public void beforeStep(final StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		this.stepExecution.getExecutionContext().put("bankName", Event.getCurrentEvent().getBankName());
		dstoprintfileoutputbatchService.initdstoprintFileNotification();
	}

	@Override
	public DstoRec process(Letter letter) throws Exception {
		DstoRec printfileObj = dstoprintfileoutputbatchService
				.generatePrintFile(letter);
		printfileObj.setBatchNum(ConverterUtils
				.getFormattedString(stepExecution.getJobExecutionId().toString(),6, "0"));
		return printfileObj;

	}

	@AfterStep
	public void sendNotification() {
		dstoprintfileoutputbatchService.notifydstoprintFiles();

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

}
