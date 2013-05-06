package com.bfds.saec.batch.in.ss_bottom_line;

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

import com.bfds.saec.batch.file.domain.in.ss_bottom_line.SsBottomLineInRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SsBottomlineInboundTest  extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job ssBottomlineInJob;

	@Autowired
    SsBottomlineInboundEventTestData eventTestData;

    @Autowired
    SsBottomlineInboundFileRecordTestData fileRecordTestData;

    
    public void afterAllTests() {
        testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
		fileRecordTestData.create();
	}

	@Override
	protected Job geJOb() {
		return ssBottomlineInJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}
    
	@Test
	@Transactional
	public void verifyssBottomlineInRecProcessed() {
		List<SsBottomLineInRec> list = testDataUtil.findAllFileRecords(SsBottomLineInRec.class);
		assertThat(list.size()).isEqualTo(3);
		assertThat(list).onProperty("processed").containsOnly(Boolean.TRUE);
	}
	
	@Test
	@Transactional
	public void verifyIdentificationNumbersbySsBottomlineInJob() {
		
		List<Payment> checks1 = Payment.findChecksByAccountNoAndNettAmount( "100007", 100d);
		Payment p1=checks1.get(0);
		assertThat(p1.getIdentificatonNo()).contains("1");
		
		checks1 = Payment.findChecksByAccountNoAndNettAmount( "100007", 200d);
		Payment p2=checks1.get(0);
		assertThat(p2.getIdentificatonNo()).contains("2");
		
		checks1 = Payment.findChecksByAccountNoAndNettAmount("100007", 300d);
		Payment p3=checks1.get(0);
		assertThat(p3.getIdentificatonNo()).contains("3");
	}
	
}
