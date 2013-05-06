package com.bfds.saec.batch.in.db_stop_confirmation;

import java.util.List;

import com.bfds.saec.batch.file.domain.in.db_stop_confirmation.StopConfirmationRec;

/**
 * Batch Services for Deutsche Bank Input File
 * 
 */
public interface StopConfirmationBatchService {

	/**
	 * Process issued Stop Confirmation
	 * 
	 * @param items list of bank records
	 */
	void processStopConfirmations(final List<? extends StopConfirmationRec> items);
}
