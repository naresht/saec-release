package com.bfds.saec.batch.out.service;

import com.bfds.saec.batch.out.dto.IssueVoidNotificationDto;
import com.bfds.saec.batch.out.dto.StopPaymentNotificationDto;

/**
 * Batch Services Common for Output File
 * 
 */
public interface OutputBatchService {

	/**
	 * Notify Issue Stop Payment
	 */
	StopPaymentNotificationDto notifyIssueStopPayment();

	/**
	 * Initialize the notification object with defaults and execution specific
	 * values.
	 */
	StopPaymentNotificationDto initIssueStopPaymentNotification();

}
