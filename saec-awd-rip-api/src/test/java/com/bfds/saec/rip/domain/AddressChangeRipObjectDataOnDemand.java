package com.bfds.saec.rip.domain;

import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity=AddressChangeRipObject.class)
public class AddressChangeRipObjectDataOnDemand {
	
    public void setStatus(AddressChangeRipObject obj, int index) {
    	obj.setStatus(RipObjectStatus.NOT_SENT);
    }
    
    public static AddressChangeRipObject getDefaultTestAddressChangeRipObject() {
		AddressChangeRipObject addressChange = new  AddressChangeRipObject();
		//addressChange.setWorkType("TADDRSCHNG");
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
