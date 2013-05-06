package com.bfds.saec.batch.out.infoage;


import com.bfds.saec.batch.TestSupport;
import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.batch.out.infoage.AddressResearchEventTestData;
import com.bfds.saec.batch.out.infoage.AddressSendJobIntegrationTest;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import javax.inject.Inject;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


public abstract class AddressPrescrubSendJobIntegrationTest extends AddressSendJobIntegrationTest {


    @Inject
    protected AddressResearchEventTestData eventTestData;

    @Override
    public void afterAllTests() {
        eventTestData.deleteData();
        eventTestData.deleteFileRecordData();
    }

    /**
     * A {@link com.bfds.saec.domain.Claimant} that has been sent for address research must have {@link com.bfds.saec.domain.Claimant#addressResearchSent} = true
     */
    @Test
    public void claimantMustBeFlaggedAddressedResearchSent() {
        final Claimant claimant_1 = Claimant.findClaimant("100000004", Claimant.ASSOCIATION_ALL);
        final Claimant claimant_2 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<Claimant> claimants = Lists.newArrayList(claimant_1, claimant_2);

        assertThat(claimants).onProperty("addressResearchSent").containsOnly(Boolean.TRUE);
    }

    /**
     * A {@link com.bfds.saec.domain.Claimant} that has been sent for address research must have an {@link com.bfds.saec.domain.activity.AddressResearchChangeActivity} associated with the
     * corresponding {@link com.bfds.saec.domain.ClaimantAddress}
     */
    @Test
    @Override
    public void addressMustHaveAddressResearchChangeActivity() {
        final Claimant claimant_1 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<Claimant> claimants = Lists.newArrayList(claimant_1);

        assertThat(claimants).onProperty("mailingAddress").onProperty("address").onProperty("researchChangeActivity").onProperty("addressResearchDescription")
                .containsOnly(AddressResearchChangeActivity.ACTIVITY_TYPE_ADDRESS_RESEARCH_PRESCRUB_SENT);

    }

    /**
     * A {@link com.bfds.saec.domain.Claimant} that has been sent for address research an {@link com.bfds.saec.domain.activity.AddressChange} activity.
     */
    @Test
    @Override
    public void claimantMustHaveAddressChangeActivity() {
        final Claimant claimant_1 = Claimant.findClaimant("100000005", Claimant.ASSOCIATION_ALL);
        List<AddressChange> addressChanges = Lists.newArrayList((AddressChange)(new TestSupport()).getActivityByType(claimant_1, AddressChange.class));

        assertThat(addressChanges).onProperty("to").onProperty("researchChangeActivity").onProperty("addressResearchDescription")
                .containsOnly(AddressResearchChangeActivity.ACTIVITY_TYPE_ADDRESS_RESEARCH_PRESCRUB_SENT);

    }


    /**
     * For each of the {@link Claimant}s send for research there must be a corresponding {@link OutboundCorporateAddressResearch}
     * created.
     */
    @Test
    @Override
    public abstract void verifyCreatedFileRecords();





}
