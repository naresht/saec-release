package com.bfds.saec.batch.out.ss_stop_payment;

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

import com.bfds.saec.batch.file.domain.out.ss_stop_payment.SsStopPaymentRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class SsStopPaymentIntegrationTest extends
		AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job ssStopPaymentJob;

	@Autowired
	SsStopPaymentEventTestData eventTestData;


	public void beforeAllTests() {
		eventTestData.create();
	}

	public void afterAllTests() {
		testDataUtil.deleteData();
	}

	@Override
	protected Job geJOb() {
		return ssStopPaymentJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all_1").toJobParameters();
	}

	/**
	 * Payment with only valid SsStopPayment payment-codes picked by the job.
	 */
	@Test
	@Transactional
	public void verifySentToBankFlag() {

		List<Payment> list = Lists.newArrayList(
				Payment.findPaymentIdentificationNo("123456"),
				Payment.findPaymentIdentificationNo("1234567"),
				Payment.findPaymentIdentificationNo("12345"),
				Payment.findPaymentIdentificationNo("1001"),
				Payment.findPaymentIdentificationNo("1002"),
				Payment.findPaymentIdentificationNo("1003"),
				Payment.findPaymentIdentificationNo("1004"));

		assertThat(list).hasSize(7);
		assertThat(list).onProperty("sentOnBankStopFile").containsOnly(
				Boolean.TRUE);
	}

	/**
	 * Payment with invalid SsStopPayment payment-codes doesn't picked by the
	 * job.
	 */
	@Test
	@Transactional
	public void verifyNotSentToBankFlag() {

		List<Payment> list = Lists.newArrayList(
				Payment.findPaymentIdentificationNo("1005"),
				Payment.findPaymentIdentificationNo("1006"));
		
		assertThat(list).hasSize(2);
		assertThat(list).onProperty("sentOnBankStopFile").isNotIn(Boolean.TRUE);
	}

	/**
	 * Payment with identificationNo == null,doesn't picked by the job.
	 */
	@Test
	@Transactional
	public void verifyNotSentToBankFlagForNullIdentification() {
		List<Payment> list = Payment.findChecksByAccountNoAndNettAmount("100007", 101.75);
		
		assertThat(list).onProperty("sentOnBankStopFile").isNotIn(Boolean.TRUE);
	}

	/**
	 * Verify the stage data.
	 */
	@Test
	public void verifyCreatedFileRecords() {
		Object[][] expectedRecords = new Object[][] {
				{ "bankNumber", "052", "052", "052", "052", "052", "052", "052" },
				{ "dda", Event.getCurrentEventDda() },
				{ "tranCode", "22" },
				{ "checkSerialNumber", "1002", "1003", "1004", "12345","123456", "1234567"},
				{ "amountOfCheck", 1050.0, 1050.0, 1050.0, 1050.0, 1050.0,100.0, 10.0}
				
		};

		final List<SsStopPaymentRec> actualRecords = testDataUtil.findAllFileRecords(SsStopPaymentRec.class);

		assertThat(actualRecords).hasSize(7);
		Collections.sort(actualRecords, new Comparator<SsStopPaymentRec>() {
			@Override
			public int compare(SsStopPaymentRec o1, SsStopPaymentRec o2) {
				return o1.getCheckSerialNumber().compareTo(o2.getCheckSerialNumber());
			}
		});
		testDataUtil.verifyData(actualRecords, expectedRecords);
	}
}
