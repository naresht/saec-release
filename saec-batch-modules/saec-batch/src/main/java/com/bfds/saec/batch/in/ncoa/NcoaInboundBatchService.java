package com.bfds.saec.batch.in.ncoa;

import java.util.List;

import com.bfds.saec.batch.file.domain.in.db_cashed_check.CashedCheckRec;
import com.bfds.saec.batch.file.domain.in.ncoa_link_record.NCOALinkRec;

public interface NcoaInboundBatchService {

	void ProcessNcoaInboundRecords(List<? extends NCOALinkRec> items);
	
}
