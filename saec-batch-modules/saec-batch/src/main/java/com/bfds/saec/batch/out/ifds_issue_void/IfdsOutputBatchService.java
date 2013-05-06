/**
 * 
 */
package com.bfds.saec.batch.out.ifds_issue_void;

import com.bfds.saec.batch.file.domain.out.ifds_issue_void.IfdsIssueVoidRec;
import com.bfds.saec.domain.Payment;

/**
 * @author dt83395
 *
 */
public interface IfdsOutputBatchService {
	
	IfdsIssueVoidRec issueVoid(Payment check);
	
	 /**
     * Notify Issue Void
     */
    void notifyIssueVoid();

}
