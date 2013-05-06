package com.bfds.saec.batch.out.dsto_check_file;

import java.math.BigDecimal;
import java.util.Date;

public class DstoCheckFileNotificationDto {
	private String mailSubject;
	private String fileType;
	private Date fileDate;
	private int totalRecords;
	private BigDecimal totalAmount;
	
	
	public DstoCheckFileNotificationDto()
	{
		this.totalAmount = new BigDecimal(0.0);
		this.totalRecords = new Integer(0);
		this.fileDate=new Date();
	}
	
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
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
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
