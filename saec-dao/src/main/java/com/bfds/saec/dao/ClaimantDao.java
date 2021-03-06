/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.dao;

import java.util.List;

import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchRecordDto;

/**
 * DAO Interface for Claimant.
 */
public interface ClaimantDao  {
	public List<ClaimantSearchRecordDto> getClaimantSearchResults(ClaimantSearchCriteriaDto param);
	public Long getClaimantSearchResultsCount(ClaimantSearchCriteriaDto param);
}
