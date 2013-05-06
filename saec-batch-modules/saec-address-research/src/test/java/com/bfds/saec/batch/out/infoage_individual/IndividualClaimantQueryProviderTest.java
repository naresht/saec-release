package com.bfds.saec.batch.out.infoage_individual;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bfds.saec.batch.out.infoage.InfoAgeOutboundServiceTestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml"})
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class })

public class IndividualClaimantQueryProviderTest {
	
	@Autowired
	private JpaQueryProvider addressResearchSendCriteria;
	
	@PersistenceContext(unitName="entityManagerFactory")
	private EntityManager entityManager;
	
	@Inject
	@Named("addressResearchSendServiceTestDataGenerator")
    InfoAgeOutboundServiceTestData datagenerator;
	
	
	@Before
	public void before() {
		addressResearchSendCriteria.setEntityManager(entityManager);
	}
	
	 public StepExecution getStepExecution() {
		 JobParameters jobParameters = new JobParametersBuilder()
	                .addString("researchType", ResearchType.RESEARCH)
	                .addString("accountType", AccountType.INDIVIDUAL)
	                .toJobParameters();		
		 StepExecution execution = MetaDataInstanceFactory.createStepExecution(jobParameters);
		 return execution;
	 }
	
	@After
	public void after() {
		datagenerator.cleanup_();
	}
	
	/**
	 * Test to verify that query provider picks {@link Claimant}s that have RPO Non Forwardable {@link Payment}
	 */
	@Test
	public void anIndividualClaimantWithVoidNonForwardablePaymentMustBePicked() {
		datagenerator.newClaimant(false, 0, 0, 0, true, false);

		List<Claimant> list = addressResearchSendCriteria.createQuery().getResultList();
		assertThat(list).hasSize(1);
		assertThat(list).onProperty("referenceNo").containsOnly("10000001");
		assertThat(list).onProperty("corporate").containsOnly(false);
	}
	
	/**
	 * Test to verify that query provider picks {@link Claimant}s that have RPO Non Forwardable {@link Letter}
	 */
	@Test
	public void anIndividualClaimantWithNonForwardableLetterMustBePicked() {
		datagenerator.newClaimant(false, 0, 0, 0, false, true);

		List<Claimant> list = addressResearchSendCriteria.createQuery().getResultList();
		assertThat(list).hasSize(1);
		assertThat(list).onProperty("referenceNo").containsOnly("10000001");
		assertThat(list).onProperty("corporate").containsOnly(false);
	}

}
