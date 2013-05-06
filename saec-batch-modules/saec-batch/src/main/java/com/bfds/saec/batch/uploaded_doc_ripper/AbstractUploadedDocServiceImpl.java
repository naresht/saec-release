package com.bfds.saec.batch.uploaded_doc_ripper;


import com.bfds.saec.rip.domain.ClaimFormSupportingDocRipObject;
import com.bfds.saec.rip.service.RipEventListener;
import com.bfds.wss.domain.ClaimFileUploaded;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public abstract class AbstractUploadedDocServiceImpl implements UploadedDocService {

    protected static final Logger log = LoggerFactory.getLogger(AbstractUploadedDocServiceImpl.class);

    @Value("${awd.out.dir}")
    private String awdOutDir;

    @Autowired
    private transient RipEventListener ripEventListener;

    @Override
    public void generateAwdRip(List<? extends ClaimFileUploaded> claimFileUploadedList) {
        preProcess(claimFileUploadedList);
        for(ClaimFileUploaded claimFileUploaded : claimFileUploadedList) {
            claimFileUploaded = claimFileUploaded.merge();
            ClaimFormSupportingDocRipObject ripObject =  process(claimFileUploaded);
            if(log.isInfoEnabled()) {
                log.info(String.format("Sending RIP request : %s", ripObject));
            }
            ripEventListener.sendClaimFormSupportingDocument(ripObject);
        }
        postProcess(claimFileUploadedList);
    }

    protected abstract void  preProcess(List<? extends ClaimFileUploaded> claimFileUploadedList);

    protected abstract void  postProcess(List<? extends ClaimFileUploaded> claimFileUploadedList);

    protected abstract void  preProcess(ClaimFileUploaded claimFileUploaded);

    protected abstract void  postProcess(ClaimFileUploaded claimFileUploaded);


    protected ClaimFormSupportingDocRipObject process(final ClaimFileUploaded claimFileUploaded)  {
        if(log.isInfoEnabled()) {
            log.info(String.format("Pocesssing uploaded file : %s, of claim : %s", claimFileUploaded.getUploadedFileName(), claimFileUploaded.getClaimantClaim().getClaimIdentifier()));
        }
        File binaryFile = new File(getNewBinaryFileName(claimFileUploaded));

        try {
            FileUtils.writeByteArrayToFile(binaryFile, claimFileUploaded.getFileData().getData());
        } catch (IOException e) {
            log.error(String.format("Error creating binary file : %s, of claim : %s", binaryFile.getAbsolutePath(), claimFileUploaded.getClaimantClaim().getClaimIdentifier()));
        }

        if(log.isInfoEnabled()) {
            log.info(String.format("Creaded binary file : %s, for AWD RIP.", binaryFile.getAbsolutePath()));
        }

        claimFileUploaded.setAbsoluteFileNameInRip(binaryFile.getAbsolutePath());

        claimFileUploaded.setDateRipped(new Date());

        final ClaimFormSupportingDocRipObject ripObject = new ClaimFormSupportingDocRipObject();
        ripObject.setCreatedByUser("uploadedDocRipperJob");

        ripObject.setWorkType(getWorkTypeForClaimForm(claimFileUploaded));

        ripObject.setFileExt(getFileExt(binaryFile.getName()));
        ripObject.setFileName(binaryFile.getName());
        ripObject.setFilePath(binaryFile.getAbsolutePath());
        ripObject.setClaimIdentifier(claimFileUploaded.getClaimantClaim().getClaimIdentifier());
        ripObject.setControlNo(String.valueOf(claimFileUploaded.getClaimantClaim().getControlNumber()));
        return ripObject;
    }

    protected abstract String getWorkTypeForClaimForm(ClaimFileUploaded claimFileUploaded);

    private String getNewBinaryFileName(ClaimFileUploaded  claimFileUploaded) {
        String rootDirName = new File(awdOutDir).getAbsolutePath();
        if(!rootDirName.endsWith(File.separator)) {
            rootDirName = rootDirName + File.separator;
        }
        rootDirName = rootDirName + "lob" + File.separator;

        if("NA".equals(claimFileUploaded.getAbsoluteFileName())) {
            return rootDirName +  getUniqueFileName(claimFileUploaded.getUploadedFileName());
        } else {
            return rootDirName +  (new File(claimFileUploaded.getAbsoluteFileName())).getName();
        }
    }

    private String getUniqueFileName(String originalFileName) {
        String ext = getFileExt(originalFileName);
        if(ext != null) {
            originalFileName = originalFileName.substring(0, originalFileName.length() - ext.length() - 1);
        }
        StringBuilder sb =  new StringBuilder()
                .append(originalFileName)
                .append("-")
                .append(new java.util.Date().getTime())
                .append("-")
                .append(java.util.UUID.randomUUID());
        if(ext != null) {
            sb.append(".").append(ext);
        }
        return sb.toString();
    }



    private String getFileExt(String fileName) {
        int index = fileName.indexOf('.');
        if(index > 0 && index < fileName.length() - 1) {
            return fileName.substring(index + 1).toUpperCase();
        }
        return null;
    }
}
