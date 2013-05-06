package com.bfds.saec.external.service;

import java.util.List;

import com.bfds.saec.external.service.dto.PaymentDto;

public interface PaymentService {
	
	List<PaymentDto> findPaymentsOfClaimant(String claimantReferenceNo, PaymentSearchFilter filter);

}
