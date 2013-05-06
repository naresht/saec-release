package com.bfds.saec.batch.out.infoage_corporate;


import com.bfds.saec.batch.TestSupport;
import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.batch.out.infoage.AddressPrescrubSendJobIntegrationTest;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import javax.inject.Inject;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


public class CorporateAddressPrescrubSendJobIntegrationTest extends AddressPrescrubSendJobIntegrationTest {

    @Inject
    Job corporateAddressSendJob;

    @Override
    public void beforeAllTests() {
        eventTestData.setCorporate(true);
        eventTestData.create();
    }

    @Override
    protected Job geJOb() {
        return corporateAddressSendJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "11111111").addString("accountType", AccountType.CORPORATE).addString("researchType", ResearchType.PRESCRUB).toJobParameters();
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
                                {"inputRecord", "", ""},
                                {"fein", "1122334455", "1122334455"},
                                {"companyName", "aaaaaaaaabbbbbbbb", "aaaaaaaaabbbbbbbb"},
                                {"streetAddress", null, "2000 crown colony dr-1"},
                                {"city", null, "Quincy-1"},
                                {"state", null, "MA"},
                                {"zipCode", null, ""}
                        };

        List<OutboundCorporateAddressResearch> actualRecords = eventTestData.findAllFileRecords(OutboundCorporateAddressResearch.class);

        (new TestSupport()).verifyData(actualRecords, expectedRecords);
    }





}
