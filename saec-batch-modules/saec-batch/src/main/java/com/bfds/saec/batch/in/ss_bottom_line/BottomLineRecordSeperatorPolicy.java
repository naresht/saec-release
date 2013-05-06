package com.bfds.saec.batch.in.ss_bottom_line;

import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;

public class BottomLineRecordSeperatorPolicy implements RecordSeparatorPolicy {

	@Override
	public boolean isEndOfRecord(String line) {
		return line.length() == 800;
	}

	@Override
	public String postProcess(String record) {
		return record;
	}

	@Override
	public String preProcess(String record) {
		return record;
	}

}
