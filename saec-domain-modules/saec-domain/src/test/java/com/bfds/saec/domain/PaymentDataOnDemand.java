package com.bfds.saec.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.bfds.saec.domain.reference.LetterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;

@RooDataOnDemand(entity = Payment.class)
public class PaymentDataOnDemand {

    @Autowired
    private ClaimantDataOnDemand claimantDataOnDemand;

    /**
     *
     * Replacing the method from dod to set the claimant.
     */
    public Payment getNewTransientPayment(int index) {
        Payment obj = new Payment();
        setPaymentCalc(obj, index);
        setPayToRegistration(obj, index);
        setWireInfo(obj, index);
        setInitialState(obj, index);
        setAddressResearchSentCount(obj, index);
        setAuditable(obj, index);
        setCheckAddress(obj, index);
        setComments(obj, index);
        setDstoCheckFileSentFlag(obj, index);
        setDstoPrintFileSentFlag(obj, index);
        setGroupMailCode(obj, index);
        setIdentificatonNo(obj, index);
        setIfdsSent(obj, index);
        setIsSendCountLimit(obj, index);
        setItemSequenceNumber(obj, index);
        setLetterCode(obj, index);
        setMailingControlNo(obj, index);
        // In roo 1.2 the setPayTo() method is not generated !!!
        setPayTo(obj, index);
        setPaymentCode(obj, index);
        setPaymentDate(obj, index);
        setPaymentRpoFlag(obj, index);
        setPaymentStatus(obj, index);
        setPaymentType(obj, index);
        setPriorRofAmount(obj, index);
        setReissueOf(obj, index);
        setReleaseRejectResponseCode(obj, index);
        setRipEventListener(obj, index);
        setRofHasResidualMonies(obj, index);
        setRofOf(obj, index);
        setRpoDate(obj, index);
        setRpoType(obj, index);
        setSentOnBankIssueVoidFile(obj, index);
        setSentOnBankStopFile(obj, index);
        setSpecialPullCode(obj, index);
        setSplitFromPayment(obj, index);
        setSplitOf(obj, index);
        setStaleByDate(obj, index);
        setStaleDated(obj, index);
        setStatusChangeDate(obj, index);
        setTranch(obj, index);
        setUserId(obj, index);
        return obj;
    }

    public void setPayTo(Payment obj, int index) {
        Claimant payTo = claimantDataOnDemand.getRandomClaimant();
        obj.setPayTo(payTo);
    }

	public void setCheckAddress(Payment obj, int index) {
		ClaimantAddress checkAddress = DataGenerator.newAddress(
				AddressType.SEASONAL_ADDRESS, "2397 Bee Street", "Muskegon",
				"MA", "49470");
		MailObjectAddress addr = new MailObjectAddress();
		checkAddress.copyTo(addr);
		obj.setCheckAddress(addr);
	}

	public void setAddressResearchSentCount(Payment obj, int index) {
		// int addressResearchSentCount = index;
		// obj.setAddressResearchSentCount(addressResearchSentCount);
	}

	public void setWireInfo(Payment obj, int index) {

	}
	
    public void setSplitFromPayment(Payment obj, int index) {
//        Payment splitFromPayment = obj;
//        obj.setSplitFromPayment(splitFromPayment);
    }	
    
    public void setInitialStatePaymentStatus(Payment obj, int index) {
        // no op
    }
    
    public void setInitialState(Payment obj, int index) {
        //No op
    }
    
    public void setInitialStatePaymentCode(PaymentState obj, int index) {
    	//No op
    }
    
    public void setInitialStatePaymentStatus(PaymentState obj, int index) {
    	//No op
    }
    
    public void setInitialStateStatusChangeDate(PaymentState obj, int index) {
    	//No op
    }
    
    public void setPaymentStatus(Payment obj, int index) {
       //noop
    }


    public void setPaymentCalc(Payment obj, int index) {
        PaymentCalc embeddedClass = new PaymentCalc();
        setPaymentCalcPaymentComp1(embeddedClass, index);
        setPaymentCalcPaymentComp2(embeddedClass, index);
        setPaymentCalcPaymentComp3(embeddedClass, index);
        setPaymentCalcPaymentComp4(embeddedClass, index);
        setPaymentCalcPaymentComp5(embeddedClass, index);
        setPaymentCalcPaymentComp6(embeddedClass, index);
        setPaymentCalcGrossAmount(embeddedClass, index);
        setPaymentCalcStateWithholding(embeddedClass, index);
        setPaymentCalcFedWithholding(embeddedClass, index);
        setPaymentCalcNraWithholding(embeddedClass, index);
        setPaymentCalcIntFedWithholding(embeddedClass, index);
        setPaymentCalcIntStateWithholding(embeddedClass, index);
        setPaymentCalcBackupWithholding(embeddedClass, index);
        setPaymentCalcNettAmount(embeddedClass, index);
        obj.setPaymentCalc(embeddedClass);
    }

    public void setPaymentCalcPaymentComp1(PaymentCalc obj, int index) {
        BigDecimal paymentComp1 = BigDecimal.valueOf(index);
        obj.setPaymentComp1(paymentComp1);
    }

    public void setPaymentCalcPaymentComp2(PaymentCalc obj, int index) {
        BigDecimal paymentComp2 = BigDecimal.valueOf(index);
        obj.setPaymentComp2(paymentComp2);
    }

    public void setPaymentCalcPaymentComp3(PaymentCalc obj, int index) {
        BigDecimal paymentComp3 = BigDecimal.valueOf(index);
        obj.setPaymentComp3(paymentComp3);
    }

    public void setPaymentCalcPaymentComp4(PaymentCalc obj, int index) {
        BigDecimal paymentComp4 = BigDecimal.valueOf(index);
        obj.setPaymentComp4(paymentComp4);
    }

    public void setPaymentCalcPaymentComp5(PaymentCalc obj, int index) {
        BigDecimal paymentComp5 = BigDecimal.valueOf(index);
        obj.setPaymentComp5(paymentComp5);
    }

    public void setPaymentCalcPaymentComp6(PaymentCalc obj, int index) {
        BigDecimal paymentComp6 = BigDecimal.valueOf(index);
        obj.setPaymentComp6(paymentComp6);
    }

    public void setPaymentCalcGrossAmount(PaymentCalc obj, int index) {
        BigDecimal grossAmount = BigDecimal.valueOf(index);
        obj.setGrossAmount(grossAmount);
    }

    public void setPaymentCalcStateWithholding(PaymentCalc obj, int index) {
        BigDecimal stateWithholding = BigDecimal.valueOf(index);
        obj.setStateWithholding(stateWithholding);
    }

    public void setPaymentCalcFedWithholding(PaymentCalc obj, int index) {
        BigDecimal fedWithholding = BigDecimal.valueOf(index);
        obj.setFedWithholding(fedWithholding);
    }

    public void setPaymentCalcNraWithholding(PaymentCalc obj, int index) {
        BigDecimal nraWithholding = BigDecimal.valueOf(index);
        obj.setNraWithholding(nraWithholding);
    }

    public void setPaymentCalcIntFedWithholding(PaymentCalc obj, int index) {
        BigDecimal intFedWithholding = BigDecimal.valueOf(index);
        obj.setIntFedWithholding(intFedWithholding);
    }

    public void setPaymentCalcIntStateWithholding(PaymentCalc obj, int index) {
        BigDecimal intStateWithholding = BigDecimal.valueOf(index);
        obj.setIntStateWithholding(intStateWithholding);
    }

    public void setPaymentCalcBackupWithholding(PaymentCalc obj, int index) {
        BigDecimal backupWithholding = BigDecimal.valueOf(index);
        obj.setBackupWithholding(backupWithholding);
    }

    public void setPaymentCalcNettAmount(PaymentCalc obj, int index) {
        BigDecimal nettAmount = BigDecimal.valueOf(index);
        obj.setNettAmount(nettAmount);
    }

    public void setPayToRegistration(Payment obj, int index) {
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

    public void setLetterCode(Payment obj, int index) {
        PaymentLetterCode letterCode = PaymentLetterCode.findByCode("007008");
        if(letterCode == null) {
            letterCode = new PaymentLetterCode("007008", "007008");
            letterCode.persist();
            letterCode.flush();
        }
        obj.setLetterCode(letterCode);
    }
}
