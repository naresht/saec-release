package com.bfds.saec.rip.transformer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.bfds.saec.rip.domain.CureLetterRipObject;
import com.bfds.saec.rip.transformer.CureLetterRipObjectTransformer;

public class CureLetterRipObjectTransformerTest {
	
	
	@Test
	public void generateRipData() {
		 CureLetterRipObject cureLetter = new  CureLetterRipObject();
		//cureLetter.setWorkType("TADDRSCHNG");
		 	cureLetter.setBusinessUnit("TESTBUSINESSUNIT");
		 	cureLetter.setHostName("TESTHOST");
			cureLetter.setCreatedByUser("csr");
			cureLetter.setReferenceNo("10000001");
			cureLetter.setRegistration1("r1");
			cureLetter.setRegistration2("r2");
			cureLetter.setRegistration3("r3");
			cureLetter.setRegistration4("r4");
			cureLetter.setRegistration5("r5");
			cureLetter.setRegistration6("r6");
			cureLetter.setAddress1("a1");
			cureLetter.setAddress2("a2");
			cureLetter.setAddress3("a3");
			cureLetter.setAddress4("a4");
			cureLetter.setAddress5("a5");
			cureLetter.setAddress6("a6");
			cureLetter.setStateCode("stateCode");
			cureLetter.setCity("city");
			cureLetter.setZipCode("zipCode");
			cureLetter.setZipExt("zipExt");
			cureLetter.setSpecialInstructions("blah blah blah");
			cureLetter.setLetterCode("101");
			cureLetter.setWorkType("TLETTER");

		CureLetterRipObjectTransformer builder = new CureLetterRipObjectTransformer();
		String ret = builder.buildRip(cureLetter);
		assertEquals(getFileContents("awd/awd-cure-letter.dat"), ret);
	}
	
	@Test(expected=NullPointerException.class)
	public void generateInvalidRipData_1() { 
		CureLetterRipObjectTransformer builder = new CureLetterRipObjectTransformer();
		builder.buildRip(null);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_2() { 
		CureLetterRipObjectTransformer builder = new CureLetterRipObjectTransformer();
		 CureLetterRipObject cureLetter = new  CureLetterRipObject();
		builder.buildRip(cureLetter);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_3() { 
		CureLetterRipObjectTransformer builder = new CureLetterRipObjectTransformer();
		 CureLetterRipObject cureLetter = new  CureLetterRipObject();
		 cureLetter.setWorkType("WORK_TYPE");
		builder.buildRip(cureLetter);
		
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
