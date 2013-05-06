package com.bfds.saec.batch.out.dsto_print_file;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Bank;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;

@Component
public class DstoPrintFileEventTestData extends com.bfds.saec.batch.util.DataGenerator{
	
	@Transactional
	public void create() {
		Claimant claimant = newClaimantforDSTO();
		createEvent();
		Letter l = new Letter();

		LetterCode lc = new LetterCode("201", "Claim Form - Blank (I)",LetterType.CLAIM_FORM);
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
		
		
		lc = new LetterCode("013", "Claim Form - Blank (III)",
				LetterType.CLAIM_FORM);
		lc.persist();
		
		l = new Letter();
		l.setAuditable(true);
		l.setComments("Test Letter");
		l.setCorrespondenceHasAwdObject(false);
		l.setDescription("Desc");
		l.setLetterCode(lc);
		l.setLetterStatus(LetterStatus.IGO);
		l.setMailDate(new Date());
		l.setMailingControlNo("1013");
		l.setMailType(MailType.INCOMING);
		l.setMailTypeStr("INCOMING");
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
        claimant.flush();
        claimant.clear();

	}
	
	@Transactional	
	public void createDSTOEvent_(){
		BankLov b = new BankLov();
		b.setActive(true);
		b.setCategory("First");
		b.setCode("107");
		b.setDescription("SSS");
		b.persist();
		
		Event e = new Event();
		e.setDda("34512");
		e.setName("sampleEven");
		e.setBankCode("107");
		e.setLibraryId("Library1");
		Bank bank = new Bank();
		bank.setAbaNo("2234");
		e.setBank(bank);
		e.persist();
	}
	
	@Transactional	
	public void createDSTOPrintFileEvent(){
		BankLov b = new BankLov();
		b.setActive(true);
		b.setCategory("First");
		b.setCode("109");
		b.setDescription("SSS");
		b.persist();
		
		Event e = new Event();
		e.setDda("34512");
		e.setName("sampleEven");
		e.setBankCode("109");
		e.setLibraryId("Library1");
		Bank bank = new Bank();
		bank.setAbaNo("2234");
		e.setBank(bank);
		e.persist();
	}
	
	

	public static Claimant newClaimantforDSTO() {

		Claimant p = new Claimant();
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("RegistrationName1");
		p.getClaimantRegistration().setRegistration2("RegistrationName2");
		p.getClaimantRegistration().setRegistration3("RegistrationName3");
		p.getClaimantRegistration().setRegistration4("RegistrationName4");
		p.getClaimantRegistration().setRegistration5("RegistrationName5");
		newPrimaryAddressforDSTO(p);
		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("112-23-3445");
		p.setTaxInfo(cl);
		p.setBrokerId("5578");
		p.setFundAccountNo("66666");
		p.setReferenceNo("100007");
		return p;
	}


	private static void newPrimaryAddressforDSTO(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("letteraddress 1");
		a.setAddress2("letteraddress 2");
		a.setAddress3("letteraddress 3");
		a.setAddress4("letteraddress 4");
		a.setAddress5("letteraddress 5");
		a.setCity("NEWYORK");
		a.setStateCode("NY");
		a.setCountryCode("USA");
		a.setZipCode(new ZipCode("23456", "4455"));
		// a.setZipCode(new ZipCode((String) line.get("ZipCode"), null));
		a.setClaimant(p);
		p.addAddress(a);
	}

}
