package com.bfds.saec.rpo.dto;

import java.util.Date;

/**
 * Dto for RPONotification
 * 
 */
public class RPONonForwardableNotificationDto {
	private String busiNessArea;

	private Date fileDate;

	private Integer rpoCheckTotalItems;

	private Integer rpoMailTotalItems;

	public RPONonForwardableNotificationDto() {
		this.rpoCheckTotalItems = new Integer(0);
		this.rpoMailTotalItems = new Integer(0);
	}

	public String getBusiNessArea() {
		return busiNessArea;
	}

	public void setBusiNessArea(String busiNessArea) {
		this.busiNessArea = busiNessArea;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public Integer getRpoCheckTotalItems() {
		return rpoCheckTotalItems;
	}

	public void setRpoCheckTotalItems(Integer rpoCheckTotalItems) {
		this.rpoCheckTotalItems = rpoCheckTotalItems;
	}

	public Integer getRpoMailTotalItems() {
		return rpoMailTotalItems;
	}

	public void setRpoMailTotalItems(Integer rpoMailTotalItems) {
		this.rpoMailTotalItems = rpoMailTotalItems;
	}

}
