package com.bfds.saec.batch.out.dsto_print_file;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

import com.bfds.saec.util.ConverterUtils;

public class DSTOPrintFileHeaderCallback implements FlatFileHeaderCallback,
		StepExecutionListener {

	private StepExecution stepExecution;

	@Override
	public void beforeStep(final StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

	private String recordType = ConverterUtils.getFormattedString1("010", 3, " ");
	private String date = ConverterUtils.getFormattedString(new Date(),"MMddyyyy"); 
	private String bankName;
	private String filler = ConverterUtils.getFormattedString1(" ", 2, " ");

	@Override
	public void writeHeader(Writer writer) throws IOException {
		String BankName = (String) stepExecution.getExecutionContext().get("bankName");
		bankName = ConverterUtils.getFormattedString1(BankName, 40, " ");
		writer.write(this.recordType + this.filler + bankName + this.date);
	}

}
