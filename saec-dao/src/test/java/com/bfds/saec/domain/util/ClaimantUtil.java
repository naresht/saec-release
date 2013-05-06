package com.bfds.saec.domain.util;

import java.util.Date;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.Contact;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;

public class ClaimantUtil {

	public Claimant newClaimant() {
		Claimant p = new Claimant();
		String id = "100";
		p.setFundAccountNo(id+"12345"+id);
		p.setAccountStatus("Open");
		p.setAccountType("Custodial"); // ???
		p.setBrokerId("BRK-"+id+"121212"+id);
		p.setBin("BIN-"+id+"34334"+id);
		p.setOmniBus(Integer.parseInt(id) % 50 == 0);
		p.setSsn("123-123-1234");
		p.setCertificationStatus("Certified"); // E
		p.setCertificationType("W9-US Citizen");// E
		p.setUsCitizen(Boolean.TRUE);
		p.setTaxCountryCode("US");
		p.setCertificationDate(new Date());
		p.setCreatedBy(AccountContext.getCurrentUsername());
		p.setOrganization(Integer.parseInt(id) % 10 == 0);
		p.setCreatedDate(new Date());
		p.setMarketTimer(Integer.parseInt(id) % 5 == 0);
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("John Smith");
		
		newPrimaryAddress(p);
		p.setPrimaryContact(newContact());
		return p;
	}	
	
	public Contact newContact() {
		Contact c = new Contact();
		c.setEmail("john@smith.com");
		c.setPhoneNo("123-123-1234");
		return c;
	}

	public void newPrimaryAddress(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("6 Cambridge Center");
		a.setCity("Cambridge");
		a.setStateCode("MA");
		a.setCountryCode("US");
		a.setZipCode(new ZipCode("02142", null));
		a.setClaimant(p);
		p.addAddress(a);
	}	
}
