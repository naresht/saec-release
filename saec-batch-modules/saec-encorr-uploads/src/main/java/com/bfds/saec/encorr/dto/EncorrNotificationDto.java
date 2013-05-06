package com.bfds.saec.encorr.dto;

import java.util.Date;

/**
 * Dto for EncorrNotification
 * 
 */
public class EncorrNotificationDto {
	private String busiNessArea;

	private Date fileDate;

	private Integer encorrTletterItems;

	private Integer encorrLetterItems;

	private Integer noMatchItems;

	public EncorrNotificationDto() {
		this.busiNessArea = "ENCORR";
		this.encorrTletterItems = new Integer(0);
		this.encorrLetterItems = new Integer(0);
		this.noMatchItems = new Integer(0);
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

	public Integer getEncorrTletterItems() {
		return encorrTletterItems;
	}

	public void setEncorrTletterItems(Integer encorrTletterItems) {
		this.encorrTletterItems = encorrTletterItems;
	}

	public Integer getEncorrLetterItems() {
		return encorrLetterItems;
	}

	public void setEncorrLetterItems(Integer encorrLetterItems) {
		this.encorrLetterItems = encorrLetterItems;
	}

	public Integer getNoMatchItems() {
		return noMatchItems;
	}

	public void setNoMatchItems(Integer noMatchItems) {
		this.noMatchItems = noMatchItems;
	}

}
