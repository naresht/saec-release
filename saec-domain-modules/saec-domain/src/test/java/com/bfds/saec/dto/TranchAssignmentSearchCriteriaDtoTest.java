package com.bfds.saec.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.bfds.saec.domain.dto.AbstractSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchCriteriaDto;
import com.bfds.saec.domain.reference.PaymentType;
import com.google.common.collect.Maps;

public class TranchAssignmentSearchCriteriaDtoTest {
	
	@Test
	public void checkDefaultSerchValues() {
		
		final TranchAssignmentSearchCriteriaDto searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.CHECK);
		
		assertThat(searchCriteria.getFromAmount()).isEqualTo(0);
		assertThat(searchCriteria.getToAmount()).isEqualTo(0);
		
		assertThat(searchCriteria.getFromAccountNo()).isEqualTo(null);
		assertThat(searchCriteria.getToAccountNo()).isEqualTo(null);
		
		assertThat(searchCriteria.getFromCheckNo()).isEqualTo(null);
		assertThat(searchCriteria.getToCheckNo()).isEqualTo(null);
		
		assertThat(searchCriteria.getAccountNo()).isEqualTo(null);
		assertThat(searchCriteria.getBrokerId()).isNull();
		
		assertThat(searchCriteria.getMaxResults()).isEqualTo(AbstractSearchCriteriaDto.DEFAULT_PAGE_SIZE);
		
		assertThat(searchCriteria.getFirstResult()).isEqualTo(0);
		
		assertThat(searchCriteria.getSortField()).isNull();
		
		assertThat(searchCriteria.hasSortFields()).isFalse();
		
		assertThat(searchCriteria.getSortOrder()).isEqualTo(AbstractSearchCriteriaDto.DEFAULT_SORT_ORDER);
		
		assertThat(searchCriteria.getFilters()).isEmpty();
		
		assertThat(searchCriteria.getSortFields()).isEmpty();
		
		assertThat(searchCriteria.getFilters()).isEmpty();
		assertThat(searchCriteria.getResolvedFilters()).isEmpty();						
	}
	
	@Test
	public void checkProperties() {
		
		final TranchAssignmentSearchCriteriaDto searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		searchCriteria.setPaymentType(PaymentType.WIRE);
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.WIRE);
		searchCriteria.setPaymentType("check");
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.CHECK);
		searchCriteria.setPaymentType("wire");
		assertThat(searchCriteria.getPaymentType()).isEqualTo(PaymentType.WIRE);
		
		searchCriteria.setFromAmount(100d);		
		assertThat(searchCriteria.getFromAmount()).isEqualTo(100);
		searchCriteria.setToAmount(200d);
		assertThat(searchCriteria.getToAmount()).isEqualTo(200);
		assertThat(searchCriteria.hasAmountFilter()).isTrue();
		
		
		searchCriteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_NAME);
		assertThat(searchCriteria.getSortField()).isEqualTo(TranchAssignmentSearchCriteriaDto.COLUMN_NAME);
		assertThat(searchCriteria.hasSortFields()).isTrue();
		assertThat(searchCriteria.getSortFields()).isNotEmpty();
		
		//TODO : Covert sort order to enum.
		searchCriteria.setSortOrder(TranchAssignmentSearchCriteriaDto.SORT_ORDER_DESC);
		assertThat(searchCriteria.getSortOrder()).isEqualTo(TranchAssignmentSearchCriteriaDto.SORT_ORDER_DESC);
		
		searchCriteria.setMaxResults(100);
		assertThat(searchCriteria.getMaxResults()).isEqualTo(100);
		
		searchCriteria.setFirstResult(10);
		assertThat(searchCriteria.getFirstResult()).isEqualTo(10);
				
		Map<String, String> filters = Maps.newHashMap();
		filters.put("x", "y");
		searchCriteria.setFilters(filters);
		assertThat(searchCriteria.getFilters()).hasSize(1);	
		//Empty as the filters are not valid.
		assertThat(searchCriteria.getResolvedFilters()).isEmpty();						
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void isvalidPaymentType() {
		final TranchAssignmentSearchCriteriaDto searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		searchCriteria.setPaymentType("ach");
	}
	
	@Test
	public void checkValidSearchCriteria() {
		TranchAssignmentSearchCriteriaDto searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		assertThat(searchCriteria.isValid()).isTrue();
		
		searchCriteria.setFromAmount(100d);		
		//searchCriteria.setToAmount(20d);
		assertThat(searchCriteria.isValid()).isFalse();
		assertThat(searchCriteria.validate()).containsOnly("Please enter 'to' amount. Must be greater than 'from' amount.");
		
		searchCriteria.setFromAmount(null);		
		searchCriteria.setToAmount(20d);
		assertThat(searchCriteria.isValid()).isFalse();
		assertThat(searchCriteria.validate()).containsOnly("Please enter 'from' amount. Must be less than 'to' amount.");
		
		searchCriteria.setFromAmount(100d);		
		searchCriteria.setToAmount(20d);
		assertThat(searchCriteria.isValid()).isFalse();
		
		searchCriteria.setFromAmount(100d);		
		searchCriteria.setToAmount(200d);
		assertThat(searchCriteria.isValid()).isTrue();
		
		searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		
		searchCriteria.getExcludes().add(123L);
		assertThat(searchCriteria.isValid()).isTrue();
		
		searchCriteria.setExcludeAll(true);
		assertThat(searchCriteria.isValid()).isFalse();
		assertThat(searchCriteria.validate()).containsOnly("Cannot have excludes when exclude-all is selected.");
		
		searchCriteria.setExcludeAll(true);
		searchCriteria.getExcludes().clear();
		assertThat(searchCriteria.isValid()).isFalse();
		assertThat(searchCriteria.validate()).containsOnly("Must have at least one include when exclude-all is selected.");
		
		searchCriteria.getIncludes().add(10L);
		searchCriteria.getExcludes().clear();
		assertThat(searchCriteria.isValid()).isTrue();
		
		searchCriteria.getIncludes().add(10L);
		searchCriteria.setExcludeAll(false);
		assertThat(searchCriteria.isValid()).isFalse();
		assertThat(searchCriteria.validate()).containsOnly("Cannot have includes when exclude-all is not selected.");
	}
	
	@Test
	public void checkSortByFields() { 
		final TranchAssignmentSearchCriteriaDto searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		searchCriteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_NAME);		
		assertThat(searchCriteria.getSortFields()).containsOnly(
			"payTo.claimantRegistration.lines.registration1",
			"payTo.claimantRegistration.lines.registration1",
			"payTo.claimantRegistration.lines.registration1",
			"payTo.claimantRegistration.lines.registration1",
			"payTo.claimantRegistration.lines.registration1",
			"payTo.claimantRegistration.lines.registration1");
		
		searchCriteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_REFERENCE_NO);		
		assertThat(searchCriteria.getSortFields()).containsOnly("payTo.referenceNo");
		
		searchCriteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_ADDRESS);		
		assertThat(searchCriteria.getSortFields()).containsOnly(
				"checkAddress.address.countryCode",
				"checkAddress.address.stateCode", "checkAddress.address.city",
				"checkAddress.address.zipCode.zip",
				"checkAddress.address.zipCode.ext",
				"checkAddress.address.address1", "checkAddress.address.address2",
				"checkAddress.address.address3", "checkAddress.address.address4",
				"checkAddress.address.address5", "checkAddress.address.address6");
		
		searchCriteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_CHECK_NO);		
		assertThat(searchCriteria.getSortFields()).containsOnly("identificatonNo");
		
		searchCriteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_PAYMENT_AMOUNT);		
		assertThat(searchCriteria.getSortFields()).containsOnly("paymentCalc.nettAmount");		
		
	}
	
	
	@Test
	public void checkResolvedFilters() { 
		final TranchAssignmentSearchCriteriaDto searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		
		Map<String, String> filters = Maps.newHashMap();
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_REFERENCE_NO, "");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_NAME, "");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_CHECK_NO, "");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_PAYMENT_AMOUNT, "");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_ADDRESS, "");
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
					+ "checkAddress.address.zipCode.zip,checkAddress.address.countryCode");	
		
		
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invaliSortField() {
		final TranchAssignmentSearchCriteriaDto searchCriteria = new TranchAssignmentSearchCriteriaDto("add");
		searchCriteria.setSortField("ach");
		searchCriteria.getSortFields();
	}
	
}
