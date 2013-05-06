package com.bfds.saec.batch.out.dsto_check_file;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import com.bfds.saec.util.ConverterUtils;

public class DSTOCheckFileFooterCallback implements StepExecutionListener,
		FlatFileFooterCallback {

	private String recordType = "999";
	private String recordCount;
	private String totalAmount;
	private String filler = ConverterUtils.getFormattedString1(" ", 2, " ");
	private String totalCheckGross = ConverterUtils.getFormattedString1("0",
			12, "0");
	private String totalCheckFees = ConverterUtils.getFormattedString1("0", 12,
			"0");
	private String totalCheckTax = ConverterUtils.getFormattedString1("0", 12,
			"0");

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

	public String getTotalCheckGross() {
		return totalCheckGross;
	}

	public void setTotalCheckGross(String totalCheckGross) {
		this.totalCheckGross = totalCheckGross;
	}

	public String getTotalCheckFees() {
		return totalCheckFees;
	}

	public void setTotalCheckFees(String totalCheckFees) {
		this.totalCheckFees = totalCheckFees;
	}

	public String getTotalCheckTax() {
		return totalCheckTax;
	}

	public void setTotalCheckTax(String totalCheckTax) {
		this.totalCheckTax = totalCheckTax;
	}

	private StepExecution stepExecution;

	@Override
	public void writeFooter(Writer writer) throws IOException {
		if (stepExecution.getExecutionContext().get("totalAmount") != null) {
			BigDecimal totalAmount = (BigDecimal) stepExecution
					.getExecutionContext().get("totalAmount");
			BigDecimal grossAmount = (BigDecimal) stepExecution
					.getExecutionContext().get("grossAmount");

			writer.write(this.recordType
					+ this.filler
					+ ConverterUtils.getFormattedString(
							String.valueOf(stepExecution.getWriteCount()), 10,
							"0")
					+ ConverterUtils.getFormattedString(totalAmount, 12, "0",true)
					+ ConverterUtils.getFormattedString(grossAmount, 12, "0",true)
					+ this.totalCheckFees + this.totalCheckTax);
		} else {
			writer.write(this.recordType + this.filler
					+ ConverterUtils.getFormattedString("", 10, "0")
					+ ConverterUtils.getFormattedString("", 12, "0")
					+ this.totalCheckGross + this.totalCheckFees
					+ this.totalCheckTax);
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
