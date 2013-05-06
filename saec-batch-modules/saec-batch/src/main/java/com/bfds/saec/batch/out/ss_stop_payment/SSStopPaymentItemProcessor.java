package com.bfds.saec.batch.out.ss_stop_payment;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.ss_stop_payment.SsStopPaymentRec;
import com.bfds.saec.domain.Payment;

/**
 * Stop Payment ItemProcessor for State Street Bank
 */
public class SSStopPaymentItemProcessor implements
		ItemProcessor<Payment, SsStopPaymentRec>, StepExecutionListener {

	final Logger log = LoggerFactory
			.getLogger(SSStopPaymentItemProcessor.class);

	@Autowired
	private SStopPaymentBatchService batchService;

	private StepExecution stepExecution;

	@Override
	public void beforeStep(final StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		batchService.initIssueStopPaymentNotification();
	}

	@Override
	public SsStopPaymentRec process(Payment check) throws Exception {
		SsStopPaymentRec stopPayment;
		Integer recNum = 1;
		if (stepExecution.getExecutionContext().get("recordNumber") != null) {
			Integer NextRecNum = (Integer) stepExecution.getExecutionContext()
					.get("recordNumber");
			NextRecNum++;
			stepExecution.getExecutionContext().put("recordNumber", NextRecNum);
			stopPayment = batchService.issueStopPayment(check, NextRecNum);
		} else {
			stepExecution.getExecutionContext().put("recordNumber", recNum);
			stopPayment = batchService.issueStopPayment(check, recNum);
		}

		updateTotalAmount(check.getPaymentAmount());
		return stopPayment;
	}

	@AfterStep
	public void sendNotification() {
		batchService.notifyIssueStopPayment();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// NO-OP
		return null;
	}

	private void updateTotalAmount(BigDecimal paymentAmount) {
		BigDecimal totalAmount = (BigDecimal) stepExecution
				.getExecutionContext().get("totalAmount");
		if (totalAmount == null) {
			totalAmount = paymentAmount;
		} else {
			totalAmount = totalAmount.add(paymentAmount);
		}
		stepExecution.getExecutionContext().put("totalAmount", totalAmount);
	}

}
