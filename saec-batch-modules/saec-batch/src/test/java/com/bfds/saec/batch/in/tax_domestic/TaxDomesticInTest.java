package com.bfds.saec.batch.in.tax_domestic;

import com.bfds.saec.batch.file.domain.in.damasco_domestic.DamascoInRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.activity.AlternatePayeeActivity;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.test.SaecTestExecutionListener;
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

import java.math.BigDecimal;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})

public class TaxDomesticInTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

    @Autowired
    private Job taxDomesticInJob;

    @Autowired
    TaxDomesticInEventTestData eventTestData;

    @Autowired
    TaxDomesticInFileRecordTestData fileRecordTestData;

    @Autowired
    private PaymentDao dao;

    public void afterAllTests() {
        testDataUtil.deleteData();
    }

    public void beforeAllTests() {
        eventTestData.create();
        fileRecordTestData.create();
    }

    @Override
    protected Job geJOb() {
        return taxDomesticInJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "all").toJobParameters();
    }

    @Test
    @Transactional
    public void alternatePayeeMustBeCreated() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("101");
        assertThat(alternatePayee.getParentClaimant().getReferenceNo()).isEqualTo("101");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration1()).isEqualTo("name11");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration2()).isEqualTo("name12");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration3()).isEqualTo("name13");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration4()).isEqualTo("name14");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration5()).isEqualTo("name15");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration6()).isEqualTo("name16");
    }

    @Test
    @Transactional
    public void alternatePayeeMustHaveAnAddressOfRecord() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("101");
        assertThat(alternatePayee.getAddressOfRecord().isMailingAddress()).isTrue();
        assertThat(alternatePayee.getAddressOfRecord().getAddressType()).isEqualTo(AddressType.ADDRESS_OF_RECORD);
        assertThat(alternatePayee.getAddressOfRecord().getAddress1()).isEqualTo("addr11");
        assertThat(alternatePayee.getAddressOfRecord().getAddress2()).isEqualTo("addr12");
        assertThat(alternatePayee.getAddressOfRecord().getAddress3()).isEqualTo("addr13");
        assertThat(alternatePayee.getAddressOfRecord().getAddress4()).isEqualTo("addr14");
        assertThat(alternatePayee.getAddressOfRecord().getAddress5()).isEqualTo("addr15");
        assertThat(alternatePayee.getAddressOfRecord().getAddress6()).isEqualTo("addr16");
        assertThat(alternatePayee.getAddressOfRecord().getCity()).isEqualTo("city1");
        assertThat(alternatePayee.getAddressOfRecord().getStateCode()).isEqualTo("state1");
        assertThat(alternatePayee.getAddressOfRecord().getCountryCode()).isEqualTo("us");
        assertThat(alternatePayee.getAddressOfRecord().getZipCode()).isEqualTo(new ZipCode("11111", "1111"));

    }

    @Test
    @Transactional
    public void parentClaimantMustHaveTaxProcessingActivity() throws Exception {
        Claimant parentClaimant = Claimant.findClaimant("101");
        assertThat(parentClaimant.getActivity()).onProperty("description").contains("Ok to reissue with withholding");
        assertThat(parentClaimant.getActivity()).onProperty("activityTypeDescription").contains("File from Tax Processing Entity");
    }

    @Test
    @Transactional
    public void alternatePayeeMustHaveAlternatePayeeActivity() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("101");
        assertThat(alternatePayee.getActivity()).onProperty("activityTypeDescription").contains(AlternatePayeeActivity.ACTIVITY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void parentClaimantMustHaveAlternatePayeeActivity() throws Exception {
        Claimant parentClaimant = Claimant.findClaimant("101");
        assertThat(parentClaimant.getActivity()).onProperty("activityTypeDescription").contains(AlternatePayeeActivity.ACTIVITY_TYPE_DESCRIPTION);
    }
    
    @Test
    @Transactional
    public void paymentHasBeenSplitValueMustbeTrue() throws Exception {
        Claimant parentClaimant = Claimant.findClaimant("101");
        Payment payment = parentClaimant.getPayments().iterator().next();
        assertThat(payment.getHasBeenSplit()).isEqualTo(Boolean.TRUE);
    }


    @Test
    @Transactional
    public void alternatePayeeMustHaveAPayment() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("101");
        assertThat(alternatePayee.getPayments().size()).isEqualTo(1);

        Payment payment = alternatePayee.getPayments().iterator().next();
        assertThat(payment.getReissueOf().getIdentificatonNo()).isEqualTo("100001");
        assertThat(payment.getPaymentCode()).isEqualTo(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CREATED);
        assertThat(payment.getSplitFromPayment().getIdentificatonNo()).isEqualTo("100001");

        assertThat(payment.getPaymentCalc().getPaymentComp1()).isEqualByComparingTo(new BigDecimal(10));
        assertThat(payment.getPaymentCalc().getPaymentComp2()).isEqualByComparingTo(new BigDecimal(20));
        assertThat(payment.getPaymentCalc().getPaymentComp3()).isEqualByComparingTo(new BigDecimal(30));

        assertThat(payment.getPaymentCalc().getFedWithholding()).isEqualByComparingTo(new BigDecimal(1));
        assertThat(payment.getPaymentCalc().getStateWithholding()).isEqualByComparingTo(new BigDecimal(2));

        assertThat(payment.getPaymentCalc().getIntFedWithholding()).isEqualByComparingTo(new BigDecimal(5));
        assertThat(payment.getPaymentCalc().getIntStateWithholding()).isEqualByComparingTo(new BigDecimal(10));
    }

    @Test
    @Transactional
    public void nettAndGrossAmountShouldBeComputed() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("101");
        Payment payment = alternatePayee.getPayments().iterator().next();
        assertThat(payment.getPaymentCalc().getGrossAmount()).isEqualByComparingTo(new BigDecimal(60));
        assertThat(payment.getPaymentCalc().getNettAmount()).isEqualByComparingTo(new BigDecimal(42));
    }

    @Test
    @Transactional
    public void nettAndGrossAmountShouldNotBeComputed() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("102");
        Payment payment = alternatePayee.getPayments().iterator().next();
        assertThat(payment.getPaymentCalc().getGrossAmount()).isEqualByComparingTo(new BigDecimal(60));
        assertThat(payment.getPaymentCalc().getNettAmount()).isEqualByComparingTo(new BigDecimal(60));;
    }

    @Test
    @Transactional
    public void alternatePayeesAndItsPaymentMustHaveTheSameAddress() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("101");
        Payment payment = alternatePayee.getPayments().iterator().next();
        assertThat(alternatePayee.getMailingAddress().getAddress()).isEqualTo(payment.getCheckAddress().getAddress());
    }

    @Test
    @Transactional
    public void theFileRecordMustBeMarkedProcessed() throws Exception {
        DamascoInRec rec = testDataUtil.findAllFileRecords(DamascoInRec.class).get(0);
        assertThat(rec.getProcessed()).isTrue();
    }

    @Test
    @Transactional
    public void fileRecordWithInvalidDispositionMustNotBeProcessed() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("103");
        assertThat(alternatePayee).isNull();
        List<Payment> reissues = Payment.entityManager().createQuery("select c from Payment as c where c.reissueOf.identificatonNo = :identificatonNo", Payment.class)
                               .setParameter("identificatonNo", "100003").getResultList();
        assertThat(reissues).isEmpty();
    }
}
