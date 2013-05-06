package com.bfds.saec.batch.out.dsto_print_file;

import java.util.Date;

import com.bfds.saec.domain.Bank;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;


public class DataGenerator extends com.bfds.saec.batch.util.DataGenerator {
	
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
	
	public static Claimant DSTOPrintfileProcessorTestData()
	{
		Claimant claimant = newClaimant();
		//createEvent();
		Letter l = new Letter();
		
		LetterCode lc = new LetterCode("999", "Claim Form - Blank (Test1)",
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
		claimant.persist();
		return claimant;
	}
	
	public static void createDSTOEvent_() {
		BankLov b = new BankLov();
		b.setActive(true);
		b.setCategory("First");
		b.setCode("SSSS");
		b.setDescription("SSS");
		b.persist();

		Event e = new Event();
		e.setDda("34512");
		e.setName("sampleEven");
		e.setBankCode("SSSS");
		e.setLibraryId("Library1");
		Bank bank = new Bank();
		bank.setAbaNo("2234");
		e.setBank(bank);
		e.persist();
	}

}
