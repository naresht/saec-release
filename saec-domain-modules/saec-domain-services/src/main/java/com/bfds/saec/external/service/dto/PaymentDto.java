package com.bfds.saec.external.service.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;

@RooJavaBean
@RooToString
public class PaymentDto implements java.io.Serializable {
	
	private String checkNo;
	private PaymentStatus paymentStatus;
	private BigDecimal paymentAmount;
	private Date paymentDate;
	private PaymentType paymentType;
	

	public static PaymentDto newPaymentDto(Payment payment) {
		PaymentDto ret = new PaymentDto();
		ret.setPaymentStatus(payment.getPaymentStatus());
		ret.setCheckNo(payment.getIdentificatonNo());
		ret.setPaymentAmount(payment.getPaymentAmount());
		ret.setPaymentDate(payment.getPaymentDate());
		ret.setPaymentType(payment.getPaymentType());
		return ret;
	}
	
}
