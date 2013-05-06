package com.bfds.saec.batch.out.bank_issue_void;

import static org.fest.assertions.Assertions.assertThat;

import com.bfds.saec.batch.file.domain.out.db_issue_void.DbIssueVoidRec;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.google.common.collect.Lists;
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

import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.test.SaecTestExecutionListener;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})

public class DbIssueVoidIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job bankIssueVoidJob;

    @Autowired
    DbIssueVoidEventTestData eventTestData;

    
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
                  .addString("bank", "DB").toJobParameters();
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

        list.addAll(testDataUtil.getAllReissues());

        Claimant claimant = Claimant.findClaimant("100000002");

        list.addAll(claimant.getPayments());

        assertThat(list).hasSize(24);
        assertThat(list).onProperty("sentOnBankIssueVoidFile").containsOnly(Boolean.TRUE);
    }

    /**
     *  At the end of job execution all re-issue approved checks that are eligible must become re-issue completed.
     */
	@Test
	public void verifyReissueCompleted() {

        List<Payment> list = Lists.newArrayList(
                Payment.findPaymentIdentificationNo("10001"),
                Payment.findPaymentIdentificationNo("10002"),
                Payment.findPaymentIdentificationNo("10003"),
                Payment.findPaymentIdentificationNo("10004"),
                Payment.findPaymentIdentificationNo("10005"));
        assertThat(list).hasSize(5);
        assertThat(list).onProperty("paymentCode").containsOnly(
                PaymentCode.VOID_DAMASCO_RE_ISSUE_COMPLETED_XX_VDX,
                PaymentCode.VOID_HOLD_RE_ISSUE_COMPLETED_XX_VHX,
                PaymentCode.VOID_RE_ISSUE_COMPLETED_XX_VX,
                PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_COMPLETED_XX_VFX,
                PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_COMPLETED__VNX);

	}

    /**
     *  At the end of job execution all re-issue approved checks that are not-eligible must remain re-issue approved.
     */
    @Test
    public void verifyReissueNoChange() {

        List<Payment> list = Lists.newArrayList(
                Payment.findPaymentIdentificationNo("10006"),
                Payment.findPaymentIdentificationNo("10007"),
                Payment.findPaymentIdentificationNo("10008"),
                Payment.findPaymentIdentificationNo("10009"),
                Payment.findPaymentIdentificationNo("10010"));
        assertThat(list).hasSize(5);
        assertThat(list).onProperty("paymentCode").containsOnly(
                PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA,
                PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA,
                PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA,
                PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA,
                PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
    }


    /**
     *  At checks that are marked res-issue completed must have new checks created.
     *  The new checks should also be marked outstanding.
     */
    @Test
    @Transactional
    public void newChecksMustBeCreatedForReissueCompleted() {
        List<Payment> list = testDataUtil.getAllReissues();
        assertThat(list).hasSize(5);
        assertThat(list).onProperty("reissueOf").onProperty("identificatonNo").containsOnly("10001", "10002", "10003", "10004", "10005");

        assertThat(list).onProperty("paymentCode").containsOnly(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);

    }

    /**
     *  At checks that are marked res-issue completed must have new checks created.
     *  The new checks should have the same {@link com.bfds.saec.domain.PaymentCalc}s values as that of the re-issue completed check.
     */
    @Test
    @Transactional
    public void newChecksForReissueCompletedMustHavePaymentCalc() {
        List<Payment> list = testDataUtil.getAllReissues();
        assertThat(list).hasSize(5);

        assertThat(list).onProperty("paymentCalc").onProperty("grossAmount").containsOnly(new BigDecimal(150).setScale(2));
        assertThat(list).onProperty("paymentCalc").onProperty("fedWithholding").containsOnly(new BigDecimal(49.5).setScale(2));
        assertThat(list).onProperty("paymentCalc").onProperty("nettAmount").containsOnly(new BigDecimal(100.5).setScale(2));
    }

    /**
     *  At the end of job execution all created checks sent to the bank must be marked outstanding.
     */
    @Test
    public void createdChecksMustBecomeOutstanding() {

        List<Payment> list = Lists.newArrayList(
                Payment.findPaymentIdentificationNo("20001"),
                Payment.findPaymentIdentificationNo("20002"),
                Payment.findPaymentIdentificationNo("20003"),
                Payment.findPaymentIdentificationNo("20004"));
        assertThat(list).hasSize(4);
        assertThat(list).onProperty("paymentCode").containsOnly(
                PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO,
                PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO,
                PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO,
                PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
    }

    /**
     *  At the end of job execution all created checks that do not have a check # must have a new check # assigned. They must also be marked outstanding.
     */
    @Test
    @Transactional
    public void createdChecksThatDoNotHaveACheckNoMustNowHaveACheckNo() {
        Claimant claimant = Claimant.findClaimant("100000002");

        Collection<Payment> list = claimant.getPayments();
        assertThat(list).hasSize(4);
        assertThat(list).onProperty("paymentCode").containsOnly(
                PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO,
                PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO,
                PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO,
                PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
    }

    /**
     * Verify data in stage table.
     */
    @Test
    public void verifyCreatedFileRecords() {
        Object[][] expectedRecords =
            new Object[][] {
                {"dda", Event.getCurrentEventDda()},
                {"checkNumber", "20001","20002","20003","20004","30001","30002","30003","30004","30005","30006","40001","40002","40003","40004","40005"}
                //TODO: verify other properties.
            };

        final List<DbIssueVoidRec> actualRecords = testDataUtil.findAllFileRecords(DbIssueVoidRec.class);
        assertThat(actualRecords).hasSize(24);
        Collections.sort(actualRecords, new Comparator<DbIssueVoidRec>() {
            @Override
            public int compare(DbIssueVoidRec o1, DbIssueVoidRec o2) {
                return o1.getCheckNumber().compareTo(o2.getCheckNumber());
            }
        });
        testDataUtil.verifyData(actualRecords, expectedRecords);
    }

}