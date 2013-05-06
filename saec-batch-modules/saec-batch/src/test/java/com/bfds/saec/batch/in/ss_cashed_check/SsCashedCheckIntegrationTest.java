package com.bfds.saec.batch.in.ss_cashed_check;

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

import com.bfds.saec.batch.file.domain.in.ss_cashed_check.SsCashedCheckRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.test.SaecTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SsCashedCheckIntegrationTest  extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job ssCashedCheckJob;

	@Autowired
	SsCashedCheckEventTestData eventTestData;

	@Autowired
	SsCashedCheckFileRecordTestData fileRecordTestData;

	public void afterAllTests() {
        testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
		fileRecordTestData.create();
	}

	@Override
	protected Job geJOb() {
		return ssCashedCheckJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}

	@Test
	@Transactional
	public void verifySsCashedCheckRecProcessed() {
		List<SsCashedCheckRec> list = testDataUtil.findAllFileRecords(SsCashedCheckRec.class);
		assertThat(list.size()).isEqualTo(3);
		assertThat(list).onProperty("processed").containsOnly(Boolean.TRUE);
	}
	
	@Test
	@Transactional
	public void verifyCashedCodeForGivenOutstandingCode() {
		
		Payment c1 = Payment.findCheckByNumberAndAmount("1", 100);
		assertThat(PaymentCode.FIRST_ISSUE_CASHED_C_FIC).isEqualTo(c1.getPaymentCode());
		
		c1 = Payment.findCheckByNumberAndAmount("2", 200);
		assertThat(PaymentCode.ISSUANCE_CASHED_C_ISC).isEqualTo(c1.getPaymentCode());
		
		c1 = Payment.findCheckByNumberAndAmount("3", 300);
		assertThat(PaymentCode.NEW_ISSUE_CASHED_C_NIC).isEqualTo(c1.getPaymentCode());
	}

}
