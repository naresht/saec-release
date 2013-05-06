package com.bfds.saec.batch.stale_date;

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

import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class StaleDateIntegrationTest extends
		AbstractSingleJobExecutionIntegrationTest {

	@Autowired
	private Job staleDateJob;

	@Autowired
	private StaleDateEventTestData eventTestData;

	public void afterAllTests() {
		(new TestDataUtil()).deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
	}
	
	/**
	 * Checks which are valid for StaleDate batch job, will be marked as staleDated = TRUE. 
	 */
	@Test
	public void varifyPaymentStaleDated() {
		List<Payment> list = getPaymentsToBeStaleDated();
		assertThat(list).onProperty("staleDated").containsOnly(Boolean.TRUE);
	}
	
	/**
	 * Checks which are not valid for StaleDate batch job, will not be marked as staleDated = TRUE. 
	 */
	@Test
	public void varifyPaymentNotStaleDatedForInvalidToDateOrPaymentCode() {
		List<Payment> list = getPaymentsNotToBeStaleDated();
		assertThat(list).onProperty("staleDated").containsOnly(Boolean.FALSE);
		assertThat(list).onProperty("paymentCode").isNotIn(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		
	}
	
	@Test
	public void varifyPaymentStatusChangedToOutstanding() {
		List<Payment> list = getPaymentsToBeStaleDated();
		assertThat(list).onProperty("paymentStatus").containsOnly(PaymentStatus.OUTSTANDING);
		
	}
	
	/**
	 * Checks which are valid for StaleDate batch job, their paymentCode will be changed as "STALE_DATE_OUTSTANDING_X_X"
	 */
	@Test
	public void varifyPaymentCodeChangedToStaleDateOutstanding() {
		List<Payment> list = getPaymentsToBeStaleDated();
		assertThat(list).onProperty("paymentCode").containsOnly(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
	}

	@Override
	protected Job geJOb() {
		return staleDateJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}
	
	private List<Payment> getPaymentsToBeStaleDated() {
		List<Payment> list = Lists.newArrayList( Payment.findPaymentIdentificationNo("1001"),
				 								 Payment.findPaymentIdentificationNo("1002"),
				 								 Payment.findPaymentIdentificationNo("1005"),
				 								 Payment.findPaymentIdentificationNo("1006"),
				 								 Payment.findPaymentIdentificationNo("1007"),
				 								 Payment.findPaymentIdentificationNo("1008"),
				 								 Payment.findPaymentIdentificationNo("1009"));
		return list;
	}
	
	private List<Payment> getPaymentsNotToBeStaleDated() {
		List<Payment> list = Lists.newArrayList( Payment.findPaymentIdentificationNo("1003")
												,Payment.findPaymentIdentificationNo("1004"));
		return list;
	}
}
