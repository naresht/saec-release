package com.bfds.saec.batch.out.dsto_print_file;

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.Letter;

public interface DSTOPrintFileOutputBatchService  {

	DstoPrintFileNotificationDto initdstoprintFileNotification();

	DstoRec generatePrintFile(Letter letter);
	
	DstoPrintFileNotificationDto notifydstoprintFiles();
}
