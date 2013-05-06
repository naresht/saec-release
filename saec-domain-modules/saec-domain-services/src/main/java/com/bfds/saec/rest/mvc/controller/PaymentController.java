package com.bfds.saec.rest.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.external.service.dto.PaymentDto;

@Controller
@RequestMapping("/payments")
public class PaymentController {
	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
	
	public static final String RES_PAYMENT_NOT_FOUND = "404-Pyment Not found: %s";
	public static final String RES_CANNOT_STOP_PAYMENT = "412-Payment cannot be stopped: %s";
	public static final String RES_REISSUE_REQUESTED = "200-Stop Replace requested:%s ";
	
	@RequestMapping(value = "/{checkNo}/reissue", method = RequestMethod.GET)
	@Transactional
	@ResponseBody
	public String reissueCheck(@PathVariable("checkNo") String checkNo, @RequestParam(value ="src", required=false) String src) {
		if(log.isInfoEnabled()) {
			log.info("Processing check reissue without approval request for Check: " + checkNo);
		}
		Payment payment = Payment.findPaymentIdentificationNo(checkNo);
		if(payment == null) {
			if(log.isInfoEnabled()) {
				log.info("Could not find Check: " + checkNo);
			}
			throw new IllegalStateException(String.format(RES_PAYMENT_NOT_FOUND, checkNo));
		}
		
		if(log.isInfoEnabled()) {
			log.info("Payment Code before: " + payment.getPaymentCode()) ;
		}
		payment.stopReplaceCheckWithoutRip();
		if(log.isInfoEnabled()) {
			log.info("Payment Code after stop/replace: " + payment.getPaymentCode()) ;
		}

		payment = payment.merge() ;
		if(log.isInfoEnabled()) {
			log.info("Payment Code after merge: " + payment.getPaymentCode()) ;
			log.info("Stop Replace requested for check: " + checkNo);
		}
		return String.format(RES_REISSUE_REQUESTED, checkNo);
	}
	
	@RequestMapping(value = "/{checkNo}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
	public PaymentDto getPayment(@PathVariable("checkNo") String checkNo, @RequestParam(value ="src", required=false) String src) {
		Payment payment = Payment.findPaymentIdentificationNo(checkNo);
		if(payment == null) {
			throw new IllegalArgumentException(RES_PAYMENT_NOT_FOUND);
		}
		return PaymentDto.newPaymentDto(payment);
	}
}
