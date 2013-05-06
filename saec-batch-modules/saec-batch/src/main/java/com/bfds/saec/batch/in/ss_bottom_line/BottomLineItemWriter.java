package com.bfds.saec.batch.in.ss_bottom_line;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.in.ss_bottom_line.SsBottomLineInRec;


/**
 * ItemWriter Implementation to read input file and process Bottom Line check
 * items
 * 
 */
public class BottomLineItemWriter implements ItemWriter<SsBottomLineInRec> {

	final Logger log = LoggerFactory.getLogger(BottomLineItemWriter.class);

	@Autowired
	private BLInboundBatchService blIbBatchService;

	@Override
	public void write(List<? extends SsBottomLineInRec> items) throws Exception {
		blIbBatchService.processBLInboundCheck(items);
	}
	
	@AfterStep
	public void sendNotification() {
		blIbBatchService.notificationMail();
	}

}
