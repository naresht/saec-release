package com.bfds.saec.batch.out.ss_stop_payment;

import com.bfds.saec.batch.file.domain.out.ss_stop_payment.SsStopPaymentRec;
import com.bfds.saec.batch.out.service.OutputBatchService;
import com.bfds.saec.domain.Payment;

/**
 * Batch Services for State Street Bank Output File
 * 
 */
public interface SStopPaymentBatchService extends OutputBatchService {

	/**
	 * Issue Stop Payment
	 * @param check
	 * @return
	 */
	SsStopPaymentRec issueStopPayment(Payment check, Integer recNum);
}
