package com.bfds.saec.dao;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;

//@RunWith(SpringJUnit4ClassRunner.class)
public class AddressTest {

	@Test
	public void checkEqualsAndHashCode() {
		Address a1 = getNewAddress();

		Address a = a1;

		a1 = getNewAddress();
		a1.setAddressType(AddressType.PAYMENT_ADDRESS); // NOT PART OF EQUALS
		a1.setMailingAddress(false); // NOT PART OF EQUALS
		a1.setValidFrom(null); // NOT PART OF EQUALS
		a1.setValidTill(null); // NOT PART OF EQUALS
		assertThat(a).isEqualTo(a1);
		assertThat(a.hashCode()).isEqualTo(a1.hashCode());
	}

	@Test
	public void checkNotEquals() {
		Address a1 = getNewAddress();
		Address a = getNewAddress();
		a1 = new Address();
		a1.setZipCode(new ZipCode("11111", "2222"));
		assertThat(a).isNotSameAs(a1);
	}

	@Test
	public void checkEqualsAndHashCodeForClaimantAddress() {
		ClaimantAddress a1 = new ClaimantAddress(getNewAddress());
		ClaimantAddress a = new ClaimantAddress(getNewAddress());
		assertThat(a).isNotSameAs(a1);
		assertThat(a).isEqualTo(a1);
		assertThat(a.hashCode()).isEqualTo(a1.hashCode());
	}

	@Test
	public void checkEqualsAndHashCodeForCheckAddress() {
		MailObjectAddress a1 = new MailObjectAddress(getNewAddress());
		MailObjectAddress a = new MailObjectAddress(getNewAddress());
		assertThat(a).isNotSameAs(a1);
		assertThat(a).isEqualTo(a1);
		assertThat(a.hashCode()).isEqualTo(a1.hashCode());
	}

	@Test
	public void getAddressAsString() {
		final String addrAsString = "1,2,3,4,5,6,c s 11111-1111,US";
		Address a = getNewAddress();
		assertThat(addrAsString).isEqualTo(a.getAddressAsString(","));
	}

	@Test
	public void copyTo() {
		Address address1 = new Address();

		address1.setAddressType(AddressType.PAYMENT_ADDRESS);
		address1.setAddress1("add1");
		address1.setAddress2("add2");
		address1.setAddress3("add3");
		address1.setAddress4("add4");
		address1.setAddress5("add5");
		address1.setAddress6("add6");
		address1.setCity("city");
		address1.setCountryCode("c1");
		address1.setStateCode("s1");
		address1.setPhone("999999999");
		address1.setPhoneExt("91");
		Address address2 = new Address();
		address1.copyTo(address2);
		assertThat(address2).isEqualTo(address1);
	}

	@Test
	public void isEmpty() {
		Address address1 = new Address();
		assertThat(address1.isEmpty()).isTrue();
		address1.setCity("c1");
		address1.setStateCode("sc1");
		assertThat(address1.isEmpty()).isFalse();

	}

	private Address getNewAddress() {
		Address a1 = new Address();
		a1.setAddress1("1");
		a1.setAddress2("2");
		a1.setAddress3("3");
		a1.setAddress4("4");
		a1.setAddress5("5");
		a1.setAddress6("6");
		a1.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a1.setMailingAddress(true);
		a1.setStateCode("s");
		a1.setCity("c");
		a1.setCountryCode("US");
		a1.setZipCode(new ZipCode("11111", "1111"));
		a1.setValidFrom(new Date());
		a1.setValidTill(new Date());
		return a1;
	}
}
