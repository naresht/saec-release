package com.bfds.saec.domain;

import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Letter.class)
public class LetterDataOnDemand {

    @Autowired
    private ClaimantDataOnDemand claimantDataOnDemand;

    /**
     *
     * Replacing the method from dod to set the claimant.
     */
    public Letter getNewTransientLetter(int index) {
        Letter obj = new Letter();
        setPayToRegistration(obj, index);
        setAddress(obj, index);
        setAddressResearchSentCount(obj, index);
        setAuditable(obj, index);
        // In roo 1.2 the setClaimant() method is not generated !!!
        setClaimant(obj, index);
        setComments(obj, index);
        setCorrespondenceHasAwdObject(obj, index);
        setDescription(obj, index);
        setDstoPrintFileSentFlag(obj, index);
        setGroupMailCode(obj, index);
        setInResponseTo(obj, index);
        setIsSendCountLimit(obj, index);
        setLetterCode(obj, index);
        setLetterCodeString(obj, index);
        setLetterStatus(obj, index);
        setMailDate(obj, index);
        setMailType(obj, index);
        setMailingControlNo(obj, index);
        setRequestType(obj, index);
        setRpoDate(obj, index);
        setRpoForwardableReissueRequired(obj, index);
        setRpoType(obj, index);
        setSpecialPull(obj, index);
        setUserId(obj, index);
        return obj;
    }

    public void setClaimant(Letter obj, int index) {
        Claimant claimant = claimantDataOnDemand.getRandomClaimant();
        obj.setClaimant(claimant);
    }

    public void setPayToRegistration(Letter obj, int index) {
        RegistrationLines embeddedClass = new RegistrationLines();
        setPayToRegistrationRegistration1(embeddedClass, index);
        setPayToRegistrationRegistration2(embeddedClass, index);
        setPayToRegistrationRegistration3(embeddedClass, index);
        setPayToRegistrationRegistration4(embeddedClass, index);
        setPayToRegistrationRegistration5(embeddedClass, index);
        setPayToRegistrationRegistration6(embeddedClass, index);
        obj.setPayToRegistration(embeddedClass);
    }

    public void setPayToRegistrationRegistration1(RegistrationLines obj, int index) {
        String registration1 = "_" + index;
        obj.setRegistration1(registration1);
    }

    public void setPayToRegistrationRegistration2(RegistrationLines obj, int index) {
        String registration2 = "_" + index;
        obj.setRegistration2(registration2);
    }

    public void setPayToRegistrationRegistration3(RegistrationLines obj, int index) {
        String registration3 = "_" + index;
        obj.setRegistration3(registration3);
    }

    public void setPayToRegistrationRegistration4(RegistrationLines obj, int index) {
        String registration4 = "_" + index;
        obj.setRegistration4(registration4);
    }

    public void setPayToRegistrationRegistration5(RegistrationLines obj, int index) {
        String registration5 = "_" + index;
        obj.setRegistration5(registration5);
    }

    public void setPayToRegistrationRegistration6(RegistrationLines obj, int index) {
        String registration6 = "_" + index;
        obj.setRegistration6(registration6);
    }

    public void setLetterCode(Letter obj, int index) {
        LetterCode letterCode = LetterCode.findByCode("007008");
        if(letterCode == null) {
            letterCode = new LetterCode("007008", "007008", LetterType.CLAIM_FORM);
            letterCode.persist();
            letterCode.flush();
        }
        obj.setLetterCode(letterCode);
    }
}
