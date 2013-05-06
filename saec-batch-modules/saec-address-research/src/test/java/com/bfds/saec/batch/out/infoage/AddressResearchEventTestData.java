package com.bfds.saec.batch.out.infoage;

import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.domain.*;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.reference.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
@Transactional
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddressResearchEventTestData {

    @PersistenceContext(unitName = "batchFilesEntityManagerFactory")
    private EntityManager entityManager;

    /**
     * To allow creation of corporate or individaul {@link com.bfds.saec.domain.Claimant}s
     */
    private boolean corporate;

    public void setCorporate(boolean corporate) {
        this.corporate = corporate;
    }


    public void create(){
        newEvent();
        createClaimantsWithoutRpoObjects();
        createClaimantWithRpoLetters();
        createClaimantWithRpoPayments();
        createClaimantWithRpoPaymentsAnLetters();

        deleteAllActivity();
    }

    /**
     *  All the test {@link Claimant}s created will not have any RPO letters or payments.
     *  These will be picked up by the prescrub job only.
     */
    public void createClaimantsWithoutRpoObjects() {
        // This claimant does not have an address.
        Claimant claimant = newClaimant(corporate);
        claimant.setReferenceNo("100000004");
        claimant.setCertificationDate(new Date());
        claimant.persist();

        // This claimant has an address.
        claimant = newClaimant(corporate);
        claimant.setReferenceNo("100000005");
        claimant.setCertificationDate(new Date());
        newPrimaryAddress(claimant, "-1");
        claimant.persist();

    }
    /**
     * All the test {@link Claimant}s created will have RPO letters.
     */
    private void createClaimantWithRpoLetters() {
        LetterCode lc = new LetterCode("101", "claim form 1", LetterType.CLAIM_FORM);
        lc.persist();
        Claimant claimant = newClaimant(corporate);
        claimant.setReferenceNo("100000001");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
        Letter letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000011001");
        letter.setMailType(MailType.OUTGOING);
        letter.setRpoType(RPOType.NONFORWARDABLE);
        claimant.addLetter(letter);
        letter.persist();
        //This letter is a RPO FORWARDABLE and so must not be updated by the bath job.
        letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000011002");
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

        Claimant claimant = newClaimant(corporate);
        claimant.setReferenceNo("100000002");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
        // This payment is an outstanding payment. This should not be updated by the batch job.
        Payment payment = Payment.newPayment(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setPaymentDate(new Date(120, 9, 1));
        payment.setIdentificatonNo("10000000201");
        payment.setPaymentAmount(new BigDecimal(200));
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        payment = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setPaymentDate(new Date(120, 9, 1));
        payment.setIdentificatonNo("10000000202");
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

        Claimant claimant = newClaimant(corporate);
        claimant.setReferenceNo("100000003");
        claimant.setPrimaryContact(new Contact());
        claimant.getPrimaryContact().setPhoneNo("0000000");
        newPrimaryAddress(claimant, "-1");
        claimant.persist();
        // This payment is an outstanding payment. This should not be updated by the batch job.
        Payment payment = Payment.newPayment(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setStatusChangeDate(new Date(2011, 9, 1));
        payment.setIdentificatonNo("10000000301");
        payment.setPaymentAmount(new BigDecimal(200));
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        payment = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
        payment.setPaymentType(PaymentType.CHECK);
        payment.setPaymentDate(new Date(120, 9, 1));
        payment.setIdentificatonNo("10000000302");
        payment.setPaymentAmount(new BigDecimal(550));
        payment.setRpoType(RPOType.NONFORWARDABLE);
        payment.setPayTo(claimant);
        claimant.addCheck(payment);
        payment.persist();

        Letter letter = new Letter();
        letter.setLetterCode(lc);
        letter.setMailingControlNo("1000000031001");
        letter.setMailType(MailType.OUTGOING);
        letter.setRpoType(RPOType.NONFORWARDABLE);
        letter.setMailDate(new Date(120, 9, 1));
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
     * Delete all {@link com.bfds.saec.domain.activity.Activity} generated during the creation of the claimants.
     * This makes it easy to verify activity generated by the batch job.
     */
    public void deleteAllActivity() {
        (new Claimant()).flush();
        (new Claimant()).clear();
        for(Activity a : Activity.findAllActivitys())  {
            a.remove();
        }
    }

    public void deleteData(){
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

    @Transactional("batchFilesTransactionManager")
    public void deleteFileRecordData() {
        for(OutboundCorporateAddressResearch r : findAllFileRecords(OutboundCorporateAddressResearch.class))  {
            entityManager.remove(r);
        }
    }

    public Claimant newClaimant(boolean isCorp) {
        Claimant account = new Claimant();
        account.setClaimantRegistration(new ClaimantRegistration());
        account.getClaimantRegistration().setRegistration1("aaaaaaaaa");
        account.getClaimantRegistration().setRegistration2("bbbbbbbb");
        account.setCorporate(isCorp);
        //TIN is used in corporate jobs
        account.setTin("1122334455");
        //TIN is used in individual jobs
        account.setSsn("11-22-33");
        account.setName(new Name("first name-1", "middle name-1", null));
        return account;
    }

    public void newPrimaryAddress(Claimant p, String var) {
        ClaimantAddress a = new ClaimantAddress();
        a.setAddressType(AddressType.ADDRESS_OF_RECORD);
        a.setAddress1("2000 crown colony dr" + var);
        a.setCity("Quincy" + var);
        a.setStateCode("MA");
        a.setCountryCode("US");
        a.setClaimant(p);
        p.addAddress(a);
    }

    public void newEvent() {
        Event e = Event.newEvent();
        e.setDda("1234");
        e.setMailSendCountLimit(100);
        e.setPaymentSendCountLimit(100);
        e.setRpoEligible(RpoEligibleOption.MAIL_AND_PAYMENT);
        e.setPreScrub(true);
        e.setAddressResearch(true);
        e.setAccountType(AccountType.INDIVIDUAL_AND_CORPORATION);
        e.persist();
    }

    public <T> List<T> findAllFileRecords(Class<T> requiredType) {
        return (List<T>) entityManager.createQuery(" from "+requiredType.getName()).getResultList();
    }
}
