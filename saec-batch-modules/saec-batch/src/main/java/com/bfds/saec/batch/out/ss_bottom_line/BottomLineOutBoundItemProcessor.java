package com.bfds.saec.batch.out.ss_bottom_line;

import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.file.domain.out.ss_bottomline.SsBottomLineOutRec;
import com.bfds.saec.domain.Payment;

public class BottomLineOutBoundItemProcessor implements
		ItemProcessor<Payment, SsBottomLineOutRec> {

	@Autowired
	private BottomLineOutBoundBatchService batchService;

	@Override
	public SsBottomLineOutRec process(Payment check) throws Exception {
		return batchService.issueBottomLine(check);
	}

	@BeforeStep
	public BottomLineOutBoundNotificationDto initOBBottomLineNotification() {
		return batchService.initOBBottomLineOutNotification();
	}
	
	@AfterStep
	public void sendNotification() {
		batchService.notifySsBottomLineOut();
	}	

}
