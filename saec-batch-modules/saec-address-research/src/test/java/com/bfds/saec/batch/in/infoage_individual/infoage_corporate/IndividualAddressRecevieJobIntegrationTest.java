package com.bfds.saec.batch.in.infoage_individual.infoage_corporate;

import com.bfds.saec.batch.in.infoage.AddressRecevieJobIntegrationTest;
import com.bfds.saec.batch.in.infoage.InfoageReceiveEventTestData;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.reference.AddressType;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


public class IndividualAddressRecevieJobIntegrationTest extends AddressRecevieJobIntegrationTest {

    @Inject
    Job individualAddressReceiveJob;

    @Autowired
    InfoageIndividualReceiveFileRecordTestData fileRecordTestData;

    @Override
    protected Job geJOb() {
        return individualAddressReceiveJob;
    }

    public void afterAllTests() {
        eventTestData.deleteData();
        fileRecordTestData.delete();
    }

    public void beforeAllTests() {
        eventTestData.create();
        fileRecordTestData.create();
    }

    @Test
    @Override
    public void addressMustBeUpdated() {
        final Claimant claimant_1 = Claimant.findClaimant("100000001", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_2 = Claimant.findClaimant("100000002", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_3 = Claimant.findClaimant("100000003", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_4 = Claimant.findClaimant("100000004", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_5 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<Claimant> claimants = Lists.newArrayList(claimant_1, claimant_2, claimant_3, claimant_4, claimant_5);

        assertThat(claimants).onProperty("mailingAddress").onProperty("address1").containsOnly("H-1234 PRE-DIR TH-FARE-NM PST-DIR TH-FARE=-TYPE");
        assertThat(claimants).onProperty("mailingAddress").onProperty("address2").containsOnly(" Apt# 301-C ");
        assertThat(claimants).onProperty("mailingAddress").onProperty("city").containsOnly("city-1");
        assertThat(claimants).onProperty("mailingAddress").onProperty("stateCode").containsOnly("state-1");
        assertThat(claimants).onProperty("mailingAddress").onProperty("zipCode").onProperty("zip").containsOnly("02142");
        assertThat(claimants).onProperty("mailingAddress").onProperty("zipCode").onProperty("ext").containsOnly("0001");
        assertThat(claimants).onProperty("mailingAddress").onProperty("addressType").containsOnly(AddressType.ADDRESS_OF_RECORD);
        assertThat(claimants).onProperty("mailingAddress").onProperty("mailingAddress").containsOnly(true);
        
    }

    /**
     * The updated ClaimantAddress must have a AddressResearchChangeActivity associated with it.
     */
    @Test
    @Override
    public void addressMustHaveAddressResearchChangeActivity() {
        final Claimant claimant_1 = Claimant.findClaimant("100000001", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_2 = Claimant.findClaimant("100000002", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_3 = Claimant.findClaimant("100000003", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_4 = Claimant.findClaimant("100000004", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_5 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<Claimant> claimants = Lists.newArrayList(claimant_1, claimant_2, claimant_3, claimant_4, claimant_5);

        assertThat(claimants).onProperty("mailingAddress").onProperty("address").onProperty("researchChangeActivity").onProperty("researchReturned").containsOnly(Boolean.TRUE);
        assertThat(claimants).onProperty("mailingAddress").onProperty("address").onProperty("researchChangeActivity").onProperty("hit").containsOnly(Boolean.TRUE);
        assertThat(claimants).onProperty("mailingAddress").onProperty("address").onProperty("researchChangeActivity").onProperty("addressResearchDescription")
                .containsOnly("Address Research Return... \nMatch: M\nAccount Holder: null null null null null\n" +
                        "Maiden Name: null\nAddress: H-1234 PRE-DIR TH-FARE-NM PST-DIR TH-FARE=-TYPE  Apt# 301-C \n" +
                        "Address Date Reported: 01-01-2012\nSsn: null\nDate of Birth: 02-01-0111\nPhone: 11111111\n" +
                        "Invalid Or Deceased Ssn Tag: null\nMatch Analysis Tag: null\nName Change Tag: null\n" +
                        "Ssn Match Tag: null\nOfac Indicator: null\n");
    }

    /**
     * The {@link Claimant} must have a {@link com.bfds.saec.domain.activity.AddressChange} activity created when the claimant's address is updated.
     * @see #newAddressMustBeCreatedIfClaimantHasNoAddress()
     */
    @Test
    @Override
    public void claimantMustHaveAddressChangeActivity() {
        final Claimant claimant_2 = Claimant.findClaimant("100000002", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_3 = Claimant.findClaimant("100000003", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_4 = Claimant.findClaimant("100000004", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_5 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<AddressChange> addressChanges = Lists.newArrayList((AddressChange)getActivityByType(claimant_2, AddressChange.class),
                (AddressChange)getActivityByType(claimant_3, AddressChange.class),
                (AddressChange)getActivityByType(claimant_4, AddressChange.class),
                (AddressChange)getActivityByType(claimant_5, AddressChange.class));
        assertThat(addressChanges).onProperty("to").onProperty("address1").containsOnly("H-1234 PRE-DIR TH-FARE-NM PST-DIR TH-FARE=-TYPE");
    }
}