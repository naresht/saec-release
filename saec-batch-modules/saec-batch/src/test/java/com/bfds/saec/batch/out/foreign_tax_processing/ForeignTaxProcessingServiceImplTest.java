package com.bfds.saec.batch.out.foreign_tax_processing;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import com.bfds.saec.batch.file.domain.out.damasco_foreign.*;
import com.bfds.saec.batch.out.foreign_tax_process.ForeignTaxProcessOutService;
import com.bfds.saec.batch.out.foreign_tax_process.ForeignTaxProcessOutServiceImpl;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;


//@MockStaticEntityMethods
public class ForeignTaxProcessingServiceImplTest {

	private ForeignTaxProcessOutService foreignTaxProcessOutService;
	
	@Before
    public void before() {
		foreignTaxProcessOutService=new ForeignTaxProcessOutServiceImpl();
	}
	
	//@Test 	
	public void testoutboundForeignTaxRecord(){
		Payment payment = newPayment();
		Payment.findPayment(1L);
		AnnotationDrivenStaticEntityMockingControl.expectReturn(payment);
		AnnotationDrivenStaticEntityMockingControl.playback();
		payment = Payment.findPayment(1L);
		payment.getPayTo().setFundAccountNo("1234567");
		payment.getPayTo().setBin("99665");
		payment.setIdentificatonNo("0955643");
		ForeignTaxOutRec rec=foreignTaxProcessOutService.buildOutboundForeignTaxRecord(payment);

		assertThat(rec.getFundAccount()).isEqualTo("1234567");
		assertThat(rec.getParentReg1OrAdd1()).isEqualTo("RegistrationName1");
		assertThat(rec.getBin()).isEqualTo("99665");
		assertThat(rec.getParentReferenceNo()).isEqualTo("100007");
		assertThat(rec.getCheckNumber()).isEqualTo("0955643");
		
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
		Claimant claimant = newClaimant();
		ret.setPayTo(claimant);
		return ret;
	}

	public static Claimant newClaimant() {

		Claimant p = new Claimant();
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("RegistrationName1");
		p.getClaimantRegistration().setRegistration2("RegistrationName2");
		p.getClaimantRegistration().setRegistration3("RegistrationName3");
		p.getClaimantRegistration().setRegistration4("RegistrationName4");
		newPrimaryAddress(p);
		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("112-23-3445");
		p.setTaxInfo(cl);
		p.setBrokerId("5578");
		p.setFundAccountNo("66666");
		p.setReferenceNo("100007");
		return p;
	}

	private static void newPrimaryAddress(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("address 1");
		a.setCity("NEWYORK");
		a.setStateCode("NY");
		a.setCountryCode("USA");
		a.setZipCode(new ZipCode("23456", "4455"));
		a.setClaimant(p);
		p.addAddress(a);
	}
}
