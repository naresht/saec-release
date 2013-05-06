package com.bfds.saec.web.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.dao.PaymentDaoImpl;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.Claimant;

public class StopLiftViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger log = LoggerFactory.getLogger(StopLiftViewModel.class);

	private String paymentIdentificationNo;

	private List<Payment> payments;

	private Payment selectedPayment;
	
	private Claimant claimant;

	@Autowired
	private transient PaymentDao paymentDao = new PaymentDaoImpl();

	public void reset() {
		paymentIdentificationNo = null;
		payments = null;
	}

	public List<Payment> loadPaymentsForStopLift(
			final MessageContext messages) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading Payments for stop lift...."));
		if (!StringUtils.hasText(paymentIdentificationNo)) {
			messages.addMessage(new MessageBuilder().error()
					.defaultText("Please enter search criteria.").build());
			return null;
		}
		this.payments = getChecksForStopLift();
		if(payments.isEmpty() ){
			messages.addMessage(new MessageBuilder().error()
					.defaultText("Invalid Check# selected for stop lift.").build());
			
		}
		return payments;
	}


	private List<Payment> getChecksForStopLift() {
		Payment check = paymentDao.getCheckForStopLift(paymentIdentificationNo);
		if(check == null) {
			return java.util.Collections.<Payment>emptyList();
		}
		List<Payment> ret = new ArrayList<Payment>();
		ret.add(check);
		this.claimant = Claimant.findClaimant(check.getPayTo().getId(), Claimant.ASSOCIATION_ADDRESSES);
		this.selectedPayment = check;
		if(log.isInfoEnabled())
			log.info(String.format("Fetched Payments for stop lift...."));
		return ret;
	}

	public boolean processStopLift(final MessageContext messages) {
		selectedPayment = (Payment) selectedPayment.merge();
		selectedPayment.doStopLift();
		selectedPayment.persist();
		if(log.isInfoEnabled())
			log.info(String.format("Processed stop lift for payment : %s.",selectedPayment.getIdentificatonNo()));
		selectedPayment.flush();
		return true;
	}

	public int getCheckCount() {
		return payments == null ? 0 : payments.size();
	}	
	
	public PaymentDao getPaymentDao() {
		return paymentDao;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public String getPaymentIdentificationNo() {
		return paymentIdentificationNo;
	}

	public void setPaymentIdentificationNo(String paymentIdentificationNo) {
		this.paymentIdentificationNo = paymentIdentificationNo;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
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
}
