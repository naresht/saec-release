package com.bfds.saec.domain.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.AddressType;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.bfds.saec.domain.Address;

@RooDataOnDemand(entity = AlternatePayeeActivity.class)
public class AlternatePayeeActivityDataOnDemand {

    public AlternatePayeeActivity getNewTransientAlternatePayeeActivity(int index) {
        AlternatePayeeActivity obj = new AlternatePayeeActivity();
        setActivityCode(obj, index);
        setActivityDate(obj, index);
        setActivityTypeDescription(obj, index);
        setAlternatePayeeId(obj, index);
        setClaimant(obj, index);
        setDescription(obj, index);
        setReferenceNo(obj, index);
        setUserId(obj, index);
        setAddress(obj, index);
        return obj;
    }

	public void setResearchChangeActivity(Address obj, int index) {
		AddressResearchChangeActivity researchChangeActivity = new AddressResearchChangeActivity();
		researchChangeActivity.setAddressResearchDate(new Date());
		researchChangeActivity.setAddressResearchDescription("AddressResearch");
		researchChangeActivity.setHit(true);
		researchChangeActivity.setResearchReturned(true);
		researchChangeActivity.setResearchSent(true);
		researchChangeActivity.setStatus("SUCCESS");
		obj.setResearchChangeActivity(researchChangeActivity);

	}

    public void setAddress(AlternatePayeeActivity obj, int index) {
        Address embeddedClass = new Address();
        setAddressAddressType(embeddedClass, index);
        setAddressAddress1(embeddedClass, index);
        setAddressAddress2(embeddedClass, index);
        setAddressAddress3(embeddedClass, index);
        setAddressAddress4(embeddedClass, index);
        setAddressAddress5(embeddedClass, index);
        setAddressAddress6(embeddedClass, index);
        setAddressCity(embeddedClass, index);
        setAddressStateCode(embeddedClass, index);
        setAddressCountryCode(embeddedClass, index);
        setAddressValidFrom(embeddedClass, index);
        setAddressValidTill(embeddedClass, index);
        setAddressPhone(embeddedClass, index);
        setAddressPhoneExt(embeddedClass, index);
        //setAddressZipCode(embeddedClass, index);
        setAddressMailingAddress(embeddedClass, index);
        setAddressAddressResearchCount(embeddedClass, index);
        setAddressRequiresAddressResearch(embeddedClass, index);
        setResearchChangeActivity(embeddedClass, index);
        obj.setAddress(embeddedClass);
    }

    public void setAddressAddressType(Address obj, int index) {
        AddressType addressType = AddressType.class.getEnumConstants()[0];
        obj.setAddressType(addressType);
    }

    public void setAddressAddress1(Address obj, int index) {
        String address1 = "address1_" + index;
        obj.setAddress1(address1);
    }

    public void setAddressAddress2(Address obj, int index) {
        String address2 = "address2_" + index;
        obj.setAddress2(address2);
    }

    public void setAddressAddress3(Address obj, int index) {
        String address3 = "address3_" + index;
        obj.setAddress3(address3);
    }

    public void setAddressAddress4(Address obj, int index) {
        String address4 = "address4_" + index;
        obj.setAddress4(address4);
    }

    public void setAddressAddress5(Address obj, int index) {
        String address5 = "address5_" + index;
        obj.setAddress5(address5);
    }

    public void setAddressAddress6(Address obj, int index) {
        String address6 = "address6_" + index;
        obj.setAddress6(address6);
    }

    public void setAddressCity(Address obj, int index) {
        String city = "city_" + index;
        obj.setCity(city);
    }

    public void setAddressStateCode(Address obj, int index) {
        String stateCode = "stateCode_" + index;
        obj.setStateCode(stateCode);
    }

    public void setAddressCountryCode(Address obj, int index) {
        String countryCode = "countryCode_" + index;
        obj.setCountryCode(countryCode);
    }

    public void setAddressValidFrom(Address obj, int index) {
        Date validFrom = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                                               Calendar.getInstance().get(Calendar.MONTH),
                                               Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                               Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                               Calendar.getInstance().get(Calendar.MINUTE),
                                               Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
                obj.setValidFrom(validFrom);
    }

    public void setAddressValidTill(Address obj, int index) {
        Date validTill = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
                obj.setValidTill(validTill);
    }

    public void setAddressPhone(Address obj, int index) {
        String phone = "phone_" + index;
        obj.setPhone(phone);
    }

    public void setAddressPhoneExt(Address obj, int index) {
        String phoneExt = "phoneExt_" + index;
        obj.setPhoneExt(phoneExt);
    }

    public void setAddressMailingAddress(Address obj, int index) {
        Boolean mailingAddress = true;
        obj.setMailingAddress(mailingAddress);
    }

    public void setAddressAddressResearchCount(Address obj, int index) {
        int addressResearchCount = index;
        obj.setAddressResearchCount(addressResearchCount);
    }

    public void setAddressRequiresAddressResearch(Address obj, int index) {
        Boolean requiresAddressResearch = Boolean.TRUE;
        obj.setRequiresAddressResearch(requiresAddressResearch);
    }

    public void setRegistration(AlternatePayeeActivity obj, int index) {
        RegistrationLines embeddedClass = new RegistrationLines();
        setRegistrationRegistration1(embeddedClass, index);
        setRegistrationRegistration2(embeddedClass, index);
        setRegistrationRegistration3(embeddedClass, index);
        setRegistrationRegistration4(embeddedClass, index);
        setRegistrationRegistration5(embeddedClass, index);
        setRegistrationRegistration6(embeddedClass, index);
        obj.setRegistration(embeddedClass);
    }

    public void setRegistrationRegistration1(RegistrationLines obj, int index) {
        String registration1 = "_" + index;
        obj.setRegistration1(registration1);
    }

    public void setRegistrationRegistration2(RegistrationLines obj, int index) {
        String registration2 = "_" + index;
        obj.setRegistration2(registration2);
    }

    public void setRegistrationRegistration3(RegistrationLines obj, int index) {
        String registration3 = "_" + index;
        obj.setRegistration3(registration3);
    }

    public void setRegistrationRegistration4(RegistrationLines obj, int index) {
        String registration4 = "_" + index;
        obj.setRegistration4(registration4);
    }

    public void setRegistrationRegistration5(RegistrationLines obj, int index) {
        String registration5 = "_" + index;
        obj.setRegistration5(registration5);
    }

    public void setRegistrationRegistration6(RegistrationLines obj, int index) {
        String registration6 = "_" + index;
        obj.setRegistration6(registration6);
    }
}
