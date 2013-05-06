package com.bfds.saec.batch.out.dsto_print_file;

import java.math.BigDecimal;
import java.util.Date;

public class DstoPrintFileNotificationDto {

	private String mailSubject;
	private String fileType;
	private Date fileDate;
	private int totalRecords;
	private BigDecimal totalAmount;

	public DstoPrintFileNotificationDto() {
		this.totalAmount = new BigDecimal(0.0);
		this.totalRecords = new Integer(0);
		this.fileDate = new Date();
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
