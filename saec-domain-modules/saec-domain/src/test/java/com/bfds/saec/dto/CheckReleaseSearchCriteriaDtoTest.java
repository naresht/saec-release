package com.bfds.saec.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CheckReleaseSearchCriteriaDtoTest {
	
	@Test
	public void checkDefaultSerchValues() {
		
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		
		assertThat(searchCriteria.getFromAmount()).isEqualTo(0);
		assertThat(searchCriteria.getToAmount()).isEqualTo(0);
		
		assertThat(searchCriteria.getFromDate()).isNull();
		assertThat(searchCriteria.getToDate()).isNull();
		
		assertThat(searchCriteria.getMaxResults()).isEqualTo(CheckReleaseSearchCriteriaDto.DEFAULT_PAGE_SIZE);
		
		assertThat(searchCriteria.getFirstResult()).isEqualTo(0);
		
		assertThat(searchCriteria.isBulk()).isFalse();
		
		assertThat(searchCriteria.getSortField()).isNull();
		
		assertThat(searchCriteria.hasSortFields()).isFalse();
		
		assertThat(searchCriteria.getSortOrder()).isEqualTo(CheckReleaseSearchCriteriaDto.DEFAULT_SORT_ORDER);
		
		assertThat(searchCriteria.getFilters()).isEmpty();
		
		assertThat(searchCriteria.getPaymentType()).isNull();
		
		assertThat(searchCriteria.getSortFields()).isEmpty();
		
		assertThat(searchCriteria.getFilters()).isEmpty();
		assertThat(searchCriteria.getResolvedFilters()).isEmpty();						
	}
	
	@Test
	public void checkProperties() {
		
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		
		searchCriteria.setFromAmount(100d);		
		assertThat(searchCriteria.getFromAmount()).isEqualTo(100);
		searchCriteria.setToAmount(200d);
		assertThat(searchCriteria.getToAmount()).isEqualTo(200);
		assertThat(searchCriteria.hasAmountFilter()).isTrue();
		
		searchCriteria.setFromDate(new Date(111, 11, 20));
		assertThat(searchCriteria.getFromDate()).isEqualTo(new Date(111, 11, 20));
		searchCriteria.setToDate(new Date(111, 11, 25));
		assertThat(searchCriteria.getToDate()).isEqualTo(new Date(111, 11, 25));
		assertThat(searchCriteria.hasDateFilter()).isTrue();
		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_NAME);
		assertThat(searchCriteria.getSortField()).isEqualTo(CheckReleaseSearchCriteriaDto.COLUMN_NAME);
		assertThat(searchCriteria.hasSortFields()).isTrue();
		assertThat(searchCriteria.getSortFields()).isNotEmpty();
		
		//TODO : Covert sort order to enum.
		searchCriteria.setSortOrder(CheckReleaseSearchCriteriaDto.SORT_ORDER_DESC);
		assertThat(searchCriteria.getSortOrder()).isEqualTo(CheckReleaseSearchCriteriaDto.SORT_ORDER_DESC);
		
		searchCriteria.setPaymentTypeCheck();
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.CHECK);
		searchCriteria.setPaymentTypeWire();
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.WIRE);
		searchCriteria.setPaymentType("check");
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.CHECK);
		searchCriteria.setPaymentType("wire");
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.WIRE);
		
		searchCriteria.setMaxResults(100);
		assertThat(searchCriteria.getMaxResults()).isEqualTo(100);
		
		searchCriteria.setFirstResult(10);
		assertThat(searchCriteria.getFirstResult()).isEqualTo(10);
		
		searchCriteria.setBulk(true);
		assertThat(searchCriteria.isBulk()).isTrue();
		
		
		Map<String, String> filters = Maps.newHashMap();
		filters.put("x", "y");
		searchCriteria.setFilters(filters);
		assertThat(searchCriteria.getFilters()).hasSize(1);	
		//Empty as the filters are not valid.
		assertThat(searchCriteria.getResolvedFilters()).isEmpty();						
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void isvalidPaymentType() {
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		searchCriteria.setPaymentType("ach");
	}
	
	@Test
	public void checkValidSearchCriteria() {
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		assertThat(searchCriteria.isValid()).isFalse();
		searchCriteria.setPaymentType(PaymentType.CHECK);
		assertThat(searchCriteria.isValid()).isTrue();
		
		searchCriteria.setFromAmount(100d);		
		searchCriteria.setToAmount(20d);
		assertThat(searchCriteria.isValid()).isFalse();
		
		searchCriteria.setFromAmount(100d);		
		searchCriteria.setToAmount(200d);
		assertThat(searchCriteria.isValid()).isTrue();

		searchCriteria.setFromDate(new Date(111, 11, 20));
		searchCriteria.setToDate(new Date(111, 10, 25));
		assertThat(searchCriteria.isValid()).isFalse();
		
		searchCriteria.setFromDate(new Date(111, 11, 20));
		searchCriteria.setToDate(new Date(111, 11, 25));
		assertThat(searchCriteria.isValid()).isTrue();
	}
	
	@Test
	public void checkTargetPaymentStatus() {
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		searchCriteria.setPaymentType(PaymentType.CHECK);
		Set<PaymentCode> requestedCodes = Sets.newHashSet();
		requestedCodes.addAll(PaymentCodeUtil.getCheckVoidReissueRequestedCodes());
		requestedCodes.addAll(PaymentCodeUtil.getCheckStopReplaceRequestedCodes());
		requestedCodes.add(PaymentCode.WIRE_REQUESTED_W_W);
		assertThat(requestedCodes.containsAll(CheckReleaseSearchCriteriaDto.getApprovalRequestCodes(searchCriteria.getPaymentType()))).isTrue();
		searchCriteria.setPaymentType(PaymentType.WIRE);
		
		assertThat(CheckReleaseSearchCriteriaDto.getApprovalRequestCodes(searchCriteria.getPaymentType())).containsOnly(PaymentCode.WIRE_REQUESTED_W_W);
	}
	
	@Test
	public void checkSortByFields() { 
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_NAME);		
		assertThat(searchCriteria.getSortFields()).containsOnly(
			"payTo.claimantRegistration.lines.registration1",
			"payTo.claimantRegistration.lines.registration2",
			"payTo.claimantRegistration.lines.registration3",
			"payTo.claimantRegistration.lines.registration4",
			"payTo.claimantRegistration.lines.registration5",
			"payTo.claimantRegistration.lines.registration6");
		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_REFERENCE_NO);		
		assertThat(searchCriteria.getSortFields()).containsOnly("payTo.referenceNo");
		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_ADDRESS);		
		assertThat(searchCriteria.getSortFields()).containsOnly(
				"checkAddress.address.countryCode",
				"checkAddress.address.stateCode", "checkAddress.address.city",
				"checkAddress.address.zipCode.zip",
				"checkAddress.address.zipCode.ext",
				"checkAddress.address.address1", "checkAddress.address.address2",
				"checkAddress.address.address3", "checkAddress.address.address4",
				"checkAddress.address.address5", "checkAddress.address.address6");
		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_CHECK_NO);		
		assertThat(searchCriteria.getSortFields()).containsOnly("identificatonNo");
		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_PAYMENT_AMOUNT);		
		assertThat(searchCriteria.getSortFields()).containsOnly("paymentCalc.nettAmount");		
		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_STATUS_DATE);		
		assertThat(searchCriteria.getSortFields()).containsOnly("statusChangeDate");		
	}
	
	
	@Test
	public void checkResolvedFilters() { 
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		searchCriteria.setPaymentType(PaymentType.CHECK);
		Map<String, String> filters = Maps.newHashMap();
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_REFERENCE_NO, "");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_NAME, "");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_CHECK_NO, "");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_PAYMENT_AMOUNT, "");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_ADDRESS, "");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_STATUS_DATE, "1");
		searchCriteria.setFilters(filters);
		
		assertThat(searchCriteria.getResolvedFilters().keySet()).containsOnly(
				"payTo.referenceNo", 
				"payTo.claimantRegistration.concatinatedLines", 
				"identificatonNo", 
				"paymentCalc.nettAmount", 
				"checkAddress.address.address1,checkAddress.address.address2,"
					+ "checkAddress.address.address3,checkAddress.address.address4,"
					+ "checkAddress.address.address5,checkAddress.address.address6,"
					+ "checkAddress.address.city,checkAddress.address.stateCode,"
					+ "checkAddress.address.zipCode.zip,checkAddress.address.countryCode", 
				"statusChangeDate");	
		
		
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invaliSortField() {
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		searchCriteria.setSortField("ach");
		searchCriteria.getSortFields();
	}
	
	@Test
	public void checkClone() {
		final CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		searchCriteria.setFromAmount(100d);		
		searchCriteria.setToAmount(200d);		
		searchCriteria.setFromDate(new Date(111, 11, 20));		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_NAME);		
		searchCriteria.setSortOrder(CheckReleaseSearchCriteriaDto.SORT_ORDER_DESC);		
		searchCriteria.setPaymentTypeCheck();		
		searchCriteria.setPaymentTypeWire();		
		searchCriteria.setMaxResults(100);		
		searchCriteria.setFirstResult(10);		
		searchCriteria.setBulk(true);		
		
		Map<String, String> filters = Maps.newHashMap();
		filters.put("x", "y");
		searchCriteria.setFilters(filters);
		
		final CheckReleaseSearchCriteriaDto searchCriteria_clone = searchCriteria.clone();
		
		assertThat(searchCriteria == searchCriteria_clone).isFalse();
		
		assertThat(searchCriteria).isEqualTo(searchCriteria);
		assertThat(searchCriteria).isEqualTo(searchCriteria_clone);		
		assertThat(searchCriteria_clone).isEqualTo(searchCriteria);
		
		assertThat(searchCriteria.hashCode()).isEqualTo(searchCriteria_clone.hashCode());		
		assertThat(searchCriteria_clone.hashCode()).isEqualTo(searchCriteria.hashCode());
		
		assertThat(searchCriteria_clone).isNotEqualTo("");		
		assertThat(searchCriteria_clone).isNotEqualTo(null);
			
	}
	
	@Test
	public void hasSearchFields() {
		CheckReleaseSearchCriteriaDto searchCriteria = new CheckReleaseSearchCriteriaDto();
		assertThat(searchCriteria.emptySearchFields()).isTrue();
		searchCriteria.setFromAmount(100d);
		assertThat(searchCriteria.emptySearchFields()).isFalse();
		
		searchCriteria = new CheckReleaseSearchCriteriaDto();
		searchCriteria.setToAmount(100d);
		assertThat(searchCriteria.emptySearchFields()).isFalse();
		
	}
}
