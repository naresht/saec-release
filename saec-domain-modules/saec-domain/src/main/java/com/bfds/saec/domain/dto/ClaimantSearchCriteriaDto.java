package com.bfds.saec.domain.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.GroupMailCode;
@RooJavaBean
public class ClaimantSearchCriteriaDto extends AbstractSearchCriteriaDto {
	private static final long serialVersionUID = 1L;

	public static final String SORT_ORDER_ASC = "asc";

	public static final String SORT_ORDER_DESC = "desc";

	public static final String DEFAULT_SORT_ORDER = SORT_ORDER_ASC;

	private static final int DEFAULT_PAGE_SIZE = 5;

	public static final String[] SORT_BY_NAME_FIELDS = new String[] {
			"claimantRegistration.lines.registration1",
			"claimantRegistration.lines.registration2",
			"claimantRegistration.lines.registration3",
			"claimantRegistration.lines.registration4",
			"claimantRegistration.lines.registration5",
			"claimantRegistration.lines.registration6" };

	public static final String[] SORT_BY_ADDRESS_FIELDS = new String[] {
			"address.address1", "address.address2", "address.address3",
			"address.address4", "address.address5", "address.address6",
			"address.countryCode", "address.stateCode", "address.city",
			"address.zipCode.zip", "address.zipCode.ext" };
	
	public static final String[] SORT_BY_CITY_FIELD = new String[] { "address.city" };

	public static final String[] SORT_BY_STATE_FIELD = new String[] { "address.stateCode" };
	
	public static final String[] SORT_BY_ZIP_FIELD = new String[] { "address.zipCode.zip" };

	public static final String COLUMN_NAME = "name";

	public static final String COLUMN_REFERENCE_NO = "referenceNo";

	public static final String COLUMN_ADDRESS = "addressLines";

	public static final String COLUMN_CITY = "city";

	public static final String COLUMN_STATE = "state";

	public static final String COLUMN_ZIP = "zip";
	
	public static final String COLUMN_CLAIM_IDENTIFIER = "claimIdentifier";

	private String referenceNo;

	private String checkNo;

	private String taxId;

	private String name;

	private String city;

	private String state;

	private String zip;

	private String bin;

	private String fundAccountNo;

	private String brokerId;

	private String groupMailCode;

	private boolean marketTimer;

	private String identificationType;

	private String ssn;

	private String tin;

	private String ein;
	
	private Long parentClaimantId;
	
	private String claimIdentifier;

	public String getIdentificationType() {
		if (StringUtils.hasText(this.identificationType)) {
			return identificationType;
		}
		else if (StringUtils.hasText(this.getSsn())) {
			return "SSN";
		}
		else if (StringUtils.hasText(this.getTin())) {
			return "TIN";
		}
		else if (StringUtils.hasText(this.getEin())) {
			return "EIN";
		}
		return "SSN";
	}

	public String getFirstName() {
		return name;
	}

	public String getLastName() {
		return name;
	}

	public String getMiddleName() {
		return name;
	}

	public String[] getAddressSortFields() {
		if (COLUMN_ADDRESS.equals(this.getSortField())) {
			return SORT_BY_ADDRESS_FIELDS;
		}
		else if(COLUMN_CITY.equals(this.getSortField())){
			return SORT_BY_CITY_FIELD;
		}
		else if(COLUMN_STATE.equals(this.getSortField())){
			return SORT_BY_STATE_FIELD;
		}
		else if(COLUMN_ZIP.equals(this.getSortField())){
			return SORT_BY_ZIP_FIELD;
		}
		return new String[] {
				
		};
	}

	public String[] getClaimantSortFields() {
		if (COLUMN_NAME.equals(this.getSortField())) {
			return SORT_BY_NAME_FIELDS;
		}
		else if (COLUMN_REFERENCE_NO.equals(this.getSortField())) {
			return new String[] { "referenceNo" };
		}
		return new String[] {};
	}
	
	public String[] getClaimantClaimIdentifierSortField(){
		if(COLUMN_CLAIM_IDENTIFIER.equals(this.getSortField())){
			return new String[] { "claimIdentifier" };
		}
		return new String[] {};
	}

	public Map<String, Object> getResolvedClaimantFilters() {
		if (filters == null) {
			return Collections.<String, Object>emptyMap();
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Map.Entry<String, String> filter : filters.entrySet()) {
			String key = filter.getKey();
			String value = filter.getValue();
			if (COLUMN_REFERENCE_NO.equals(key)) {
				ret.put("referenceNo", value);
			}
			else if (COLUMN_NAME.equals(key)) {
				ret.put("claimantRegistration.concatinatedLines", value);
			}
		}
		return ret;
	}

	public Map<String, Object> getResolvedAddressFilters() {
		if (filters == null) {
			return Collections.<String, Object>emptyMap();
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Map.Entry<String, String> filter : filters.entrySet()) {
			String key = filter.getKey();
			String value = filter.getValue();
			if (COLUMN_ADDRESS.equals(key)) {
				ret.put("address.address1", value);
			}
			else if (COLUMN_CITY.equals(key)) {
				ret.put("address.city", value);
			}
			else if (COLUMN_STATE.equals(key)) {
				ret.put("address.stateCode", value);
			}
			else if (COLUMN_ZIP.equals(key)) {
				ret.put("address.zipCode.zip", value);
			}
		}
		return ret;
	}


	@Override
	public boolean isValid() {
		return true;
	}
}