package com.bfds.saec.batch.in.ss_bottom_line;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.in.ss_bottom_line.SsBottomLineInRec;
import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.util.ConverterUtils;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;

/**
 * Batch Services for State Street Bank Input File
 */
@Service
public class BLInboundBatchServiceImpl implements BLInboundBatchService {
	
	final Logger log = LoggerFactory.
			getLogger(BLInboundBatchServiceImpl.class);

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private IMailSender mailSender;

	private Date boNotificationDate;

	// NOTE: Not Thread safe but intended to be shared by Batch Worker items
	private BottomLineNotificationDto notification = new BottomLineNotificationDto();

	@Override
	public void processBLInboundCheck(List<? extends SsBottomLineInRec> items) {
		log.info("Processing BottomLine Inbound Checks ... ");
		for (SsBottomLineInRec bottomLineCheck : items) {
			Payment check = findCheck(bottomLineCheck);
			if (check == null) {
				log.error("Check can't be null!");
				continue;
			}
			log.info("Found Check: " + check.getIdentificatonNo());
			updateNotificationInfo(check, bottomLineCheck);
			// Update Check Number and Status Change Date
			check.setIdentificatonNo(bottomLineCheck.getCheckNumber());
			check.setStatusChangeDate(bottomLineCheck.getCheckIssueDate());
		}

	}

	private Payment findCheck(SsBottomLineInRec bottomLineCheck) {
		final String fundNo = bottomLineCheck.getFundNumber();

		final String accountNo = bottomLineCheck.getAccountNumber();

		List<Payment> checks = Payment.findChecksByAccountNoAndNettAmount(accountNo, bottomLineCheck.getCheckAmount());
		Payment ret = null;
		for (Payment check : checks) {
			if (!StringUtils.hasText(check.getIdentificatonNo())) {
				if (ret != null) {
					throw new IllegalStateException(String.format(
							"More than one Check without check# found for"
									+ " fund# : %s, account#: %s, amount : %s",
							fundNo, accountNo, bottomLineCheck.getCheckAmount()));
				}
				ret = check;
			}
		}
		return ret;
	}
	
	private String getSsBottomlineInboundMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "BottomLinefileupdated_" + currentMonthDateYear + "";
		return subject;
	}

	private String getNotificationText(BottomLineNotificationDto notificationVo) {
		if (notificationVo.getFileDate() != null) 
			boNotificationDate = notificationVo.getFileDate();
		else 
			boNotificationDate = new Date();
		
		return "DDA :: "
				+ notificationVo.getDda() + "\n"
				+"FileDate :: "+ ConverterUtils.getFormatedYearDate(boNotificationDate,"MM/dd/yyyy") + "\n" 
				+ "Account total items :: "+ notificationVo.getAccountTotalItems() + "\n"
				+ "Account total amount :: $"+ notificationVo.getAccountTotalAmount();
	}
	
	private void updateNotificationInfo(Payment check,SsBottomLineInRec bottomLineDto) {
		// Update Total, date
		BigDecimal totalAmount = notification.getAccountTotalAmount();
		notification.setAccountTotalItems(notification.getAccountTotalItems() + 1);
		totalAmount = totalAmount.add(check.getPaymentAmount());
		notification.setAccountTotalAmount(totalAmount);
		notification.setFileDate(new Date());
		notification.setDda(bottomLineDto.getDda());		
	}

	
	@Override
	public BottomLineNotificationDto notificationMail() {
		// TODO: Update DDA
		// notification.setDda("");
		notification.setMailSubject(getSsBottomlineInboundMailSubject());
		mailSender.send(MailingList.findByCode("batch.jobs.statestreetbank"),getSsBottomlineInboundMailSubject(), getNotificationText(notification));
		log.info("Mail sent for SS Bottomline Inbound.....");
		notification.setAccountTotalAmount(new BigDecimal(0));
		notification.setAccountTotalItems(0);
		return notification;
	}

	
	

}
