package com.bfds.saec.batch.util;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;

 
public class RegistrationAndAddressLinesUtilTest {

	@Test
	public void testnonUSCountryAddresswithMoreRegistrationLines() {
		List<String> regandAddressList = RegistrationAndAddressLinesUtil.buildRegistrationLines(getMailingAddress(), getRegistrationAddress());
		assertThat(regandAddressList).hasSize(7);
		assertThat(regandAddressList).containsExactly("RegistrationName1","RegistrationName2","RegistrationName3","RegistrationName4","address 1","NEWYORK NY 23456","USLLA");
	}

	@Test
	public void testnonUSCountryAddresswithLessRegistrationLines() {
		ClaimantRegistration claimantRegistration = getRegistrationAddress();
		claimantRegistration.setRegistration2(null);
		claimantRegistration.setRegistration3(null);
		claimantRegistration.setRegistration4(null);
		claimantRegistration.setRegistration5(null);
		Address address=getMailingAddress();
		address.setAddress2(null);
		address.setAddress3(null);
		address.setAddress4(null);
		address.setZipCode(new ZipCode(null,null));
		List<String> regandAddressList=RegistrationAndAddressLinesUtil.buildRegistrationLines(address, claimantRegistration);
		assertThat(regandAddressList).hasSize(5);
		assertThat(regandAddressList).containsExactly("RegistrationName1","RegistrationName6", "address 1","NEWYORK NY","USLLA");
	}


	@Test
	public void testUSCountryAddresswithMoreRegistrationLines() {
		Address address=getMailingAddress();
		address.setCountryCode("USA");
		List<String> regandAddressList=RegistrationAndAddressLinesUtil.buildRegistrationLines(address, getRegistrationAddress());
		assertThat(regandAddressList).hasSize(7);
		assertThat(regandAddressList).containsExactly("RegistrationName1","RegistrationName2","RegistrationName3","RegistrationName4","address 1","address 2","NEWYORK NY 23456");
	}


	@Test
	public void testUSCountryAddresswithLessRegistrationLines() {
		ClaimantRegistration claimantRegistration = getRegistrationAddress();
		claimantRegistration.setRegistration2(null);
		claimantRegistration.setRegistration3(null);
		claimantRegistration.setRegistration4(null);
		claimantRegistration.setRegistration5(null);
		Address address=getMailingAddress();
		address.setCountryCode("USA");
		address.setZipCode(new ZipCode("23456","7789"));
		address.setAddress2(null);
		address.setAddress3(null);
		address.setAddress4(null);
		List<String> regandAddressList=RegistrationAndAddressLinesUtil.buildRegistrationLines(address, claimantRegistration);
		assertThat(regandAddressList).hasSize(4);
		assertThat(regandAddressList).containsExactly("RegistrationName1","RegistrationName6","address 1","NEWYORK NY 23456-7789");
	}

	private Address getMailingAddress()
	{
		Address address=new Address();
		address.setAddress1("address 1");
		address.setAddress2("address 2");
	 
		address.setCity("NEWYORK");
		address.setStateCode("NY");
		address.setCountryCode("USLLA");
		address.setZipCode(new ZipCode("23456",null));
		return address;

	}

	private ClaimantRegistration getRegistrationAddress()
	{
		ClaimantRegistration claimantRegistration=new ClaimantRegistration();
		claimantRegistration.setRegistration1("RegistrationName1");
		claimantRegistration.setRegistration2("RegistrationName2");
		claimantRegistration.setRegistration3("RegistrationName3");
		claimantRegistration.setRegistration4("RegistrationName4");
		claimantRegistration.setRegistration5("RegistrationName5");
		claimantRegistration.setRegistration6("RegistrationName6");
		return claimantRegistration;
	}

}
