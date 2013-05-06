package com.bfds.saec.batch.out.dsto_check_file;

import java.math.BigDecimal;

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

@Component
public class DstoCheckFileEventTestData extends com.bfds.saec.batch.util.DataGenerator{
	 	
		@Transactional
		public void create() {
			Claimant claimant = newClaimantforDSTO();
			createDSTOEvent();
			Payment c;
	
			c = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
			c.setPaymentType(PaymentType.CHECK);
			c.setIdentificatonNo("1234561444");
			c.setPaymentAmount(new BigDecimal("100.75"));
            c.getPaymentCalc().setBackupWithholding(new BigDecimal("10.75"));
			c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
			c.setDstoCheckFileSentFlag(Boolean.FALSE);
			c.setPayTo(claimant);
			c.setAuditable(Boolean.TRUE);
            PaymentLetterCode code1 = new PaymentLetterCode("c1", "code 1");
			code1.persist();
			c.setLetterCode(code1);
			claimant.addCheck(c);
	
			c = Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
			c.setPaymentType(PaymentType.CHECK);
			c.setIdentificatonNo("1234561555");
			c.setPaymentAmount(new BigDecimal("345.75"));
			c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
			c.setDstoCheckFileSentFlag(Boolean.FALSE);
			c.setPayTo(claimant);
			c.setAuditable(Boolean.TRUE);
            PaymentLetterCode code2 = new PaymentLetterCode("c2", "code 2");
			code2.persist();
			c.setLetterCode(code2);
			claimant.addCheck(c);
			
			c = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
			c.setPaymentType(PaymentType.CHECK);
			c.setIdentificatonNo("1234561666");
			c.setPaymentAmount(new BigDecimal("100.75"));
			c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
			c.setDstoCheckFileSentFlag(Boolean.FALSE);
			c.setPayTo(claimant);
			c.setAuditable(Boolean.TRUE);
            PaymentLetterCode code3 = new PaymentLetterCode("c3", "code 3");
			code3.persist();
			c.setLetterCode(code3);
			claimant.addCheck(c);
			
			c = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
			c.setPaymentType(PaymentType.CHECK);
			c.setPaymentAmount(new BigDecimal("101.75"));
			c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
			c.setDstoCheckFileSentFlag(Boolean.FALSE);
			c.setPayTo(claimant);
			c.setAuditable(Boolean.TRUE);
            PaymentLetterCode code4 = new PaymentLetterCode("c4", "code 4");
			code4.persist();
			c.setLetterCode(code4);
			claimant.addCheck(c);
			
			c = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
			c.setPaymentType(PaymentType.WIRE);
			c.setIdentificatonNo("1234561777");
			c.setPaymentAmount(new BigDecimal("100.75"));
			c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
			c.setDstoCheckFileSentFlag(Boolean.FALSE);
			c.setPayTo(claimant);
			c.setAuditable(Boolean.TRUE);
            PaymentLetterCode code5 = new PaymentLetterCode("c5", "code 5");
			code5.persist();
			c.setLetterCode(code5);
			claimant.addCheck(c);

	        claimant.persist();
	        claimant.flush();
	        claimant.clear();

		}	


		public static Claimant newClaimantforDSTO() {

			Claimant p = new Claimant();
			p.setClaimantRegistration(new ClaimantRegistration());
			p.getClaimantRegistration().setRegistration1("RegistrationName1");
			p.getClaimantRegistration().setRegistration2("RegistrationName2");
			p.getClaimantRegistration().setRegistration3("RegistrationName3");
			p.getClaimantRegistration().setRegistration4("RegistrationName4");
			p.getClaimantRegistration().setRegistration5("RegistrationName5");
			newPrimaryAddressforDSTO(p);
			ClaimantTaxInfo cl = new ClaimantTaxInfo();
			cl.setSsn("112-23-3445");
			p.setTaxInfo(cl);
			p.setBrokerId("5578");
			p.setFundAccountNo("66666");
			p.setReferenceNo("100007");
			return p;
		}


		private static void newPrimaryAddressforDSTO(Claimant p) {
			ClaimantAddress a = new ClaimantAddress();
			a.setAddressType(AddressType.ADDRESS_OF_RECORD);
			a.setAddress1("address 1");
			a.setAddress2("address 2");
			a.setAddress3("address 3");
			a.setAddress4("address 4");
			a.setAddress5("address 5");
			a.setCity("NEWYORK");
			a.setStateCode("NY");
			a.setCountryCode("US");
			a.setZipCode(new ZipCode("23456", "4455"));
			// a.setZipCode(new ZipCode((String) line.get("ZipCode"), null));
			a.setClaimant(p);
			p.addAddress(a);
		}
}
