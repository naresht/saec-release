package com.bfds.saec.batch.in.ncoa;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

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

import com.bfds.saec.batch.file.domain.in.ncoa_link_record.NCOALinkRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class NcoaAddressResearchInTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job ncoaInboundAddressResearchJob;

	@Autowired
	NcoaInboundEventTestData eventTestData;

	@Autowired
	NcoaInboundRecordData fileRecordTestData;

	public void afterAllTests() {
        testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
		fileRecordTestData.create();
	}

	@Override
	protected Job geJOb() {
		return ncoaInboundAddressResearchJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}

	@Test
	@Transactional
	public void verifyaddressResearchSentFlag() {
		Claimant claimant = Claimant.findClaimant("100000001");
		assertThat(claimant.getAddressResearchSent()).isFalse();
	}
	
	@Test
	@Transactional
	public void verifyActivityDescription() {
		Claimant claimant = Claimant.findClaimant("100000001");
		assertThat(claimant.getActivity()).onProperty("activityTypeDescription").contains("NCOA Address Research");
		assertThat(claimant.getActivity()).onProperty("description").contains("NCOA indicates change of Address");
		assertThat(claimant.getActivity()).onProperty("userId").contains("NCOA");
	}
	
 
	@Test
	@Transactional
	public void verifyClaimantAddressChange() {
		List<AddressChange> alladdresschanges = AddressChange
				.findAllAddressChanges();
		
		assertThat(alladdresschanges).onProperty("to.city")
				.contains("TestCity");

		assertThat(alladdresschanges).onProperty("to.address1").contains(
				"Test Primary Address");
		alladdresschanges.removeAll(Collections.singleton(null));
		assertThat(alladdresschanges).onProperty("to.address2").isNotNull();
		 
		assertThat(alladdresschanges).onProperty("to.zipCode.zip").contains(
				"21453");
	}
	
	

	@Test
	@Transactional
	public void thefileRecordMustbeMArkedAsProcessed() {
		NCOALinkRec ncoarec = testDataUtil.findAllFileRecords(NCOALinkRec.class).get(0);
		assertThat(ncoarec.getProcessed()).isTrue();
	}
	
	
	@Test
	@Transactional
	public void addresscanNotbeUpdtedIfNOMacth() {
		 final Claimant claimant_1 = Claimant.findClaimant("100000002", Claimant.ASSOCIATION_ALL);
		 List<Claimant> claimants = Lists.newArrayList(claimant_1);
		 assertThat(claimants).onProperty("mailingAddress").onProperty("address1").containsOnly("address 2");
	}
	
	
	@Test
	@Transactional
	public void verifyActivityDescriptionIfNoAddressMatch() {
		Claimant claimant = Claimant.findClaimant("100000002");
		assertThat(claimant.getActivity()).onProperty("activityTypeDescription").contains("NCOA Address Research");
		assertThat(claimant.getActivity()).onProperty("description").contains("No NCOA Match");
		assertThat(claimant.getActivity()).onProperty("userId").contains("NCOA");
	}
	
}
