package com.bfds.saec.batch.out.ifds_issue_void;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.out.ifds_issue_void.IfdsIssueVoidRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class IfdsIssueVoidIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job IfdsIssueVoidJob;

    @Autowired
    IfdsIssueVoidEventTestData eventTestData;

    
    public void beforeAllTests() {
        eventTestData.create();
    }
    
	public void afterAllTests() {
        testDataUtil.deleteData();
	}

    @Override
    protected Job geJOb() {
        return IfdsIssueVoidJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "11111111").toJobParameters();
    }
	
    
    @Test
    @Transactional
    public void verifySentToBankFlag() {
    	 List<Payment> list = Lists.newArrayList(
                 Payment.findPaymentIdentificationNo("12345"),
                 Payment.findPaymentIdentificationNo("55555555"),
                 Payment.findPaymentIdentificationNo("12346"),
                 Payment.findPaymentIdentificationNo("66666666"),
                 Payment.findPaymentIdentificationNo("777777"),
    	 		 Payment.findPaymentIdentificationNo("88888888"));
    	 

         assertThat(list).onProperty("ifdsSent").containsOnly(Boolean.TRUE);
    }
    
    @Test
    @Transactional
    public void VerifycheckReissue() {
    	Payment payment =    Payment.findPaymentIdentificationNo("12346");
    	
    	List<Payment> payments = Lists.newArrayList(payment);
    	
    	assertThat(payments).onProperty("reissueOf").onProperty("identificatonNo").containsOnly("12347");
    }
    
    
    @Test
    public void verifyCreatedFileRecords() {
        Object[][] expectedRecords =
            new Object[][] {
                {"dda", Event.getCurrentEventDda()},
                {"checkNumber", "12345","12346","55555555","66666666","777777","88888888"}
            };

        final List<IfdsIssueVoidRec> actualRecords = testDataUtil.findAllFileRecords(IfdsIssueVoidRec.class);
        assertThat(actualRecords).hasSize(6);
        Collections.sort(actualRecords, new Comparator<IfdsIssueVoidRec>() {
            @Override
            public int compare(IfdsIssueVoidRec o1, IfdsIssueVoidRec o2) {
                return o1.getCheckNumber().compareTo(o2.getCheckNumber());
            }
        });
        testDataUtil.verifyData(actualRecords, expectedRecords);
    }
}
