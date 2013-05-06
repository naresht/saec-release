package com.bfds.saec.batch.encorr.util;

import java.util.List;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterType;

public class DataGenerator {

	public static void createEncorrItemWriterData() {
		if(LetterCode.findByCode("712") == null) {
			(new LetterCode("712", "Courtesy Letters", LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		}
		Claimant claimant = newClaimant();
		Letter l;

		l = new Letter();
		l.setMailingControlNo("234566");
		l.setLetterCode(LetterCode.findByCode("712"));
		l.setDescription("ClaimTest1");
		l.setClaimant(claimant);
		claimant.addLetter(l);

		l = new Letter();
		l.setMailingControlNo("234567");
		l.setLetterCode(LetterCode.findByCode("712"));
		l.setDescription("ClaimTest2");
		l.setClaimant(claimant);
		claimant.addLetter(l);

		l = new Letter();
		l.setMailingControlNo("123450");
		l.setLetterCode(LetterCode.findByCode("712"));
		l.setDescription("ClaimTest2");
		l.setClaimant(claimant);
		claimant.addLetter(l);

		l = new Letter();
		l.setMailingControlNo("123451");
		l.setLetterCode(LetterCode.findByCode("712"));
		l.setDescription("ClaimTest2");
		l.setClaimant(claimant);
		claimant.addLetter(l);

		l = new Letter();
		l.setMailingControlNo("123321");
		l.setLetterCode(LetterCode.findByCode("712"));
		l.setDescription("ClaimTest2");
		l.setClaimant(claimant);
		claimant.addLetter(l);

		claimant.persist();
	}

	public static Claimant newClaimant() {

		Claimant p = new Claimant();
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("aaaaaaaaa");
		p.getClaimantRegistration().setRegistration2("bbbbbbbb");
		newPrimaryAddress(p);
		return p;
	}

	private static void newPrimaryAddress(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("address 1");
		a.setCity("city");
		a.setStateCode("state");
		a.setCountryCode("US");
		a.setClaimant(p);
		p.addAddress(a);
	}

	public static void deleteData() {
		List<Claimant> list = Claimant.findAllClaimants();
		for (Claimant c : list) {
			for (Letter l : c.getMails()) {
				l.remove();
			}
			c.remove();
			c.flush();
		}

		Event e = new Event();
		if (e.getDda() != null) {
			e.setDda(null);
		}

	}

}
