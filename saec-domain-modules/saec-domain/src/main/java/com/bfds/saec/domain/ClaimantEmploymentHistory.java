package com.bfds.saec.domain;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@Embeddable
public class ClaimantEmploymentHistory implements java.io.Serializable,
		Cloneable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date serviceStartDate1;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date serviceEndDate1;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date serviceStartDate2;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date serviceEndDate2;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((serviceStartDate1 == null) ? 0 : serviceStartDate1
						.hashCode());
		result = prime
				* result
				+ ((serviceStartDate2 == null) ? 0 : serviceStartDate2
						.hashCode());
		result = prime * result
				+ ((serviceEndDate1 == null) ? 0 : serviceEndDate1.hashCode());
		result = prime * result
				+ ((serviceEndDate2 == null) ? 0 : serviceEndDate2.hashCode());
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
		ClaimantEmploymentHistory other = (ClaimantEmploymentHistory) obj;
		if (serviceStartDate1 == null) {
			if (other.serviceStartDate1 != null)
				return false;
		} else if (other.serviceStartDate1 == null) {
			return false;
		} else if (!getAsDate(serviceStartDate1).equals(
				getAsDate(other.serviceStartDate1))) {
			return false;
		}

		if (serviceStartDate2 == null) {
			if (other.serviceStartDate2 != null)
				return false;
		} else if (other.serviceStartDate2 == null) {
			return false;
		} else if (!getAsDate(serviceStartDate2).equals(
				getAsDate(other.serviceStartDate2))) {
			return false;
		}

		if (serviceEndDate1 == null) {
			if (other.serviceEndDate1 != null)
				return false;
		} else if (other.serviceEndDate1 == null) {
			return false;
		} else if (!getAsDate(serviceEndDate1).equals(
				getAsDate(other.serviceEndDate1))) {
			return false;
		}

		if (serviceEndDate2 == null) {
			if (other.serviceEndDate2 != null)
				return false;
		} else if (other.serviceEndDate2 == null) {
			return false;
		} else if (!getAsDate(serviceEndDate2).equals(
				getAsDate(other.serviceEndDate2))) {
			return false;
		}

		return true;
	}

	/**
	 * This is to fix a strange problem in
	 * {@link ClaimantEmploymentHistory#equals(Object)}
	 * 
	 * @param serviceDate
	 *            {@link java.util.Date}
	 * @return A new date
	 */
	private Date getAsDate(Date serviceDate) {
		return new Date(serviceDate.getYear(), serviceDate.getMonth(),
				serviceDate.getDay(), serviceDate.getHours(),
				serviceDate.getMinutes(), serviceDate.getSeconds());
	}

	public ClaimantEmploymentHistory clone() {
		try {
			return (ClaimantEmploymentHistory) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

}