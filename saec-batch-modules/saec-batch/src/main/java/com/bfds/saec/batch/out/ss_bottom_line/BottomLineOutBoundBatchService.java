package com.bfds.saec.batch.out.ss_bottom_line;

import com.bfds.saec.batch.file.domain.out.ss_bottomline.SsBottomLineOutRec;
import com.bfds.saec.domain.Payment;

public interface BottomLineOutBoundBatchService {

	/**
	 * Newly created Check with null check number
	 * 
	 * @param check
	 * @return
	 */
	SsBottomLineOutRec issueBottomLine(Payment check);

	/**
	 * Initialize the notification object with defaults and execution specific
	 * values.
	 */
	BottomLineOutBoundNotificationDto initOBBottomLineOutNotification();
	
	/**
	 * Notify BottomLine OutBound Notification
	 */
	BottomLineOutBoundNotificationDto notifySsBottomLineOut();
	
	
	
}
