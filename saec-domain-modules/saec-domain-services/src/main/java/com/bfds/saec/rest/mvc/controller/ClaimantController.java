package com.bfds.saec.rest.mvc.controller;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.external.service.PaymentSearchFilter;
import com.bfds.saec.external.service.PaymentService;
import com.bfds.saec.external.service.dto.PaymentDto;

@Controller
@RequestMapping("/claimants")
public class ClaimantController {
	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
	
	public static final String CLAIMANT_FOUND = "true";
	
	public static final String CLAIMANT_NOT_FOUND = "false";
	
	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping(value = "/{referenceNo}/payments", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<PaymentDto> getPaymentsOfClaimant(@PathVariable("referenceNo") String referenceNo, 			
			                                      @RequestParam(value ="canReissue", required=false) boolean canReissue,
			                                      @RequestParam(value ="src", required=false) String src) {
		if(log.isInfoEnabled()) {
			log.info("Fetching payments for Claimant: " + referenceNo);
		}
		PaymentSearchFilter filter = new PaymentSearchFilter();
		filter.setReIssuable(canReissue);
		return paymentService.findPaymentsOfClaimant(referenceNo, filter);
	}

	@RequestMapping(value = "/{referenceNo}/validate", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String validateClaimantByReferenceNumber(@PathVariable("referenceNo") String referenceNo) {
		if(log.isInfoEnabled()) {
			log.info("Fetching Claimant: " + referenceNo);
		}
		
		Claimant claimant = Claimant.findClaimantsByReferenceNo(referenceNo).getSingleResult();
		if (claimant != null && claimant.getId() != null) {
			if(log.isInfoEnabled()) {
				log.info("Found claimant: " + referenceNo) ;
			}
			return CLAIMANT_FOUND ;
		} else {
			if(log.isInfoEnabled()) {
				log.info("Not Found claimant: " + referenceNo) ;
			}
			return CLAIMANT_NOT_FOUND ;
		}
	}
	
	@RequestMapping(value = "/{referenceNo}/payments/last", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public PaymentDto getLastPaymentOfClaimant(@PathVariable("referenceNo") String referenceNo,
			                                      @RequestParam(value ="src", required=false) String src) {
		if(log.isInfoEnabled()) {
			log.info("Fetching last payment for Claimant: " + referenceNo);
		}
		List<PaymentDto> allPayments =  paymentService.findPaymentsOfClaimant(referenceNo, new PaymentSearchFilter());
		if(allPayments.size() == 0) {
			return null;
		}
		if(allPayments.size() == 1) {
			return allPayments.get(0);
		}
		
		java.util.Collections.sort(allPayments, new Comparator<PaymentDto>() {

			@Override
			public int compare(PaymentDto o1, PaymentDto o2) {
				if(o1.getPaymentDate() == null || o2.getPaymentDate() == null) {
					throw new IllegalStateException("Cannot determine last payment. One or more of the payments have a null payment data.");
				}
				return 0 - o1.getPaymentDate().compareTo(o2.getPaymentDate());
			}
		});
		
		return allPayments.get(0);
	}
		
}
