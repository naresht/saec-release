package com.bfds.saec.batch.out.foreign_tax_process;

import com.bfds.saec.batch.file.domain.out.damasco_foreign.*;
import com.bfds.saec.domain.Payment;


public interface ForeignTaxProcessOutService {

	ForeignTaxOutRec buildOutboundForeignTaxRecord(Payment payment);
	
}
