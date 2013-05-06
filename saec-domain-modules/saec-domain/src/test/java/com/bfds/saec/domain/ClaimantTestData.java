package com.bfds.saec.domain;

import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.activity.PhoneCallDataOnDemand;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.CallType;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@Component
public class ClaimantTestData {

    @Autowired
    private ClaimantDataOnDemand claimantDod;

    @Autowired
    private ContactDataOnDemand contactDod;

    @Autowired
    private PhoneCallDataOnDemand phoneCallDod;

    @Autowired
    private PaymentDataOnDemand paymentDod;

    @Autowired
    private LetterDataOnDemand letterDod;

    private Claimant claimant;

    public Claimant newClaimant() {
        createClaimant();
        addSeasonalAddress();
        addContact();
        addPhoneCall();
        addPayment();
        addLetter();
        addAlternatePayee();

        updateMailingAddress();
        updatePrimaryContact();
        updateTaxInfo();
        updateRegistrationInfo();
        return claimant;
    }

	private void createClaimant() {
		this.claimant = claimantDod.getNewTransientClaimant(100);
		// RipEventListener ripListener = mock(RipEventListener.class);
		// this.claimant.setRipEventListener(ripListener);
		this.claimant.persist();
		this.clearSession();
	}

	private void addSeasonalAddress() {
		claimant = claimant.merge();
		claimant.setSeasonalAddress(DataGenerator.newAddress(
				AddressType.SEASONAL_ADDRESS, "2397 Bee Street", "Muskegon",
				"MA", "49470"));
		claimant.persist();
		this.clearSession();
	}

	private void addContact() {
		claimant = claimant.merge();
		claimant.addContact(contactDod.getNewTransientContact(100));
		claimant.persist();
		this.clearSession();
	}

	private void addPhoneCall() {
		claimant = claimant.merge();
		PhoneCall call = phoneCallDod.getNewTransientPhoneCall(100);
		call.setCallType(CallType.INBOUND);
		call.setShapshot(false);
		claimant.addPhoneCall(call);
		claimant.persist();
		this.clearSession();
	}

	private void addPayment() {
		claimant = claimant.merge();
		claimant.addCheck(paymentDod.getNewTransientPayment(100));
		claimant.persist();
		this.clearSession();
	}

	private void addLetter() {
		claimant = claimant.merge();
		claimant.addLetter(letterDod.getNewTransientLetter(100));
		claimant.persist();
		this.clearSession();
	}

	private void addAlternatePayee() {
		claimant = claimant.merge();
		Claimant alternatePayee = claimantDod.getNewTransientClaimant(200);
		claimant.addAlternatePayee(alternatePayee);
		alternatePayee.persist();
		this.clearSession();
	}

	private void updateMailingAddress() {
		claimant = claimant.merge();
		claimant.getMailingAddress().setAddress1("asdasd dAD ASDAS DAsd ");
		claimant.persist();
		this.clearSession();
	}

	private void updatePrimaryContact() {
		claimant = claimant.merge();
		Contact contact = claimant.getPrimaryContact();
		contact.setCellPhoneNo("asdasd dAD ASDAS DAsd ");
		contact.setPrimaryContactOf(claimant);
		claimant = claimant.merge();
		this.clearSession();
	}

	private void updateTaxInfo() {
		claimant = claimant.merge();
		claimant.setSsn("1212121212121212121212121");
		claimant = claimant.merge();
		this.clearSession();
	}

	private void updateRegistrationInfo() {
		claimant = claimant.merge();
		claimant.setRegistration1("r1");
		claimant = claimant.merge();
		this.clearSession();
	}


    @Transactional
    public void deleteClaimant(Claimant claimant) {

        for(Activity c : Activity.findAllActivitys()) {
            c.remove();
        }

        for(Claimant c : claimant.getAlternatePayees()) {
            c.remove();
        }
        claimant.flush();

        for(Letter c : claimant.getLetters()) {
             c.remove();
        }

        for(Payment c : claimant.getPayments()) {
            c.remove();
        }
        claimant.remove();
        claimant.flush();
        claimant.clear();

    }

	private void clearSession() {
		claimant.flush();
		claimant.clear();
	}
}
