package com.bfds.saec.batch.out.dsto_check_file;

import java.math.BigDecimal;

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
import com.bfds.saec.domain.Payment;
import com.bfds.saec.util.ConverterUtils;

public class DSTOCheckFileItemProcessor implements
		ItemProcessor<Payment, DstoRec>, StepExecutionListener {

	final Logger log = LoggerFactory
			.getLogger(DSTOCheckFileItemProcessor.class);

	@Autowired
	private DSTOCheckFileOutputBatchService dstocheckfileoutputbatchService;

	private StepExecution stepExecution;

	@Override
	public void beforeStep(final StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		this.stepExecution.getExecutionContext().put("bankName",
				Event.getCurrentEvent().getBankName());
		dstocheckfileoutputbatchService.initdstocheckFileNotification();
	}

	@Override
	public DstoRec process(Payment check) throws Exception {
		DstoRec checkfileObj = dstocheckfileoutputbatchService
				.generateCheckFile(check, this.stepExecution.getJobParameters().getString("runType"));
		checkfileObj.setBatchNum(ConverterUtils
				.getFormattedString(stepExecution.getJobExecutionId().toString(),6, "0"));
		updateTotalNetAmount(check.getPaymentCalc().getNettAmount());
		updateTotalGrossAmount(check.getPaymentCalc().getGrossAmount());
		return checkfileObj;
	}

	@AfterStep
	public void sendNotification() {
		dstocheckfileoutputbatchService.notifydstoCheckFile();

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// NO-OP
		return null;
	}

	private void updateTotalNetAmount(BigDecimal paymentAmount) {
		BigDecimal totalAmount = (BigDecimal) stepExecution.getExecutionContext().get("totalAmount");
		if (totalAmount == null) {
			totalAmount = paymentAmount;
		} else {
			totalAmount = totalAmount.add(paymentAmount);
		}
		stepExecution.getExecutionContext().put("totalAmount", totalAmount);
	}
	
	private void updateTotalGrossAmount(BigDecimal paymentAmount) {
		BigDecimal grossAmount = (BigDecimal) stepExecution.getExecutionContext().get("grossAmount");
		if (grossAmount == null) {
			grossAmount = paymentAmount;
		} else {
			grossAmount = grossAmount.add(paymentAmount);
		}
		stepExecution.getExecutionContext().put("grossAmount", grossAmount);
	}
	

}
