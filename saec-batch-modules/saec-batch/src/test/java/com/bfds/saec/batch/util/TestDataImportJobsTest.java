package com.bfds.saec.batch.util;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.RPOType;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml"} )
public class TestDataImportJobsTest {
	
	@Autowired
	private Job importTestClaimantsJob;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private PaymentDao dao;
	
	@Before
	public void start() {
		if(LetterCode.findByCode("712") == null) {
			(new LetterCode("712", "Courtesy Letters", LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		}
	}
	
	@After
	public void after() {
		(new TestDataUtil()).deleteData();
	}
	
	@Test
	public void testClaimantRead() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		
		TestClaimantItemReader reader = new TestClaimantItemReader();
		reader.setResource(new ClassPathResource("/util-claimants.xml"));
		reader.open(null);
		Claimant c = reader.read().getClaimant();
		assertThat(c.getFundAccountNo()).isEqualTo("100001");
		assertThat(c.getCorporate()).isEqualTo(Boolean.TRUE);
		assertThat(c.getPayments().size()).isEqualTo(2);				
	}
	
	@Test
	public void testClaimantJob() throws Exception {		
		JobParameters jobParameters = new JobParametersBuilder().addString(
				"inputResource",
				"/util-claimants.xml")
				.toJobParameters();
		JobExecution jobExecution = jobLauncher.run(importTestClaimantsJob,
				jobParameters);
		assertThat(jobExecution.getExitStatus())
				.isEqualTo(ExitStatus.COMPLETED);

		Claimant c = Claimant.findAllClaimants().get(0);
		c = c.findClaimant(c.getId(), Claimant.ASSOCIATION_PAYMENTS);
		assertThat(c.getFundAccountNo()).isEqualTo("100001");
		assertThat(c.getCorporate()).isEqualTo(Boolean.TRUE);
		assertThat(c.getPayments().size()).isEqualTo(2);
		
		Payment check = dao.findCheckByNumberAndAmount("100001", 100);
		assertThat(check.getPaymentStatus()).isEqualTo(PaymentStatus.CASHED);
		assertThat(check.getRpoType()).isEqualTo(RPOType.FORWARDABLE);
		
		Long i = c.getMailCount();
		Letter letter = Letter.findByMailingControlNo("1000002");
		assertThat(letter.getRpoType()).isEqualTo(RPOType.NONFORWARDABLE);
		
	}		
}
