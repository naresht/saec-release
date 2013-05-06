package com.bfds.saec.batch.out.ss_stop_payment;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

import com.bfds.saec.util.ConverterUtils;

/**
 * TODO
 */
public class SSStopPaymentHeaderCallBack implements FlatFileHeaderCallback {

	private String filler1 = ConverterUtils.getFormattedString(" ", 3, " ");
	private String zeroes = "0000000000";
	private String filler2 = " ";
	private String date = ConverterUtils.getFormattedString(new Date(),
			"MM/dd/yyyy");
	private String filler3 = ConverterUtils.getFormattedString(" ", 56, " ");

	public String getFiller1() {
		return filler1;
	}

	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}

	public String getZeroes() {
		return zeroes;
	}

	public void setZeroes(String zeroes) {
		this.zeroes = zeroes;
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFiller3() {
		return filler3;
	}

	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}

	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write(this.filler1 + this.zeroes + this.filler2 + this.date
				+ this.filler3);
	}
}
