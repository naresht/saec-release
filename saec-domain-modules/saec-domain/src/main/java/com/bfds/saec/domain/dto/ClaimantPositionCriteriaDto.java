package com.bfds.saec.domain.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.Claimant;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantPosition.Method;
import com.bfds.wss.domain.ClaimantPosition.PositionType;

@RooJavaBean
public class ClaimantPositionCriteriaDto extends AbstractSearchCriteriaDto {
	private static final long serialVersionUID = 1L;

	public static final String SORT_ORDER_ASC = "asc";

	public static final String SORT_ORDER_DESC = "desc";

	public static final String DEFAULT_SORT_ORDER = SORT_ORDER_DESC;

	private static final int DEFAULT_PAGE_SIZE = 5;

	public static final String COLUMN_POSITION_DATE = "positionDate";

	public static final String FUND_FIELD = "securityFund.fund";

	public static final String CUISP_FIELD = "securityFund.securityRef.cusip";

	public static final String TICKER_FIELD = "securityFund.securityRef.ticker";

	public static final String POSITION_TYPE_FIELD = "positionType";

	public static final String POSITION_METHOD_FIELD = "method";

	public static final String STATUS_FIELD = "status";

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
			} else if ("ME".startsWith(value.toUpperCase())) {
				ret.put(POSITION_TYPE_FIELD, PositionType.ME);
			} else if ("EOD".startsWith(value.toUpperCase())) {
				ret.put(POSITION_TYPE_FIELD, PositionType.EOD);
			} else if ("QE".startsWith(value.toUpperCase())) {
				ret.put(POSITION_TYPE_FIELD, PositionType.QE);
			} else if ("YE".startsWith(value.toUpperCase())) {
				ret.put(POSITION_TYPE_FIELD, PositionType.YE);
			} else if ("CALCULATED".startsWith(value.toUpperCase())) {
				ret.put(POSITION_METHOD_FIELD, Method.CALCULATED);
			} else if ("PROVIDED".startsWith(value.toUpperCase())) {
				ret.put(POSITION_METHOD_FIELD, Method.PROVIDED);
			} else if (STATUS_FIELD.equals(key)) {
				ret.put(STATUS_FIELD, value);
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