package com.bfds.saec.batch.in.ncoa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;

@Component
public class NcoaInboundEventTestData extends
		com.bfds.saec.batch.util.DataGenerator {
	@Transactional
	public void create() {
		createEvent();
		Claimant claimant = newClaimant();
		claimant=newSecondaryAddress(claimant);
		claimant.setReferenceNo("100000001");
		claimant.persist();
		
		Claimant claimant2 = newClaimant();
		claimant2=newSecondaryAddress(claimant2);
		claimant2.setReferenceNo("100000002");
		claimant2.persist();
	}

	private Claimant newSecondaryAddress(Claimant claimant) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.SEASONAL_ADDRESS);
		a.setAddress1("address 2");
		a.setCity("NEWYORK1");
		a.setStateCode("NYc");
		a.setCountryCode("US");
		a.setMailingAddress(true);
		a.setZipCode(new ZipCode("34454", "3446"));
		a.setClaimant(claimant);
		claimant.addAddress(a);
		return claimant;
	}

}
