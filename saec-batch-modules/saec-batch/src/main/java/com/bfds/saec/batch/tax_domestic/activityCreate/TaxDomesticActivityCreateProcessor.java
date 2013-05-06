package com.bfds.saec.batch.tax_domestic.activityCreate;

import org.springframework.batch.item.ItemProcessor;

import com.bfds.saec.batch.file.domain.out.damasco_domestic.OutboundDomesticTaxRec;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.Activity;
import com.google.common.base.Preconditions;

public class TaxDomesticActivityCreateProcessor implements
		ItemProcessor<OutboundDomesticTaxRec, OutboundDomesticTaxRec> {
	
	
	/*
	 *                                                    
	 * Each Out bound Tax record must create a activity record for claimant. 
	 *
	 */
	@Override
	public OutboundDomesticTaxRec process(OutboundDomesticTaxRec item)
			throws Exception {

		Preconditions.checkNotNull(item, "rec is null");
		Preconditions.checkNotNull(item.getCheckNumber(), "check# is null");
		final Payment payment = Payment.findPaymentIdentificationNo(item
				.getCheckNumber());
		final Claimant claimant = payment.getPayTo();
		Preconditions.checkState(claimant != null,
				"No Claimant for check#: %s", item.getCheckNumber());

		Activity activity = new Activity();
		Activity.setActivityDefaults(activity);
		activity.setActivityDate((item.getDateSent()));
		activity.setUserId("Damasco");
		activity.setDescription("File to Damasco");
		claimant.addActivity(activity);
		return item;
	}

}
