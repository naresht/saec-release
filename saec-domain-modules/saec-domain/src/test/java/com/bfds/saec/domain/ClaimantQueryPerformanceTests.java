package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.sqlrecorder.events.listener.QueryOutputCollectorListener;

import com.bfds.saec.domain.reference.AddressType;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class ClaimantQueryPerformanceTests {
	
	@Autowired
	private QueryOutputCollectorListener queryOutputCollectorListener;

	@Before
	public void setup() {
		queryOutputCollectorListener.executedQueries().clear();
	}
	
	/**
	 * A claimant should be loaded in a single query. The other relationships should be loaded lazily.
	 */
	@Test
	@Ignore("Not working on cloud bees jenkins")
	public void loadClaimantInOneQuery() {
		assertThat(queryOutputCollectorListener.executedQueries()).isEmpty();
	
		createClaimant();		
		assertThat(queryOutputCollectorListener.executedQueries()).isNotEmpty();
		queryOutputCollectorListener.executedQueries().clear();
		assertThat(queryOutputCollectorListener.executedQueries()).isEmpty();
		
		Claimant claimant = Claimant.findAllClaimants().get(0);
		queryOutputCollectorListener.executedQueries().clear();
		Claimant.entityManager().clear();
		claimant = Claimant.findClaimant(claimant.getId());
		
		assertThat(queryOutputCollectorListener.executedQueries()).hasSize(1);
		
	}
	
	/**
	 * A claimant should be loaded in a single query. The other relationships should be loaded lazily.
	 */
	@Test
	@Ignore("Not working on cloud bees jenkins")
	public void loadClaimantWithAllAssociations() {
		assertThat(queryOutputCollectorListener.executedQueries()).isEmpty();
	
		createClaimant();		
		assertThat(queryOutputCollectorListener.executedQueries()).isNotEmpty();
		queryOutputCollectorListener.executedQueries().clear();
		assertThat(queryOutputCollectorListener.executedQueries()).isEmpty();
		
		Claimant claimant = Claimant.findAllClaimants().get(0);
		queryOutputCollectorListener.executedQueries().clear();
		Claimant.entityManager().clear();
		claimant = Claimant.findClaimant(claimant.getId(), Claimant.ASSOCIATION_ALL);
		assertThat(claimant.getAddresses()).isNotEmpty();
		assertThat(claimant.getPayments()).isNotEmpty();
		assertThat(queryOutputCollectorListener.executedQueries()).hasSize(3);
		
	}

	private void createClaimant() {
		Claimant claimant = DataGenerator.newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				DataGenerator.newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
						DataGenerator.newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), DataGenerator.newCheck("1000010", 100),
						DataGenerator.newCheck("1000020", 200));
		
		Contact contact = new Contact();
		contact.setPhoneNo("111111111");
		claimant.addContact(contact);
		contact = new Contact();
		contact.setPhoneNo("2222222222");
		claimant.addContact(contact);
		
		
		
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
	}
}
