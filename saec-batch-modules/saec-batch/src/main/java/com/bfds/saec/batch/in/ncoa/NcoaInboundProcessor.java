package com.bfds.saec.batch.in.ncoa;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.in.ncoa_link_record.NCOALinkRec;

public class NcoaInboundProcessor implements ItemWriter<NCOALinkRec> {

	@Autowired
	private NcoaInboundBatchService batchService;

	@Override
	public void write(List<? extends NCOALinkRec> items) throws Exception {
		batchService.ProcessNcoaInboundRecords(items);
	}

}
