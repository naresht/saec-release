package com.bfds.saec.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.google.common.base.Preconditions;

/**
 * To represent a state of a {@link Payment}. As of now will be used only to hold the initial state of the {@link Payment}.  
 *
 */
@RooJavaBean(settersByDefault=false)
@RooToString
@RooConfigurable
@Embeddable
public class PaymentState implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private PaymentCode paymentCode;
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	@Column(nullable=false)
	private Date statusChangeDate;
	
	
	/**
	 * @deprecated - For use by frameworks only. JPA .
	 * Use {@link PaymentState#PaymentState(PaymentCode, PaymentStatus, Date) instead.
	 */
	@Deprecated
	private PaymentState() {
		
	}
	
	public PaymentState(final PaymentCode paymentCode, final PaymentStatus paymentStatus, final  Date statusChangeDate) {
		Preconditions.checkArgument(paymentCode != null, "paymentCode is null");
		Preconditions.checkArgument(paymentStatus != null, "paymentStatus is null");
		this.paymentCode = paymentCode;
		this.paymentStatus = paymentStatus;
		this.statusChangeDate = statusChangeDate == null ? new Date() : statusChangeDate;
	}
}
