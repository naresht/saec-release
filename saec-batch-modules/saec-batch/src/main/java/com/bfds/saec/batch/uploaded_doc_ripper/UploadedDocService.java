package com.bfds.saec.batch.uploaded_doc_ripper;


import com.bfds.wss.domain.ClaimFileUploaded;

import java.util.List;

/**
 * A service interface for processing {@link com.bfds.wss.domain.ClaimFileUploaded} objects.
 */
public interface UploadedDocService {

    void generateAwdRip(List<? extends ClaimFileUploaded> claimFileUploadedList);
}
