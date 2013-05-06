/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.web.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.ActivityCode;

public interface TaxProcessService extends Serializable {

	List<Payment> getOutboundDomesticTaxPayments(Long claimantId);

	List<OutboundDomesticTaxRec> getAllUnProcessedOutboundDomesticTaxRec();

	boolean saveOutboundDomesticTaxRec(
			OutboundDomesticTaxRec outboundDomesticTaxRec);

	boolean updateOutboundDomesticTaxRec(
			OutboundDomesticTaxRec outboundDomesticTaxRec);

	boolean addActivityForOutboundDomesticTaxRec(
			OutboundDomesticTaxRec outboundDomesticTaxRec,
			ActivityCode activityCode);

    void deleteAll(Collection<OutboundDomesticTaxRec> values);
}