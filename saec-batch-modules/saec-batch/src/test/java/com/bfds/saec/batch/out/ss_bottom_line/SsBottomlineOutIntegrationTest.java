package com.bfds.saec.batch.out.ss_bottom_line;

import static org.fest.assertions.Assertions.assertThat;

import java.util.*;

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
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.file.domain.out.ss_bottomline.SsBottomLineOutRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SsBottomlineOutIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {


    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job bottomlineOutJob;

    @Autowired
    SsBottomLineOutTestData eventTestData;

    
    public void beforeAllTests() {
        eventTestData.create();
    }
    
	public void afterAllTests() {
        testDataUtil.deleteData();
	}

    @Override
    protected Job geJOb() {
        return bottomlineOutJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "all").toJobParameters();
    }
    
    /**
     * All {@link Payment}s sent to bank must have {@link Payment#sentOnBankIssueVoidFile} = true
     */
    @Test
    @Transactional
    public void verifySentToBankFlag() {
        List<Payment> list = Lists.newArrayList(Payment.findPaymentIdentificationNo("40001"));        
        assertThat(list).hasSize(1);       
        
    }

    /**
	 * Verify data in stage table.
	 */
	@Test
	public void verifyCreatedFileRecords() {
		Object[][] expectedRecords = new Object[][] {
				{ "accountNumber","100000013", "100000014", "100000015", "100000016"},
				{ "fundCode","fund-123"},
				{ "checkAmount",100.5},
				{ "system","TEST1"},
			    { "offsettingDda","DDA-1"},
				{ "checkName","CHKNAME4SSBBLOUTFILE"},
				{ "registration1","RegistrationName1"},
                { "registration2","RegistrationName2"},
                { "registration3","RegistrationName3"},
                { "registration4","RegistrationName4"},
                { "registration5","address 1"},
                { "registration6","NEWYORK NY 23456"},
                { "registration7","USLLA"}
		};

		final List<SsBottomLineOutRec> actualRecords = testDataUtil.findAllFileRecords(SsBottomLineOutRec.class);
		assertThat(actualRecords).hasSize(4);
		Collections.sort(actualRecords, new Comparator<SsBottomLineOutRec>() {
			@Override
			public int compare(SsBottomLineOutRec o1, SsBottomLineOutRec o2) {
				return o1.getAccountNumber().compareTo(o2.getAccountNumber());
			}
		});
		testDataUtil.verifyData(actualRecords, expectedRecords);
	}
	
	/**
     * All Payments sent on the botton line file must have {@link com.bfds.saec.domain.Payment#getSentOnBottomLineFile()} = True.
     */
    @Test
    public void paymentSentOnFileMustBeFlaggedAsSent() {
        List<Payment> list = Lists.newArrayList();
        list.addAll(removePaymentsWithCheckNo(Payment.findChecksByAccountNoAndNettAmount("100000013", 100.5)));
        list.addAll(removePaymentsWithCheckNo(Payment.findChecksByAccountNoAndNettAmount("100000014", 100.5)));
        list.addAll(removePaymentsWithCheckNo(Payment.findChecksByAccountNoAndNettAmount("100000015", 100.5)));
        list.addAll(removePaymentsWithCheckNo(Payment.findChecksByAccountNoAndNettAmount("100000016", 100.5)));

        assertThat(list).onProperty("sentOnBottomLineFile").containsOnly(Boolean.TRUE);
    }


    private Collection<? extends Payment> removePaymentsWithCheckNo(List<Payment> checks) {
        for(Iterator<Payment> itr = checks.iterator(); itr.hasNext();) {
            Payment p = itr.next();
            if(StringUtils.hasText(p.getIdentificatonNo())) {
                itr.remove();
            }
        }
        return checks;
    }
	
}
