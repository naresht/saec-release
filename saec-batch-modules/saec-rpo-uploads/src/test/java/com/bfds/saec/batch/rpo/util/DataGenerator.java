package com.bfds.saec.batch.rpo.util;

import java.math.BigDecimal;
import java.util.List;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;

public class DataGenerator {

	public static void createRpoItemWriterData() {

		if(LetterCode.findByCode("712") == null) {
			(new LetterCode("712", "Courtesy Letters", LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		}
		Claimant claimant = newClaimant();
		Payment c;
		Letter l;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("123450");
		c.setPaymentAmount(new BigDecimal(900));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("123451");
		c.setPaymentAmount(new BigDecimal(800));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("123321");
		c.setPaymentAmount(new BigDecimal(1000));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		l = new Letter();
		l.setMailingControlNo("234566");
		l.setLetterCode(LetterCode.findByCode("712"));
		l.setClaimant(claimant);
		claimant.addLetter(l);

		l = new Letter();
		l.setMailingControlNo("234567");
		l.setLetterCode(LetterCode.findByCode("712"));
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
