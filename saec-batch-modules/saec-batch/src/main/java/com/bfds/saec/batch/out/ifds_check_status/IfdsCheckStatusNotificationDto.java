/**
 * 
 */
package com.bfds.saec.batch.out.ifds_check_status;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dt83395
 *
 */
public class IfdsCheckStatusNotificationDto {
	public IfdsCheckStatusNotificationDto()
	{
		this.totalAmount = new BigDecimal(0.0);
		this.recordCount = new Integer(0);
		this.fileDate=new Date();
	}
	private String dda;
	private Date fileDate;
	private Integer recordCount;
	private BigDecimal totalAmount;
	/**
	 * @return the dda
	 */
	public String getDda() {
		return dda;
	}
	/**
	 * @param dda the dda to set
	 */
	public void setDda(String dda) {
		this.dda = dda;
	}
	/**
	 * @return the fileDate
	 */
	public Date getFileDate() {
		return fileDate;
	}
	/**
	 * @param fileDate the fileDate to set
	 */
	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}
	/**
	 * @return the recordCount
	 */
	public Integer getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return the totalAmount
	 */
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	
	
}
