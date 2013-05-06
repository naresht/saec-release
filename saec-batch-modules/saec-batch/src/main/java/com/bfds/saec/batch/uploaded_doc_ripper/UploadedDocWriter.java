package com.bfds.saec.batch.uploaded_doc_ripper;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bfds.saec.rip.domain.ClaimFormSupportingDocRipObject;
import com.bfds.saec.rip.service.RipEventListener;
import com.bfds.wss.domain.ClaimFileUploaded;

public class UploadedDocWriter implements ItemWriter<ClaimFileUploaded> {
	
	private static final Logger log = LoggerFactory.getLogger(UploadedDocWriter.class);
	@Value("${awd.out.dir}")
	private String awdOutDir;
	
    @Autowired
    private RipEventListener ripEventListener;

    @Autowired
    private UploadedDocService uploadedDocService;

	@Override
	public void write(List<? extends ClaimFileUploaded> items) throws Exception {
        uploadedDocService.generateAwdRip(items);
	}


}
