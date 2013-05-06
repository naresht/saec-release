package com.bfds.saec.domain;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@Embeddable
public class WireInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Embedded
    private Bank receivingBank;
    private String authorizedApprover;
    private String authorizedTitle;
    private Date authorizedDate;

    @Embedded
    private WireOriginationInfo originationInfo;

    public WireInfo() {
        originationInfo = new WireOriginationInfo();
        receivingBank = new Bank();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((authorizedApprover == null) ? 0 : authorizedApprover
						.hashCode());
		result = prime * result
				+ ((authorizedDate == null) ? 0 : authorizedDate.hashCode());
		result = prime * result
				+ ((authorizedTitle == null) ? 0 : authorizedTitle.hashCode());
		result = prime * result
				+ ((originationInfo == null) ? 0 : originationInfo.hashCode());
		result = prime * result
				+ ((receivingBank == null) ? 0 : receivingBank.hashCode());
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
		WireInfo other = (WireInfo) obj;
		if (authorizedApprover == null) {
			if (other.authorizedApprover != null)
				return false;
		} else if (!authorizedApprover.equals(other.authorizedApprover))
			return false;
		if (authorizedDate == null) {
			if (other.authorizedDate != null)
				return false;
		} else if (!authorizedDate.equals(other.authorizedDate))
			return false;
		if (authorizedTitle == null) {
			if (other.authorizedTitle != null)
				return false;
		} else if (!authorizedTitle.equals(other.authorizedTitle))
			return false;
		if (originationInfo == null) {
			if (other.originationInfo != null)
				return false;
		} else if (!originationInfo.equals(other.originationInfo))
			return false;
		if (receivingBank == null) {
			if (other.receivingBank != null)
				return false;
		} else if (!receivingBank.equals(other.receivingBank))
			return false;
		return true;
	}

}
