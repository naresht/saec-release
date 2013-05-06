package com.bfds.saec.batch.out.bank_issue_void;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.google.common.base.Preconditions;

/**
 * New {@link Payment}s are created for all reissue approved {@link Payment}s.
 */
public class ReissueApprovedChecksProcessor implements Tasklet, InitializingBean {

    final Logger log = LoggerFactory.getLogger(ReissueApprovedChecksProcessor.class);
    
    @Value("#{stepExecution}")
    protected StepExecution stepExecution;

    private String bank;
    
    @Override
    @Transactional
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
    	
    	if("SS".equals(bank) && Boolean.TRUE.equals(Event.getCurrentEvent().getUseBottomLineForCheckNoAssignment())) {
            // Reissue approved checks will be processed by bottom line process.
            return null;
        }

        List<Payment> checks = Payment.findReissueApprovedChecksOlderThan(2);

        for (Payment c : checks) {
            Preconditions.checkState(PaymentCodeUtil.getCheckVoidReissueApprovedCodes().contains(c.getPaymentCode())
                                     || PaymentCodeUtil.getCheckStopReplaceApproveCodes().contains(c.getPaymentCode()),
                    "Unsuuported paymentCode %s for Payment : %s", c.getPaymentCode(), c.getIdentificatonNo());

            final String newCheckNo = Event.getCurrentEvent().getNextCheckSequenceNumber();
            if (!StringUtils.hasText(newCheckNo)) {
                log.error("Generated check# is null. Skipping Payment:" + c.getIdentificatonNo());
                continue;
            }

            Payment newCheck = c.getNewCheckForReissue();
            if(log.isInfoEnabled()) {
                log.info(String.format("Assigning new Check#: %s to check ID: %s.", newCheckNo, newCheck.getId()));
            }
            newCheck.setIdentificatonNo(newCheckNo);
            if (PaymentCodeUtil.getCheckVoidReissueApprovedCodes().contains(c.getPaymentCode())) {
                c.setPaymentCode(PaymentCodeUtil.getVoidReissueCompletedCodeForGivenVoidReissueApprovedCode(c.getPaymentCode()));
            } else if (PaymentCodeUtil.getCheckStopReplaceApproveCodes().contains(c.getPaymentCode())) {
                c.setPaymentCode(PaymentCodeUtil.getStopReplaceCompletedCodeForGivenStopApprovedCode(c.getPaymentCode()));
            }
            newCheck.persist();
        }
       (new Claimant()).flush();
       (new Claimant()).clear();
        
        return RepeatStatus.FINISHED;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(stepExecution, "stepExecution is null");
        if(bank == null) {
            bank = stepExecution.getJobParameters().getString("bank");
        }
        Preconditions.checkArgument("SS".equals(bank) || "DB".equals(bank), "bank must be either 'SS' or 'DB'. Is %s", bank);
    }
}
