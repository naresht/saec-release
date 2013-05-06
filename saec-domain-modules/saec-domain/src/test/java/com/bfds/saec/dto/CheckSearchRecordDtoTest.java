package com.bfds.saec.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.dto.CheckSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

public class CheckSearchRecordDtoTest {
	
	@Test
	public void createCheckSearchRecordDtoAndVerifyAttributes() {
		Object[] record = new Object[] {
				19L, "check_123", PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR, 100L, "reference_01", 
			      new BigDecimal(200), "r1", "r2", "r3", 
			      "r4", "r5", "r6", "a1", 
			      "a2", "a3", "a4", "a5", 
			      "a6", "city", "state", "1111", 
			      "22", "us", new Date(111, 11, 10)
		}; 
		
		CheckSearchRecordDto dto = CheckSearchRecordDto.CheckSearchRecordVoBuilder.build( record);
		
		assertThat(dto.getId()).isEqualTo(19L);
		assertThat(dto.getCheckNo()).isEqualTo("check_123");
		assertThat(dto.getPaymentCode()).isEqualTo(PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR);	
		assertThat(dto.getClaimantId()).isEqualTo(100);
		assertThat(dto.getReferenceNo()).isEqualTo("reference_01");		
		assertThat(dto.getPaymentAmount()).isEqualTo(200);
		assertThat(dto.getName()).isEqualTo("r1<br/>r2<br/>r3<br/>r4<br/>r5<br/>r6");
		assertThat(dto.getAddress()).isEqualTo("a1<br/>a2<br/>a3<br/>a4<br/>a5<br/>city state 1111-22<br/>us");
		assertThat(dto.getStatusChangeDate()).isEqualTo(new Date(111, 11, 10));
 
	}

}
