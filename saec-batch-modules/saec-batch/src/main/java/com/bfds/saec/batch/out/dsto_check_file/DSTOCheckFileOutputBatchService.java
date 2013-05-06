package com.bfds.saec.batch.out.dsto_check_file;

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.Payment;

public interface DSTOCheckFileOutputBatchService  {

	DstoCheckFileNotificationDto initdstocheckFileNotification();
	DstoRec generateCheckFile(Payment payment,String runType);
	DstoCheckFileNotificationDto notifydstoCheckFile();
}
