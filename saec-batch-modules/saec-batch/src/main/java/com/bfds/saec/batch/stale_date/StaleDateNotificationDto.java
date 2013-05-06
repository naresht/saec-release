package com.bfds.saec.batch.stale_date;

import java.util.Date;


/**
 * Notification Bean to hold Stale date processing Statistics
 */

public class StaleDateNotificationDto {

	private String businessArea;
	
	/**
	 * Date of scan
	 */
	private Date scanDate;
	/**
	 * total # of check objects in an outstanding status for that event
	 */
	private Long totalOutstandingChecks;
	/**
	 * total # of objects where status was changed to stale date as a result of the scan
	 */
	private int checksStaleDated;
	public String getBusinessArea() {
		return businessArea;
	}
	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}
	public Date getScanDate() {
		return scanDate;
	}
	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}
	public Long getTotalOutstandingChecks() {
		return totalOutstandingChecks;
	}
	public void setTotalOutstandingChecks(Long totalOutstandingChecks) {
		this.totalOutstandingChecks = totalOutstandingChecks;
	}
	public int getChecksStaleDated() {
		return checksStaleDated;
	}
	public void setChecksStaleDated(int checksStaleDated) {
		this.checksStaleDated = checksStaleDated;
	}
	
}
