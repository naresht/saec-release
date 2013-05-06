/**
 * 
 */
package com.bfds.saec.batch.out.ifds_check_status;

import com.bfds.saec.batch.file.domain.out.ifds_check_status.IfdsCheckStatusRec;
import com.bfds.saec.domain.Payment;

/**
 * @author dt83395
 * 
 */
public interface IfdsCheckStatusBatchService extends
		IfdsCheckStatusNotifyService {

	IfdsCheckStatusRec getCheckStatus(Payment check);

}