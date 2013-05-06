package com.bfds.saec.batch.out.db_stop_payment;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

import com.bfds.saec.domain.Event;
import com.bfds.saec.util.ConverterUtils;

public class StopPaymentHeaderCallBack implements FlatFileHeaderCallback {

	private static final String recordType = "H";

	private static final String filler = ConverterUtils.getFormattedString1(
			" ", 63, " ");

	@Override
	public void writeHeader(Writer writer) throws IOException {

		writer.write(StopPaymentHeaderCallBack.recordType
				+ ConverterUtils.getFormattedString(new Date(), "MM/dd/yy")
				+ ConverterUtils.getFormattedString1(Event
						.getCurrentEvent().getDeutscheBankUserId(), 8," ")
				+ StopPaymentHeaderCallBack.filler);
	}
}
