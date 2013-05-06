package com.bfds.saec.batch.out.service;

import java.math.BigDecimal;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bfds.saec.batch.out.dto.IssueVoidNotificationDto;
import com.bfds.saec.batch.out.dto.StopPaymentNotificationDto;
import com.bfds.saec.domain.Event;

/**
 * Batch Services Common for Output File
 */
public abstract class OutputBatchServiceImpl implements OutputBatchService {

	final Logger log = LoggerFactory.getLogger(OutputBatchServiceImpl.class);

	protected StopPaymentNotificationDto stopPaymentNotification = new StopPaymentNotificationDto();

	@Autowired
	private IMailSender mailSender;

	@Override
	public StopPaymentNotificationDto notifyIssueStopPayment() {
		final StopPaymentNotificationDto ret = stopPaymentNotification;
		stopPaymentNotification.setDda(Event.getCurrentEventDda());
		mailSender.send(MailingList.findByCode("batch.jobs.deutschebank"), 
				stopPaymentNotification.getMailSubject(), 
				StopPaymentNotificationDto.getNotificationText(stopPaymentNotification));
		log.info("Sent mail for Stop Payment Notification....");
		stopPaymentNotification.setRecordCount(0);
		stopPaymentNotification.setTotalAmount(new BigDecimal(0));
		stopPaymentNotification = new StopPaymentNotificationDto();
		return ret;

	}

	@Override
	public StopPaymentNotificationDto initIssueStopPaymentNotification() {
		return stopPaymentNotification = new StopPaymentNotificationDto();
	}
}
