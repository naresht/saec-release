package com.bfds.saec.batch.out.infoage;

import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.test.SaecTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Tests common to both corporate prescrub and corporate research jobs.
 *
 * @see  AddressPrescrubSendJobIntegrationTest
 * @see AddressResearchSendJobIntegrationTest
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml"})
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public abstract class AddressSendJobIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {


    public abstract void beforeAllTests();

    public abstract void afterAllTests();

    /**
     * A {@link com.bfds.saec.domain.Claimant} that has been sent for address research must have {@link com.bfds.saec.domain.Claimant#addressResearchSent} = true
     */
    @Test
    public abstract void claimantMustBeFlaggedAddressedResearchSent();

    /**
     * A {@link com.bfds.saec.domain.Claimant} that has been sent for address research must have an {@link com.bfds.saec.domain.activity.AddressResearchChangeActivity} associated with the
     * corresponding {@link com.bfds.saec.domain.ClaimantAddress}
     */
    @Test
    public abstract void addressMustHaveAddressResearchChangeActivity();

    /**
     * A {@link com.bfds.saec.domain.Claimant} that has been sent for address research an {@link com.bfds.saec.domain.activity.AddressChange} activity.
     */
    @Test
    public abstract void claimantMustHaveAddressChangeActivity();

    /**
     * For each of the {@link com.bfds.saec.domain.Claimant}s send for research there must be a corresponding {@link com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch}
     * created.
     */
    @Test
    public abstract void verifyCreatedFileRecords();

}
