package com.bfds.saec.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.payment.service.PaymentService;

public class StopVoidReverseViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger log = LoggerFactory.getLogger(StopVoidReverseViewModel.class);

	private String paymentIdentificationNo;

	private Payment selectedPayment;
	
	private Claimant claimant;
	
	private boolean voidCheckAvailable;
	@Autowired
	private transient PaymentService paymentService;

	public void reset() {
		paymentIdentificationNo = null;
		selectedPayment = null;
	}

	public Payment loadPaymentsForStopVoidReverse(
			final MessageContext messages) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading Payments for stop void reverse...."));
		if (!StringUtils.hasText(paymentIdentificationNo)) {
			messages.addMessage(new MessageBuilder().error()
					.defaultText("Please enter search criteria.").build());
			return null;
		}
		this.selectedPayment = getChecksForStopVoidReverse();
		if(this.selectedPayment == null) {
			messages.addMessage(new MessageBuilder().error()
				.defaultText("Invalid Check# selected for reversal.").build());
		}
		else
			setVoidCheckAvailable(true);
		return this.selectedPayment;
	}


	private Payment getChecksForStopVoidReverse() {
		Payment check = paymentService.getCheckForStopVoidReversal(paymentIdentificationNo);
		if(check == null) {
			return check;
		}
		this.claimant = Claimant.findClaimant(check.getPayTo().getId(), Claimant.ASSOCIATION_ADDRESSES);
		if(log.isInfoEnabled())
			log.info(String.format("Fetched Payments for stop void reverse...."));
		return check;
	}

	public boolean processStopVoidReverse(final MessageContext messages) {
		this.selectedPayment = paymentService.reverseStopOrVoidOnPayment(selectedPayment.getId());
		return true;
	}
	
	public String getCheckStatusForDisplayInMessage() {
		if(this.selectedPayment == null) {
			return "";
		}
		return PaymentCodeUtil.getStopRequestedCodes().contains(this.selectedPayment.getPaymentCode()) ? "stop" : "void";
	}

	public String getPaymentIdentificationNo() {
		return paymentIdentificationNo;
	}

	public void setPaymentIdentificationNo(String paymentIdentificationNo) {
		this.paymentIdentificationNo = paymentIdentificationNo;
	}

	public Payment getSelectedPayment() {
		return selectedPayment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public Claimant getClaimant() {
		return claimant;
	}

	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	public boolean isVoidCheckAvailable() {
		return voidCheckAvailable;
	}

	public void setVoidCheckAvailable(boolean voidCheckAvailable) {
		this.voidCheckAvailable = voidCheckAvailable;
	}
	
}
