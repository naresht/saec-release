package com.bfds.saec.batch.in.infoage;

import com.bfds.saec.domain.*;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.reference.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Event test data common to both corporate and individaul receive jobs.
 */
@Component
@Transactional
public class InfoageReceiveEventTestData {

    public void create() {
        newEvent();
        createClaimantWithoutRpoObjects();
        createClaimantWithRpoLetters();
        createClaimantWithRpoPayments();
        createClaimantWithRpoPaymentsAnLetters();

        deleteAllActivity();
    }

    /**
     * All the test {@link com.bfds.saec.domain.Claimant}s created will not have RPO objects. Processing {@link com.bfds.saec.batch.in.infoage_corporate.CorporateAddressResearch}
     * for these claimants will be equivalent to a pre-scrub.
     */
    private void createClaimantWithoutRpoObjects() {
        /** This claimant does not have an address and primary contact. The Address and primary contact must be created by the batch job.
         **/
        Claimant claimant = newClaimant();
        claimant.setCorporate(Boolean.TRUE);
        claimant.setReferenceNo("100000001");
        claimant.persist();

        /** This claimant has an address and primary contact. The Address and primary contact must be updated by the batch job.
         **/
        claimant = newClaimant();
        claimant.setCorporate(Boolean.TRUE);
        claimant.setReferenceNo("100000002");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
    }


    /**
     * All the test {@link Claimant}s created will have RPO letters.
     */
    private void createClaimantWithRpoLetters() {
        LetterCode lc = new LetterCode("101", "claim form 1", LetterType.CLAIM_FORM);
        lc.persist();
        Claimant claimant = newClaimant();
        claimant.setCorporate(Boolean.TRUE);
        claimant.setReferenceNo("100000003");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
        Letter letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000031001");
        letter.setMailType(MailType.OUTGOING);
        letter.setRpoType(RPOType.NONFORWARDABLE);
        claimant.addLetter(letter);
        letter.persist();
        //This letter is a RPO FORWARDABLE and so must not be updated by the bath job.
        letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000031002");
        letter.setMailType(MailType.OUTGOING);
        letter.setLetterStatus(LetterStatus.RPO);
        letter.setRpoType(RPOType.FORWARDABLE);
        claimant.addLetter(letter);
        letter.persist();
    }

    /**
     * All the test {@link Claimant}s created will have RPO payments.
     */
    private void createClaimantWithRpoPayments() {

        Claimant claimant = newClaimant();
        claimant.setCorporate(Boolean.TRUE);
        claimant.setReferenceNo("100000004");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
        // This payment is an outstanding payment. This should not be updated by the batch job.
        Payment payment = Payment.newPayment(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setStatusChangeDate(new Date(2011, 9, 1));
        payment.setIdentificatonNo("10000000401");
        payment.setPaymentAmount(new BigDecimal(200));
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        payment = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setStatusChangeDate(new Date(2011, 9, 1));
        payment.setIdentificatonNo("10000000402");
        payment.setPaymentAmount(new BigDecimal(550));
        payment.setRpoType(RPOType.NONFORWARDABLE);
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

    }

    /**
     * All the test {@link Claimant}s created will have RPO payments and letters.
     */
    private void createClaimantWithRpoPaymentsAnLetters() {

        LetterCode lc = new LetterCode("102", "claim form 2", LetterType.CLAIM_FORM);
        lc.persist();

        Claimant claimant = newClaimant();
        claimant.setCorporate(Boolean.TRUE);
        claimant.setReferenceNo("100000005");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
        // This payment is an outstanding payment. This should not be updated by the batch job.
        Payment payment = Payment.newPayment(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setStatusChangeDate(new Date(2011, 9, 1));
        payment.setIdentificatonNo("10000000501");
        payment.setPaymentAmount(new BigDecimal(200));
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        payment = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setStatusChangeDate(new Date(2011, 9, 1));
        payment.setIdentificatonNo("10000000502");
        payment.setPaymentAmount(new BigDecimal(550));
        payment.setRpoType(RPOType.NONFORWARDABLE);
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        Letter letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000051001");
        letter.setMailType(MailType.OUTGOING);
        letter.setRpoType(RPOType.NONFORWARDABLE);
        claimant.addLetter(letter);
        letter.persist();
        //This letter is a RPO FORWARDABLE and so must not be updated by the bath job.
        letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000051002");
        letter.setMailType(MailType.OUTGOING);
        letter.setLetterStatus(LetterStatus.RPO);
        letter.setRpoType(RPOType.FORWARDABLE);
        claimant.addLetter(letter);
        letter.persist();

        /** This Claimant is identical to "100000005". The corresponding CorporateAddressResearch
         * represents an unsuccessful address research.
         */
        claimant = newClaimant();
        claimant.setCorporate(Boolean.TRUE);
        claimant.setReferenceNo("100000006");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
        // This payment is an outstanding payment. This should not be updated by the batch job.
        payment = Payment.newPayment(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setStatusChangeDate(new Date(2011, 9, 1));
        payment.setIdentificatonNo("10000000601");
        payment.setPaymentAmount(new BigDecimal(200));
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        payment = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setStatusChangeDate(new Date(2011, 9, 1));
        payment.setIdentificatonNo("10000000602");
        payment.setPaymentAmount(new BigDecimal(550));
        payment.setRpoType(RPOType.NONFORWARDABLE);
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000061001");
        letter.setMailType(MailType.OUTGOING);
        letter.setRpoType(RPOType.NONFORWARDABLE);
        claimant.addLetter(letter);
        letter.persist();
        //This letter is a RPO FORWARDABLE and so must not be updated by the bath job.
        letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000061002");
        letter.setMailType(MailType.OUTGOING);
        letter.setLetterStatus(LetterStatus.RPO);
        letter.setRpoType(RPOType.FORWARDABLE);
        claimant.addLetter(letter);
        letter.persist();

    }


    /**
     * Delete all {@link com.bfds.saec.domain.activity.Activity} generated during the creation of the claimants.
     * This makes it easy to verify activity generated by the batch job.
     */
    private void deleteAllActivity() {
        (new Claimant()).flush();
        (new Claimant()).clear();
        for(Activity a : Activity.findAllActivitys())  {
            a.remove();
        }
    }

    public void deleteData() {
        List<AddressResearchReturn> alist = AddressResearchReturn
                .findAllAddressResearchReturns();
        for (AddressResearchReturn a : alist) {
            // NOTE = Throws Integrity constraint violation if data already
            // exists
            a.remove();
        }

        List<Letter> llist = Letter.findAllLetters();

        for (Letter c : llist) {
            c.remove();
        }

        List<Claimant> clist = Claimant.findAllClaimants();
        for (Claimant c : clist) {
            c.remove();
        }

        List<LetterCode> lclist = LetterCode.findAllLetterCodes();
        for (LetterCode c : lclist) {
            c.remove();
        }

        List<Event> elist = Event.findAllEvents();
        for (Event c : elist) {
            c.remove();
        }
    }

    private void newEvent() {
        Event e = Event.newEvent();
        e.setDda("11111111");
        e.setMailSendCountLimit(100);
        e.setPaymentSendCountLimit(100);
        e.setPreScrub(true);
        e.setAddressResearch(true);
        e.setAccountType(AccountType.INDIVIDUAL_AND_CORPORATION);
        e.persist();
    }

    public static Claimant newClaimant() {
        Claimant account = new Claimant();
        account.setClaimantRegistration(new ClaimantRegistration());
        account.getClaimantRegistration().setRegistration1("aaaaaaaaa");
        return account;
    }

    private static void newPrimaryAddress(Claimant p, String var) {
        ClaimantAddress a = new ClaimantAddress();
        a.setAddressType(AddressType.ADDRESS_OF_RECORD);
        a.setAddress1("2000 crown colony dr" + var);
        a.setCity("Quincy" + var);
        a.setStateCode("MA");
        a.setCountryCode("US");
        a.setClaimant(p);
        p.addAddress(a);
    }
}
