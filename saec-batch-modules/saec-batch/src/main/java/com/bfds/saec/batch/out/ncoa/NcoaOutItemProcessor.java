package com.bfds.saec.batch.out.ncoa;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.ncoa_outbound.NCOAOutboundRec;
import com.bfds.saec.domain.Claimant;

public class NcoaOutItemProcessor implements
		ItemProcessor<Claimant, NCOAOutboundRec> {

	@Autowired
	private NcoaOutBatchService batchService;

	@Override
	public NCOAOutboundRec process(Claimant claimant) throws Exception {
		return batchService.createNcoaAddressResearch(claimant);
	}

}

