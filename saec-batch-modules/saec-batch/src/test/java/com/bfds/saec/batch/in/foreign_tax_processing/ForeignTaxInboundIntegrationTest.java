package com.bfds.saec.batch.in.foreign_tax_processing;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

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

import com.bfds.saec.batch.file.domain.in.foreign_tax.ForeignTaxInRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.test.SaecTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class ForeignTaxInboundIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {


    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
    private Job foreignTaxInboundJob;

    @Autowired
    ForeignTaxInEventTestData eventTestData;

    @Autowired
    ForeignTaxInboundFileRecordTestData fileRecordTestData;

    public void afterAllTests() {
        testDataUtil.deleteData();
    }

    public void beforeAllTests() {
        eventTestData.create();
        fileRecordTestData.create();
    }

    @Override
    protected Job geJOb() {
        return foreignTaxInboundJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "all").toJobParameters();
    }
    
    @Test
    @Transactional
    public void testnonW4PAccount()
    {
    	 Claimant parentClaimant = Claimant.findClaimant("101");
         assertThat(parentClaimant.getActivity()).onProperty("description").contains("Ok to reissue with withholding");
         assertThat(parentClaimant.getActivity()).onProperty("activityTypeDescription").contains("File from Tax Processing Entity");
    }
    
    @Test
    @Transactional
    public void nettAndGrossAmountShouldBeComputedforNonW4P() throws Exception {
        Claimant claimant = Claimant.findClaimant("101");
        Payment payment = claimant.getPayments().iterator().next();
        assertThat(payment.getPaymentCalc().getGrossAmount()).isEqualByComparingTo(new BigDecimal(50));
        assertThat(payment.getPaymentCalc().getNettAmount()).isEqualByComparingTo(new BigDecimal(45));
    }
    
    @Test
    @Transactional
    public void nettAndGrossAmountShouldBeComputedforW4P() throws Exception {
    	Claimant alternatePayee = eventTestData.getAlternatePayeeOf("102");
        Payment payment = alternatePayee.getPayments().iterator().next();
        assertThat(payment.getPaymentCalc().getGrossAmount()).isEqualByComparingTo(new BigDecimal(50));
        assertThat(payment.getPaymentCalc().getNettAmount()).isEqualByComparingTo(new BigDecimal(45));
    }
    

    @Test
    @Transactional
    public void theFileRecordMustBeMarkedProcessed() throws Exception {
       ForeignTaxInRec rec = testDataUtil.findAllFileRecords(ForeignTaxInRec.class).get(0);
        assertThat(rec.getProcessed()).isTrue();
    }
    
    
    @Test
    @Transactional
    public void fileRecordWithInvalidDispositionMustNotBeProcessed() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("103");
        assertThat(alternatePayee).isNull();
    }
    
    @Test
    @Transactional
    public void verifyUpdatedPaymentCodeForStopChecks() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("102");
        Payment payment = alternatePayee.getPayments().iterator().next();
        assertThat(payment.getPaymentCode()).isEqualTo(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
    }
    
    @Test
    @Transactional
    public void verifyUpdatedPaymentCodeForVoidChecks() throws Exception {
    	Claimant claimant = Claimant.findClaimant("101");
        Payment payment = claimant.getPayments().iterator().next();
        assertThat(payment.getPaymentCode()).isEqualTo(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
    }
    
    @Test
    @Transactional
    public void verifyAlternatePayeeRegistratinLines() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("102");
        assertThat(alternatePayee.getParentClaimant().getReferenceNo()).isEqualTo("102");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration1()).isEqualTo("RegistrationName1");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration2()).isEqualTo("RegistrationName2");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration3()).isEqualTo("RegistrationName3");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration4()).isEqualTo("RegistrationName4");
        assertThat(alternatePayee.getClaimantRegistration().getRegistration5()).isEqualTo("");
    }
    
    @Test
    @Transactional
    public void alternatePayeeMustHaveAnAddressOfRecord() throws Exception {
        Claimant alternatePayee = eventTestData.getAlternatePayeeOf("102");
        assertThat(alternatePayee.getAddressOfRecord().isMailingAddress()).isTrue();
        assertThat(alternatePayee.getAddressOfRecord().getAddressType()).isEqualTo(AddressType.ADDRESS_OF_RECORD);
        assertThat(alternatePayee.getAddressOfRecord().getAddress1()).isEqualTo("address 1");
        assertThat(alternatePayee.getAddressOfRecord().getAddress2()).isNull();
        assertThat(alternatePayee.getAddressOfRecord().getCity()).isEqualTo("altPayeeCity");
        assertThat(alternatePayee.getAddressOfRecord().getStateCode()).isEqualTo("altPayeeState");
        assertThat(alternatePayee.getAddressOfRecord().getCountryCode()).isEqualTo("altPayeeCountry");
        assertThat(alternatePayee.getAddressOfRecord().getZipCode()).isEqualTo(new ZipCode("44556", "7788"));

    }

}
