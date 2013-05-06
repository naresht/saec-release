package com.bfds.saec.batch.out.ss_bottom_line;

import java.math.BigDecimal;
import java.util.Date;

import com.bfds.saec.domain.Event;
import com.bfds.saec.util.ConverterUtils;

public class BottomLineOutBoundNotificationDto {
	private String dda;

	private Date fileDate;

	private BigDecimal totalAmount;

	private int recordCount;

	private String mailSubject;

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public BottomLineOutBoundNotificationDto() {
		this.totalAmount = new BigDecimal(0.0);
		this.recordCount = new Integer(0);
	}

	public String getDda() {
		return dda;
	}

	public void setDda(String dda) {
		this.dda = Event.getCurrentEventDda();
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

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public static String getNotificationText(BottomLineOutBoundNotificationDto notificationVo) {
		Date stopPaymentDate = null;
		if (notificationVo != null) {
			if (notificationVo.getFileDate() != null)
				stopPaymentDate = notificationVo.getFileDate();
			else
				stopPaymentDate = new Date();
		}
		return "DDA :: "+ notificationVo.getDda()+ "\n"
				+ "File date ::    "+ ConverterUtils.getFormatedYearDate(stopPaymentDate,"MM/dd/yyyy") + "\n" 
				+ "Record Count::  "+ notificationVo.getRecordCount() + "\n" 
				+ "Total Amount :: $"+ notificationVo.getTotalAmount();
	}

}
