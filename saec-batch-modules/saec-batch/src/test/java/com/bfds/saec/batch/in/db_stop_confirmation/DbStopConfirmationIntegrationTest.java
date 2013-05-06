package com.bfds.saec.batch.in.db_stop_confirmation;

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

import com.bfds.saec.batch.file.domain.in.db_stop_confirmation.StopConfirmationRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.test.SaecTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class DbStopConfirmationIntegrationTest  extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job dbStopConfirmationJob;

    @Autowired
    DbStopConfirmationEventTestData dbStopConfirmationEventTestData;

    @Autowired
    DbStopConfirmationFileRecordTestData dbStopConfirmationFileRecordTestData;

	public void afterAllTests() {
        testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		dbStopConfirmationEventTestData.create();
		dbStopConfirmationFileRecordTestData.create();
	}

	@Override
	protected Job geJOb() {
		return dbStopConfirmationJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}
    
	/**
	 *  Verify all {@link StopConfirmationRec}s should be Processed .
	 *  i.e processed flag should be set as true. 
	 */
	@Test
	@Transactional
	public void verifyStopConfirmationRecProcessed() {
		List<StopConfirmationRec> list =  testDataUtil.findAllFileRecords(StopConfirmationRec.class);
		assertThat(list.size()).isEqualTo(4);
		assertThat(list).onProperty("processed").containsOnly(Boolean.TRUE);
		
		
	}
	
	/**
	 *  Verifies the stop conform records which are having reject code "000" .
	 */
	@Test
	@Transactional
	public void verifyStopConfirmedRecords() {
		
		Payment c1 = Payment.findCheckByNumberAndAmount("4881918444", 100);
		assertThat(c1.getPaymentStatus()).isEqualTo(PaymentStatus.STOPPED_CONFIRMED);
		c1 =Payment.findCheckByNumberAndAmount("3608287157", 25.25);
		assertThat(c1.getPaymentStatus()).isEqualTo(PaymentStatus.STOPPED_CONFIRMED);
	}

	/**
	 * Verifies the stop rejected records which are having reject code "1" .
	 */
	@Test
	@Transactional
	public void verifyStopRejectedRecords() {
		
		Payment c1 = Payment.findCheckByNumberAndAmount("3608287156", 25.25);
		assertThat(c1.getPaymentStatus()).isEqualTo(PaymentStatus.STOP_REJECTED);
		c1 =Payment.findCheckByNumberAndAmount("3608287158", 12.25);
		assertThat(c1.getPaymentStatus()).isEqualTo(PaymentStatus.STOP_REJECTED);
	}
	
	
	

}
