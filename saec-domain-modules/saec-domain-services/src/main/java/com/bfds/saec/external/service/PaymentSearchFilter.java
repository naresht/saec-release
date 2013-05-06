package com.bfds.saec.external.service;

import org.springframework.roo.addon.tostring.RooToString;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@RooToString
public class PaymentSearchFilter {
	
	private String claimantReferenceNo;
	/**
	 * If true, fetch Payments that can be re-issued. Else fetch all payments.
	 */
	private Boolean reIssuable;

}
