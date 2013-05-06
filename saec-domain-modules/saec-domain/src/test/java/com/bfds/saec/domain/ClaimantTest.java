package com.bfds.saec.domain;

import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.activity.PhoneCallDataOnDemand;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
@Transactional
public class ClaimantTest{

    @Autowired
    private PhoneCallDataOnDemand phoneCallDod;

    @Autowired
    private ClaimantTestData claimantTestData;


    private static Claimant claimant;

    private static StopWatch sw = new StopWatch();
    public void beforeAllTests() {
        sw.start();
        claimant = claimantTestData.newClaimant();
    }

    public void afterAllTests() {
        claimantTestData.deleteClaimant(claimant);
        claimant = null;
        sw.stop();
        System.out.println(sw.getTotalTimeMillis());
    }

    @Before
    public void before() {
        claimant = Claimant.findClaimant(claimant.getId(), Claimant.ASSOCIATION_ALL);
    }

    @Before
    public void createClaimantWithAllAssociations() {

    }



    @Test
    public void validateSeasonalAddress() {
        assertThat(Claimant.findClaimant(claimant.getId()).hasSeasonalAddress())
                .isEqualTo(true);
    }

    @Test
    public void validateContacts() {
        assertThat(Claimant.findClaimant(claimant.getId()).getContacts())
                .isNotEmpty();
    }

    @Test
    public void validatePhoneCall() {
        assertThat(Claimant.findClaimant(claimant.getId()).getPhoneCall())
                .isNotEmpty();
    }

    @Test
    public void validateLetter() {
        assertThat(Claimant.findClaimant(claimant.getId()).getLetters())
                .isNotEmpty();
    }

    @Test
    public void validatePayment() {
        assertThat(Claimant.findClaimant(claimant.getId()).getPayments())
                .isNotEmpty();
    }

    @Test
    public void validateAlternatePayee() {
        List<Claimant> alternamtePayees = Claimant.findClaimant(
                claimant.getId()).getAlternatePayees();
        assertThat(alternamtePayees).isNotEmpty();
    }

    @Test
    @Ignore
    public void validateActivity() {
        List<Activity> activityList = Claimant.findClaimant(claimant.getId())
                .getAllActivity();
        assertThat(activityList).isNotEmpty();
        assertThat(activityList).onProperty("activityTypeDescription")
                .contains("Payment Management", "Alternate Payee Created",
                        "Incoming Call", "Address Change",
                        "Primary Contact Info Change", "Tax Info Change",
                        "Name Change");
    }

    @Test
    public void testMailingAddesss() {
        assertThat(claimant.isAorMailingAddress()).isTrue();
        assertThat(claimant.isSeasonalMailingAddress()).isFalse();
    }

    @Test
    public void addRemoveContacts() {
        // claimant.remove();
        // createClaimant();
        List<Contact> contactsList = new ArrayList<Contact>();
        Contact c1 = new Contact();
        c1.setCellPhoneNo("cell-1");
        contactsList.add(c1);
        Contact c2 = new Contact();
        c2.setCellPhoneNo("cell-2");
        contactsList.add(c2);

        claimant = claimant.merge();
        claimant.addContacts(contactsList);
        claimant = claimant.merge();
        claimant.persist();
        this.clearSession();

        claimant = Claimant.findClaimant(claimant.getId());
        assertThat(claimant.getContacts()).onProperty("cellPhoneNo").contains(
                "cell-1", "cell-2");

        claimant.removeContact(c1.merge());
        claimant.removeContact(c2.merge());
        c1.persist();
        c2.persist();
        claimant = claimant.merge();
        claimant.persist();
        assertThat(claimant.merge().getContacts()).hasSize(1);
        this.clearSession();
        // assertThat(claimant.merge().getContacts()).hasSize(1);
        // assertThat(Claimant.findClaimant(claimant.getId()).getContacts()).hasSize(1);

        // Now remove all contacts.
        // assertThat(Claimant.findClaimant(claimant.getId()).getContacts()).isNotEmpty();

        claimant = claimant.merge();
        claimant.removeContacts(claimant.getContacts());
        claimant = claimant.merge();
        claimant.persist();
        this.clearSession();

        // assertThat(Claimant.findClaimant(claimant.getId()).getContacts()).isEmpty();

    }

    @Test
    @ExpectedException(IllegalStateException.class)
    public void setAddressTwice() {
        claimant.setAddressOfRecord(DataGenerator.newAddress(
                AddressType.SEASONAL_ADDRESS, "2397 Bee Street", "Muskegon",
                "MA", "49470"));
    }

    @Test
    public void setMailingAddressByType() {
        claimant.setMailingAddressByType(AddressType.SEASONAL_ADDRESS);
        assertThat(claimant.getMailingAddress().getAddressType()).isEqualTo(
                AddressType.SEASONAL_ADDRESS);
    }

    @Test
    public void hasAddressOfRecord() {
        assertThat(claimant.hasAddressOfRecord()).isTrue();
    }

    @Test
    public void removeCheck() {
        claimant.removeCheck(claimant.getPayments().iterator().next());
        assertThat(claimant.getPayments()).isEmpty();
    }

    @Test
    public void removePaymentById() {
        claimant.removePaymentById(claimant.getPayments().iterator().next()
                .getId());
        assertThat(claimant.getPayments()).isEmpty();
    }

    @Test
    public void removePaymentByIdentificationNo() {
        claimant.removePaymentByIdentificationNo(claimant.getPayments()
                .iterator().next().getIdentificatonNo());
        assertThat(claimant.getPayments()).isEmpty();
    }

    @Test
    public void verifyRegistration() {
        claimant.setRegistration1("r1");
        claimant.setRegistration2("r2");
        claimant.setRegistration3("r3");
        claimant.setRegistration4("r4");
        claimant.setRegistration5("r5");
        claimant.setRegistration6("r6");
        assertThat(claimant.getRegistrationLinesAsString("")).isEqualTo(
                "r1r2r3r4r5r6");

    }

    @Test
    public void verifyTaxInfo() {
        claimant.setCertificationDate(new Date(112, 2, 1));
        claimant.setCertificationStatus("s");
        claimant.setCertificationType("t");
        claimant.setEin("1111");
        claimant.setSsn("2222");
        claimant.setTaxCountryCode("US");
        claimant.setTin("3333");
        claimant.setUsCitizen(Boolean.TRUE);
        assertThat(claimant.getCertificationDate()).isEqualTo(
                new Date(112, 2, 1));
        assertThat(claimant.getCertificationStatus()).isEqualTo("s");
        assertThat(claimant.getCertificationType()).isEqualTo("t");
        assertThat(claimant.getEin()).isEqualTo("1111");
        assertThat(claimant.getSsn()).isEqualTo("2222");
        assertThat(claimant.getTaxCountryCode()).isEqualTo("US");
        assertThat(claimant.getTin()).isEqualTo("3333");
        assertThat(claimant.getUsCitizen()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void verifyName() {
        claimant.setFirstName("fn");
        claimant.setMiddleName("mn");
        claimant.setLastName("ln");
        assertThat(claimant.getName().getFirstName()).isEqualTo("fn");
        assertThat(claimant.getName().getMiddleName()).isEqualTo("mn");
        assertThat(claimant.getName().getLastName()).isEqualTo("ln");

    }

    @Test
    public void testSnapShotCallLogsInActivityListner() {

        claimant = Claimant.findClaimant(claimant.getId());
        List<Activity> activityList = Lists
                .newArrayList(claimant.getActivity());
        for (Activity activity : activityList) {
            claimant.removeActivity(activity);
        }
        claimant.persist();
        claimant.flush();
        claimant.clear();
        claimant = Claimant.findClaimant(claimant.getId());
        assertThat(claimant.getActivity()).isEmpty();
        PhoneCall call = phoneCallDod.getNewTransientPhoneCall(10);
        call.setShapshot(true);
        claimant.addPhoneCall(call);
        claimant.persist();
        claimant.flush();
        claimant.clear();

        claimant = Claimant.findClaimant(claimant.getId());
        assertThat(claimant.getActivity()).hasSize(1);

        assertThat(claimant.getActivity()).onProperty("shapshot").containsOnly(
                Boolean.TRUE);

        assertThat(claimant.getAllActivity()).hasSize(0);

        call = phoneCallDod.getNewTransientPhoneCall(10);
        call.setShapshot(false);
        claimant.addPhoneCall(call);
        claimant.persist();
        claimant.flush();
        claimant.clear();

        claimant = Claimant.findClaimant(claimant.getId());
        assertThat(claimant.getActivity()).hasSize(2);

        assertThat(claimant.getActivity()).onProperty("shapshot").containsOnly(
                Boolean.TRUE, Boolean.FALSE);

        assertThat(claimant.getAllActivity()).hasSize(1);

    }

    @Test
    public void checkString() {
        claimant.toString();
    }

    /**
     * To test that method should return only those {@link Payment} objects
     * which have not been yet mailed .
     */
    @Test
    public void testPaymentsEligibleForMailing() {
        assertThat(this.claimant.getPaymentsEligibleForMailing()).isNotEmpty();
        assertThat(this.claimant.getPaymentsEligibleForMailing().size())
                .isEqualTo(1);

    }

    private void clearSession() {
        claimant.flush();
        claimant.clear();
    }
}
