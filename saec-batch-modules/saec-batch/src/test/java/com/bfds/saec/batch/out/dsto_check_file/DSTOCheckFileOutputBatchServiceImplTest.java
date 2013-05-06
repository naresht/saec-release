package com.bfds.saec.batch.out.dsto_check_file;


import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.*;
import com.bfds.saec.domain.reference.PaymentCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

@MockStaticEntityMethods
public class DSTOCheckFileOutputBatchServiceImplTest {

    private DSTOCheckFileOutputBatchService service;

    @Before
    public void before() {
        service = new DSTOCheckFileOutputBatchServiceImpl();
        service.initdstocheckFileNotification();
    }

    /**
     * Tests RegistrationAndAddressesLines Mapping when Registration Lines size is less than 4
     */
    @Test
    public void testRegistrationAndAddressLinesMappingWhenRegistrationLinesAreLessThan4() {
        Payment payment = newPayment();
        Payment.findPayment(1L);
        AnnotationDrivenStaticEntityMockingControl.expectReturn(payment);
        Event event = new Event();
        Event.getCurrentEvent();
        AnnotationDrivenStaticEntityMockingControl.expectReturn(event);
        AnnotationDrivenStaticEntityMockingControl.playback();

        payment.getCheckAddress().setAddress1("cc");
        payment.getCheckAddress().setAddress2("dd");
        DstoRec rec =  service.generateCheckFile(payment,null);

        assertThat(rec.getReg1OrAdd1()).isEqualTo("aa");
        assertThat(rec.getReg2OrAdd2()).isEqualTo("bb");
        assertThat(rec.getReg3OrAdd3()).isEqualTo("cc");
        assertThat(rec.getReg4OrAdd4()).isEqualTo("dd");
    }
    
    /**
     * Tests RegistrationAndAddressesLines Mapping when Registration Lines size is more than 4
     */
    @Test
    public void testRegistrationAndAddressLinesMappingWhenRegistrationLinesAreMoreThan4() {
        Payment payment = newPayment();
        payment.getPayTo().getClaimantRegistration().setRegistration3("r3");
        payment.getPayTo().getClaimantRegistration().setRegistration4("r4");
        payment.getPayTo().getClaimantRegistration().setRegistration5("r5");
        Payment.findPayment(1L);
        AnnotationDrivenStaticEntityMockingControl.expectReturn(payment);
        Event event = new Event();
        Event.getCurrentEvent();
        AnnotationDrivenStaticEntityMockingControl.expectReturn(event);
        AnnotationDrivenStaticEntityMockingControl.playback();

        payment.getCheckAddress().setAddress1("a1");
        payment.getCheckAddress().setAddress2("a2");
        DstoRec rec =  service.generateCheckFile(payment,null);

        assertThat(rec.getReg1OrAdd1()).isEqualTo("aa");
        assertThat(rec.getReg2OrAdd2()).isEqualTo("bb");
        assertThat(rec.getReg3OrAdd3()).isEqualTo("r3");
        assertThat(rec.getReg4OrAdd4()).isEqualTo("r4");
        assertThat(rec.getReg5OrAdd5()).isEqualTo("a1");
        assertThat(rec.getReg6OrAdd6()).isEqualTo("a2");
    }

    /**
     * If Country code is US/USA then country code must always be set to USA.
     */
    @Test
    public void testUSCountrySpecificProcessing() {
        Payment payment = newPayment();
        Payment.findPayment(1L);
        AnnotationDrivenStaticEntityMockingControl.expectReturn(payment);
        Event event = new Event();
        Event.getCurrentEvent();
        AnnotationDrivenStaticEntityMockingControl.expectReturn(event);
        AnnotationDrivenStaticEntityMockingControl.playback();
        payment.getCheckAddress().setCountryCode("US");
        DstoRec rec =  service.generateCheckFile(payment,null);

        assertThat(rec.getCountry()).isEqualTo("USA");

    }

    /**
     * If Country is not US/USA then, we will need to pass City/St/Zip in Reg/Address (so we pass Registration then Address then City State Zip on the next line pass Country.)
     */
    @Test
    public void testNonUSCountrySpecificProcessing() {
        Payment payment = newPayment();
        Payment.findPayment(1L);
        AnnotationDrivenStaticEntityMockingControl.expectReturn(payment);
        Event event = new Event();
        Event.getCurrentEvent();
        AnnotationDrivenStaticEntityMockingControl.expectReturn(event);
        AnnotationDrivenStaticEntityMockingControl.playback();
        payment.getCheckAddress().setCountryCode("IN");
        payment.getCheckAddress().setCity("Hyderabad");
        payment.getCheckAddress().setStateCode("A.P");
        payment.getCheckAddress().setZip("500073");
        DstoRec rec =  service.generateCheckFile(payment,null);

        assertThat(rec.getReg3OrAdd3()).isEqualTo("Hyderabad A.P 500073");
        assertThat(rec.getReg4OrAdd4()).isEqualTo("IN");
    }

    @Test
    public void testSsnTruncation() {
        Payment payment = newPayment();
        Payment.findPayment(1L);
        AnnotationDrivenStaticEntityMockingControl.expectReturn(payment);
        Event event = new Event();
        Event.getCurrentEvent();
        AnnotationDrivenStaticEntityMockingControl.expectReturn(event);
        AnnotationDrivenStaticEntityMockingControl.playback();

        payment.getPayTo().getTaxInfo().setSsn("445-44-4040");

        DstoRec rec =  service.generateCheckFile(payment,null);

        assertThat(rec.getTaxID()).isEqualTo("445444040");
    }

    private Payment newPayment() {
        Payment ret = new Payment() {
            @Override
            public Payment merge() {
               return this;
            }
        };
        ret.setId(1L);
        ret.setCheckAddress(new MailObjectAddress());
        ret.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        ret.setPaymentCalc(new PaymentCalc());
        ret.setPaymentAmount(new BigDecimal(100));
        Claimant claimant = new Claimant();
        claimant.setTaxInfo(new ClaimantTaxInfo());
        claimant.setClaimantRegistration(new ClaimantRegistration());
        claimant.getClaimantRegistration().setRegistration1("aa");
        claimant.getClaimantRegistration().setRegistration2("bb");
        claimant.setAddressOfRecord(new ClaimantAddress());
        ret.setPayTo(claimant);
        return ret;
    }
}
