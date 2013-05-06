package com.bfds.saec.payment.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

	final static Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	@Inject
	private PaymentDao dao;
	
	/**
	 * @see {@link PaymentService#reverseStopOrVoidOnPayment(Long)}
	 * 
	 */
	@Override
	@Transactional
	public Payment reverseStopOrVoidOnPayment(final Long paymentId) {
		final Payment payment = Payment.findPayment(paymentId);
		PaymentCode paymentCode = dao.getPreviousPaymentCode(payment.getId(), payment.getPaymentCode());
		if(paymentCode == null) {
			throw new IllegalStateException(String.format("Invalid Check# %s for StopVoidReverse.", payment.getIdentificatonNo()));

		}
		payment.doStopVoidReverse(paymentCode);
		payment.persist();
		if(log.isInfoEnabled()) {
			log.info(String.format("Processed stop void reverse for payment : %s.",payment.getIdentificatonNo()));
		}
		payment.flush();	
		return payment;
	}
	
	/**
	 * @see {@link PaymentService#getCheckForStopVoidReversal(String)}
	 * 
	 */
	@Override
	public Payment getCheckForStopVoidReversal(final String identificationNo) {
		return dao.getCheckForStopVoidReversal(identificationNo);
	}

}
