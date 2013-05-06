package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class NameTest {
	
	@Test
	public void verifyEquals() {
		Name n1 = newName("firstName" , "middleName", "lastName");				
		Name n2 = newName("firstName" , "middleName", "lastName");	
		assertThat(n1).isEqualTo(n1);
		assertThat(n1).isNotEqualTo(null);
		assertThat(n1).isNotEqualTo("");
		assertThat(n1).isEqualTo(n2);
	}

	private Name newName(String firstName, String middleName, String lastName) {
		Name ret = new Name();
		ret.setFirstName(firstName);
		ret.setLastName(lastName);
		ret.setMiddleName(middleName);
		return ret;
	}

	@Test
	public void verifyHashcode() {
		Name n1 = new Name("firstName" , "middleName", "lastName");	
		Name n2 = new Name("firstName" , "middleName", "lastName");
		
		assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
		assertThat(n1.hashCode()).isNotEqualTo(new Name().hashCode());
		assertThat((new Name()).hashCode()).isEqualTo((new Name()).hashCode());		
	}
	
	@Test
	public void verifyIsEmpty() {
		Name n1 = new Name();									
		
		assertThat(n1.isEmpty()).isTrue();
		
		n1 = new Name("firstName", null,null);
		assertThat(n1.isEmpty()).isFalse();
		
		n1 = new Name(null, "middleName", null);
		assertThat(n1.isEmpty()).isFalse();	
		
		n1 = new Name(null, null, "lastName" );
		assertThat(n1.isEmpty()).isFalse();	
	}
	
	@Test
	public void verifyClone() {
		Name name1 = new Name();									
		Name name2 = name1.copy();
		
		assertThat(name1).isEqualTo(name2);
		assertThat(name2).isEqualTo(name1);
		assertThat(name1 == name2).isFalse();		
	}
	
	@Test
	public void verifyToString() {
		Name c1 = newName("firstName" , "middleName", "lastName");										
				
		assertThat(c1.toString()).isEqualTo("Name[firstName=firstName,lastName=lastName,middleName=middleName]");
	
	}
	
	@Test
	public void verifyProperties() {
		Name name1 = new Name("firstName" , "middleName", "lastName");				
		
		assertThat(name1.getFirstName()).isEqualTo("firstName");
		assertThat(name1.getMiddleName()).isEqualTo("middleName");
		assertThat(name1.getLastName()).isEqualTo("lastName");
	}
}
