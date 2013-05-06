package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.bfds.saec.domain.reference.AddressType;

public class AddressWrappersTest {

	@Test
	public void testAccessors() {
		IAddress a1 = new ClaimantAddress();
		testAccessors(a1);

		a1 = new MailObjectAddress();
		testAccessors(a1);
	}

	public void testAccessors(IAddress a1) {
		a1.setAddress1("1");
		a1.setAddress2("2");
		a1.setAddress3("3");
		a1.setAddress4("4");
		a1.setAddress5("5");
		a1.setAddress6("6");
		a1.setCity("c");
		a1.setStateCode("s");
		a1.setZipCode(new ZipCode());
		a1.getZipCode().setZip("1111");
		a1.getZipCode().setExt("22");
		a1.setCountryCode("us");
		a1.setMailingAddress(true);
		a1.setAddressType(AddressType.ADDRESS_OF_RECORD);

		assertThat(a1.getAddress1()).isEqualTo("1");
		assertThat(a1.getAddress2()).isEqualTo("2");
		assertThat(a1.getAddress3()).isEqualTo("3");
		assertThat(a1.getAddress4()).isEqualTo("4");
		assertThat(a1.getAddress5()).isEqualTo("5");
		assertThat(a1.getAddress6()).isEqualTo("6");
		assertThat(a1.getCity()).isEqualTo("c");
		assertThat(a1.getStateCode()).isEqualTo("s");
		assertThat(a1.getZipCode()).isEqualTo(new ZipCode("1111", "22"));
		assertThat(a1.getCountryCode()).isEqualTo("us");
		assertThat(a1.isMailingAddress()).isTrue();
		assertThat(a1.getAddressType())
				.isEqualTo(AddressType.ADDRESS_OF_RECORD);

	}

	@Test
	public void testEquals() {
		IAddress a1 = new ClaimantAddress();
		IAddress a2 = new ClaimantAddress();
		testEquals(a1, a2);

		a1 = new MailObjectAddress();
		a2 = new MailObjectAddress();
		testEquals(a1, a2);
	}

	public void testEquals(IAddress a1, IAddress a2) {
		a1.setAddress1("1");
		a1.setAddress2("2");
		a1.setAddress3("3");
		a1.setAddress4("4");
		a1.setAddress5("5");
		a1.setAddress6("6");
		a1.setCity("c");
		a1.setStateCode("s");
		a1.setZipCode(new ZipCode());
		a1.getZipCode().setZip("1111");
		a1.getZipCode().setExt("22");
		a1.setCountryCode("c");
		a1.setMailingAddress(true);
		a1.setAddressType(AddressType.ADDRESS_OF_RECORD);

		a2.setAddress1("1");
		a2.setAddress2("2");
		a2.setAddress3("3");
		a2.setAddress4("4");
		a2.setAddress5("5");
		a2.setAddress6("6");
		a2.setCity("c");
		a2.setStateCode("s");
		a2.setZipCode(new ZipCode());
		a2.getZipCode().setZip("1111");
		a2.getZipCode().setExt("22");
		a2.setCountryCode("c");
		a2.setMailingAddress(false);
		a2.setAddressType(AddressType.SEASONAL_ADDRESS);

		assertThat(a1).isEqualTo(a2);
	}

	@Test
	public void getNonEmptyLines() {
		IAddress a1 = new ClaimantAddress();
		getNonEmptyLines(a1);

		a1 = new MailObjectAddress();
		getNonEmptyLines(a1);
	}

	public void getNonEmptyLines(IAddress a1) {

		a1.setAddress1("1");
		// a1.setAddress2("2");
		a1.setAddress3("3");
		// a1.setAddress4("4");
		a1.setAddress5("5");
		// a1.setAddress6("6");

		assertThat(a1.getNonEmptyLine(1)).isEqualTo("1");
		assertThat(a1.getNonEmptyLine(2)).isEqualTo("3");
		assertThat(a1.getNonEmptyLine(3)).isEqualTo("5");
		assertThat(a1.getNonEmptyLine(4)).isNull();
	}

	@Test
	public void getAddressLinesAsString() {
		IAddress a1 = new ClaimantAddress();
		getAddressLinesAsString(a1);

		a1 = new MailObjectAddress();
		getAddressLinesAsString(a1);
	}

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

	public void getAddressLinesAsString(IAddress a1) {
		a1.setAddress1("1");
		// a1.setAddress2("2");
		a1.setAddress3("3");
		// a1.setAddress4("4");
		a1.setAddress5("5");
		// a1.setAddress6("6");

		a1.setCity("c");
		a1.setStateCode("s");
		a1.setZipCode(new ZipCode());
		a1.getZipCode().setZip("1111");
		a1.getZipCode().setExt("22");
		a1.setCountryCode("us");

		assertThat(a1.getAddressLinesAsString("")).isEqualTo("135");
		assertThat(a1.getAddressLinesAsString()).isEqualTo("1, 3, 5");
		assertThat(a1.getAddressAsString("")).isEqualTo("135c s 1111-22us");
		assertThat(a1.getAddressAsString()).isEqualTo(
				"1, 3, 5, c s 1111-22, us");
	}

}
