package com.bfds.saec.batch.out.db_stop_payment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.out.db_stop_payment.DbStopPaymentRec;
import com.bfds.saec.batch.out.dto.StopPaymentNotificationDto;
import com.bfds.saec.batch.out.service.OutputBatchServiceImpl;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;

/**
 * Batch Services for Deutsche Bank Input File
 */
@Service
public class DbStopPaymentServiceImpl extends OutputBatchServiceImpl implements
		DbStopPaymentService {

	final Logger log = LoggerFactory.getLogger(DbStopPaymentServiceImpl.class);

	@Override
	public DbStopPaymentRec issueStopPayment(Payment check) {
		log.info("Processing Db Stop Payment for Check : #%s"+check.getIdentificatonNo());
		check = Payment.findPayment(check.getId());
		
		if(!StringUtils.hasText(check.getIdentificatonNo())
		        || check.getPaymentAmount() == null 
		        || check.getPaymentAmount().compareTo(new BigDecimal(0))==0) {
		            throw new IllegalStateException(String.format("Check number is null or Payment amount is zero for DB Stop Payment Check : %s", check.getIdentificatonNo()));
		}
		// Update Total, Date
		updateDBStopPaymentNotification(check);
		if(log.isInfoEnabled()) {
			log.info(String.format("Notification for DB Stop Payment Job is updated..."));
		}		
		
		final DbStopPaymentRec stopPayment = new DbStopPaymentRec();
		if (StringUtils.hasText(Event.getCurrentEventDda())) {
			stopPayment.setDda(Event.getCurrentEventDda());
		}

		stopPayment.setAccountNumber(check.getPayTo().getReferenceNo());
		stopPayment.setSerialNumber(check.getIdentificatonNo());
		stopPayment.setToSerialNumber(check.getIdentificatonNo());
		stopPayment.setStopAmount(check.getPaymentAmount().doubleValue());
		stopPayment.setIssueDate(check.getPaymentDate());
		stopPayment.setPayee(check.getPayTo().getClaimantRegistration().getRegistration1());
		check.setSentOnBankStopFile(Boolean.TRUE);
		check.merge();
		return stopPayment;
	}

	private void updateDBStopPaymentNotification(Payment check) {
		BigDecimal totalAmount = stopPaymentNotification.getTotalAmount();
		stopPaymentNotification.setRecordCount(stopPaymentNotification
				.getRecordCount() + 1);
		totalAmount = totalAmount.add(check.getPaymentAmount());
		stopPaymentNotification.setTotalAmount(totalAmount);
		stopPaymentNotification.setFileDate(new Date());
		stopPaymentNotification.setMailSubject(getDBStopPaymentMailSubject());
	}

	private String getDBStopPaymentMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "DB Stoppayment_" + currentMonthDateYear + "";
		return subject;
	}

	@Override
	public StopPaymentNotificationDto initDBIssueStopPaymentNotification() {
		stopPaymentNotification = new StopPaymentNotificationDto();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "DB Stoppayment_" + currentMonthDateYear + "";
		stopPaymentNotification.setMailSubject(subject);
		if(log.isInfoEnabled()) {
			log.info("Sent Mail for DB Stop Payment Checks....");
		}
		return stopPaymentNotification;
	}

}