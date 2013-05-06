/**
 * 
 */
package com.bfds.saec.batch.out.ifds_issue_void;

import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.ifds_issue_void.IfdsIssueVoidRec;
import com.bfds.saec.domain.Payment;

/**
 * @author dt83395
 *
 */
public class IfdsIssueVoidItemProcessor implements
		ItemProcessor<Payment, IfdsIssueVoidRec> {

	@Autowired
	private IfdsOutputBatchService ifdsoutputbatchService;

	@Override
	public IfdsIssueVoidRec process(Payment check) throws Exception {
		return ifdsoutputbatchService.issueVoid(check);
	}

	@AfterStep
	public void sendNotification() {
		ifdsoutputbatchService.notifyIssueVoid();
	}
}
