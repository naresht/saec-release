package com.bfds.saec.batch.in.db_cashed_check;

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

import com.bfds.saec.batch.file.domain.in.db_cashed_check.CashedCheckRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.test.SaecTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class DbCashedCheckIntegrationTest  extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job dbCashedCheckJob;

	@Autowired
	EventTestData eventTestData;

	@Autowired
	FileRecordTestData fileRecordTestData;

	public void afterAllTests() {
        testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
		fileRecordTestData.create();
	}

	@Override
	protected Job geJOb() {
		return dbCashedCheckJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}

	/**
	 *  Verify all {@link CashedCheckRec}s should be Processed .
	 *  i.e processed flag should be set as true. 
	 */
	@Test
	@Transactional
	public void verifyDbCashedCheckRecProcessed() {
		List<CashedCheckRec> list =  testDataUtil.findAllFileRecords(CashedCheckRec.class);
		assertThat(list.size()).isEqualTo(5);
		assertThat(list).onProperty("processed").containsOnly(Boolean.TRUE);
		
		
	}
	
	/**
	 *  verify all out standing checks are cashed.  
	 * 
	 */
	@Test
	@Transactional
	public void verifyCashedCodeForGivenOutstandingCode() {
		Payment c =Payment.findCheckByNumberAndAmount("4881918444", 100);
		assertThat(PaymentCodeUtil.getCashedCodes()).contains(c.getPaymentCode());
		c =Payment.findCheckByNumberAndAmount("3608287156", 25);
		assertThat(PaymentCodeUtil.getCashedCodes()).contains(c.getPaymentCode());
		c =Payment.findCheckByNumberAndAmount("3608287157", 25);
		assertThat(PaymentCodeUtil.getCashedCodes()).contains(c.getPaymentCode());
		c =Payment.findCheckByNumberAndAmount("3608287158", 25);
		assertThat(PaymentCodeUtil.getCashedCodes()).contains(c.getPaymentCode());
		c =Payment.findCheckByNumberAndAmount("3608287159", 25);
		assertThat(PaymentCodeUtil.getCashedCodes()).contains(c.getPaymentCode());
	}

}
