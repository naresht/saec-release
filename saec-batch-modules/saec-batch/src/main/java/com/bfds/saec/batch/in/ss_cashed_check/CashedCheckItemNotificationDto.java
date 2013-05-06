package com.bfds.saec.batch.in.ss_cashed_check;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Dto for Notification
 * 
 */
public class CashedCheckItemNotificationDto {
	private String dda;

	private Date fileDate;

	private BigDecimal accountTotalAmount;

	private Integer accountTotalItems;
	
	private String mailSubject;

	public CashedCheckItemNotificationDto() {
		this.accountTotalAmount = new BigDecimal(0.0);
		this.accountTotalItems = new Integer(0);
	}

	public String getDda() {
		return dda;
	}

	public void setDda(String dda) {
		this.dda = dda;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public BigDecimal getAccountTotalAmount() {
		return accountTotalAmount;
	}

	public void setAccountTotalAmount(BigDecimal accountTotalAmount) {
		this.accountTotalAmount = accountTotalAmount;
	}

	public Integer getAccountTotalItems() {
		return accountTotalItems;
	}

	public void setAccountTotalItems(Integer accountTotalItems) {
		this.accountTotalItems = accountTotalItems;
	}

}