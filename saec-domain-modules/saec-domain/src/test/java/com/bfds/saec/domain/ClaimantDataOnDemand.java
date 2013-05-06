package com.bfds.saec.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.bfds.saec.domain.reference.AddressType;

@RooDataOnDemand(entity = Claimant.class)
public class ClaimantDataOnDemand {
	@Autowired
	ContactDataOnDemand cod;
    public Claimant getNewTransientClaimant(int index) {
        Claimant obj = DataGenerator.newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				DataGenerator.newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
						null, 
						null, null);
        setName(obj, index);
        obj.setTaxInfo(new ClaimantTaxInfo());
        setAccountStatus(obj, index);
        setAccountType(obj, index);
        setAccountTypeOtherDescription(obj, index);
        setAddressResearchSentCount(obj, index);
        setAlternatePayeeResonCode(obj, index);
        setBin(obj, index);
        setBrokerId(obj, index);
        //setClaimantRegistration(obj, index);
        setCorporate(obj, index);
        setCreatedBy(obj, index);
        setCreatedDate(obj, index);
        setDoAudit(obj, index);
        setFundAccountNo(obj, index);
        setIsSendCountLimit(obj, index);
        setMarketTimer(obj, index);
        setOmniBus(obj, index);
        setOrganization(obj, index);
        //setParentClaimant(obj, index);
        setNotEligibleForPayment(obj, index);
        obj.setPrimaryContact(cod.getNewTransientContact(index));
        //setReferenceNo(obj, index);
        setSpecialPull(obj, index);
        return obj;
    }

    public void setName(Claimant obj, int index) {
        Name embeddedClass = new Name();
        setNameFirstName(embeddedClass, index);
        setNameLastName(embeddedClass, index);
        setNameMiddleName(embeddedClass, index);
        obj.setName(embeddedClass);
    }

    public void setNameFirstName(Name obj, int index) {
        String firstName = "firstName_" + index;
        obj.setFirstName(firstName);
    }

    public void setNameLastName(Name obj, int index) {
        String lastName = "lastName_" + index;
        obj.setLastName(lastName);
    }

    public void setNameMiddleName(Name obj, int index) {
        String middleName = "middleName_" + index;
        obj.setMiddleName(middleName);
    }
}
