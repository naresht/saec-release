package com.bfds.saec.domain.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.SaecDateUtils;
import com.bfds.saec.util.SaecStringUtils;
import com.google.common.collect.Lists;

@RooJavaBean
public class CheckReleaseSearchCriteriaDto extends AbstractSearchCriteriaDto implements Cloneable {

	private static final Double DOUBLE_ZERO = new Double(0);

	public static final String[] SORT_BY_NAME_FIELDS = new String[] {
			"payTo.claimantRegistration.lines.registration1",
			"payTo.claimantRegistration.lines.registration2",
			"payTo.claimantRegistration.lines.registration3",
			"payTo.claimantRegistration.lines.registration4",
			"payTo.claimantRegistration.lines.registration5",
			"payTo.claimantRegistration.lines.registration6" };

	public static final String[] SORT_BY_ADDRESS_FIELDS = new String[] {
			"checkAddress.address.countryCode",
			"checkAddress.address.stateCode", "checkAddress.address.city",
			"checkAddress.address.zipCode.zip",
			"checkAddress.address.zipCode.ext",
			"checkAddress.address.address1", "checkAddress.address.address2",
			"checkAddress.address.address3", "checkAddress.address.address4",
			"checkAddress.address.address5", "checkAddress.address.address6" };

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_REFERENCE_NO = "referenceNo";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_CHECK_NO = "checkNo";
	public static final String COLUMN_PAYMENT_AMOUNT = "paymentAmount";
	public static final String COLUMN_STATUS_DATE = "statusChangeDate";

	private static final long serialVersionUID = 1L;
	
	private Double fromAmount = DOUBLE_ZERO;
	private Double toAmount = DOUBLE_ZERO;
	private Date fromDate;
	private Date toDate;
	private boolean isBulk;
	private PaymentType paymentType;
	
	/**
	 * Note that this applies to only bulk release. 
	 * When true the bulk release will behave like a manual release. This flag is currently use in the UI only.
	 */
	private boolean excludeAllByDefault = true;
	

	public Double getFromAmount() {
		return fromAmount == null ? DOUBLE_ZERO : fromAmount;
	}

	public void setFromAmount(Double fromAmount) {
		this.fromAmount = fromAmount == null ? DOUBLE_ZERO : fromAmount;
	}

	public Double getToAmount() {
		return toAmount == null ? DOUBLE_ZERO : toAmount;
	}

	public void setToAmount(Double toAmount) {
		this.toAmount = toAmount == null ? DOUBLE_ZERO : toAmount;
	}

	public int getMaxResults() {
		return maxResults == 0 ? DEFAULT_PAGE_SIZE : maxResults;
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
		} else if (COLUMN_STATUS_DATE.equals(this.getSortField())) {
			return new String[] { "statusChangeDate" };
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
				if(this.getPaymentType().equals(PaymentType.WIRE)) {
					ret.put("reissueOf.identificatonNo", value);
				} else {
					ret.put("identificatonNo", value);					
				}
			} else if (COLUMN_PAYMENT_AMOUNT.equals(key)) {
				ret.put("paymentCalc.nettAmount", new BigDecimal(SaecStringUtils.getAsDouble(value)));
			} else if (COLUMN_ADDRESS.equals(key)) {
				ret.put("checkAddress.address.address1,checkAddress.address.address2,"
						+ "checkAddress.address.address3,checkAddress.address.address4,"
						+ "checkAddress.address.address5,checkAddress.address.address6,"
						+ "checkAddress.address.city,checkAddress.address.stateCode,"
						+ "checkAddress.address.zipCode.zip,checkAddress.address.countryCode",
						value);
			} else if (COLUMN_STATUS_DATE.equals(key)) {
				if (value.length() > 0) {
					ret.put("statusChangeDate", SaecDateUtils.toIntArray(value));
				}
			}
		}
		return ret;
	}

	public boolean isValid() {
		return paymentType != null && validDateRange() && validAmountRange();

	}
	
	public static List<PaymentCode> getApprovalRequestCodes(PaymentType paymentType) {
		final List<PaymentCode> ret = Lists.newArrayList();
		if(PaymentType.CHECK == paymentType) {			
			ret.addAll(PaymentCodeUtil.getCheckVoidReissueRequestedCodes());
			ret.addAll(PaymentCodeUtil.getCheckStopReplaceRequestedCodes());
		} else if(PaymentType.WIRE == paymentType) {			
			ret.add(PaymentCode.WIRE_REQUESTED_W_W);
		} else {
			throw new IllegalStateException(String.format("Unsupported PaymentType %s for Payment release", paymentType));
		}
		return ret;
	}
	
	public static PaymentStatus getApprovedStatus(PaymentType paymentType) {
		return PaymentType.CHECK == paymentType ? PaymentStatus.REISSUE_APPROVED
				: PaymentStatus.WIRE_APPROVED;
	}
	
	public static PaymentCode getApprovedCode(final PaymentCode currentCode, PaymentType paymentType) {
		if (paymentType == PaymentType.CHECK) {
			if(PaymentCodeUtil.getCheckVoidReissueRequestedCodes().contains(currentCode)) {
				return PaymentCodeUtil.getVoidReissueApprovedCodeForGivenVoidReissueRequestedCode(currentCode);
			} else if(PaymentCodeUtil.getCheckStopReplaceRequestedCodes().contains(currentCode)) {
				return PaymentCodeUtil.getStopReplaceApprovedCodeForGivenStopRequestedCode(currentCode);
			}
		} else {
			if (currentCode == PaymentCode.WIRE_REQUESTED_W_W) {
				return PaymentCode.WIRE_APPROVED_WA_WA;
			} 
		}
		throw new IllegalArgumentException("Unknow Code : " + currentCode);		
	}

	public static PaymentStatus getRejectedStatus(PaymentType paymentType) {
		return PaymentType.CHECK == paymentType ? PaymentStatus.REISSUE_REJECTED
				: PaymentStatus.WIRE_REJECTED;
	}
	
	public static PaymentCode getRejectedCode(final PaymentCode currentCode, PaymentType paymentType) {
		if (paymentType == PaymentType.CHECK) {
			if(PaymentCodeUtil.getCheckVoidReissueRequestedCodes().contains(currentCode)) {
				return PaymentCodeUtil.getVoidReissueRejectedCodeForGivenVoidReissueRequestedCode(currentCode);
			} else if(PaymentCodeUtil.getCheckStopReplaceRequestedCodes().contains(currentCode)) {
				return PaymentCodeUtil.getStopReplaceRejectedCodeForGivenStopRequestedCode(currentCode);
			}
		} else {
			if (currentCode == PaymentCode.WIRE_REQUESTED_W_W) {
				return PaymentCode.WIRE_REJECTED_WR_WR;
			} 
		}
		throw new IllegalArgumentException("Unknow Code : " + currentCode);		
	}

	private boolean validDateRange() {
		return fromDate == null || toDate == null || fromDate.before(toDate) || fromDate.equals(toDate);
	}

	private boolean validAmountRange() {
		return toAmount == 0 || (fromAmount <= toAmount);
	}

	public boolean hasDateFilter() {
		return fromDate != null || toDate != null;
	}

	public boolean hasAmountFilter() {
		return fromAmount != 0 || toAmount != 0;
	}

	public boolean isBulk() {
		return isBulk;
	}

	public void setBulk(boolean isBulk) {
		this.isBulk = isBulk;
	}

	public void setPaymentTypeCheck() {
		this.setPaymentType(PaymentType.CHECK);
	}

	public void setPaymentTypeWire() {
		this.setPaymentType(PaymentType.WIRE);
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
	
	public boolean emptySearchFields() {
		return !(fromDate != null || toDate != null || fromAmount > 0
				|| toAmount > 0 || StringUtils.hasLength(sortField));
	}	

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((fromAmount == null) ? 0 : fromAmount.hashCode());
		result = prime * result
				+ ((fromDate == null) ? 0 : fromDate.hashCode());
		result = prime * result + (isBulk ? 1231 : 1237);
		result = prime * result
				+ ((paymentType == null) ? 0 : paymentType.hashCode());
		result = prime * result
				+ ((toAmount == null) ? 0 : toAmount.hashCode());
		result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
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
		CheckReleaseSearchCriteriaDto other = (CheckReleaseSearchCriteriaDto) obj;
		if (fromAmount == null) {
			if (other.fromAmount != null) {
				return false;
			}
		}
		else if (!fromAmount.equals(other.fromAmount)) {
			return false;
		}
		if (fromDate == null) {
			if (other.fromDate != null) {
				return false;
			}
		}
		else if (!fromDate.equals(other.fromDate)) {
			return false;
		}
		if (isBulk != other.isBulk) {
			return false;
		}
		if (paymentType != other.paymentType) {
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
		if (toDate == null) {
			if (other.toDate != null) {
				return false;
			}
		}
		else if (!toDate.equals(other.toDate)) {
			return false;
		}
		return true;
	}

	public CheckReleaseSearchCriteriaDto clone() {
		try {
			return (CheckReleaseSearchCriteriaDto) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}
}