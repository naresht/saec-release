package com.bfds.saec.rip.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", table="awd_rip_stop_replace_check")
@RooJavaBean
@RooConfigurable
@RooToString
public class StopReplaceCheckRipObject extends RipObject {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String referenceNo;

	@Column(nullable=false)
	private String paymentIdentificationNo;
	
	private String registration1;

	private String registration2;

	private String registration3;

	private String registration4;

	private String registration5;

	private String registration6;
	
	private BigDecimal grossAmount;
	
	private String mailedDate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((paymentIdentificationNo == null) ? 0
						: paymentIdentificationNo.hashCode());
		result = prime * result
				+ ((referenceNo == null) ? 0 : referenceNo.hashCode());
		return result;
	}

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
		StopReplaceCheckRipObject other = (StopReplaceCheckRipObject) obj;
		if (paymentIdentificationNo == null) {
			if (other.paymentIdentificationNo != null) {
				return false;
			}
		} else if (!paymentIdentificationNo
				.equals(other.paymentIdentificationNo)) {
			return false;
		}
		if (referenceNo == null) {
			if (other.referenceNo != null) {
				return false;
			}
		} else if (!referenceNo.equals(other.referenceNo)) {
			return false;
		}
		return true;
	}
		
}
