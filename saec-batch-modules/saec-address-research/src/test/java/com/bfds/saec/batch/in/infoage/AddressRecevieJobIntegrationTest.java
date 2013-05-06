package com.bfds.saec.batch.in.infoage;

import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.ContactChange;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Tests common to both corporate and individual receive jobs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml"})
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public abstract class AddressRecevieJobIntegrationTest extends AbstractSingleJobExecutionIntegrationTest{


    @Autowired
    protected InfoageReceiveEventTestData eventTestData;

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "11111111").addString("researchType", ResearchType.RESEARCH).toJobParameters();
    }
    /**
     * A {@link com.bfds.saec.domain.Claimant} might not have an address. This is possible during the data-intake process.
     * This precludes the possibility of the Claimant having any RPO items.
     */
    @Test
    public void newAddressMustBeCreatedIfClaimantHasNoAddress() {
        final Claimant claimant = Claimant.findClaimant("100000001", Claimant.ASSOCIATION_ALL);
        assertThat(claimant.getMailingAddress()) .isNotNull();
    }

    /**
     * After processing the {@link com.bfds.saec.batch.in.infoage_corporate.CorporateAddressResearch} object it's corresponding
     * {@link Claimant#addressResearchSent} must be set to false.
     */
    @Test
    public void addressResearchSentFlagMustBeFalse() {
        final Claimant claimant_1 = Claimant.findClaimant("100000001", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_2 = Claimant.findClaimant("100000002", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_3 = Claimant.findClaimant("100000003", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_4 = Claimant.findClaimant("100000004", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_5 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<Claimant> claimants = Lists.newArrayList(claimant_1, claimant_2, claimant_3, claimant_4, claimant_5);
        assertThat(claimants).onProperty("addressResearchSent").containsOnly(Boolean.FALSE);
    }


    @Test
    public abstract void addressMustBeUpdated();

    /**
     * The updated ClaimantAddress must have a AddressResearchChangeActivity associated with it.
     */
    @Test
    public abstract void addressMustHaveAddressResearchChangeActivity();

    /**
     * The {@link Claimant} must have it's {@link com.bfds.saec.domain.Contact} updated/created when {@link com.bfds.saec.batch.in.infoage_corporate.CorporateAddressResearch#phone} is not null.
     */
    @Test
    public void claimantMustHaveItsContactUpdated() {
        final Claimant claimant_1 = Claimant.findClaimant("100000001", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_2 = Claimant.findClaimant("100000002", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_3 = Claimant.findClaimant("100000003", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_4 = Claimant.findClaimant("100000004", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_5 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<Claimant> claimants = Lists.newArrayList(claimant_1, claimant_2, claimant_3, claimant_4, claimant_5);

        assertThat(claimants).onProperty("primaryContact").onProperty("phoneNo").containsOnly("01-11111111");
    }

    /**
     * The {@link Claimant} must have a {@link com.bfds.saec.domain.activity.ContactChange} activity created when {@link com.bfds.saec.batch.in.infoage_corporate.CorporateAddressResearch#phone} is not null.
     */
    @Test
    public void claimantMustHaveContactChangeActivity() {
        final Claimant claimant_1 = Claimant.findClaimant("100000001", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_2 = Claimant.findClaimant("100000002", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_3 = Claimant.findClaimant("100000003", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_4 = Claimant.findClaimant("100000004", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_5 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<ContactChange> contactChanges = Lists.newArrayList((ContactChange)getActivityByType(claimant_1, ContactChange.class),
                (ContactChange)getActivityByType(claimant_2, ContactChange.class),
                (ContactChange)getActivityByType(claimant_3, ContactChange.class),
                (ContactChange)getActivityByType(claimant_4, ContactChange.class),
                (ContactChange)getActivityByType(claimant_5, ContactChange.class));
        assertThat(contactChanges).onProperty("to").onProperty("phoneNo").containsOnly("01-11111111");
    }

    /**
     * The {@link Claimant} must have a {@link com.bfds.saec.domain.activity.AddressChange} activity created when the claimant does not have an address.
     * @see #newAddressMustBeCreatedIfClaimantHasNoAddress()
     */
    @Test
    @Ignore("This is not working. Low priority.")
    public void claimantMustHaveAddressChangeActivityWhenClaimantDoesNotHaveAnAddress() {

    }

    /**
     * The {@link Claimant} must have a {@link com.bfds.saec.domain.activity.AddressChange} activity created when the claimant's address is updated.
     * @see #newAddressMustBeCreatedIfClaimantHasNoAddress()
     */
    @Test
    public abstract void claimantMustHaveAddressChangeActivity();

    /**
     * Letters with {@link com.bfds.saec.domain.reference.RPOType#NONFORWARDABLE} must have their status set to {@link com.bfds.saec.schema.util.claimant.LetterStatus#SUBMITTED}
     */
    @Test
    public void rpoNonForwardableLetterStatusMustBeUpdated() {
        final Letter letter_1 = Letter.findByMailingControlNo("1000000031001");
        final Letter letter_2 = Letter.findByMailingControlNo("1000000041001");
        final Letter letter_3 = Letter.findByMailingControlNo("1000000051001");
        List<Letter> letters = Lists.newArrayList(letter_1, letter_2, letter_3);
        assertThat(letters).onProperty("letterStatus").containsOnly(LetterStatus.SUBMITTED);
    }
    /**
     * Letters with {@link com.bfds.saec.domain.reference.RPOType#FORWARDABLE} must not be updated.
     */
    @Test
    public void rpoForwardableLetterMustNotBeUpdated() {
        final Letter letter_1 = Letter.findByMailingControlNo("1000000031002");
        final Letter letter_2 = Letter.findByMailingControlNo("1000000041002");
        List<Letter> letters = Lists.newArrayList(letter_1, letter_2);
        assertThat(letters).onProperty("letterStatus").containsOnly(LetterStatus.RPO);
    }

    /**
     * Payments with {@link com.bfds.saec.domain.reference.PaymentCode#VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN} must have their paymentCode set to {@link com.bfds.saec.domain.reference.PaymentCode#VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA}
     * when successful {@link com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch} is processed.
     */
    @Test
    public void rpoNonForwardablePaymentMustBeUpdated() {
        final Payment payment = Payment.findPaymentIdentificationNo("10000000402");
        List<Payment> payments = Lists.newArrayList(payment);
        assertThat(payments).onProperty("paymentCode").containsOnly(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
    }

    /**
     * Payments without {@link com.bfds.saec.domain.reference.PaymentCode#VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN} must not be updated
     * when successful {@link com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch} is processed.
     */
    @Test
    public void paymentThatAreNotrpoNonForwardableMustNotBeUpdated() {
        final Payment payment = Payment.findPaymentIdentificationNo("10000000401");
        List<Payment> payments = Lists.newArrayList(payment);
        assertThat(payments).onProperty("paymentCode").containsOnly(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
    }


    /**
     * Payments with {@link com.bfds.saec.domain.reference.PaymentCode#VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN} must have its' comments updated
     * when unsuccessful {@link com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch} is processed.
     */
    @Test
    public void rpoNonForwardablePaymentMustHaveItsCommentsUpdated() {
        final Payment payment_1 = Payment.findPaymentIdentificationNo("10000000602");
        List<Payment> payments = Lists.newArrayList(payment_1);
        for(Payment payment : payments) {
            assertThat(payment.getComments()).endsWith(InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
        }
    }

    /**
     * Letter with {@link com.bfds.saec.domain.reference.RPOType#FORWARDABLE} must have its' comments updated
     * when unsuccessful {@link com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch} is processed.
     */
    @Test
    public void rpoNonForwardableLetterMustHaveItsCommentsUpdated() {
        final Letter letter_1 = Letter.findByMailingControlNo("1000000061001");
        List<Letter> letters = Lists.newArrayList(letter_1);
        for(Letter letter : letters) {
            assertThat(letter.getComments()).endsWith(InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
        }
    }

    /**
     * Returns the activity of the given activityType if present else null.
     * @param claimant - The claimant whose activity list has to be searched.
     * @param activityType - The type of activity to look for.
     * @return activity of the given activity
     * @throws IllegalStateException if more than one activity of the given type exists.
     */
    protected Activity getActivityByType(Claimant claimant, Class<? extends Activity> activityType) {
        Activity  ret = null;
        for(Activity activity : claimant.getActivity()) {
            if(activity.getClass().isAssignableFrom(activityType)) {
                if(ret != null) {
                    throw new IllegalStateException("More than one activity of type " + activityType);
                }
                ret = activity;
            }
        }
        return ret;
    }
}
