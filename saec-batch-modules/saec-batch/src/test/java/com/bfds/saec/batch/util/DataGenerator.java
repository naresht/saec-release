package com.bfds.saec.batch.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.bfds.saec.domain.*;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecDateUtils;

public class DataGenerator {

	public static void createEvent() {
		Event e = new Event();
		e.setDda("DDA-1");
		e.setCode("TEST1");
		e.setBankCode("SS");
		Bank bank = new Bank();
		bank.setAbaNo("2234");
		e.setBank(bank);
		e.setDeutscheBankUserId("USER-123");
		e.setCode("TEST1");
		e.setCheckNameforBottomlineOutFile("CHKNAME4SSBBLOUTFILE");
		e.persist();
		e.flush();

	}

    public static void createTranch(String code) {
        Tranch e = new Tranch();
        e.setCode(code);
        e.persist();
        e.flush();

    }

	public static void createBankLov() {

		BankLov b = new BankLov();
		b.setActive(true);
		b.setCategory("First");
		b.setCode("SS");
		b.setDescription("Boston Financial Data");
		b.persist();
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
		// a.setZipCode(new ZipCode((String) line.get("ZipCode"), null));
		a.setClaimant(p);
		p.addAddress(a);
	}

	public static void SsibCashedCheckJobData() {
		createEvent();
		Claimant claimant = newClaimant();

		Payment c1 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c1.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c1.setPaymentType(PaymentType.CHECK);
		c1.setStatusChangeDate(new Date(2011, 9, 1));
		c1.setIdentificatonNo("1");
		c1.setPaymentAmount(new BigDecimal(100));
		claimant.addCheck(c1);

		Payment c2 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c2.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c2.setPaymentType(PaymentType.CHECK);
		c2.setStatusChangeDate(new Date(2011, 9, 1));
		c2.setIdentificatonNo("2");
		c2.setPaymentAmount(new BigDecimal(200));
		claimant.addCheck(c2);

		claimant.persist();
	}

	public static void createStaleDateProcessingData() {
		createEvent();
		Claimant claimant = newClaimant();
		Payment c;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setIdentificatonNo("1000");
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentAmount(new BigDecimal(50));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1001");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1002");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(200));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1003");
		c.setStaleByDate(new Date());
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1004");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		c.setIdentificatonNo("1005");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		c.setIdentificatonNo("1006");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		c.setIdentificatonNo("1007");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		c.setIdentificatonNo("1008");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		c.setIdentificatonNo("1009");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
		c.setIdentificatonNo("1010");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_REPLACE_REJECTED_RR_SRJ);
		c.setIdentificatonNo("1011");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		c.setIdentificatonNo("1012");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		claimant.persist();

	}

	protected static void createDSTOEvent() {
		Event e = new Event();
		e.setDda("34512");
		e.setName("sampleEven");
		e.setBankCode("SS");
		e.setLibraryId("Library1");
        e.setCode("101");
		Bank bank = new Bank();
		bank.setAbaNo("2234");
		e.setBank(bank);
		
		SmallCheckConfig s=new SmallCheckConfig();
		s.setEventNameAddress1("Eventadd1");
		s.setEventNameAddress2("Eventadd2");
		s.setEventNameAddress3("Eventadd3");
		s.setEventNameAddress4("Eventadd4");
		s.setEventNameAddress5("Eventadd5");
		s.setEventNameAddress6("Eventadd6");
		s.setVariableVerbiage("variableverbiage");
		s.setBankInfo1("BankInfo1");
		s.setBankInfo2("BankInfo2");
		s.setBankInfo3("BankInfo3");
		s.setDistributionText("distribution");
		s.setAbaTop("abatop");
		s.setAbaBottom("ababottom");
		 
		e.setSmallCheckConfig(s);
		e.persist();

	}

	public static void createDSTOCheckFileJobData() {
		Claimant claimant = newClaimant();
		createDSTOEvent();
		Payment c;

		c = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("1234561444");
		c.setPaymentAmount(new BigDecimal("100.75"));
		c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		c.setPayTo(claimant);
		c.setAuditable(Boolean.TRUE);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("1234561555");
		c.setPaymentAmount(new BigDecimal("345.00"));
		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		c.setPayTo(claimant);
		c.setAuditable(Boolean.TRUE);
		claimant.addCheck(c);

        c = Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
        c.setPaymentType(PaymentType.CHECK);
        c.setIdentificatonNo("1234561666");
        c.setPaymentAmount(new BigDecimal("445.00"));
        c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        c.setPayTo(claimant);
        c.setAuditable(Boolean.TRUE);
        claimant.addCheck(c);

		claimant.persist();

	}

	public static void createDSTOPrintFileJobData() {
		Claimant claimant = newClaimant();
		createEvent();
		Letter l = new Letter();

		LetterCode lc = new LetterCode("201", "Claim Form - Blank (I)",
				LetterType.CLAIM_FORM);
		lc.persist();

		l.setAuditable(true);
		l.setComments("Test Letter");
		l.setCorrespondenceHasAwdObject(false);
		l.setDescription("Desc");
		l.setLetterCode(lc);
		l.setLetterStatus(LetterStatus.IGO);
		l.setMailDate(new Date());
		l.setMailingControlNo("1010");
		l.setMailType(MailType.OUTGOING);
		l.setMailTypeStr("OUTGOING");
		l.setDstoPrintFileSentFlag(Boolean.FALSE);
		l.setClaimant(claimant);
		claimant.addLetter(l);
		
		lc = new LetterCode("010", "Claim Form - Blank (Test)",
				LetterType.CLAIM_FORM);
		lc.persist();
		
		l = new Letter();

		l.setAuditable(true);
		l.setComments("TestPrintLetter");
		l.setCorrespondenceHasAwdObject(false);
		l.setDescription("Description");
		l.setLetterCode(lc);
		l.setLetterStatus(LetterStatus.IGO);
		l.setMailDate(new Date());
		l.setMailingControlNo("1011");
		l.setMailType(MailType.OUTGOING);
		l.setMailTypeStr("OUTGOING");
		l.setDstoPrintFileSentFlag(Boolean.FALSE);
		l.setClaimant(claimant);
		claimant.addLetter(l);
		
		lc = new LetterCode("011", "Optout - Blank",
				LetterType.OPTOUT_FORM);
		lc.persist();
		
		l = new Letter();

		l.setAuditable(true);
		l.setComments("TestPrintLetter");
		l.setCorrespondenceHasAwdObject(false);
		l.setDescription("Description");
		l.setLetterCode(lc);
		l.setLetterStatus(LetterStatus.IGO);
		l.setMailDate(new Date());
		l.setMailingControlNo("1012");
		l.setMailType(MailType.OUTGOING);
		l.setMailTypeStr("OUTGOING");
		l.setDstoPrintFileSentFlag(Boolean.FALSE);
		l.setClaimant(claimant);
		claimant.addLetter(l);

		claimant.persist();

	}
	

}
