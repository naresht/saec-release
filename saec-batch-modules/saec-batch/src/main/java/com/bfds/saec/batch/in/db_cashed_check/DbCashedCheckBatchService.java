package com.bfds.saec.batch.in.db_cashed_check;

import java.util.List;

import com.bfds.saec.batch.file.domain.in.db_cashed_check.CashedCheckRec;


public interface DbCashedCheckBatchService {

	/**
	 * Process issued Paid Checks
	 * 
	 * @param items list of bank records
	 */
	void processPaidChecks(List<? extends CashedCheckRec> items);

	
	/**
	 * Notify Bank on Paid Checks
	 */
	PaidCheckNotificationDto notifyPaidChecks();

	
}
