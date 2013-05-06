package com.bfds.saec.batch.out.ifds_check_status;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.Comparator;
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

import com.bfds.saec.batch.file.domain.out.ifds_check_status.IfdsCheckStatusRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class IfdsCheckStatusIntegrationTest extends
		AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job IfdsCheckStatusJob;

	@Autowired
	IfdsCheckStatusEventTestData eventTestData;

	public void beforeAllTests() {
		eventTestData.create();
	}

	public void afterAllTests() {
		testDataUtil.deleteData();
	}

	@Override
	protected Job geJOb() {
		return IfdsCheckStatusJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}

	/**
	 * Payment with only valid IfdsCheckStatus payment-codes picked by the job.
	 */
	@Test
	@Transactional
	public void verifyIfdsSentFlag() {
		List<Payment> list = Lists.newArrayList(
				Payment.findPaymentIdentificationNo("00001011"),
				Payment.findPaymentIdentificationNo("111121"),
				Payment.findPaymentIdentificationNo("111122"),
				Payment.findPaymentIdentificationNo("00002233"));
		

		assertThat(list).hasSize(4);
		assertThat(list).onProperty("ifdsSent").containsOnly(Boolean.TRUE);
	}

	/**
	 * Payment with invalid IfdsCheckStatus payment-codes or invalid
	 * payment-type doesn't picked by the job.
	 */
	@Test
	@Transactional
	public void verifyNotIfdsSentFlag() {
		Payment check = Payment.findPaymentIdentificationNo("55544433");
		assertThat(check.getIfdsSent()).isEqualTo(Boolean.FALSE);
	}

	/**
	 * Verify data in stage table.
	 */
	@Test
	public void verifyCreatedFileRecords() {
		Object[][] expectedRecords = new Object[][] {
				{ "bankRouting", "2234", "2234", "2234" },
				{ "dda", "DDA-1", "DDA-1", "DDA-1" },
				{ "accountNumber", "100007", "100007", "100007" },
				{ "checkNumber", "00001011","00002233", "111121", "111122" },
				{ "netAmount", 550.0, 550.0, 550.0 , 550.0},
				{ "checkStatus", null, null, "INT", "PRA" },
				{ "wireStatus", "WA", "W", null, null  } };

		final List<IfdsCheckStatusRec> actualRecords = testDataUtil.findAllFileRecords(IfdsCheckStatusRec.class);

		assertThat(actualRecords).hasSize(4);

		Collections.sort(actualRecords, new Comparator<IfdsCheckStatusRec>() {
			@Override
			public int compare(IfdsCheckStatusRec o1, IfdsCheckStatusRec o2) {
				return o1.getCheckNumber().compareTo(o2.getCheckNumber());
			}
		});
		testDataUtil.verifyData(actualRecords, expectedRecords);
	}

}
