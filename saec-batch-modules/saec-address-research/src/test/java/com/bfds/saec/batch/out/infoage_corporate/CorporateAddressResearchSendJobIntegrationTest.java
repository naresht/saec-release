package com.bfds.saec.batch.out.infoage_corporate;


import com.bfds.saec.batch.TestSupport;
import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.batch.out.infoage.AddressResearchSendJobIntegrationTest;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import javax.inject.Inject;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


public class CorporateAddressResearchSendJobIntegrationTest extends AddressResearchSendJobIntegrationTest {

    @Inject
    Job corporateAddressSendJob;

    @Override
    protected Job geJOb() {
        return corporateAddressSendJob;
    }

    @Override
    public void beforeAllTests() {
        eventTestData.setCorporate(true);
        eventTestData.create();
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "11111111").addString("accountType", AccountType.CORPORATE).addString("researchType", ResearchType.RESEARCH).toJobParameters();
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
                                {"userRef", "100000001", "100000002", "100000003"} ,
                                {"inputRecord", "", "", ""},
                                {"fein", "1122334455", "1122334455", "1122334455"},
                                {"companyName", "aaaaaaaaabbbbbbbb", "aaaaaaaaabbbbbbbb", "aaaaaaaaabbbbbbbb"},
                                {"streetAddress", "2000 crown colony dr-1", "2000 crown colony dr-1", "2000 crown colony dr-1"},
                                {"city", "Quincy-1", "Quincy-1", "Quincy-1"},
                                {"state", "MA", "MA", "MA"},
                                {"zipCode", "", "", ""}
                        };

        List<OutboundCorporateAddressResearch> actualRecords = eventTestData.findAllFileRecords(OutboundCorporateAddressResearch.class);

        (new TestSupport()).verifyData(actualRecords, expectedRecords);
    }
}
