package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.AddressType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class ClaimantAssociationsTest{ 

	@Test
	public void testPhoneCalls() {
		Claimant claimant = newClaimant();
		claimant.getPhoneCall().add(new PhoneCall());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		// Should be empty as we should not directly add a PhoneCall to the collection.
		assertThat(claimant.getPhoneCall()).isEmpty();
		//Right way of adding a PhoneCall.
		claimant.startCallLog("csr");
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		assertThat(claimant.getPhoneCall()).hasSize(1);	
		claimant.remove();
	}
	
	@Test
	@Ignore
	public void testActivity() {
		Claimant claimant = newClaimant();
		claimant.getActivity().add(new Activity());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		// Should be empty as we should not directly add a Activity to the collection.
		assertThat(claimant.getActivity()).isEmpty();
		//Right way of adding a Activity.
		claimant.addActivity(new Activity());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		
		assertThat(claimant.getActivity()).hasSize(1);
		claimant.remove();
	}

	@Test
	public void testContacts() {
		Claimant claimant = newClaimant();
		claimant.getContacts().add(new Contact());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		// Should be empty as we should not directly add a Contact to the collection.
		assertThat(claimant.getContacts()).isEmpty();
		//Right way of adding a Contact.
		claimant.addContact(new Contact());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		
		assertThat(claimant.getContacts()).hasSize(1);	
		claimant.remove();
	}
	
	@Test
	public void testPrimaryContact() {
		Claimant claimant = newClaimant();
		claimant.setPrimaryContact(new Contact());
		assertThat(claimant.getContacts()).hasSize(0);	
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		assertThat(claimant.getPrimaryContact()).isNotNull();
		assertThat(claimant.getPrimaryContact().getPrimaryContactOf()).isNotNull();
		assertThat(claimant.getContacts()).hasSize(0);	
		claimant.remove();
	}
	
	@Test
	@Ignore
	public void testAddresses() {
		Claimant claimant = newClaimant();
		claimant.getAddresses().add(DataGenerator.newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
				"Muskegon", "MI", "49470"));
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		// Should be empty as we should not directly add a Address to the collection.
		assertThat(claimant.getAddresses()).isEmpty();
		//Right way of adding a Contact.
		claimant.addAddress(DataGenerator.newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
				"Muskegon", "MI", "49470"));
		claimant.setPrimaryContact(new Contact());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		assertThat(claimant.getAddresses()).hasSize(1);	
		claimant.remove();
	}
	
	@Test
	@Ignore
	public void testChecks() {
		Claimant claimant = newClaimant();
		claimant.addAddress(DataGenerator.newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
				"Muskegon", "MI", "49470"));
		Payment check = DataGenerator.newCheck("1000020", 200);
		MailObjectAddress address = new MailObjectAddress();
		claimant.getAddressOfRecord().copyTo(address);
		check.setCheckAddress(address);
		claimant.getPayments().add(check);
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		// Should be empty as we should not directly add a Check to the collection.
		assertThat(claimant.getPayments()).isEmpty();
		//Right way of adding a Check.
		claimant.addCheck(DataGenerator.newCheck("1000021", 200));
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		assertThat(claimant.getPayments()).hasSize(1);	
		claimant.remove();
	}
	
	@Test
	@Ignore
	public void testLetters() {
		Claimant claimant = newClaimant();
		claimant.getLetters().add(new Letter());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		// Should be empty as we should not directly add a Letter to the collection.
		assertThat(claimant.getLetters()).isEmpty();
		//Right way of adding a Letter.
		claimant.addLetter(new Letter());
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		assertThat(claimant.getLetters()).hasSize(1);	
		claimant.remove();
	}
	
	@Test
	@Ignore
	public void testFetchAssociations() {
		Claimant claimant = newClaimant();
		claimant.startCallLog("csr");
		claimant.startCallLog("csr");
		claimant.addActivity(new Activity());
		claimant.addActivity(new Activity());
		Contact contact = new Contact();
		contact.setPhoneNo("11111111111");
		claimant.addContact(contact);
		contact = new Contact();
		contact.setPhoneNo("2222222");
		claimant.addContact(contact);
		claimant.setPrimaryContact(new Contact());
		claimant.addAddress(DataGenerator.newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
				"Muskegon", "MI", "49470"));
		claimant.addAddress(DataGenerator.newAddress(AddressType.SEASONAL_ADDRESS, "2398 Bee Street",
				"Muskegon", "MI", "49470"));
		claimant.addCheck(DataGenerator.newCheck("1000020", 200));
		claimant.addCheck(DataGenerator.newCheck("1000021", 200));
		claimant.addLetter(new Letter());
		claimant.addLetter(new Letter());	
		
		claimant.persist();
		claimant.flush();
		Claimant.entityManager().clear();
		claimant = Claimant.findAllClaimants().get(0);	
		claimant = Claimant.findClaimant(claimant.getId(), Claimant.ASSOCIATION_ACTIVITY, 
				                                           Claimant.ASSOCIATION_ADDRESSES,
				                                           Claimant.ASSOCIATION_CALLS,
				                                           Claimant.ASSOCIATION_CONTACTS, 
				                                           Claimant.ASSOCIATION_LETTERS, 
				                                           Claimant.ASSOCIATION_PAYMENTS);
		Claimant.entityManager().clear();
		assertThat(claimant.getActivity().size()).isGreaterThan(2);
		assertThat(claimant.getContacts()).hasSize(2);	
		assertThat(claimant.getAddresses()).hasSize(2);		
		assertThat(claimant.getPayments()).hasSize(2);			
		assertThat(claimant.getLetters()).hasSize(2);
		claimant.remove();
	}

	private Claimant newClaimant() {
		Claimant claimant = DataGenerator.newClaimant("Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				null,
				null, null, null);
		return claimant;
	}
}
