package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ContactTest {
	
	@Test
	public void verifyEquals() {
		Contact c1 = newContact("name" , "1111", "2222", "3333", "a@a.com", "comments 1");				
		Contact c2 = newContact("name" , "1111", "2222", "3333", "a@a.com", "comments 1");	
		assertThat(c1).isEqualTo(c1);
		assertThat(c1).isNotEqualTo(null);
		assertThat(c1).isNotEqualTo("");
		assertThat(c1).isNotEqualTo(new ClaimantRegistration());
		assertThat(new ZipCode()).isNotEqualTo(c1);
		assertThat(new ZipCode()).isEqualTo(new ZipCode());
		
		assertThat(c1).isEqualTo(c2);
	}

	private Contact newContact(String name, String phoneNo, String workPhoneNo,
			String cellPhoneNo, String email, String comments) {
		Contact ret = new Contact();
		ret.setComments(comments);
		ret.setEmail(email);
		ret.setPhoneNo(cellPhoneNo);
		ret.setCellPhoneNo(cellPhoneNo);
		ret.setWorkPhoneNo(workPhoneNo);
		ret.setName(new Name(name, null, null));
		return ret;
	}

	@Test
	public void verifyHashcode() {
		Contact c1 = newContact("name" , "1111", "2222", "3333", "a@a.com", "comments 1");				
		Contact c2 = newContact("name" , "1111", "2222", "3333", "a@a.com", "comments 1");				
		
		assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
		assertThat(c1.hashCode()).isNotEqualTo(new Contact().hashCode());
		assertThat((new Contact()).hashCode()).isEqualTo((new Contact()).hashCode());		
	}
	
	@Test
	public void verifyIsEmpty() {
		Contact c1 = new Contact();									
		
		assertThat(c1.isEmpty()).isTrue();
		
		c1 = new Contact();
		c1.setCellPhoneNo("13121231231");
		assertThat(c1.isEmpty()).isFalse();
		
	}
	
	@Test
	public void verifyClone() {
		Contact c1 = newContact("name" , "1111", "2222", "3333", "a@a.com", "comments 1");								
		Contact c2 = c1.clone();
		
		assertThat(c1).isEqualTo(c2);
		assertThat(c2).isEqualTo(c1);
		assertThat(c1 == c2).isFalse();		
	}
	
	@Test
	public void verifyToString() {
		Contact c1 = newContact("name" , "1111", "2222", "3333", "a@a.com", "comments 1");										
				
		assertThat(c1.toString()).isEqualTo("Name: Name[firstName=name,lastName=<null>,middleName=<null>], Email: a@a.com, PhoneNo: 3333, CellPhoneNo: 3333, WorkPhoneNo: 2222");
	
	}
}
