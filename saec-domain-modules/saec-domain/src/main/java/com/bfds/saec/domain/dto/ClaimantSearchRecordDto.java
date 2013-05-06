package com.bfds.saec.domain.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.IAddress;
import com.bfds.saec.domain.IRegistrationLines;
import com.bfds.saec.domain.reference.AddressType;

public final class ClaimantSearchRecordDto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private final Long id;
	private final String referenceNo;	
	private final String address1;
	private final String address2;
	private final String name;
	private final String city;
	private final String state;
	private final String zip;
	private final String countryCode;
	private final boolean mailingAddress;
	private final AddressType addressType;
	private final double paymentAmount;
	private final String addressLines;
	private final String checkNo;
	private final String claimIdentifier;
	
	

	public ClaimantSearchRecordDto(Long id, String referenceNo,String claimIdentifier, String name,
                                   String address1, String address2, String addressLines, String city, String state,
                                   String zip, String countryCode, boolean mailingAddress, AddressType addressType, String checkNo, 
                                   double paymentAmount) {
		super();
		this.id = id;
		this.referenceNo = referenceNo;
		this.address1 = address1;
		this.address2 = address2;
		this.name = name;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.countryCode = countryCode;
		this.paymentAmount= paymentAmount;
		this.addressLines = addressLines;
		this.mailingAddress = mailingAddress;
		this.addressType = addressType;
		this.checkNo = checkNo;
		this.claimIdentifier=claimIdentifier;
	}

	public Long getId() {
		return id;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public String getAddressLines() {
		return addressLines;
	}		
	
	public String getCheckNo() {
		return checkNo;
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	

	public String getClaimIdentifier() {
		return claimIdentifier;
	}



	public static class Builder {
		public List<ClaimantSearchRecordDto> build(Collection<Object[]> records) {
			final List<ClaimantSearchRecordDto> ret = new ArrayList<ClaimantSearchRecordDto>();			
			for(Object[] record : records) {
				ret.add(build(record));
			}
			return ret;
		}	
		
		public ClaimantSearchRecordDto build(Object[] record) {
			IAddress aor = (Address) record[3];
			ClaimantSearchRecordDto ret = new ClaimantSearchRecordDto(
					(Long)record[0], (String)record[1],(String)record[4],
					getName((IRegistrationLines) record[2]), getAddress1(aor), getAddress2(aor),
					getAddressLinesAsString(aor), getCity(aor), getState(aor), getZip(aor), getCountryCode(aor),
					isMailingAddress(aor) , getAddressType(aor), 
					(String)record[5], record[6] == null ? 0d : ((BigDecimal) record[6]).doubleValue());
			return ret;
		}		

		private AddressType getAddressType(IAddress aor) {
			return aor != null ? aor.getAddressType() : null;
		}

		private boolean isMailingAddress(IAddress aor) {
			return aor != null ? aor.isMailingAddress() : false;
		}

		private String getAddressLinesAsString(IAddress aor) {
			return aor != null ? aor.getAddressLinesAsString() : null;
		}
		
		private String getState(IAddress aor) {
			return aor != null ? aor.getStateCode() : null;
		}

		private String getCity(IAddress aor) {
			return aor != null ? aor.getCity() : null;
		}

		private String getZip(IAddress aor) {
			return (aor != null && aor.getZipCode() !=null) ? aor.getZipCode().toString() : null;
		}
		
		private String getCountryCode(IAddress aor) {
			return (aor != null && aor.getCountryCode() !=null) ? aor.getCountryCode().toString() : null;
		}
		
		private String getName(IRegistrationLines registration) {		
			if(registration != null) {
				return registration.getRegistrationLinesAsString();
			}
			return  null;
		}		

		private String getAddress1(IAddress aor) {
			return aor != null ? aor.getNonEmptyLine(1) : null;
		}
		
		private String getAddress2(IAddress aor) {
			return aor != null ? aor.getNonEmptyLine(2) : null;
		}		
	}



}