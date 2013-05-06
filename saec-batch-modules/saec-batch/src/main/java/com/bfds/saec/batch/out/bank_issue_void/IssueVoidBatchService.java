package com.bfds.saec.batch.out.bank_issue_void;

import com.bfds.saec.batch.file.domain.out.db_issue_void.BankIssueVoidRec;
import com.bfds.saec.batch.out.dto.IssueVoidNotificationDto;
import com.bfds.saec.domain.Payment;

public interface IssueVoidBatchService {

	/**
	 * Issue Void Check
	 * 
	 * @param check
	 * @return
	 */
    BankIssueVoidRec issueVoid(Payment check, String bank);


	 /**
     * Initialize the notification object with defaults and execution specific values.
     */
	IssueVoidNotificationDto intiDBIssueVoidNotification(String bank);

    IssueVoidNotificationDto notifyIssueVoid(String bank);


}
