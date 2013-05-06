package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ZipCodeTest {

	@Test
	public void verifyEquals() {
		ZipCode zip1 = new ZipCode("1111", "22");
		ZipCode zip2 = new ZipCode();
		zip2.setZip("1111");
		zip2.setExt("22");
		assertThat(zip1).isEqualTo(zip1);
		assertThat(zip1).isEqualTo(zip1);
		assertThat(zip1).isNotEqualTo(null);
		assertThat(zip1).isNotEqualTo("");
		assertThat(zip1).isEqualTo(zip1);
		assertThat(zip1).isNotEqualTo(new RegistrationLines());
		assertThat(new ZipCode()).isNotEqualTo(zip1);
		assertThat(new ZipCode()).isEqualTo(new ZipCode());

		assertThat(zip1).isEqualTo(zip2);

		zip2.setExt(null);
		assertThat(zip1).isNotEqualTo(zip2);
	}

	@Test
	public void verifyHashcode() {
		ZipCode zip1 = new ZipCode("1111", "22");
		ZipCode zip2 = new ZipCode("1111", "22");

		assertThat(zip1.hashCode()).isEqualTo(zip2.hashCode());
		assertThat(zip1.hashCode()).isNotEqualTo(new ZipCode().hashCode());
		assertThat((new ZipCode()).hashCode()).isEqualTo(
				(new ZipCode()).hashCode());
	}

	@Test
	public void verifyIsEmpty() {
		ZipCode zip1 = new ZipCode();

		assertThat(zip1.isEmpty()).isTrue();

		zip1 = new ZipCode("1111", null);
		assertThat(zip1.isEmpty()).isFalse();

		zip1 = new ZipCode(null, "22");
		assertThat(zip1.isEmpty()).isFalse();
	}

	@Test
	public void verifyClone() {
		ZipCode zip1 = new ZipCode();
		ZipCode zip2 = zip1.clone();

		assertThat(zip1).isEqualTo(zip2);
		assertThat(zip2).isEqualTo(zip1);
		assertThat(zip1 == zip2).isFalse();
	}

	@Test
	public void verifyToString() {
		ZipCode zip1 = new ZipCode("1111", "22");

		assertThat(zip1.toString()).isEqualTo("1111-22");
		zip1 = new ZipCode("1111", null);

		assertThat(zip1.toString()).isEqualTo("1111");
	}

	@Test
	public void verifyProperties() {
		ZipCode zip1 = new ZipCode("1111", "22");

		assertThat(zip1.getZip()).isEqualTo("1111");
		assertThat(zip1.getExt()).isEqualTo("22");
	}
	
	@Test
	public void verifyConvertedZipCode() {
		ZipCode zip1 = ZipCode.parse("02142-0001");
		assertThat(zip1.getZip()).isEqualTo("02142");
		assertThat(zip1.getExt()).isEqualTo("0001");
		
		ZipCode zip2 = ZipCode.parse("02142");
		assertThat(zip2.getZip()).isEqualTo("02142");
		assertThat(zip2.getExt()).isNull();
	}
	
	@Test
	public void validateZipCode() {
		boolean isvalid = ZipCode.isAValidZipCode("021420001");
		assertThat(isvalid).isFalse();
		
		isvalid = ZipCode.isAValidZipCode("0214#-0001");
		assertThat(isvalid).isFalse();
		
		isvalid = ZipCode.isAValidZipCode("0214");
		assertThat(isvalid).isFalse();
	}
}
