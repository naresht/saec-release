package com.bfds.saec.batch.out.bank_issue_void;

import com.bfds.saec.batch.file.domain.out.db_issue_void.BankIssueVoidRec;
import com.google.common.base.Preconditions;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.out.dto.IssueVoidNotificationDto;
import com.bfds.saec.domain.Payment;
import org.springframework.beans.factory.annotation.Value;

public class IssueVoidItemProcessor implements ItemProcessor<Payment, BankIssueVoidRec>, InitializingBean {

	@Autowired
	private IssueVoidBatchService batchService;

    private String bank;

    @Value("#{stepExecution}")
    protected StepExecution stepExecution;

	@Override
	public BankIssueVoidRec process(Payment check) throws Exception {
		return batchService.issueVoid(check, bank);
	}

	@BeforeStep
	public IssueVoidNotificationDto intiIssueVoidNotification() {
		return batchService.intiDBIssueVoidNotification(bank);
	}

	@AfterStep
	public void sendNotification() {
		batchService.notifyIssueVoid(bank);
	}

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(stepExecution, "stepExecution is null");
        if(bank == null) {
            bank = stepExecution.getJobParameters().getString("bank");
        }
        Preconditions.checkArgument("SS".equals(bank) || "DB".equals(bank), "bank must be either 'SS' or 'DB'. Is %s", bank);
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

}
