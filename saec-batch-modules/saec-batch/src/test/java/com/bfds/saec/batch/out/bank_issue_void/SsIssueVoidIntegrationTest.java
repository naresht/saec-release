package com.bfds.saec.batch.out.bank_issue_void;

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

import com.bfds.saec.batch.file.domain.out.ss_issue_void.SsIssueVoidRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SsIssueVoidIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;
	
	@Autowired
	private Job bankIssueVoidJob;

    @Autowired
    SsIssueVoidTestData eventTestData;

    
    public void beforeAllTests() {
        eventTestData.create();
    }
    
	public void afterAllTests() {
        testDataUtil.deleteData();
	}

    @Override
    protected Job geJOb() {
        return bankIssueVoidJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder()
                .addString("dda", "all")
                .addString("tranchCode", "all")
                .addString("bank", "SS").toJobParameters();
    }
    
    /**
     * All {@link Payment}s sent to bank must have {@link Payment#sentOnBankIssueVoidFile} = true
     */
    @Test
    @Transactional
    public void verifySentToBankFlag() {
        List<Payment> list = Lists.newArrayList(
                Payment.findPaymentIdentificationNo("20001"),
                Payment.findPaymentIdentificationNo("20002"),
                Payment.findPaymentIdentificationNo("20003"),
                Payment.findPaymentIdentificationNo("20004"),

                Payment.findPaymentIdentificationNo("30001"),
                Payment.findPaymentIdentificationNo("30002"),
                Payment.findPaymentIdentificationNo("30003"),
                Payment.findPaymentIdentificationNo("30004"),
                Payment.findPaymentIdentificationNo("30005"),
                Payment.findPaymentIdentificationNo("30006"),

                Payment.findPaymentIdentificationNo("40001"),
                Payment.findPaymentIdentificationNo("40002"),
                Payment.findPaymentIdentificationNo("40003"),
                Payment.findPaymentIdentificationNo("40004"),
                Payment.findPaymentIdentificationNo("40005"));
        
        assertThat(list).hasSize(15);
        assertThat(list).onProperty("sentOnBankIssueVoidFile").containsOnly(Boolean.TRUE);
    }

    /**
	 * Verify data in stage table.
	 */
	@Test
	public void verifyCreatedFileRecords() {
		Object[][] expectedRecords = new Object[][] {
				{ "dda", "DDA-1" },
				{ "checkNumber", "20001", "20002", "20003", "20004", "30001", "30002", "30003", "30004", "30005", "30006", "40001", "40002", "40003", "40004", "40005"},
				{ "checkAmount",100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5},
				{ "additionalData", "RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4","RegistrationName1RegistrationName2RegistrationName3RegistrationName4" },
				{ "voidIndicator", "V","V","V","V","V","V","V","V","V" }
		};

		final List<SsIssueVoidRec> actualRecords = testDataUtil.findAllFileRecords(SsIssueVoidRec.class);
		assertThat(actualRecords).hasSize(15);
		Collections.sort(actualRecords, new Comparator<SsIssueVoidRec>() {
			@Override
			public int compare(SsIssueVoidRec o1, SsIssueVoidRec o2) {
				return o1.getCheckNumber().compareTo(o2.getCheckNumber());
			}
		});
		testDataUtil.verifyData(actualRecords, expectedRecords);
	}
	
	/**
	 * Verify payments with createdPaymentCode will change to appropriate OutStandingCode, once the job runs successfully.  
	 */
	@Test
	public void varifyOutStandingCodeForGivenCreatedCode(){
		List<Payment> list = Lists.newArrayList(
                Payment.findPaymentIdentificationNo("20001"),
                Payment.findPaymentIdentificationNo("20002"),
                Payment.findPaymentIdentificationNo("20003"),
                Payment.findPaymentIdentificationNo("20004"));
		assertThat(list.get(0).getPaymentCode()).isEqualTo(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		assertThat(list.get(1).getPaymentCode()).isEqualTo(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		assertThat(list.get(2).getPaymentCode()).isEqualTo(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		assertThat(list.get(3).getPaymentCode()).isEqualTo(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);	
	}
	
	/**
     * Verify payments with a reissue approved code are not processed.
     */
    @Test
    public void reIssueApprovedChecksMustNotBeProcessed() {
        List<Payment> list = Lists.newArrayList(
                Payment.findPaymentIdentificationNo("10001"),
                Payment.findPaymentIdentificationNo("10002"),
                Payment.findPaymentIdentificationNo("10003"),
                Payment.findPaymentIdentificationNo("10004"),
                Payment.findPaymentIdentificationNo("10005"),
                Payment.findPaymentIdentificationNo("10006"),
                Payment.findPaymentIdentificationNo("10007"),
                Payment.findPaymentIdentificationNo("10008"),
                Payment.findPaymentIdentificationNo("10009"),
                Payment.findPaymentIdentificationNo("10010"));
        assertThat(list).onProperty("paymentCode").containsOnly(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA,
                PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA,
                PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA,
                PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA,
                PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA,
                PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA,
                PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA,
                PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA,
                PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA,
                PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
    }
}
