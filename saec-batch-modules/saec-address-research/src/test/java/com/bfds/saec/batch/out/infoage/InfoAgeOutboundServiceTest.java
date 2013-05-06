package com.bfds.saec.batch.out.infoage;

import static org.fest.assertions.Assertions.assertThat;

import javax.inject.Inject;
import javax.inject.Named;

import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.file.domain.out.infoage_individual.OutboundIndividualAddressResearch;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.RpoEligibleOption;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml"})
public class InfoAgeOutboundServiceTest {
	
	@Autowired
	private AddressResearchOutboundService<OutboundCorporateAddressResearch> corpAddressResearchOutboundService;
	
	@Autowired
	private AddressResearchOutboundService<OutboundIndividualAddressResearch> individualAddressResearchOutboundService;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Inject
	@Named("addressResearchSendServiceTestDataGenerator")
    InfoAgeOutboundServiceTestData datagenerator;
	
	@After
	public void after() {
		datagenerator.cleanup_();
	}

	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is not sent for research if it has exceeded it's send count limit for {@link Payment}s. 
	 */
	@Test
	public void testCorporateSendCountLimitExceededForPayment() {
		datagenerator.newEvent(10, 0);
		datagenerator.newClaimant(15, 0, 0, true, false);
		
		final OutboundCorporateAddressResearch research = getOutboundCorporateAddressResearch("10000001");		
		
		assertThat(research).isNull();
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is not sent for research if it has exceeded it's send count limit for {@link Letter}s. 
	 */
	@Test
	public void testCorporateSendCountLimitExceededForLetter() {
		datagenerator.newEvent(0, 5);
		datagenerator.newClaimant(0, 6, 0, false, true);

		final OutboundCorporateAddressResearch research = getOutboundCorporateAddressResearch("10000001");		
		
		assertThat(research).isNull();			
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is not sent for research if it has exceeded it's send count limit for {@link Letter}s and {@link Payment}s.
	 *  
	 */
	@Test
	public void testCorporateSendCountLimitExceededForBothLettersAndChecks() {
		datagenerator.newEvent(5, 5);
		datagenerator.newClaimant(3, 3, 5, true, true);

		final OutboundCorporateAddressResearch research = getOutboundCorporateAddressResearch("10000001");		
		
		assertThat(research).isNull();				
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is sent for research if it has not exceeded it's send count limit for {@link Payment}s. 
	 */
	@Test
	@Ignore("This test is failing. Fix it.")
	public void testCorporateSendCountLimitNotExceededForPayment() {
		datagenerator.newEvent(10, 0);
		datagenerator.newClaimant(9, 0, 0, true, false);
		final OutboundCorporateAddressResearch research = getOutboundCorporateAddressResearch("10000001");		
		
		assertThat(research).isNotNull();			
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is sent for research if it has not exceeded it's send count limit for {@link Letter}s. 
	 */
	@Test
	public void testCorporateSendCountLimitNotExceededForLetter() {
		datagenerator.newEvent(0, 5);
		datagenerator.newClaimant(0, 4, 0, false, true);
		final OutboundCorporateAddressResearch research = getOutboundCorporateAddressResearch("10000001");		
		
		assertThat(research).isNotNull();				
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is sent for research if it has not exceeded it's send count limit for {@link Letter}s and {@link Payment}s.
	 *  
	 */
	@Test
	@Ignore("This test is failing. Fix it.")
	public void testCorporateSendCountLimitNotExceededForBothLettersAndChecks() {
		datagenerator.newEvent(5, 5);
		datagenerator.newClaimant(3, 3, 1, true, true);
		final OutboundCorporateAddressResearch research = getOutboundCorporateAddressResearch("10000001");		
		
		assertThat(research).isNotNull();				
	}

	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is not sent for research if it has exceeded it's send count limit for {@link Payment}s. 
	 */
	@Test
	public void testIndividualSendCountLimitExceededForPayment() {
		datagenerator.newEvent(10, 0);
		datagenerator.newClaimant(15, 0, 0, true, false);
		
		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNull();
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is not sent for research if it has exceeded it's send count limit for {@link Letter}s. 
	 */
	@Test
	public void testIndividualSendCountLimitExceededForLetter() {
		datagenerator.newEvent(0, 5);
		datagenerator.newClaimant(0, 6, 0, false, true);

		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNull();			
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is not sent for research if it has exceeded it's send count limit for {@link Letter}s and {@link Payment}s.
	 *  
	 */
	@Test
	public void testIndividualSendCountLimitExceededForBothLettersAndChecks() {
		datagenerator.newEvent(5, 5);
		datagenerator.newClaimant(3, 3, 5, true, true);

		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNull();				
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is sent for research if it has not exceeded it's send count limit for {@link Payment}s. 
	 */
	@Test
	@Ignore("This test is failing. Fix it.")
	public void testIndividualSendCountLimitNotExceededForPayment() {
		datagenerator.newEvent(10, 0);
		datagenerator.newClaimant(9, 0, 0, true, false);
		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNotNull();			
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is sent for research if it has not exceeded it's send count limit for {@link Letter}s. 
	 */
	@Test
	public void testIndividualSendCountLimitNotExceededForLetter() {
		datagenerator.newEvent(0, 5);
		datagenerator.newClaimant(0, 4, 0, false, true);
		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNotNull();				
	}
	
	/**
	 * A Claimant is not eligible for address research if it has exceeded its send count. 
	 * This test verifies that the claimant is sent for research if it has not exceeded it's send count limit for {@link Letter}s and {@link Payment}s.
	 *  
	 */
	@Test
	@Ignore("This test is failing. Fix it.")
	public void testIndividualSendCountLimitNotExceededForBothLettersAndChecks() {
		datagenerator.newEvent(5, 5);
		datagenerator.newClaimant(3, 3, 1, true, true);
		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNotNull();				
	}
	
	/**
	 * Verifies that {@link Payment}s are not considered for address research if {@link Event} configuration excludes them.
	 * This implies that a {@link Claimant} with only RPO Non Forwardable {@link Payment} will not go out for research.
	 */
	@Test
	public void testExclusionOfChecksFromResearch() {
		datagenerator.newEvent(50, 50, RpoEligibleOption.MAIL);
		datagenerator.newClaimant(3, 3, 1, true, false);
		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNull();				
	}
	
	/**
	 * Verifies that {@link Letter}s are not considered for address research if {@link Event} configuration excludes them.
	 * This implies that a {@link Claimant} with only RPO Non Forwardable {@link Letter} will not go out for research.
	 */
	@Test
	public void testExclusionOfLettersFromResearch() {
		datagenerator.newEvent(50, 50, RpoEligibleOption.PAYMENT);
		datagenerator.newClaimant(3, 3, 1, false, true);
		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");		
		
		assertThat(research).isNull();				
	}
	
	/**
	 * A {@link Claimant} must be flagged as sent after it is sent for address research.
	 */
	@Test
	public void setAddressResearchSentFlagToTrueAfterPickingTheClaimantForAddressResearch() {
		datagenerator.newEvent(50, 50, RpoEligibleOption.MAIL_AND_PAYMENT);
		datagenerator.newClaimant(3, 3, 1, false, true);
		final OutboundIndividualAddressResearch research = getOutboundIndividualAddressResearch("10000001");				
		assertThat(research).isNotNull();				
		Claimant claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();
		assertThat(claimant.getAddressResearchSent()).isTrue();	
	}

	private  OutboundIndividualAddressResearch getOutboundIndividualAddressResearch(final String referenceNo) {
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		return  tt.execute(new TransactionCallback<OutboundIndividualAddressResearch>() {
			@Override
			public OutboundIndividualAddressResearch doInTransaction(TransactionStatus status) {
				Claimant claimant = Claimant.findClaimantsByReferenceNo(referenceNo).getSingleResult();
				
				OutboundIndividualAddressResearch research = (OutboundIndividualAddressResearch) individualAddressResearchOutboundService.createAddressResearch(claimant);
				// Persist changes made by the service.
				claimant.merge().flush();
				return research;
			}
		});
	}
	
	
	private  OutboundCorporateAddressResearch getOutboundCorporateAddressResearch(final String referenceNo) {
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		return  tt.execute(new TransactionCallback<OutboundCorporateAddressResearch>() {
			@Override
			public OutboundCorporateAddressResearch doInTransaction(TransactionStatus status) {
				Claimant claimant = Claimant.findClaimantsByReferenceNo(referenceNo).getSingleResult();
				
				OutboundCorporateAddressResearch research = (OutboundCorporateAddressResearch) corpAddressResearchOutboundService.createAddressResearch(claimant);								
			
				return research;
			}
		});
	}
	
}
