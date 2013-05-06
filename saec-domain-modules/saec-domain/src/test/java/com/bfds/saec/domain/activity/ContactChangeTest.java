package com.bfds.saec.domain.activity;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.bfds.saec.domain.Name;

public class ContactChangeTest {

	/**
	 * test for Primary Contact Change Activity Description
	 */
	@Test
	public void testPrimaryContactChangeActivityDescription() {
		ContactChange activity = new ContactChange();
		com.bfds.saec.domain.Contact from = new com.bfds.saec.domain.Contact();
		from.setName(new Name("first name", null, null));
		from.setPhoneNo("111111");
		
		activity.setFrom(from);
		
		com.bfds.saec.domain.Contact to = new com.bfds.saec.domain.Contact();
		to.setName(new Name("first name changed", null, null));
		to.setPhoneNo("111111 changed");
		activity.setTo(to);
		
		activity.setIsPrimaryContact(Boolean.TRUE);
		
		assertThat(activity.getDescription()).isEqualTo("first name, 111111 To first name changed, 111111 changed");		
	}
	
	/**
	 * test for Additional Contact Creation Activity Description
	 */
	@Test
	public void testAdditionalContactActivityDescription() {
		ContactChange activity = new ContactChange();
		com.bfds.saec.domain.Contact addContact = new com.bfds.saec.domain.Contact();
		addContact.setName(new Name("Additional name", null, null));
		addContact.setPhoneNo("22222222");
		activity.setTo(addContact);		
		activity.setIsPrimaryContact(Boolean.FALSE);
		
		assertThat(activity.getDescription()).isEqualTo("Additional name, 22222222");
	}
	
	/**
	 * test for Activity Types Descriptions
	 */
	@Test
	public void testActivityTypeDescription() { 
		ContactChange activity = new ContactChange();
		activity.setIsPrimaryContact(Boolean.TRUE);
		assertThat(activity.getActivityTypeDescription()).isEqualTo("Primary Contact Info Change");
		activity.setIsPrimaryContact(Boolean.FALSE);
		assertThat(activity.getActivityTypeDescription()).isEqualTo("Additional Contact Created");
		
	}
}
