package com.bfds.saec.batch.out.ncoa;

import com.bfds.saec.batch.file.domain.out.ncoa_outbound.NCOAOutboundRec;
import com.bfds.saec.domain.Claimant;

public interface NcoaOutBatchService {

	NCOAOutboundRec createNcoaAddressResearch(Claimant claimant);
	
}
