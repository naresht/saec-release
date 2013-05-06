package com.bfds.saec.batch.in.ss_cashed_check;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bfds.saec.batch.file.domain.in.ss_cashed_check.SsCashedCheckRec;
import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.ConverterUtils;

/**
 * Batch Services for State Street Bank Input File
 */
@Service
public class SsCashedCheckBatchServiceImpl implements SsCashedCheckBatchService {

	final Logger log = LoggerFactory
			.getLogger(SsCashedCheckBatchServiceImpl.class);

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private IMailSender mailSender;

	private Date cashedCheckPaymentDate;

	// NOTE: Not Thread safe but intended to be shared by Batch Worker items
	private CashedCheckItemNotificationDto notification = new CashedCheckItemNotificationDto();

	@Override
	public void processCashedCheck(List<? extends SsCashedCheckRec> items) {
		for (SsCashedCheckRec cashedCheck : items) {
			final String checkNo = ConverterUtils.getUnformatterString(
					cashedCheck.getCheckNumber(), "0");
			Payment check = paymentDao.findCheckByNumberAndAmount(checkNo,cashedCheck.getCheckAmount());

			if (check == null) {
				// TODO: Record error.
				log.error("Check can't be null!");

				continue;
			}
			log.info("Found Check: " + checkNo);

			// Update Total, date
            updateNotificationDto(cashedCheck, check);
			// Update Check
			check.setPaymentCode(PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(check, check.getPaymentCode()));
		}
	}

    private void updateNotificationDto(SsCashedCheckRec cashedCheck, Payment check) {
        BigDecimal totalAmount = notification.getAccountTotalAmount();
        notification.setAccountTotalItems(notification
                .getAccountTotalItems() + 1);
        totalAmount = totalAmount.add(check.getPaymentAmount());
        notification.setAccountTotalAmount(totalAmount);
        notification.setFileDate(new Date());
        notification.setDda(cashedCheck.getDda());
    }

    private String getNotificationText(
			CashedCheckItemNotificationDto notificationVo) {
		if (notificationVo != null) {
			if (notificationVo.getFileDate() != null) {
				cashedCheckPaymentDate = notificationVo.getFileDate();
			} else {
				cashedCheckPaymentDate = new Date();
			}
		}
		return "DDA      "
				+ notificationVo.getDda()
				+ "\n"
				+ "FileDate "
				+ ConverterUtils.getFormatedYearDate(cashedCheckPaymentDate,
						"MM/dd/yyyy") + "\n" + "Account total items "
				+ notificationVo.getAccountTotalItems() + "\n"
				+ "Account total amount $"
				+ notificationVo.getAccountTotalAmount();
	}

	@Override
	public CashedCheckItemNotificationDto notifyBank() {
		// TODO: Update DDA
		// notification.setDda("");
		notification.setMailSubject(getSSInboundPaidCheckMailSubject());
		mailSender.send(MailingList.findByCode("batch.jobs.statestreetbank"), 
				getSSInboundPaidCheckMailSubject(), getNotificationText(notification));
		log.info("Mail sent for SS Cashed Checks.....");
		notification.setAccountTotalAmount(new BigDecimal(0));
		notification.setAccountTotalItems(0);
		return notification;
	}

	private String getSSInboundPaidCheckMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "SSC Paid Check_" + currentMonthDateYear + "";
		return subject;
	}	
}
