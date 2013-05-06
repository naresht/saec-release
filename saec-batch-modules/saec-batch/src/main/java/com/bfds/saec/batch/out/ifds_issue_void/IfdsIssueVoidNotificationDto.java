/**
 * 
 */
package com.bfds.saec.batch.out.ifds_issue_void;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dt83395
 *
 */
public class IfdsIssueVoidNotificationDto {
	private String dda;

	private Date issueDate;

	private BigDecimal accountTotalAmount;

	private int accountTotalItems;

	public IfdsIssueVoidNotificationDto() {
		this.accountTotalAmount = new BigDecimal(0.0);
		this.accountTotalItems = new Integer(0);
		this.issueDate=new Date();
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
		this.dda = dda;
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
