package com.bfds.saec.batch.in.db_cashed_check;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.in.db_cashed_check.CashedCheckRec;

/**
 * ItemWriter Implementation to read input file and process paid check items
 * 
 */
public class PaidCheckItemWriter implements ItemWriter<CashedCheckRec> {

	@Autowired
	private DbCashedCheckBatchService batchService;

	@Override
	public void write(List<? extends CashedCheckRec> items) throws Exception {
		batchService.processPaidChecks(items);
	}

}
