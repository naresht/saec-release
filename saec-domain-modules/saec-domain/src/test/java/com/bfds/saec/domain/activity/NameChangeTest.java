package com.bfds.saec.domain.activity;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.bfds.saec.domain.RegistrationLines;

public class NameChangeTest {

	
	@Test
	public void verifyActivityDescription() {
		NameChange activity = new NameChange();
		
		RegistrationLines from = new RegistrationLines();
		from.setRegistration1("r1");
		from.setRegistration2("r2");
		from.setRegistration3("r3");
		from.setRegistration4("r4");
		from.setRegistration5("r5");
		from.setRegistration6("r6");
		activity.setFrom(from);
		
		RegistrationLines to = new RegistrationLines();
		to.setRegistration1("r11");
		to.setRegistration2("r21");
		to.setRegistration3("r31");
		to.setRegistration4("r41");
		to.setRegistration5("r51");
		to.setRegistration6("r61");
		activity.setTo(to);
		
		assertThat(activity.getDescription()).isEqualTo(" From : r1, r2, r3, r4, r5, r6 To : r11, r21, r31, r41, r51, r61");
	}
	
	@Test
	public void verifyActivityTypeDescription() { 
		Activity activity = new NameChange();
		assertThat(activity.getActivityTypeDescription()).isEqualTo("Name Change");
	}
}
