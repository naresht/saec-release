package com.bfds.saec.batch.out.dsto_print_file;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import com.bfds.saec.util.ConverterUtils;

public class DSTOPrintFileFooterCallback implements StepExecutionListener,
		FlatFileFooterCallback {

	private String recordType = "999";
	private String recordCount;
	private String totalAmount;
	private String filler = ConverterUtils.getFormattedString1(" ", 2, " ");
	private String totalCheckGross = ConverterUtils.getFormattedString1(" ",
			12, " ");
	private String totalCheckFees = ConverterUtils.getFormattedString1(" ", 12,
			" ");
	private String totalCheckTax = ConverterUtils.getFormattedString1(" ", 12,
			" ");

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
		if (stepExecution != null) {
			writer.write(this.recordType
					+ this.filler
					+ ConverterUtils.getFormattedString(
							String.valueOf(stepExecution.getWriteCount()), 10,
							"0")
					+ ConverterUtils.getFormattedString("", 12, "0")
					+ ConverterUtils.getFormattedString("", 12, "0")
					+ ConverterUtils.getFormattedString("", 12, "0")
					+ ConverterUtils.getFormattedString("", 12, "0"));
		} else {
			writer.write(this.recordType + this.filler
					+ ConverterUtils.getFormattedString("", 10, "0")
					+ ConverterUtils.getFormattedString("", 12, "0")
					+ ConverterUtils.getFormattedString("", 12, "0")
					+ ConverterUtils.getFormattedString("", 12, "0")
					+ ConverterUtils.getFormattedString("", 12, "0"));
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
