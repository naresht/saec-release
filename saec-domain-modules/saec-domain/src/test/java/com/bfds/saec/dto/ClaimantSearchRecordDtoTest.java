package com.bfds.saec.dto;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.dto.ClaimantSearchRecordDto;

import java.math.BigDecimal;

public class ClaimantSearchRecordDtoTest {
	
	@Test
	public void createClaimantSearchRecordDtoAndVerifyAttributes() {
		ClaimantSearchRecordDto dto = (new ClaimantSearchRecordDto.Builder()).build(
				new Object[] {19L, "reference_01", getRegistration(), getAddress(),null, "123", new BigDecimal( 100.0)}
		);
		
		assertThat(dto.getId()).isEqualTo(19L);
		assertThat(dto.getReferenceNo()).isEqualTo("reference_01");
		assertThat(dto.getAddress1()).isEqualTo("a1");
		assertThat(dto.getAddress2()).isEqualTo("a2");
		assertThat(dto.getAddressLines()).isEqualTo(getAddress().getAddressLinesAsString());
		assertThat(dto.getCity()).isEqualTo("city");
		assertThat(dto.getPaymentAmount()).isEqualTo(100.0);
		assertThat(dto.getState()).isEqualTo("state");
		assertThat(dto.getName()).isEqualTo(getRegistration().getRegistrationLinesAsString());
		assertThat(dto.getZip()).isEqualTo("1111-22");
		assertThat(dto.getCheckNo()).isEqualTo("123");
		
		dto = (new ClaimantSearchRecordDto.Builder()).build(
				new Object[] {19L, "reference_01", null, getAddress(), null,null, null}		
		);
		
		assertThat(dto.getName()).isNull();
		
		dto = (new ClaimantSearchRecordDto.Builder()).build(
				new Object[] {19L, "reference_01", null, null,null, null, null}		
		);
		
		assertThat(dto.getAddress1()).isNull();
		assertThat(dto.getAddress2()).isNull();
		assertThat(dto.getAddressLines()).isNull();
		assertThat(dto.getCity()).isNull();
		assertThat(dto.getState()).isNull();
		assertThat(dto.getZip()).isNull();
	}

	private Address getAddress() {
		Address ret = new Address();
		ret.setAddress1("a1");
		ret.setAddress2("a2");
		ret.setAddress3("a3");
		ret.setAddress4("a4");
		ret.setAddress5("a5");
		ret.setAddress6("a6");
		ret.setCity("city");
		ret.setStateCode("state");
		ret.setCountryCode("US");
		ret.setZipCode(new ZipCode("1111", "22"));
		return ret;
	}

	private ClaimantRegistration getRegistration() {
		ClaimantRegistration ret = new ClaimantRegistration();
		ret.setRegistration1("r1");
		ret.setRegistration2("r2");
		ret.setRegistration3("r3");
		ret.setRegistration4("r4");
		ret.setRegistration5("r5");
		ret.setRegistration6("r6");
		return ret;
	}

}
