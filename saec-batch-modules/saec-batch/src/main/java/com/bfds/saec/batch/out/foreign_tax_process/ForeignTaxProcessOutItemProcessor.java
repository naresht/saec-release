package com.bfds.saec.batch.out.foreign_tax_process;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.damasco_foreign.*;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;

public class ForeignTaxProcessOutItemProcessor implements ItemProcessor<Payment, ForeignTaxOutRec> {
	
	@Autowired
	private ForeignTaxProcessOutService foreignTaxProcessOutService;

	@Override
	public ForeignTaxOutRec process(Payment payment) throws Exception {
		return foreignTaxProcessOutService.buildOutboundForeignTaxRecord(payment);
	}

}
