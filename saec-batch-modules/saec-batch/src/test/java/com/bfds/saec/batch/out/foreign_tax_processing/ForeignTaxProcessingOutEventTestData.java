package com.bfds.saec.batch.out.foreign_tax_processing;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.EventPaymentConfig;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentComponentTypeLov;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentComponentType;
import com.bfds.saec.domain.reference.PaymentType;

@Component
public class ForeignTaxProcessingOutEventTestData extends com.bfds.saec.batch.util.DataGenerator{
	
	@Transactional
	public void create() {
		createEvent();
	    createEventPaymentConfig(PaymentComponentType.paymentComp1, "component1");
	    createEventPaymentConfig(PaymentComponentType.paymentComp2, "component2");
	    createEventPaymentConfig(PaymentComponentType.paymentComp3, "component3");
	    createEventPaymentConfig(PaymentComponentType.paymentComp4, "component4");
	    createEventPaymentConfig(PaymentComponentType.paymentComp5, "component5");
		Claimant claimant = newClaimant1();
        Payment c;

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("00001011");
        c.setPaymentAmount(new BigDecimal(550));
        c.setPayTo(claimant);
        c.setPaymentCode(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
        c.setIfdsSent(true);
        c.setPaymentDate(new Date());
        c.setSentOnTaxFile(false);
        claimant.addCheck(c);

        Payment reIssueOf = c;
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.WIRE);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("20047");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);
		c.setPaymentDate(new Date());
		c.setPaymentCode(PaymentCode.WIRE_APPROVED_WA_WA);
        c.setReissueOf(reIssueOf);
		claimant.addCheck(c);
		
        claimant.persist();
        claimant.flush();
        claimant.clear();
        
        Claimant claimant2 = newClaimant2();
        Payment c1;

        c1 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c1.setPaymentType(PaymentType.CHECK);
        c1.setStatusChangeDate(new Date(2011, 9, 1));
        c1.setIdentificatonNo("00060334");
        c1.setPaymentAmount(new BigDecimal(550));
        c1.setPayTo(claimant2);
        c1.setPaymentCode(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
        c1.setIfdsSent(true);
        claimant2.addCheck(c1);
        
        claimant2.setParentClaimant(claimant);
        claimant2.persist();
        claimant2.flush();
        claimant2.clear();
        
	}
	

    public static Claimant newClaimant1() {

		Claimant p = new Claimant();
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("RegistrationName1");
		p.getClaimantRegistration().setRegistration2("RegistrationName2");
		p.getClaimantRegistration().setRegistration3("RegistrationName3");
		p.getClaimantRegistration().setRegistration4("RegistrationName4");
		p.getClaimantRegistration().setRegistration5("RegistrationName5");
		p.getClaimantRegistration().setRegistration6("RegistrationName6");
		newPrimaryAddress(p);
		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("112-23-3445");
		cl.setTin("647483");
		cl.setForeignTax(true);
		p.setTaxInfo(cl);
		p.setBrokerId("5578");
		p.setBin("883398");
		p.setFundAccountNo("66666");
		p.setReferenceNo("100007");
		p.setCertificationType("W4P");
		 
		return p;
	}

	private static void newPrimaryAddress(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("address 1");
		a.setAddress2("address 2");
		a.setAddress3("address 3");
		a.setAddress4("address 4");
		a.setAddress5("address 5");
		a.setAddress6("address 6");
		a.setCity("NEWYORK");
		a.setStateCode("NY");
		a.setCountryCode("USA");
		a.setZipCode(new ZipCode("23456", "4455"));
		// a.setZipCode(new ZipCode((String) line.get("ZipCode"), null));
		a.setClaimant(p);
		p.addAddress(a);
	}
	
	
	public static Claimant newClaimant2() {

		Claimant p = new Claimant();
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("RegistrationName11");
		p.getClaimantRegistration().setRegistration2("RegistrationName22");
		p.getClaimantRegistration().setRegistration3("RegistrationName33");
		p.getClaimantRegistration().setRegistration4("RegistrationName44");
		p.getClaimantRegistration().setRegistration5("RegistrationName55");
		p.getClaimantRegistration().setRegistration6("RegistrationName66");
		newPrimaryAddress(p);
		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("112-23-3445");
		cl.setForeignTax(false);
		p.setTaxInfo(cl);
		p.setBrokerId("5578");
		p.setFundAccountNo("88995");
		p.setReferenceNo("10679");
		p.setCertificationType("Wc");

		return p;
	}
    private void createEventPaymentConfig(PaymentComponentType paymentComponentType, String description) {
        EventPaymentConfig config = new EventPaymentConfig();
        config.setDefaultDescription(description);
        PaymentComponentTypeLov paymentComponentTypeLov = new PaymentComponentTypeLov();
        paymentComponentTypeLov.setCode(paymentComponentType.name());
        paymentComponentTypeLov.setDescription("NA");
        paymentComponentTypeLov.persist();
        config.setPaymentComponentType(paymentComponentTypeLov);
        config.setEvent(Event.getCurrentEvent());
        config.persist();
    }

}
