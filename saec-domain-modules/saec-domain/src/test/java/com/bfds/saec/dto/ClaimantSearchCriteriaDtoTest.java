package com.bfds.saec.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.google.common.collect.Maps;

public class ClaimantSearchCriteriaDtoTest {
	
	@Test
	public void checkDefaultSerchValues() {
		
		final ClaimantSearchCriteriaDto searchCriteria = new ClaimantSearchCriteriaDto();
		
		assertThat(searchCriteria.getIdentificationType()).isEqualTo("SSN");
		assertThat(searchCriteria.getMaxResults()).isEqualTo(CheckReleaseSearchCriteriaDto.DEFAULT_PAGE_SIZE);
		
		assertThat(searchCriteria.getMaxResults()).isEqualTo(CheckReleaseSearchCriteriaDto.DEFAULT_PAGE_SIZE);
		
		assertThat(searchCriteria.getFirstResult()).isEqualTo(0);
		
		
		assertThat(searchCriteria.getSortField()).isNull();
		
		assertThat(searchCriteria.getAddressSortFields()).isEmpty();
		
		assertThat(searchCriteria.getClaimantSortFields()).isEmpty();
		
		assertThat(searchCriteria.hasSortFields()).isFalse();
		
		assertThat(searchCriteria.getSortOrder()).isEqualTo(CheckReleaseSearchCriteriaDto.DEFAULT_SORT_ORDER);
		
		assertThat(searchCriteria.getFilters()).isEmpty();
		
		assertThat(searchCriteria.getResolvedAddressFilters()).isEmpty();	
		assertThat(searchCriteria.getResolvedClaimantFilters()).isEmpty();	
	}
	
	@Test
	public void checkProperties() {
		
		final ClaimantSearchCriteriaDto searchCriteria = new ClaimantSearchCriteriaDto();
		
		searchCriteria.setSsn("ssn");
		assertThat(searchCriteria.getSsn()).isEqualTo("ssn");		
		
		searchCriteria.setTin("tin");
		assertThat(searchCriteria.getTin()).isEqualTo("tin");
		
		searchCriteria.setEin("ein");
		assertThat(searchCriteria.getEin()).isEqualTo("ein");		
		
		searchCriteria.setIdentificationType("SSN");
		assertThat(searchCriteria.getIdentificationType()).isEqualTo("SSN");		
		searchCriteria.setIdentificationType(null);
		assertThat(searchCriteria.getIdentificationType()).isEqualTo("SSN");
		searchCriteria.setSsn(null);
		assertThat(searchCriteria.getIdentificationType()).isEqualTo("TIN");
		searchCriteria.setTin(null);
		assertThat(searchCriteria.getIdentificationType()).isEqualTo("EIN");		
		
		searchCriteria.setName("name");
		assertThat(searchCriteria.getName()).isEqualTo("name");	
		assertThat(searchCriteria.getFirstName()).isEqualTo("name");	
		assertThat(searchCriteria.getMiddleName()).isEqualTo("name");	
		assertThat(searchCriteria.getLastName()).isEqualTo("name");	
		
		searchCriteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_NAME);
		assertThat(searchCriteria.getSortField()).isEqualTo(CheckReleaseSearchCriteriaDto.COLUMN_NAME);
		assertThat(searchCriteria.hasSortFields()).isTrue();
		assertThat(searchCriteria.getClaimantSortFields()).isNotEmpty();
		
		//TODO : Covert sort order to enum.
		searchCriteria.setSortOrder(CheckReleaseSearchCriteriaDto.SORT_ORDER_DESC);
		assertThat(searchCriteria.getSortOrder()).isEqualTo(CheckReleaseSearchCriteriaDto.SORT_ORDER_DESC);
		
		
		searchCriteria.setMaxResults(100);
		assertThat(searchCriteria.getMaxResults()).isEqualTo(100);
		
		searchCriteria.setFirstResult(10);
		assertThat(searchCriteria.getFirstResult()).isEqualTo(10);
		
		
		
		Map<String, String> filters = Maps.newHashMap();
		filters.put("x", "y");
		searchCriteria.setFilters(filters);
		assertThat(searchCriteria.getFilters()).hasSize(1);	
		//Empty as the filters are not valid.
		assertThat(searchCriteria.getResolvedAddressFilters()).isEmpty();
		assertThat(searchCriteria.getResolvedClaimantFilters()).isEmpty();
	}
	
	@Test
	public void checkSortByFields() { 
		final ClaimantSearchCriteriaDto searchCriteria = new ClaimantSearchCriteriaDto();
		
		searchCriteria.setSortField(ClaimantSearchCriteriaDto.COLUMN_NAME);		
		assertThat(searchCriteria.getClaimantSortFields()).containsOnly(
				"claimantRegistration.lines.registration1",
				"claimantRegistration.lines.registration2",
				"claimantRegistration.lines.registration3",
				"claimantRegistration.lines.registration4",
				"claimantRegistration.lines.registration5",
				"claimantRegistration.lines.registration6");
		
		searchCriteria.setSortField(ClaimantSearchCriteriaDto.COLUMN_REFERENCE_NO);		
		assertThat(searchCriteria.getClaimantSortFields()).containsOnly(
				"referenceNo");
		
		searchCriteria.setSortField(ClaimantSearchCriteriaDto.COLUMN_ADDRESS);		
		assertThat(searchCriteria.getAddressSortFields()).containsOnly(
				"address.countryCode", "address.stateCode", "address.city",
				"address.zipCode.zip", "address.zipCode.ext", "address.address1",
				"address.address2", "address.address3", "address.address4",
				"address.address5", "address.address6");
	}
	
	
	@Test
	public void checkResolvedFilters() { 
		final ClaimantSearchCriteriaDto searchCriteria = new ClaimantSearchCriteriaDto();
		
		Map<String, String> filters = Maps.newHashMap();
		filters.put(ClaimantSearchCriteriaDto.COLUMN_REFERENCE_NO, "");
		filters.put(ClaimantSearchCriteriaDto.COLUMN_NAME, "");
		searchCriteria.setFilters(filters);		
		assertThat(searchCriteria.getResolvedClaimantFilters().keySet()).containsOnly("referenceNo", "claimantRegistration.concatinatedLines");		
		assertThat(searchCriteria.getResolvedAddressFilters()).isEmpty();
		
		
		filters = Maps.newHashMap();
		filters.put(ClaimantSearchCriteriaDto.COLUMN_ADDRESS, "");
		filters.put(ClaimantSearchCriteriaDto.COLUMN_CITY, "");
		filters.put(ClaimantSearchCriteriaDto.COLUMN_STATE, "");
		filters.put(ClaimantSearchCriteriaDto.COLUMN_ZIP, "");
		searchCriteria.setFilters(filters);		
		assertThat(searchCriteria.getResolvedAddressFilters().keySet()).containsOnly(
				"address.address1", "address.city", "address.stateCode", "address.zipCode.zip");
		assertThat(searchCriteria.getResolvedClaimantFilters()).isEmpty();
		
	}
}
