package com.bfds.saec.rip.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bfds.saec.rip.domain.AddressChangeRipObject;
import com.bfds.saec.util.FileUtils;

public class AddressChangeRipObjectTransformerTest {
	
	
	@Test
	public void generateRipData() {
		 AddressChangeRipObject addressChange = getDefaultTestAddressChangeRipObject();

		AddressChangeRipObjectTransformer builder = new AddressChangeRipObjectTransformer();
		String ret = builder.buildRip(addressChange);
		assertEquals(FileUtils.getFileContents("awd/address-change.dat"), ret);
	}

	
	
	@Test(expected=NullPointerException.class)
	public void generateInvalidRipData_1() { 
		AddressChangeRipObjectTransformer builder = new AddressChangeRipObjectTransformer();
		builder.buildRip(null);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_2() { 
		AddressChangeRipObjectTransformer builder = new AddressChangeRipObjectTransformer();
		 AddressChangeRipObject addressChange = new  AddressChangeRipObject();
		builder.buildRip(addressChange);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void generateInvalidRipData_3() { 
		AddressChangeRipObjectTransformer builder = new AddressChangeRipObjectTransformer();
		 AddressChangeRipObject addressChange = new  AddressChangeRipObject();
		 addressChange.setWorkType("WORK_TYPE");
		builder.buildRip(addressChange);
		
	}
	
	public static AddressChangeRipObject getDefaultTestAddressChangeRipObject() {
		AddressChangeRipObject addressChange = new  AddressChangeRipObject();
		//addressChange.setWorkType("TADDRSCHNG");
		addressChange.setBusinessUnit("TESTBUSINESSUNIT");
		addressChange.setHostName("TESTHOST");		
		addressChange.setCreatedByUser("csr");
		addressChange.setReferenceNo("10000001");
		addressChange.setFromAddress1("fa1");
		addressChange.setFromAddress2("fa2");
		addressChange.setFromAddress3("fa3");
		addressChange.setFromAddress4("fa4");
		addressChange.setFromAddress5("fa5");
		// Deliberately left out 6.
		addressChange.setFromStateCode("fs");
		addressChange.setFromCity("fc");
		addressChange.setFromCountryCode("US");
		addressChange.setFromZipCode("11111");
		addressChange.setFromZipExt("1111");
		addressChange.setToAddress1("ta1");
		addressChange.setToAddress2("ta2");
		addressChange.setToAddress3("ta3");
		addressChange.setToAddress4("ta4");
		addressChange.setToAddress5("ta5");
		// Deliberately left out 6.
		addressChange.setToStateCode("ts");
		addressChange.setToCity("tc");
		addressChange.setToCountryCode("US");
		addressChange.setToZipCode("22222");
		addressChange.setToZipExt("2222");
		return addressChange;
	}


}
