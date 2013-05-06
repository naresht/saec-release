package com.bfds.saec.rip.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bfds.saec.rip.domain.ClaimFormSupportingDocRipObject;
import com.bfds.saec.util.FileUtils;

public class ClaimFormSupportingDocRipObjectTransformerTest {
	
	
	@Test
	public void generateRipData() {
		
		ClaimFormSupportingDocRipObject ripObject = newClaimFormSupportingDocRipObject();
		 ripObject.setCreatedByUser("csr");


        ClaimFormSupportingDocRipObjectTransformer builder = new ClaimFormSupportingDocRipObjectTransformer();
		
		String ret = builder.buildRip(ripObject);
		assertEquals(FileUtils.getFileContents("awd/awd-claim-supporting-doc.dat"), ret);
	}

    private ClaimFormSupportingDocRipObject newClaimFormSupportingDocRipObject() {
        ClaimFormSupportingDocRipObject ripObject = new ClaimFormSupportingDocRipObject();
        ripObject.setHostName("TESTHOST");
        ripObject.setBusinessUnit("TESTBUSINESSUNIT");
        ripObject.setWorkType("WT-1");
        ripObject.setFileExt("PDF");
        ripObject.setFileName("claim-form-1355427361550-9da1fc43-354d-4340-a24e-8deb0ee83272.PDF");
        ripObject.setClaimIdentifier("600000005");
        ripObject.setControlNo("1");
        ripObject.setFilePath("\\\\bfripshare\\dev\\federated\\lob\\claim-form-1355427361550-9da1fc43-354d-4340-a24e-8deb0ee83272.PDF");
        return ripObject;
    }

}
