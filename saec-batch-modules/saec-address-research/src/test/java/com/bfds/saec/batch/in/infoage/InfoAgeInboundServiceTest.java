package com.bfds.saec.batch.in.infoage;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.HitIndicatorCorpType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.HitIndicatorType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_individual.MatchIndicatorType;
import com.bfds.saec.batch.in.infoage_corporate.CorpInfoAgeInboundServiceImpl;
import com.bfds.saec.batch.in.infoage_individaul.IndividualInfoAgeInboundServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.in.infoage.rule.RulesManager;
import com.bfds.saec.batch.in.infoage.rule.SimpleRulesManager;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })

public class InfoAgeInboundServiceTest {
	
	@Inject
    AddressResearchInboundService individualAddressResearchInboundService;
	
	@Inject
	AddressResearchInboundService corpAddressResearchInboundService;
	
	@Inject
    CorpInfoAgeInboundServiceImpl corpInfoAgeInboundServiceImpl;
	
	@Inject
    IndividualInfoAgeInboundServiceImpl individualInfoAgeInboundServiceImpl;
	
	@Inject
	SimpleRulesManager simpleRulesManager;
	
	@Inject
	@Named("addressResearchReceiveServiceTestDataGenerator")
    InfoAgeInboundServiceTestData datagenerator;
	
	RulesManager rulesManager = new SimpleRulesManager();
	
	@Before
	public void before() {
		datagenerator.newEvent();
	}
	
	/**
	 * If the OFAC indicator is set to 'O' then the candidate {@link Payment} and {@link Letter} object comments column should be updated to 
	 * InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED.
	 * This is for Individual address research only. 
	 * 
	 * @see https://github.com/BFDS/saec/issues/498
	 */
	@Test
	@Transactional
	public void testIndividualResearchOFACUpdatesOnRPOItems() {
		
		datagenerator.newClaimant(true, true);
		
		individualAddressResearchInboundService.processResearchedAddresses(asList(datagenerator.newIndividualAddressResearch(HitIndicatorType.Y, MatchIndicatorType.H, "D", null, "N", "S", "O")));
		
		final Claimant claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();
		final Letter letter     = claimant.getLetters().iterator().next();
		final Payment payment   = claimant.getPayments().iterator().next();			
		
		assertThat(payment.getComments()).containsIgnoringCase(InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
		
		assertThat(letter.getComments()).containsIgnoringCase(InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
	}
	
	
	/**
	 * Test to verify that all the fields returned in a individual research file (<IndividualAddressResearch>) are stored in the {@link Activity}
	 */
	@Test
	@Transactional
	@Ignore("This test is failing. Fix it.")
	public void testIndividualResearchFields() {
		
		datagenerator.newClaimant(false, false);
		
		individualAddressResearchInboundService.processResearchedAddresses(asList(datagenerator.newIndividualAddressResearch(HitIndicatorType.Y, MatchIndicatorType.M, "D", null, null, null, null)));
		(new AddressChange()).flush();
		List<AddressChange> activityList = AddressChange.findAllAddressChanges();
		
		assertThat(activityList).hasSize(1);
		AddressResearchChangeActivity researchChangeActivity = activityList.get(0).getTo().getResearchChangeActivity();
		
		assertThat(researchChangeActivity.getAddressResearchDescription()).contains("Match:");
		
		
	}
	
	/**
	 * Test to verify that all the fields returned in a corporate research file (<CorporateAddressResearch>) are stored in the {@link Activity}
	 */
	@Test
	@Transactional
	@Ignore("This test is failing. Fix it.")
	public void testCorporateResearchFields() {
		
		datagenerator.newClaimant(false, false);
		
		corpAddressResearchInboundService.processResearchedAddresses(asList(datagenerator.newCorporateAddressResearch(HitIndicatorCorpType.Y, null)));
		(new AddressChange()).flush();
		List<AddressChange> activityList = AddressChange.findAllAddressChanges();
		
		AddressResearchChangeActivity researchChangeActivity = activityList.get(0).getTo().getResearchChangeActivity();
		
		assertThat(researchChangeActivity.getAddressResearchDescription()).contains("Company Name:");
	}
	
	
	/**
	 * If the {@link CorporateAddressResearch#getHitIndicator()} == HitIndicatorCorpType.N then the candidate {@link Payment} and {@link Letter} object comments column should be updated to 
	 * InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED.
	 * This is for both Corporate and Individual address research. 
	 */	
	@Test
	@Transactional
	public void testAddrsssResearchHitMissUpdatesOnRPOItems() {
		
		datagenerator.newClaimant(true, true);
		
		corpAddressResearchInboundService.processResearchedAddresses(asList(datagenerator.newCorporateAddressResearch(HitIndicatorCorpType.N, null)));
		
		final Claimant claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();
		final Letter letter     = claimant.getLetters().iterator().next();
		final Payment payment   = claimant.getPayments().iterator().next();			
		
		assertThat(payment.getComments()).containsIgnoringCase(InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
		
		assertThat(letter.getComments()).containsIgnoringCase(InfoAgeInboundService.UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
	}
	
	/**
	 * The flat to indicate that the {@link Claimant} is sent for address research must be reset once the {@link Claimant}s research info is received from address research.
	 */
	@Test
	@Transactional
	public void setAddressResearchSentFlagToFalseAfterReceivingAddressResearchUpdate() {
		datagenerator.newClaimant(true, true);
		Claimant claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();
		// Set the flag to true. 
		claimant.setAddressResearchSent(true);
		claimant.merge().flush();
		corpAddressResearchInboundService.processResearchedAddresses(asList(datagenerator.newCorporateAddressResearch(HitIndicatorCorpType.N, null)));
		claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();		 		
		assertThat(claimant.getAddressResearchSent()).isFalse();
		
	}
	
	/**
	 * For GitHub Issue #715 [ISSUE-1,issue_1,Log is not
	 * getting created when the address is not updated i.e Hit indicator=N, for CorpAddressResearchJob]
	 */
	@Test
	@Transactional
	public void testUpdateCorpAddressChange(){
		datagenerator.newClaimant(true, true);
		Claimant claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();	
		corpInfoAgeInboundServiceImpl.updateAddressChange(claimant, getAddressResearchStatus());
		assertThat(claimant.getMailingAddress().getAddress().getResearchChangeActivity().getAddressResearchDescription()).isNotNull();
		assertThat(claimant.getMailingAddress().getAddress().getResearchChangeActivity().getAddressResearchDescription()).contains("Address Research Return...");
	}
	
	/**
	 * For GitHub Issue #715 [ISSUE-1,issue_1,Log is not
	 * getting created when the address is not updated i.e Hit indicator=N, for IndividualAddressResearchJob]
	 */
	
	@Test
	@Transactional
	public void testIndividualUpdateAddressChange(){
		datagenerator.newClaimant(true, true);
		Claimant claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();	
		individualInfoAgeInboundServiceImpl.updateAddressChange(claimant, getIndividualAddressResearchStatus());
		assertThat(claimant.getMailingAddress().getAddress().getResearchChangeActivity().getAddressResearchDescription()).isNotNull();
		assertThat(claimant.getMailingAddress().getAddress().getResearchChangeActivity().getAddressResearchDescription()).contains("Address Research Return...");
	}

	private List<AddressResearchUpdate> asList(IndividualAddressResearch researchReturn) {
		List<AddressResearchUpdate> list = Lists.newArrayList();
		list.add(researchReturn);
		return list;
	}
	
	private List<AddressResearchUpdate> asList(CorporateAddressResearch researchReturn) {
		List<AddressResearchUpdate> list = Lists.newArrayList();
		list.add(researchReturn);
		return list;
	}
	
	private AddressResearchStatus getAddressResearchStatus() {

		CorporateAddressResearch account = new CorporateAddressResearch();
		account.setHitIndicator(HitIndicatorCorpType.N);
		account.setAddressDateReported("20120529");
		account.setAddress("test 2000 crown colony dr Quincy");
		AddressResearchStatus researchStatus = simpleRulesManager
				.fireAllRules(account);
		researchStatus.setAddressResearchUpdate(account);
		return researchStatus;

	}

	private AddressResearchStatus getIndividualAddressResearchStatus() {

		IndividualAddressResearch account = new IndividualAddressResearch();
		account.setHitIndicator(HitIndicatorType.N);
		account.setAddressDateReported("20120529");
		account.setMatchIndicator(MatchIndicatorType.M);
		account.setPrefix("Mr");
		account.setFirstName("AA");
		account.setMiddleName("");
		account.setLastName("");
		account.setSuffix("");
		account.setMaternalName("");
		account.setSsn("");
		account.setDateOfBirth(Calendar.getInstance());
		account.setPhone("8099195347");
		account.setInvalidOrDeceasedSSNTag("M");
		account.setNameChangeTag("A");
		account.setSsnMatchTag("");
		account.setOfacIndicator("");
		AddressResearchStatus researchStatus = simpleRulesManager
				.fireAllRules(account);
		researchStatus.setAddressResearchUpdate(account);
		return researchStatus;

	}

}
