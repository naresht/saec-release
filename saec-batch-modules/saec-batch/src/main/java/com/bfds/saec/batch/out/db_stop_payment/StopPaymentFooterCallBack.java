package com.bfds.saec.batch.out.db_stop_payment;

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
 * Note this is common for DB
 */
public class StopPaymentFooterCallBack implements StepExecutionListener,
		FlatFileFooterCallback {

	private String recordType = "T";
	private String recordCount;
	private String totalAmount;
	private String filler = ConverterUtils.getFormattedString1(" ", 56, " ");

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

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

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	private StepExecution stepExecution;

	@Override
	public void writeFooter(Writer writer) throws IOException {
		final BigDecimal totalAmount = (BigDecimal) stepExecution
				.getExecutionContext().get("totalAmount");
		if (totalAmount != null) {

			writer.write(this.recordType
					+ ConverterUtils.getFormattedString(
							String.valueOf(stepExecution.getWriteCount()), 5,
							"0")
					+ ConverterUtils.getFormattedString(totalAmount, 18, "0",
							true) + this.filler);
		} else {
			writer.write(this.recordType + "00000" + "000000000000000000"
					+ this.filler);
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
