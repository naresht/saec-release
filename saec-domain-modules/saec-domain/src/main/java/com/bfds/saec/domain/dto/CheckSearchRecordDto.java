package com.bfds.saec.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.util.AddressUtils;
import com.bfds.saec.util.SaecStringUtils;

public class CheckSearchRecordDto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String checkNo;
	private PaymentCode paymentCode;
	private Long claimantId;
	private Date requestedDate;
	private String referenceNo;
	private String name;
	private String address;
	private double paymentAmount;
	private String comments;
	private boolean approved;
	private boolean rejected;
	private String rejectReasonCode;
	private Date statusChangeDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public String getRejectReasonCode() {
		return rejectReasonCode;
	}

	public void setRejectReasonCode(String rejectReasonCode) {
		this.rejectReasonCode = rejectReasonCode;
	}

	public Long getClaimantId() {
		return claimantId;
	}

	public void setClaimantId(Long claimantId) {
		this.claimantId = claimantId;
	}

	public Date getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(Date statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}			

	public PaymentCode getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(PaymentCode paymentCode) {
		this.paymentCode = paymentCode;
	}

	public void clearSelection() {
		this.setApproved(false);
		this.setRejected(false);
		this.setComments(null);
	}

	
	public static class CheckSearchRecordVoBuilder {

		// TODO: Unit test.
		public static CheckSearchRecordDto build(Object[] record) {
			final CheckSearchRecordDto ret = new CheckSearchRecordDto();
			ret.setId((Long) record[0]);
			ret.setCheckNo((String) record[1]);
			ret.setPaymentCode((PaymentCode)record[2]);
			ret.setClaimantId((Long) record[3]);
			ret.setReferenceNo((String) record[4]);
			ret.setPaymentAmount(((BigDecimal) record[5]).doubleValue());
			ret.setName(getNameString(record));
			ret.setAddress(getAddressString(record));
			ret.setStatusChangeDate((Date)record[23]);
			return ret;
		}

		private static String getAddressString(Object[] extractAddressFields) {
			return AddressUtils.getAddressAsString(
					java.util.Arrays.copyOfRange(extractAddressFields, 12, 17),
					(String)extractAddressFields[18], (String)extractAddressFields[19],
					(String)extractAddressFields[20], (String)extractAddressFields[21],
					(String)extractAddressFields[22], "<br/>");
		}

		private static String getNameString(Object[] extractNameFields) {
			return SaecStringUtils.getArrayString(
					java.util.Arrays.copyOfRange(extractNameFields, 6, 12),
					"<br/>");
		}
	}
}
