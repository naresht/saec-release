package com.bfds.saec.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.dto.TranchAssignmentSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentType;

public class TranchAssignmentSearchRecordDtoTest {
	
	@Test
	public void createTranchAssignmentSearchRecordDtoAndVerifyAttributes() {
		Object[] record = new Object[] {
				19L, "check_123", 100L, "reference_01", 
			      new BigDecimal(200), "r1", "r2", "r3", 
			      "r4", "r5", "r6", "a1", 
			      "a2", "a3", "a4", "a5", 
			      "a6", "city", "state", "1111", 
			      "22", "us", 
			      PaymentType.CHECK
		}; 
		
		TranchAssignmentSearchRecordDto dto = TranchAssignmentSearchRecordDto.TranchAssignmentSearchRecordDtoBuilder.build( record);
		
		assertThat(dto.getId()).isEqualTo(19L);
		assertThat(dto.getCheckNo()).isEqualTo("check_123");
		assertThat(dto.getClaimantId()).isEqualTo(100);
		assertThat(dto.getReferenceNo()).isEqualTo("reference_01");		
		assertThat(dto.getPaymentAmount()).isEqualTo(200);
		assertThat(dto.getName()).isEqualTo("r1<br/>r2<br/>r3<br/>r4<br/>r5<br/>r6");
		assertThat(dto.getAddress1()).isEqualTo("a1");
		assertThat(dto.getAddress2()).isEqualTo("a2");
		assertThat(dto.getCity()).isEqualTo("city");
		assertThat(dto.getState()).isEqualTo("state");
		assertThat(dto.getZipCode()).isEqualTo(new ZipCode("1111", "22"));
		assertThat(dto.getPaymentType()).isEqualTo(PaymentType.CHECK);
 
	}
}
