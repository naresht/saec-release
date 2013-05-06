package com.bfds.saec.rip.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bfds.saec.rip.domain.SpecialhandlingRipObject;
import com.bfds.saec.util.FileUtils;

public class SpecialhandlingRipObjectTransformerTest {
	
	
	@Test
	public void generateRipData() {
		
		SpecialhandlingRipObject ripObject = new  SpecialhandlingRipObject();
			ripObject.setHostName("TESTHOST");
			ripObject.setBusinessUnit("TESTBUSINESSUNIT");
		 	ripObject.setCreatedByUser("csr");
			ripObject.setReferenceNo("10000001");
			ripObject.setRegistration1("r1");
			ripObject.setRegistration2("r2");
			ripObject.setRegistration3("r3");
			ripObject.setRegistration4("r4");
			ripObject.setRegistration5("r5");
			ripObject.setRegistration6("r6");
			ripObject.setAddress1("a1");
			ripObject.setAddress2("a2");
			ripObject.setAddress3("a3");
			ripObject.setAddress4("a4");
			ripObject.setAddress5("a5");
			ripObject.setAddress6("a6");
			ripObject.setStateCode("stateCode");
			ripObject.setCity("city");
			ripObject.setZipCode("zipCode");
			ripObject.setZipExt("zipExt");
			ripObject.setSpecialInstructions("blah blah blah");

		SpecialhandlingRipObjectTransformer builder = new SpecialhandlingRipObjectTransformer();
		String ret = builder.buildRip(ripObject);
		assertEquals(FileUtils.getFileContents("awd/awd-special-handling.dat"), ret);
	}

	
	
	@Test(expected=NullPointerException.class)
	public void generateInvalidRipData_1() { 
		AddressChangeRipObjectTransformer builder = new AddressChangeRipObjectTransformer();
		builder.buildRip(null);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_2() { 
		SpecialhandlingRipObjectTransformer builder = new SpecialhandlingRipObjectTransformer();
		 SpecialhandlingRipObject addressChange = new  SpecialhandlingRipObject();
		builder.buildRip(addressChange);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_3() { 
		SpecialhandlingRipObjectTransformer builder = new SpecialhandlingRipObjectTransformer();
		 SpecialhandlingRipObject ripObject = new  SpecialhandlingRipObject();
		 ripObject.setWorkType("WORK_TYPE");
		builder.buildRip(ripObject);
		
	}
}
