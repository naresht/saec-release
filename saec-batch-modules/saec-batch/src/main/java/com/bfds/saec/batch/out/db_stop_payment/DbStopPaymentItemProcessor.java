package com.bfds.saec.batch.out.db_stop_payment;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.db_stop_payment.DbStopPaymentRec;
import com.bfds.saec.domain.Payment;

/**
 * Stop Payment ItemProcessor
 */
public class DbStopPaymentItemProcessor implements
		ItemProcessor<Payment, DbStopPaymentRec>, StepExecutionListener {

	final Logger log = LoggerFactory
			.getLogger(DbStopPaymentItemProcessor.class);

	@Autowired
	private DbStopPaymentService batchService;

	private StepExecution stepExecution;

	@Override
	public void beforeStep(final StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		batchService.initDBIssueStopPaymentNotification();
	}

	@Override
	public DbStopPaymentRec process(Payment check) throws Exception {
		DbStopPaymentRec stopPayment = batchService.issueStopPayment(check);
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
