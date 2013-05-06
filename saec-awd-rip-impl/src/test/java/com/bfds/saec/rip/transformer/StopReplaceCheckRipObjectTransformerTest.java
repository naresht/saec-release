package com.bfds.saec.rip.transformer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.bfds.saec.rip.domain.StopReplaceCheckRipObject;

public class StopReplaceCheckRipObjectTransformerTest {
	@Test
	public void generateRipData() {
		StopReplaceCheckRipObject stopReplace = new  StopReplaceCheckRipObject();
		stopReplace.setHostName("TESTHOST");
		stopReplace.setBusinessUnit("TESTBUSINESSUNIT");
		stopReplace.setCreatedByUser("csr");
		stopReplace.setReferenceNo("10000001");
		stopReplace.setPaymentIdentificationNo("12345678");
		stopReplace.setRegistration1("Registration1");
		stopReplace.setRegistration2("Registration2");
		stopReplace.setRegistration6("Registration6");
		stopReplace.setGrossAmount(new BigDecimal(100.25));
		stopReplace.setMailedDate("19/12/2012");
		StopReplaceCheckRipObjectTransformer builder = new StopReplaceCheckRipObjectTransformer();
		String ret = builder.buildRip(stopReplace);
		assertEquals(getFileContents("awd/awd-stop-replace-check.dat"), ret);
	}
	
	@Test(expected=NullPointerException.class)
	public void generateInvalidRipData_1() { 
		AddressChangeRipObjectTransformer builder = new AddressChangeRipObjectTransformer();
		builder.buildRip(null);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_2() { 
		StopReplaceCheckRipObjectTransformer builder = new StopReplaceCheckRipObjectTransformer();
		StopReplaceCheckRipObject addressChange = new  StopReplaceCheckRipObject();
		builder.buildRip(addressChange);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_3() { 
		StopReplaceCheckRipObjectTransformer builder = new StopReplaceCheckRipObjectTransformer();
		StopReplaceCheckRipObject addressChange = new  StopReplaceCheckRipObject();
		 addressChange.setWorkType("WORK_TYPE");
		builder.buildRip(addressChange);
		
	}

	private String getFileContents(final String path) {
		ClassPathResource r = new ClassPathResource(path);
		try {
			return FileUtils.readFileToString(r.getFile());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
