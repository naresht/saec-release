package com.bfds.saec.domain.activity;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.ZipCode;

public class AlternatePayeeActivityTest {

	
	@Test
	public void verifyActivityDescription() {
		AlternatePayeeActivity activity = new AlternatePayeeActivity();
		activity.setReferenceNo("1000001");
		Address address = new Address();
		address.setAddress1("a1");
		address.setAddress2("a2");
		address.setAddress3("a3");
		address.setAddress4("a4");
		address.setAddress5("a5");
		address.setAddress6("a6");
		address.setCity("city");
		address.setStateCode("state");
		address.setCountryCode("US");
		address.setZipCode(new ZipCode("1111", "22"));
		activity.setAddress(address);
		
		RegistrationLines registration = new RegistrationLines();
		registration.setRegistration1("r1");
		registration.setRegistration2("r2");
		registration.setRegistration3("r3");
		registration.setRegistration4("r4");
		registration.setRegistration5("r5");
		registration.setRegistration6("r6");
		activity.setRegistration(registration);
		assertThat(activity.getDescription()).isEqualTo("<b>Reference#</b><a href='/saec/app/claimant/edit?id=null'>1000001</a>"+
														"<br/><b>Name:</b>"+registration.getRegistrationLinesAsString()+
														"<br/><b>Address:</b>"+address.getAddressLinesAsString());
		
		assertThat(activity.getShortDescription()).hasSize(Activity.DEFAULT_SHORT_DESCRIPTION_LENGTH);			

	}
	
	@Test
	public void verifyActivityTypeDescription() { 
		Activity activity = new AlternatePayeeActivity();
		assertThat(activity.getActivityTypeDescription()).isEqualTo("Alternate Payee Created");
	}
}
