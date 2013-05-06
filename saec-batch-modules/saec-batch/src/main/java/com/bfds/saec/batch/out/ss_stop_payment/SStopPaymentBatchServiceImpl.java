package com.bfds.saec.batch.out.ss_stop_payment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.out.ss_stop_payment.SsStopPaymentRec;
import com.bfds.saec.batch.out.dto.StopPaymentNotificationDto;
import com.bfds.saec.batch.out.ss_issue_void.SSOutputBatchServiceImpl;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;

/**
 * Batch Services for State Street Bank Output File
 */
@Service
public class SStopPaymentBatchServiceImpl extends SSOutputBatchServiceImpl
		implements SStopPaymentBatchService {

	final Logger log = LoggerFactory
			.getLogger(SStopPaymentBatchServiceImpl.class);

	@Override
	public SsStopPaymentRec issueStopPayment(Payment check, Integer recNum) {
		log.info("Processing SS Stop Payment....");
		check = Payment.findPayment(check.getId());
		if(!StringUtils.hasText(check.getIdentificatonNo())
		        || check.getPaymentAmount() == null 
		        || check.getPaymentAmount().compareTo(new BigDecimal(0))==0) {
		            throw new IllegalStateException(String.format("Check number is null or Payment amount is zero for SS Stop Payment Check : %s", check.getIdentificatonNo()));
		}
		String recNo = String.valueOf(recNum);
		// Update Total, Date
		updateStopPaymentNotification(check);
		if(log.isInfoEnabled()) {
			log.info(String.format("Notification for SS Stop Payment Job is updated..."));
		}
		// Update remaining props
		final SsStopPaymentRec stopPayment = new SsStopPaymentRec();
		stopPayment.getBankNumber();
		stopPayment.setDda(Event.getCurrentEventDda());
		//stopPayment.setCheckingAccountNumber(check.getPayTo().getReferenceNo());
		stopPayment.setCheckSerialNumber(check.getIdentificatonNo());
		stopPayment.setAmountOfCheck(check.getPaymentAmount().doubleValue());
		stopPayment.setDate(check.getPaymentDate());
		stopPayment.setSequenceNumber(recNo);
		//stopPayment.setBlanks(ConverterUtils.getFormattedString1("", 21, " "));

		check.setSentOnBankStopFile(Boolean.TRUE);
		check.merge();

		return stopPayment;
	}

	private void updateStopPaymentNotification(final Payment check) {
		BigDecimal totalAmount = stopPaymentNotification.getTotalAmount();
		stopPaymentNotification.setRecordCount(stopPaymentNotification
				.getRecordCount() + 1);
		stopPaymentNotification.setTotalAmount(totalAmount.add(check
				.getPaymentAmount()));
		stopPaymentNotification.setFileDate(new Date());
		stopPaymentNotification.setMailSubject(getStopPaymentMailSubject());
	}

	private String getStopPaymentMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "SSCStoppayment_" + currentMonthDateYear + "";
		return subject;
	}

	@Override
	public StopPaymentNotificationDto initIssueStopPaymentNotification() {
		stopPaymentNotification = new StopPaymentNotificationDto();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "SSCStoppayment_" + currentMonthDateYear + "";
		stopPaymentNotification.setMailSubject(subject);
		if(log.isInfoEnabled()) {
			log.info("Sent Mail for SS Stop Payment Checks....");
		}
		return stopPaymentNotification;
	}

}