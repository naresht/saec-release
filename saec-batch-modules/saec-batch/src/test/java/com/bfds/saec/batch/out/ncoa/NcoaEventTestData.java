package com.bfds.saec.batch.out.ncoa;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.Name;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecDateUtils;

@Component
public class NcoaEventTestData extends com.bfds.saec.batch.util.DataGenerator {

    @Transactional
	public void create() {
        Claimant claimant = newClaimant();
        createEvent();
        Payment c;
        
        Name name=new Name();
        name.setFirstName("Name1");
        name.setMiddleName("Name2");
        name.setLastName("Name3");
        claimant.setName(name); 
        
        c = Payment.newPayment(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
        c.setPaymentType(PaymentType.CHECK);
        c.setIdentificatonNo("557789");
        c.setPaymentAmount(new BigDecimal(100));

        c.setPayTo(claimant);
        claimant.addCheck(c);

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
        c.setIdentificatonNo("1234571");
        c.setPaymentAmount(new BigDecimal(200));

        c.setPayTo(claimant);
        claimant.addCheck(c);

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
        c.setIdentificatonNo("1234591");
        c.setPaymentAmount(new BigDecimal(3050));
        c.setPaymentCode(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
        c.setPayTo(claimant);
        claimant.addCheck(c);

        claimant.persist();
        claimant.flush();
        claimant.clear();

	}
    
	public void createdClaimantWithoutName() {

		Claimant p = new Claimant();
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("RegistrationName1");
		p.getClaimantRegistration().setRegistration2("RegistrationName2");
		p.getClaimantRegistration().setRegistration3("RegistrationName3");
		p.getClaimantRegistration().setRegistration4("RegistrationName4");

		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("Address_1");
		a.setCity("NEWYORK");
		a.setStateCode("NY");
		a.setCountryCode("USA");
		a.setZipCode(new ZipCode("23456", "4455"));
		a.setClaimant(p);
		p.addAddress(a);

		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("222");
		p.setTaxInfo(cl);
		p.setBrokerId("333");
		p.setFundAccountNo("444");
		p.setReferenceNo("55554");

		p.persist();
		p.flush();
		p.clear();

	}

    
    public static void createBankLov() {

		BankLov b = new BankLov();
		b.setActive(true);
		b.setCategory("First");
		b.setCode("SS");
		b.setDescription("Boston Financial Data");
		b.persist();
	}
}
