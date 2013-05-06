/**
 * 
 */
package com.bfds.saec.batch.out.ifds_check_status;

import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.ifds_check_status.IfdsCheckStatusRec;
import com.bfds.saec.domain.Payment;

/**
 * @author dt83395
 * 
 */
public class IfdsCheckStatusItemProcessor implements
		ItemProcessor<Payment, IfdsCheckStatusRec> {

	@Autowired
	private IfdsCheckStatusBatchService ifdsCheckStatusBatchService;

	@Override
	public IfdsCheckStatusRec process(Payment check) throws Exception {
		return ifdsCheckStatusBatchService.getCheckStatus(check);
	}

	@AfterStep
	public void sendNotification() {
		ifdsCheckStatusBatchService.notifyCheckStatus();
	}
}
