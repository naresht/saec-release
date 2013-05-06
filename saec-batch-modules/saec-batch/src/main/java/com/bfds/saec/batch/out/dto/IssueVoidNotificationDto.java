package com.bfds.saec.batch.out.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.bfds.saec.domain.Event;
import com.bfds.saec.util.ConverterUtils;

/**
 * Notification Bean
 * 
 */
public class IssueVoidNotificationDto {
	private String dda;

	private Date issueDate;

	private BigDecimal accountTotalAmount;

	private int accountTotalItems;
	
	private String mailSubject;

	public IssueVoidNotificationDto() {
		this.accountTotalAmount = new BigDecimal(0.0);
		this.accountTotalItems = new Integer(0);
		this.issueDate=new Date();
	}

	/**
	 * @param mailSubject the mailSubject to set
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	
	/**
	 * @return the mailSubject
	 */
	public String getMailSubject() {
		return mailSubject;
	}
	
	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getDda() {
		return dda;
	}

	public void setDda(String dda) {
		this.dda = Event.getCurrentEventDda();
	}

	public BigDecimal getAccountTotalAmount() {
		return accountTotalAmount;
	}

	public void setAccountTotalAmount(BigDecimal accountTotalAmount) {
		this.accountTotalAmount = accountTotalAmount;
	}

	public int getAccountTotalItems() {
		return accountTotalItems;
	}

	public void setAccountTotalItems(int accountTotalItems) {
		this.accountTotalItems = accountTotalItems;
	}
	
	public static String getNotificationText(IssueVoidNotificationDto notificationVo) {
		Date stopPaymentDate = null;
		if (notificationVo != null) {
			if (notificationVo.getIssueDate() != null) {
				stopPaymentDate = notificationVo.getIssueDate();
			} else {
				stopPaymentDate = new Date();
			}
		}
		String dateLabel = notificationVo.getMailSubject();
		if (dateLabel != null && dateLabel.startsWith("SSC")) {
			dateLabel = "File date";
		} else {
			dateLabel = "Issue date";
		}
		return "DDA :: "
				+ notificationVo.getDda()
				+ "\n"
				+ dateLabel
				+ " :: "
				+ ConverterUtils.getFormatedYearDate(stopPaymentDate,
						"MM/dd/yyyy") + "\n" + "Account total amount :: $"
				+ notificationVo.getAccountTotalAmount() + "\n"
				+ "Account total items ::"
				+ notificationVo.getAccountTotalItems();
	}

}
