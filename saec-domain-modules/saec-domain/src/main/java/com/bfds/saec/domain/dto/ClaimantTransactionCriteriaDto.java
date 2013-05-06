package com.bfds.saec.domain.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.wss.domain.ClaimantClaim;

@RooJavaBean
public class ClaimantTransactionCriteriaDto extends AbstractSearchCriteriaDto {
	private static final long serialVersionUID = 1L;

	public static final String SORT_ORDER_ASC = "asc";

	public static final String SORT_ORDER_DESC = "desc";

	public static final String DEFAULT_SORT_ORDER = SORT_ORDER_DESC;

	private static final int DEFAULT_PAGE_SIZE = 5;

	public static final String FUND_FIELD = "securityFund.fund";

	public static final String CUISP_FIELD = "securityFund.securityRef.cusip";

	public static final String TICKER_FIELD = "securityFund.securityRef.ticker";
	
	public static final String TRANS_TYPE = "sourceTransactionType";

	private ClaimantClaim claimantClaim;

	public String[] getSortFields() {
		if (!hasSortFields()) {
			return new String[] {};
		}
		return new String[] { getSortField() };
	}

	public Map<String, Object> getResolvedClaimantFilters() {
		if (filters == null) {
			return Collections.<String, Object> emptyMap();
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Map.Entry<String, String> filter : filters.entrySet()) {
			String key = filter.getKey();
			String value = filter.getValue();
			if (FUND_FIELD.equals(key)) {
				ret.put(FUND_FIELD, value);
			} else if (CUISP_FIELD.equals(key)) {
				ret.put(CUISP_FIELD, value);
			} else if (TICKER_FIELD.equals(key)) {
				ret.put(TICKER_FIELD, value);
			} else if (TRANS_TYPE.equals(key)) {
				ret.put(TRANS_TYPE, value);
			}
		}
		return ret;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public ClaimantClaim getClaimantClaim() {
		return claimantClaim;
	}

	public void setClaimantClaim(ClaimantClaim claimantClaim) {
		this.claimantClaim = claimantClaim;
	}
	
}