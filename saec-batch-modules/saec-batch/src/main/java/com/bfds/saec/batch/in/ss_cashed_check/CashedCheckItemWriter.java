package com.bfds.saec.batch.in.ss_cashed_check;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.in.ss_cashed_check.SsCashedCheckRec;

/**
 * ItemWriter Implementation to read input file and process cashed check items
 * 
 */
public class CashedCheckItemWriter implements ItemWriter<SsCashedCheckRec> {

	final Logger log = LoggerFactory.getLogger(CashedCheckItemWriter.class);

	@Autowired
	private SsCashedCheckBatchService batchService;

	@Override
	public void write(List<? extends SsCashedCheckRec> items)
			throws Exception {

		batchService.processCashedCheck(items);
	}

	@AfterStep
	public void sendNotification() {
		batchService.notifyBank();
	}

}
