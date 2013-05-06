package com.bfds.saec.batch.in.ss_cashed_check;


import java.util.List;

import com.bfds.saec.batch.file.domain.in.ss_cashed_check.SsCashedCheckRec;

/**
 * Batch Services for State Street Bank Input File
 * 
 */
public interface SsCashedCheckBatchService {

	/**
	 * Process Cashed Checks
	 * @param items
	 */
	void processCashedCheck(List<? extends SsCashedCheckRec> items);

	/**
	 * Issue a bank notification
	 */
	CashedCheckItemNotificationDto notifyBank();
}
