package com.bfds.saec.domain.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecStringUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@RooJavaBean
public class TranchAssignmentSearchCriteriaDto extends AbstractSearchCriteriaDto implements Cloneable {
	
	private static final Double DOUBLE_ZERO = new Double(0);
	private static final long serialVersionUID = 1L;
	
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_REFERENCE_NO = "referenceNo";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_CHECK_NO = "checkNo";
	public static final String COLUMN_PAYMENT_AMOUNT = "paymentCalc.nettAmount";
	
	public static final String[] SORT_BY_NAME_FIELDS = new String[] {
		"payTo.claimantRegistration.lines.registration1",
		"payTo.claimantRegistration.lines.registration1",
		"payTo.claimantRegistration.lines.registration1",
		"payTo.claimantRegistration.lines.registration1",
		"payTo.claimantRegistration.lines.registration1",
		"payTo.claimantRegistration.lines.registration1" };

	public static final String[] SORT_BY_ADDRESS_FIELDS = new String[] {
		"checkAddress.address.countryCode",
		"checkAddress.address.stateCode", "checkAddress.address.city",
		"checkAddress.address.zipCode.zip",
		"checkAddress.address.zipCode.ext",
		"checkAddress.address.address1", "checkAddress.address.address2",
		"checkAddress.address.address3", "checkAddress.address.address4",
		"checkAddress.address.address5", "checkAddress.address.address6" };

	private String[] stateCode;
	private Double fromAmount = DOUBLE_ZERO;
	private Double toAmount = DOUBLE_ZERO;
	private String fromCheckNo;
	private String toCheckNo; 
	private String fromAccountNo;
	private String toAccountNo;
	private String brokerId;
	private PaymentType paymentType;
	
	/**
	 * tranchCode should be set only for removing items from a tranch. ie when this{@link #tranchMode} = 'remove'
	 */
	private String tranchCode;
	
	/**
	 * Only two possible values
	 * 1. null - Used when adding {@link Payment}s to a tranch.
	 * 2. remove - Used when removing {@link Payment}s from a tranch.
	 */
	private final String tranchMode;
	
	/**
	 * To search by exact account #.
	 */
	private String accountNo;
	
	/**
	 * The Primary Keys {@link PaymentType#id} to exclude from tranch selection. 
	 */
	private List<Long> excludes = Lists.newArrayList(); 
	
	/**
	 * The Primary Keys {@link PaymentType#id} to include in tranch selection. 
	 */
	private List<Long> includes = Lists.newArrayList(); 
	
	/**
	 * When this is true, none of the search results are assigned to the tranch. Only those manually selected (TranchAssignmentSearchCriteriaDto#includes) will be assigned. 
	 * 
	 */
	private boolean excludeAll;
	
	public static TranchAssignmentSearchCriteriaDto newInstance(final String tranchMode) {		
		return new TranchAssignmentSearchCriteriaDto(tranchMode);
	}
	
	public TranchAssignmentSearchCriteriaDto(final String tranchMode) {
		Preconditions.checkArgument("add".equals(tranchMode) || "remove".equals(tranchMode), "Invalid tranchMode :", tranchMode);
		this.tranchMode = tranchMode;
	}
	
	public List<String> validate() {
		List<String> errors = new ArrayList<String>();
		validateAmountRange(fromAmount, toAmount, "amount", errors);
		validateNumberRange(fromCheckNo, toCheckNo, "Check#", errors);
		validateNumberRange(fromAccountNo, toAccountNo, "Account#", errors);
		validateExcludes(errors);
		return errors;
	}

	private void validateExcludes(List<String> errors) {
		if(excludeAll && excludes.size() > 0) {
			errors.add("Cannot have excludes when exclude-all is selected.");
		} else if(excludeAll && includes.size() == 0) {
			errors.add("Must have at least one include when exclude-all is selected.");
		} else if(!excludeAll && includes.size() > 0) {
			errors.add("Cannot have includes when exclude-all is not selected.");
		}		
	}

	private void validateAmountRange(final Double from,
			final Double to,  final String messageParam, final List<String> errors) {
		if(from != null && from > 0 && ( to == null || to <= 0)) {
			errors.add(String.format("Please enter 'to' %s. Must be greater than 'from' %s.", messageParam, messageParam));
		} else if(to != null && to > 0 && ( from == null || from <= 0)) {
			errors.add(String.format("Please enter 'from' %s. Must be less than 'to' %s.", messageParam, messageParam));
		} else if(!validAmountRange(from, to)) {
			errors.add(String.format("Please enter 'to' %s greater than 'from' %s.", messageParam, messageParam));
		}
	}
	
	private void validateNumberRange(final String fromStr,
			final String toStr, final String messageParam, final List<String> errors) {
		final Long from = asLong(fromStr);
		final Long to = asLong(toStr);
		if(from  == null || to ==null) {
			if(from  == null) {
				errors.add(String.format("Please enter valid 'from' %s. Must be numeric.", messageParam));
			}
			if(to  == null) {
				errors.add(String.format("Please enter valid 'to' %s. Must be numeric.", messageParam));
			}
		} else {
			if(from > 0 && to <= 0) {
				errors.add(String.format("Please enter 'to' %s. Must be greater than 'from' %s.", messageParam, messageParam));
			} else if(to > 0 && from <= 0) {
				errors.add(String.format("Please enter 'from' %s. Must be less than 'to' %s.", messageParam, messageParam));
			} else if(!validAmountRange(from, to)) {
				errors.add(String.format("Please enter 'to' %s greater than 'from' %s.", messageParam, messageParam));
			}
		}
	}

	/**
	 * @param from - The String to be converted to Long
	 * @return Long value of String or null if conversion failed. If the string is empty or null a Long(0) is returned.
	 */
	private Long asLong(String from) {
		from = StringUtils.hasText(from) ? from.trim() : "0";
		try {
			return NumberUtils.parseNumber(from, Long.class);
		} catch (Exception e) {
			return null;
		}
	}

	private boolean validAmountRange(final Double from, final Double to) {
		return to == 0 || (from <= to);
	}
	
	private boolean validAmountRange(final Long from, final Long to) {
		return to == 0 || (from <= to);
	}
	
	
	public String[] getSortFields() {
		if(!hasSortFields()) {return new String[] {};}
		if (COLUMN_NAME.equals(this.getSortField())) {
			return SORT_BY_NAME_FIELDS;
		} else if (COLUMN_REFERENCE_NO.equals(this.getSortField())) {
			return new String[] { "payTo.referenceNo" };
		} else if (COLUMN_ADDRESS.equals(this.getSortField())) {
			return SORT_BY_ADDRESS_FIELDS;
		} else if (COLUMN_CHECK_NO.equals(this.getSortField())) {
			return new String[] { "identificatonNo" };
		} else if (COLUMN_PAYMENT_AMOUNT.equals(this.getSortField())) {
			return new String[] { "paymentCalc.nettAmount" };
		} else {
			throw new IllegalArgumentException("Sort field not supported "
					+ this.getSortField());
		}
	}

	
	public Map<String, Object> getResolvedFilters() {
		if (filters == null) {
			return Collections.<String, Object>emptyMap();
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Map.Entry<String, String> filter : filters.entrySet()) {
			String key = filter.getKey();
			String value = filter.getValue();
			if (COLUMN_REFERENCE_NO.equals(key)) {
				ret.put("payTo.referenceNo", value);
			} else if (COLUMN_NAME.equals(key)) {
				ret.put("payTo.claimantRegistration.concatinatedLines", value);
			} else if (COLUMN_CHECK_NO.equals(key)) {
				ret.put("identificatonNo", value);
			} else if (COLUMN_PAYMENT_AMOUNT.equals(key)) {
				ret.put("paymentCalc.nettAmount", new BigDecimal(SaecStringUtils.getAsDouble(value)));
			} else if (COLUMN_ADDRESS.equals(key)) {
				ret.put("checkAddress.address.address1,checkAddress.address.address2,"
						+ "checkAddress.address.address3,checkAddress.address.address4,"
						+ "checkAddress.address.address5,checkAddress.address.address6,"
						+ "checkAddress.address.city,checkAddress.address.stateCode,"
						+ "checkAddress.address.zipCode.zip,checkAddress.address.countryCode",
						value);
			} 
		}
		return ret;
	}
	
	public void setPaymentType(final String type) {
		if ("CHECK".equalsIgnoreCase(type)) {
			setPaymentTypeCheck();
		} else if ("WIRE".equalsIgnoreCase(type)) {
			setPaymentTypeWire();
		} else {
			throw new IllegalArgumentException("Unsupported payment type "
					+ type);
		}
	}	
	
	public void setPaymentTypeCheck() {
		this.setPaymentType(PaymentType.CHECK);
	}

	public void setPaymentTypeWire() {
		this.setPaymentType(PaymentType.WIRE);
	}	

	public boolean hasAmountFilter() {
		return fromAmount != 0 || toAmount != 0;
	}
	
	public PaymentType getPaymentType() {
		return paymentType == null ? PaymentType.CHECK : paymentType;
	}
	
	@Override
	public boolean isValid() {
		return this.validate().isEmpty();

	}
	
	/**
	 * This is useful in the UI. We do not have do a "tranchMode == 'remove'" every time.
	 */
	public boolean isTranchModeRemove() {
		return "remove".equals(this.tranchMode);
	}

	
	public TranchAssignmentSearchCriteriaDto clone() {
		try {
			final TranchAssignmentSearchCriteriaDto ret =  (TranchAssignmentSearchCriteriaDto) super.clone();
			ret.setExcludes(Lists.<Long>newArrayList() );
			ret.getExcludes().addAll(this.getExcludes());
			ret.setIncludes(Lists.<Long>newArrayList() );
			ret.getIncludes().addAll(this.getIncludes());
			
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}
	
    public Double getFromAmount() {
        return this.fromAmount;
    }
    
    public void setFromAmount(Double fromAmount) {
    	if(fromAmount == null) {
    		fromAmount = 0d;
    	}
        this.fromAmount = fromAmount;
    }
    
    public Double getToAmount() {
        return this.toAmount;
    }
    
    public void setToAmount(Double toAmount) {
    	if(toAmount == null) {
    		toAmount = 0d;
    	}    	
        this.toAmount = toAmount;
    }
    

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((accountNo == null) ? 0 : accountNo.hashCode());
		result = prime * result
				+ ((brokerId == null) ? 0 : brokerId.hashCode());
		result = prime * result + (excludeAll ? 1231 : 1237);
		result = prime * result
				+ ((excludes == null) ? 0 : excludes.hashCode());
		result = prime * result
				+ ((fromAccountNo == null) ? 0 : fromAccountNo.hashCode());
		result = prime * result
				+ ((fromAmount == null) ? 0 : fromAmount.hashCode());
		result = prime * result
				+ ((fromCheckNo == null) ? 0 : fromCheckNo.hashCode());
		result = prime * result
				+ ((includes == null) ? 0 : includes.hashCode());
		result = prime * result
				+ ((paymentType == null) ? 0 : paymentType.hashCode());
		result = prime * result + Arrays.hashCode(stateCode);
		result = prime * result
				+ ((toAccountNo == null) ? 0 : toAccountNo.hashCode());
		result = prime * result
				+ ((toAmount == null) ? 0 : toAmount.hashCode());
		result = prime * result
				+ ((toCheckNo == null) ? 0 : toCheckNo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TranchAssignmentSearchCriteriaDto other = (TranchAssignmentSearchCriteriaDto) obj;
		if (accountNo == null) {
			if (other.accountNo != null) {
				return false;
			}
		}
		else if (!accountNo.equals(other.accountNo)) {
			return false;
		}
		if (brokerId == null) {
			if (other.brokerId != null) {
				return false;
			}
		}
		else if (!brokerId.equals(other.brokerId)) {
			return false;
		}
		if (excludeAll != other.excludeAll) {
			return false;
		}
		if (excludes == null) {
			if (other.excludes != null) {
				return false;
			}
		}
		else if (!excludes.equals(other.excludes)) {
			return false;
		}
		if (fromAccountNo == null) {
			if (other.fromAccountNo != null) {
				return false;
			}
		}
		else if (!fromAccountNo.equals(other.fromAccountNo)) {
			return false;
		}
		if (fromAmount == null) {
			if (other.fromAmount != null) {
				return false;
			}
		}
		else if (!fromAmount.equals(other.fromAmount)) {
			return false;
		}
		if (fromCheckNo == null) {
			if (other.fromCheckNo != null) {
				return false;
			}
		}
		else if (!fromCheckNo.equals(other.fromCheckNo)) {
			return false;
		}
		if (includes == null) {
			if (other.includes != null) {
				return false;
			}
		}
		else if (!includes.equals(other.includes)) {
			return false;
		}
		if (paymentType != other.paymentType) {
			return false;
		}
		if (!Arrays.equals(stateCode, other.stateCode)) {
			return false;
		}
		if (toAccountNo == null) {
			if (other.toAccountNo != null) {
				return false;
			}
		}
		else if (!toAccountNo.equals(other.toAccountNo)) {
			return false;
		}
		if (toAmount == null) {
			if (other.toAmount != null) {
				return false;
			}
		}
		else if (!toAmount.equals(other.toAmount)) {
			return false;
		}
		if (toCheckNo == null) {
			if (other.toCheckNo != null) {
				return false;
			}
		}
		else if (!toCheckNo.equals(other.toCheckNo)) {
			return false;
		}
		return true;
	}

}
