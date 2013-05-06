package com.bfds.saec.batch.in.db_stop_confirmation;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.in.db_stop_confirmation.StopConfirmationRec;

/**
 * ItemWriter Implementation to read input file and process stop confirmation
 * items
 * 
 */
public class StopConfirmationItemWriter implements
		ItemWriter<StopConfirmationRec> {

	@Autowired
	private StopConfirmationBatchService batchService;

	@Override
	public void write(List<? extends StopConfirmationRec> items)
			throws Exception {
		batchService.processStopConfirmations(items);
	}
}
