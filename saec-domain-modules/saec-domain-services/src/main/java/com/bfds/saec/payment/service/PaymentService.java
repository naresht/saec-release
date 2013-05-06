package com.bfds.saec.payment.service;

import com.bfds.saec.domain.Payment;


/**
 * 
 * UI Service for Payment processing.
 *
 */
public interface PaymentService {
	
	/**
	 * Find the {@link PaymentService} for the given identificationNo on which a stop or void reversal can be performed.
	 * 
	 * @param identificationNo - The {@link Payment#getIdentificatonNo()} of the Payment.
	 * @return The Payment for the given identificationNo or null if a Payment does not exist or is not eligible for void or stop reversal.
	 */
	Payment getCheckForStopVoidReversal(String paymentIdentificationNo);
	
	/**
	 * Reverse the STOP or VOID operation performed on the payment. 
	 * 
	 * @param paymentId - The id of the {@link Payment}
	 */
	Payment reverseStopOrVoidOnPayment(Long paymentId);

}
