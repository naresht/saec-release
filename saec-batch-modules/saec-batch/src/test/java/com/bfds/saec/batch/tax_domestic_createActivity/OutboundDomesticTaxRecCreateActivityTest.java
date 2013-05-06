package com.bfds.saec.batch.tax_domestic_createActivity;

import static org.fest.assertions.Assertions.assertThat;

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

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.test.SaecTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class OutboundDomesticTaxRecCreateActivityTest extends
		AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job outboundDomesticTaxReccActivityCreateJob;

	@Autowired
	TaxDomesticEventCreateActivityTestData eventTestData;

	@Autowired
	TaxDomesticFileRecordCreateActivityTestData fileRecordTestData;

	public void afterAllTests() {
		testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
		fileRecordTestData.create();
	}

	@Override
	protected Job geJOb() {
		return outboundDomesticTaxReccActivityCreateJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}

	/**
	 * 
	 * Verify {@link Claimant} must have Activity record ones the out bound file
	 * processed for Tax processing .
	 * 
	 */

	@Test
	@Transactional
	public void claimantMustHaveTaxProcessingActivity() throws Exception {
		Claimant claimant = Claimant.findClaimant("1001");
		assertThat(claimant.getActivity()).onProperty("description").contains(
				"File to Damasco");
	}

	/**
	 * Verify all {@link OutboundDomesticTaxRec}s should be Processed . i.e
	 * createdActivity flag should be set as true.
	 */
	@Test
	@Transactional
	public void verifyOutboundDomesticTaxRecProcessed() {
		List<OutboundDomesticTaxRec> list = testDataUtil.findAllFileRecords(OutboundDomesticTaxRec.class);
		assertThat(list.size()).isEqualTo(1);
		assertThat(list).onProperty("createdActivity").containsOnly(
				Boolean.TRUE);

	}

}
