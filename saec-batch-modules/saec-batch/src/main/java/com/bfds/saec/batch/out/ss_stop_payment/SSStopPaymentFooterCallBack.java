package com.bfds.saec.batch.out.ss_stop_payment;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import com.bfds.saec.util.ConverterUtils;

/**
 * TODO
 * 
 * Note this is common for SS
 */
public class SSStopPaymentFooterCallBack implements StepExecutionListener,
		FlatFileFooterCallback {

	private String filler1 = "  ";
	private String zeroes1 = "00";
	private String nines = "99999999";
	private String eights = "88888888";
	private String filler2 = "  ";
	private String recordCount;
	private String totalAmount;
	private String filler3 = ConverterUtils.getFormattedString1(" ", 34, " ");

	public String getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getFiller1() {
		return filler1;
	}

	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}

	public String getZeroes1() {
		return zeroes1;
	}

	public void setZeroes1(String zeroes1) {
		this.zeroes1 = zeroes1;
	}

	public String getNines() {
		return nines;
	}

	public void setNines(String nines) {
		this.nines = nines;
	}

	public String getEights() {
		return eights;
	}

	public void setEights(String eights) {
		this.eights = eights;
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}

	public String getFiller3() {
		return filler3;
	}

	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}

	private StepExecution stepExecution;

	@Override
	public void writeFooter(Writer writer) throws IOException {

		BigDecimal totalAmount = (BigDecimal) stepExecution
				.getExecutionContext().get("totalAmount");
		if (totalAmount != null) {

			writer.write(this.filler1
					+ this.zeroes1
					+ this.nines
					+ this.eights
					+ this.filler2
					+ ConverterUtils.getFormattedString(
							String.valueOf(stepExecution.getWriteCount()), 12,
							"0")
					+ ConverterUtils.getFormattedString(totalAmount, 12, "0",
							true) + this.filler3);
		} else {
			writer.write(this.filler1 + this.zeroes1 + this.nines + this.eights
					+ this.filler2 + "000000000000" + "000000000000"
					+ this.filler3);
		}

	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

}
