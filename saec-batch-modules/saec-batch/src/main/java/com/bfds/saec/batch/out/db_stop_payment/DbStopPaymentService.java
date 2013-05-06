package com.bfds.saec.batch.out.db_stop_payment;

import com.bfds.saec.batch.file.domain.out.db_stop_payment.DbStopPaymentRec;
import com.bfds.saec.batch.out.dto.StopPaymentNotificationDto;
import com.bfds.saec.batch.out.service.OutputBatchService;
import com.bfds.saec.domain.Payment;

/**
 * Batch Services for Deutsche Bank Output File
 * 
 */
public interface DbStopPaymentService extends OutputBatchService {


	/**
	 * Issue Stop Payment
	 * 
	 * @param check
	 * @return
	 */
	DbStopPaymentRec issueStopPayment(Payment check);

	 /**
     * Initialize the notification object with defaults and execution specific values.
     */
	StopPaymentNotificationDto initDBIssueStopPaymentNotification();

}
