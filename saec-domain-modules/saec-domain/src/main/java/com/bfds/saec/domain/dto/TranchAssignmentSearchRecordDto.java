package com.bfds.saec.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.AddressUtils;
import com.bfds.saec.util.SaecStringUtils;
@RooJavaBean
public class TranchAssignmentSearchRecordDto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String checkNo;
	private Long claimantId;
	private Date requestedDate;
	private String referenceNo;
	private String name;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private ZipCode zipCode;
	private double paymentAmount;
	private boolean include;
	private PaymentType paymentType;

	public void clearSelection() {
		this.setInclude(true);
	}
	
	public static class TranchAssignmentSearchRecordDtoBuilder {

		public static TranchAssignmentSearchRecordDto build(Object[] record) {
			final TranchAssignmentSearchRecordDto ret = new TranchAssignmentSearchRecordDto();
			ret.setId((Long) record[0]);
			ret.setCheckNo((String) record[1]);
			ret.setClaimantId((Long) record[2]);
			ret.setReferenceNo((String) record[3]);
			ret.setPaymentAmount(((BigDecimal) record[4]).doubleValue());
			ret.setName(getNameString(record));
			ret.setAddress1((String) record[11]);
			ret.setAddress2((String) record[12]);
			ret.setCity((String) record[17]);
			ret.setState((String) record[18]);
			ret.setZipCode(new ZipCode((String) record[19], (String) record[20]));			
			ret.setPaymentType((PaymentType)record[22]);
			ret.setInclude(true);
			return ret;
		}

		private static String getAddressString(Object[] extractAddressFields) {
			return AddressUtils.getAddressAsString(
					java.util.Arrays.copyOfRange(extractAddressFields, 11, 16),
					(String)extractAddressFields[17], (String)extractAddressFields[18],
					(String)extractAddressFields[19], (String)extractAddressFields[20],
					(String)extractAddressFields[21], "<br/>");
		}

		private static String getNameString(Object[] extractNameFields) {
			return SaecStringUtils.getArrayString(
					java.util.Arrays.copyOfRange(extractNameFields, 5, 11),
					"<br/>");
		}
	}
}
