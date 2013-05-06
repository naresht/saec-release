package com.bfds.saec.domain;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import com.bfds.saec.util.ConverterUtils;
import com.bfds.saec.util.SaecStringUtils;

@RooJavaBean
@RooToString
@Embeddable
public class ClaimantTaxInfo implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_LINE_SEPERATOR = ", ";
	
	public static final String CERTIFICATION_TYPE_W4P = "W4P";

	private String ssn;

	private String ein;

	private String tin;

	private String certificationStatus;

	private String certificationType;

	private String taxCountryCode;

	private Boolean usCitizen;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date certificationDate;

	private Boolean foreignTax;

	private String foreignTaxClassification;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date dateSolicitationReceived;

	public String getAsFormattedText() {
		return getAsFormattedText(DEFAULT_LINE_SEPERATOR);
	}

	public String getAsFormattedText(String lineSeperator) {
		final String[] lines = new String[6];
		lines[0] = "Tax ID/SSN:"
				+ SaecStringUtils.defaultString(getTaxIdentifier());
		lines[1] = "Certification Type:"
				+ SaecStringUtils.defaultString(this.getCertificationType());
		lines[2] = "Certification Status:"
				+ SaecStringUtils.defaultString(this.getCertificationStatus());

		if (this.getUsCitizen() != null) {
			if (this.getUsCitizen()) {
				lines[3] = "US Citizen:" + "Yes";
			}

			else {
				lines[3] = "US Citizen:" + "No";
			}
		}
		lines[4] = "Tax Country:"
				+ SaecStringUtils.defaultString(this.getTaxCountryCode());
		// TODO: Format MM/DD/YY
		lines[5] = "Certification Date:";

		if (this.getCertificationDate() != null) {
			lines[5] = lines[5]
					+ ConverterUtils.getFormattedString(
							this.getCertificationDate(), "MM/dd/yyyy");
		}
		return SaecStringUtils.getArrayString(lines, StringUtils
				.hasText(lineSeperator)
				? lineSeperator
				: DEFAULT_LINE_SEPERATOR);
	}

	/**
	 * @return The tax ID. The Tax ID is one of ssn, ein and tin. If more than
	 *         one of these are present the tax ID is chosed in the following
	 *         order 1. Return ssn if ssn present 2. Return ein if ssn not
	 *         present and ein present 3. Return tin if tin present and ssn and
	 *         ein not present. 4. Else return null.
	 * 
	 */
	public String getTaxIdentifier() {
		return StringUtils.hasText(ssn) ? ssn : StringUtils.hasText(ein)
				? ein
				: StringUtils.hasText(tin) ? tin : null;
	}
	/**
     * Removes all "-" from {@link #ssn} copy. The actual {@link #ssn} is not changed.
     * eg. 445-44-4040 will become 445444040
     *
	 * @return ssn than can be converted to a {@link java.lang.Long}
	 */
	public String getNumericSsn() {
		if(ssn != null) {
			return ssn.replace("-", "");
	    }
		return ssn;
	}
	/**
	 * Based on ssn, ein, tin;
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ein == null) ? 0 : ein.hashCode());
		result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
		result = prime * result + ((tin == null) ? 0 : tin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClaimantTaxInfo other = (ClaimantTaxInfo) obj;

		if (ein == null) {
			if (other.ein != null)
				return false;
		} else if (!ein.equals(other.ein))
			return false;
		if (ssn == null) {
			if (other.ssn != null)
				return false;
		} else if (!ssn.equals(other.ssn))
			return false;
		if (tin == null) {
			if (other.tin != null)
				return false;
		} else if (!tin.equals(other.tin))
			return false;
		return true;
	}

	@Override
	public ClaimantTaxInfo clone() {
		try {
			return (ClaimantTaxInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

}
