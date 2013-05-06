package com.bfds.saec.batch.out.ncoa;

import static org.fest.assertions.Assertions.assertThat;

import com.bfds.saec.batch.util.TestDataUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.out.ncoa_outbound.NCOAOutboundRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.test.SaecTestExecutionListener;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class NcoaAdressResearchOutTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job ncoaAddressSendJob;

	@Autowired
	NcoaEventTestData eventTestData;

	public void afterAllTests() {
        testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
		eventTestData.createdClaimantWithoutName();
	}

	@Override
	protected Job geJOb() {
		return ncoaAddressSendJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}
	

	@Test
	@Transactional
	public void addressResearchSentFlagMustbeTrue() {
		final Claimant c = Claimant.findAllClaimants().get(0);
		assertThat(c.getAddressResearchSent()).isTrue();
	}
	
	@Test
	@Transactional
	public void OutboundRecordMusthavetheValues() {
		NCOAOutboundRec ncoarec = testDataUtil.findAllFileRecords(NCOAOutboundRec.class).get(0);
		assertThat(ncoarec.getCity()).isEqualTo("NEWYORK");
		assertThat(ncoarec.getZip()).isEqualTo("23456-4455");
	}
	
	@Test
	@Transactional
	public void verifyActivityDescription() {
		final Claimant claimant = Claimant.findAllClaimants().get(0);
		assertThat(claimant.getActivity()).onProperty("activityTypeDescription").contains("NCOA Address Research");
	}
	
	/**
	 * PIA-172 To verify ClaimanttRegistarion lines(1 to 6) are displayed in the NCOA Outbound file from position 58 if first,middle and last names
	 * are not available.
	 */
	@Test
	@Transactional
	public void varifyOutboundRecordMustHaveName() {
		
		NCOAOutboundRec ncoarec = testDataUtil.findAllFileRecords(NCOAOutboundRec.class).get(1);
		assertThat(ncoarec.getName()).isNotEqualTo(null);
		assertThat(ncoarec.getName()).isEqualTo("RegistrationName1 RegistrationName2 RegistrationName3 RegistrationName4");

	}
	
}
