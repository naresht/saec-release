package com.bfds.saec.batch.in.ss_bottom_line;

import java.util.List;

import com.bfds.saec.batch.file.domain.in.ss_bottom_line.SsBottomLineInRec;


public interface BLInboundBatchService {
	/**
	 * Process Bottom Line In bound Checks
	 * 
	 * @param items
	 */
	void processBLInboundCheck(List<? extends SsBottomLineInRec> items);

	/**
	 * leverage the email distribution list in the dashboard for the bank
	 * processing file notification type
	 */

	BottomLineNotificationDto notificationMail();

}
