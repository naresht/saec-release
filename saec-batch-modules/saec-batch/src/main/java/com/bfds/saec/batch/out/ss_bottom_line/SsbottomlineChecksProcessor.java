/**
 * 
 */
package com.bfds.saec.batch.out.ss_bottom_line;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.util.PaymentCodeUtil;

 
public class SsbottomlineChecksProcessor implements Tasklet {

	@Autowired
	private PaymentDao dao;

	final Logger log = LoggerFactory
			.getLogger(SsbottomlineChecksProcessor.class);

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {

		List<Payment> checks = dao.findReissueApprovedChecksOlderThan(2);

		for (Payment c : checks) {
			Payment newCheck = c.getNewCheckForReissue();
			if (PaymentCodeUtil.getCheckVoidReissueApprovedCodes().contains(c.getPaymentCode())) {
				c.setPaymentCode(PaymentCodeUtil.getVoidReissueCompletedCodeForGivenVoidReissueApprovedCode(c.getPaymentCode()));
			} else if (PaymentCodeUtil.getCheckStopReplaceApproveCodes().contains(c.getPaymentCode())) {
				c.setPaymentCode(PaymentCodeUtil.getStopReplaceCompletedCodeForGivenStopApprovedCode(c.getPaymentCode()));
			}  else {
				throw new IllegalStateException("Cannot handle PaymentCode "+c.getPaymentCode());
			}
			newCheck.persist();
		}
		return RepeatStatus.FINISHED;
	}

}
