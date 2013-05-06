package com.bfds.saec.batch.out.infoage_individual;


import com.bfds.saec.batch.TestSupport;
import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.file.domain.out.infoage_individual.OutboundIndividualAddressResearch;
import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.batch.out.infoage.AddressPrescrubSendJobIntegrationTest;
import com.bfds.saec.batch.out.infoage.AddressResearchEventTestData;
import com.bfds.saec.batch.out.infoage.AddressSendJobIntegrationTest;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import javax.inject.Inject;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


public class IndividualAddressPrescrubSendJobIntegrationTest extends AddressPrescrubSendJobIntegrationTest {

    @Inject
    Job individualAddressSendJob;

    @Override
    public void beforeAllTests() {
        eventTestData.setCorporate(false);
        eventTestData.create();
    }

    @Override
    protected Job geJOb() {
        return individualAddressSendJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "11111111").addString("accountType", AccountType.INDIVIDUAL).addString("researchType", ResearchType.PRESCRUB).toJobParameters();
    }

    /**
     * For each of the {@link com.bfds.saec.domain.Claimant}s send for research there must be a corresponding {@link com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch}
     * created.
     */
    @Test
    @Override
    public void verifyCreatedFileRecords() {
        Object[][] expectedRecords =
                new Object[][]
                        {
                                {"userRef", "100000004", "100000005"} ,
                                {"firstName", "first name-1", "first name-1"},
                                {"middleName", "middle name-1", "middle name-1"},
                                {"inputRecord", "", ""},
                                {"ssn", "11-22-33", "11-22-33"},
                                {"streetAddress", null, "2000 crown colony dr-1"},
                                {"city", null, "Quincy-1"},
                                {"state", null, "MA"},
                                {"zipCode", null, ""}
                        };

        List<OutboundIndividualAddressResearch> actualRecords = eventTestData.findAllFileRecords(OutboundIndividualAddressResearch.class);

        (new TestSupport()).verifyData(actualRecords, expectedRecords);
    }





}
