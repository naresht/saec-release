package com.bfds.saec.batch.out.ss_bottom_line;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.AddressType;

@Component
public class SsBottomLineOutTestData extends com.bfds.saec.batch.util.DataGenerator {
	  @Transactional
	    public void create() {
		  createEvent();
	      Claimant claimant = newClaimant1();
	      claimant.setReferenceNo("100000013");
          claimant.setFundAccountNo("fund-123");
          newPayment(claimant, "40001", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
          claimant.persist();

          claimant = newClaimant1();
          claimant.setReferenceNo("100000014");
          claimant.setFundAccountNo("fund-123");
          newPayment(claimant, "40002", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
          claimant.persist();

          claimant = newClaimant1();
          claimant.setReferenceNo("100000015");
          claimant.setFundAccountNo("fund-123");
          newPayment(claimant, "40003", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
          claimant.persist();

          claimant = newClaimant1();
          claimant.setReferenceNo("100000016");
          claimant.setFundAccountNo("fund-123");
          newPayment(claimant, "40004", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
          claimant.persist();

          claimant.flush();
	      claimant.clear();
	    }

	    /**
	     * Creates Checks in the void re-issue requested state/status.
	     *
	     * @param claimant The {@link Claimant} to which the checks are assigned.
	     */
	    private void createVoidReissueApprovedChecks(Claimant claimant) {
	        newPayment(claimant, "40001", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);	        
	    }
	    

	    private Payment newPayment(Claimant claimant, String checkNo, PaymentCode paymentCode) {
	        PaymentCalc calc = new PaymentCalc();
	        calc.setNettAmount(new BigDecimal(100.5).setScale(2));
	        calc.setGrossAmount(new BigDecimal(150).setScale(2));
	        calc.setFedWithholding(new BigDecimal(49.5).setScale(2));
	        return newPayment(claimant, checkNo, paymentCode, calc);
	    }


	    private Payment newPayment(Claimant claimant, String checkNo, PaymentCode paymentCode, PaymentCalc paymentCalc) {
	        Payment c = Payment.newPayment(paymentCode);
	        c.setPaymentType(PaymentType.CHECK);
	        c.setIdentificatonNo(checkNo);
	        c.setPaymentCalc(paymentCalc);
	        c.setStatusChangeDate(new Date(112, 10, 03));
	        c.setPayTo(claimant);
	        claimant.addCheck(c);
	        return c;
	    }

	public static Claimant newClaimant1() {

		Claimant p = new Claimant();
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1("RegistrationName1");
		p.getClaimantRegistration().setRegistration2("RegistrationName2");
		p.getClaimantRegistration().setRegistration3("RegistrationName3");
		p.getClaimantRegistration().setRegistration4("RegistrationName4");
		newPrimaryAddress(p);
		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("112-23-3445");
		p.setTaxInfo(cl);
		p.setBrokerId("5578");
		p.setFundAccountNo("66666");
		p.setReferenceNo("100007");
		return p;
	}

	private static void newPrimaryAddress(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("address 1");
		a.setAddress2("address 2");
		a.setAddress3("address 3");
		a.setAddress4("address 4");
		a.setCity("NEWYORK");
		a.setStateCode("NY");
		a.setCountryCode("USLLA");
		a.setZipCode(new ZipCode("23456", null));
		a.setClaimant(p);
		p.addAddress(a);
	}

}
