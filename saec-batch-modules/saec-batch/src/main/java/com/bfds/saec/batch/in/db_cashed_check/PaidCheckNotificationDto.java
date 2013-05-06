package com.bfds.saec.batch.in.db_cashed_check;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Notification Bean
 * 
 */
public class PaidCheckNotificationDto {

	private String dda;

	private Date paidDate;

	private BigDecimal accountTotalAmount = new BigDecimal(0.0);

	private int accountTotalItems;
	
	private String mailSubject;
	

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getDda() {
		return dda;
	}

	public void setDda(String dda) {
		this.dda = dda;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
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
}
