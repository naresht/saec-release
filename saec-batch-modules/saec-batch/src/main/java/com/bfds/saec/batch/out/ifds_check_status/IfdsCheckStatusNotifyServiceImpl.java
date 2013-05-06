/**
 * 
 */
package com.bfds.saec.batch.out.ifds_check_status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bfds.saec.domain.Event;
import com.bfds.saec.util.ConverterUtils;

/**
 * @author dt83395
 * 
 */
public class IfdsCheckStatusNotifyServiceImpl implements
		IfdsCheckStatusNotifyService {
	protected IfdsCheckStatusNotificationDto ifdsCheckStatusNotificationDto = new IfdsCheckStatusNotificationDto();

	@Autowired
	private IMailSender mailSender;
	
	private Date checkIssueDate;

	@Override
	public void notifyCheckStatus() {
		ifdsCheckStatusNotificationDto.setDda(Event.getCurrentEventDda());
		mailSender.send(MailingList.findByCode("batch.jobs.ifds"), getIFDSCheckStatusMailSubject(), getNotificationText(ifdsCheckStatusNotificationDto));
		ifdsCheckStatusNotificationDto.setRecordCount(0);
		ifdsCheckStatusNotificationDto.setTotalAmount(new BigDecimal(0));
	}

	private String getIFDSCheckStatusMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "IFDS Check Status_" + currentMonthDateYear + "";
		return subject;
	}

	protected String getNotificationText(
			IfdsCheckStatusNotificationDto ifdsCheckStatusNotificationDto) {
		if (ifdsCheckStatusNotificationDto != null) {
			if (ifdsCheckStatusNotificationDto.getFileDate() != null) {
				checkIssueDate = ifdsCheckStatusNotificationDto.getFileDate();
			} else {
				checkIssueDate = new Date();
			}
		}
		return "DDA :: "
				+ ifdsCheckStatusNotificationDto.getDda()
				+ "\n"
				+ "Issue date ::"
				+ ConverterUtils
						.getFormatedYearDate(checkIssueDate, "MM/dd/yy") + "\n"
				+ "Account total amount ::"
				+ ifdsCheckStatusNotificationDto.getTotalAmount() + "\n"
				+ "Account total items ::"
				+ ifdsCheckStatusNotificationDto.getRecordCount();
	}
}
