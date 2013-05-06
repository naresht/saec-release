package com.bfds.saec.batch.in.db_cashed_check;

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

import com.bfds.saec.batch.file.domain.in.db_cashed_check.CashedCheckRec;
import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.ConverterUtils;

@Service
public class DbCashedCheckBatchServiceImpl implements DbCashedCheckBatchService {

	final Logger log = LoggerFactory
			.getLogger(DbCashedCheckBatchServiceImpl.class);

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private IMailSender mailSender;

	private String dateString;

	private Date paidChecksDate;

	// NOTE: Not Thread safe but intended to be shared by Batch Worker items
	private PaidCheckNotificationDto paidCheckNotification = new PaidCheckNotificationDto();;

	@Override
	public void processPaidChecks(List<? extends CashedCheckRec> items) {
		for (CashedCheckRec paidCheck : items) {
			if ("9999999999".equals(paidCheck.getCheckNumber())) {
				log.debug("Trailer record found and skipping ...");
				paidCheckNotification.setPaidDate(paidCheck.getDate());
				continue;
			}
			Payment check = paymentDao.findCheckByNumberAndAmount(paidCheck.getCheckNumber(), paidCheck.getCheckAmount());
			if (check == null) {
				// TODO: Record error.
				continue;
			}
            updateNotificationDto(paidCheck, check);

			check.setPaymentCode(PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(check, check.getPaymentCode()));
			check.setItemSequenceNumber(paidCheck.getItemSeqNumber());
		}
	}

    private void updateNotificationDto(CashedCheckRec paidCheck, Payment check) {
        BigDecimal totalAmount = paidCheckNotification.getAccountTotalAmount();
        paidCheckNotification.setAccountTotalItems(paidCheckNotification.getAccountTotalItems() + 1);
        totalAmount = totalAmount.add(check.getPaymentAmount());
        paidCheckNotification.setAccountTotalAmount(totalAmount);
        paidCheckNotification.setDda(paidCheck.getDda());
    }

    @Override
	public PaidCheckNotificationDto notifyPaidChecks() {
		// TODO Use StringBuilder
		PaidCheckNotificationDto notification = paidCheckNotification;
		if (notification.getPaidDate() != null) {
			paidChecksDate = notification.getPaidDate();
		} else {
			paidChecksDate = new Date();
		}

		notification.setMailSubject(getDBPaidCheckMailSubject());
		String mail = "DDA :: "
				+ notification.getDda()
				+ "\n"
				+
				/* "Paiddate ::" + notification.getPaidDate() + "\n"+ */
				"Paiddate ::"
				+ ConverterUtils.getFormatedYearDate(paidChecksDate,
						"MM/dd/yyyy") + "\n" + "Account total amount :: $"
				+ notification.getAccountTotalAmount() + "\n"
				+ "Account total items :: "
				+ notification.getAccountTotalItems();

		mailSender.send(MailingList.findByCode("batch.jobs.deutschebank"), getDBPaidCheckMailSubject(), mail);
		log.info("Mail Sent for Db Cashed Check....");
		paidCheckNotification.setAccountTotalAmount(new BigDecimal(0));
		paidCheckNotification.setAccountTotalItems(0);
		return notification;
	}

	private String getDBPaidCheckMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "DB Paid Check_" + currentMonthDateYear + "";
		return subject;
	}
	
}