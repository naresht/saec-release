package com.bfds.saec.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.bfds.saec.domain.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.ProcessError.ErrorSeverity;
import com.bfds.saec.domain.ProcessError.ErrorType;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.ProcessName;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantClaimId;
import com.bfds.wss.domain.reference.ClaimStatus;

public class DataGenerator {
	
	
	 
	public static void generateClaimantDaoImplTestData() {
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
						newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
								"New York", "NY", "4470"), 
				newCheck("1000010", 100),
				paymentWithGroupMailCode());
		claimant.setMailingAddressByType(AddressType.SEASONAL_ADDRESS);
		claimant.persist();
		
		ClaimantClaimId claimantClaimId = new ClaimantClaimId();
		claimantClaimId.setClaimIdentifier("60000001");
		claimantClaimId.setControlNumber(1);
		claimantClaimId.setClaimant(claimant);
		claimantClaimId.persist();
		
		claimant = newClaimant(
				"Edward",
				"Waldrop",
				"100002",
				"200002",
				"30002",
				"400002",
				"50002",
				"60002",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "560 Mount Street",
						"Saginaw", "MI", "48607"), null,
				newCheck("2000010", 100), newCheck("2000020", 200));
		claimant.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
	
		
		claimant.persist();
		
		claimantClaimId = new ClaimantClaimId();
		claimantClaimId.setClaimIdentifier("60000003");
		claimantClaimId.setControlNumber(1);
		claimantClaimId.setClaimant(claimant);
		claimantClaimId.persist();
		
		
		claimant = newClaimant(
				"david",
				"Gonzalez",
				"100003",
				"200003",
				"30003",
				"400003",
				"50003",
				"60003",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "671 Ben Street",
						"Cobleskill", "NY", "12043"), null,
				newCheck("3000010", 100), newCheck("3000020", 200));
		claimant.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
		claimant.persist();
		
		ClaimantClaim claim = new ClaimantClaim();
		claim.setClaimIdentifier("60000002");
		claim.setDateFiled(new Date());
		claim.setClaimant(claimant);
		claim.setStatus(ClaimStatus.PENDING);
		claim.persist();
		
		
		claimant = newClaimant(
				"narayan",
				"Dasari",
				"100004",
				"200004",
				"30004",
				"400004",
				"50004",
				"60004",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "671 Ben Street",
						"Cobleskill", "NY", "12043"), null,
				newCheck("4000010", 100), newCheck("3000021", 300));
		claimant.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
		claimant.persist();
		
		claim = new ClaimantClaim();
		claim.setClaimIdentifier("60000004");
		claim.setDateFiled(new Date());
		claim.setClaimant(claimant);
		claim.setStatus(ClaimStatus.PENDING);
		claim.persist();
		
	}

	public static void generatePaymentDaoImplTestData() {
		PaymentLetterCode code3 = new PaymentLetterCode("c3", "code 3");
		code3.persist();
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
				newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), newCheck("1000010", 100),
				newCheck("1000021", 200));
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("100001");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		c.setIdentificatonNo("100002");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setLetterCode(code3);
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		c.setIdentificatonNo("100003");
		c.setPaymentAmount(new BigDecimal(100));
		c.setStatusChangeDate(new Date(111, 7, 10));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		c.setIdentificatonNo("100004");
		c.setPaymentAmount(new BigDecimal(300));
		c.setStatusChangeDate(new Date(101, 7, 14));
		c.setPayTo(claimant);
		claimant.addCheck(c);

        PaymentLetterCode code4 = new PaymentLetterCode("c4", "code 4");
		code4.persist();
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.WIRE);
		c.setSpecialPullCode("SP-1");
		c.setLetterCode(code4);
		c.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
		// c.setIdentificatonNo(String.valueOf(25 + 20 * counter));
		c.setPaymentAmount(new BigDecimal(550));
		c.setStatusChangeDate(new Date(101, 7, 14));
		c.setPayTo(claimant);
		claimant.addCheck(c);

        PaymentLetterCode code5 = new PaymentLetterCode("c5", "code 5");
		code5.persist();
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		// c.setPaymentType(PaymentType.WIRE);
		c.setSpecialPullCode("SP-1");
		c.setLetterCode(code5);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		// c.setIdentificatonNo(String.valueOf(25 + 20 * counter));
		c.setPaymentAmount(new BigDecimal(600));
		c.setStatusChangeDate(new Date(101, 7, 14));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		claimant.persist();

	}
	
	public static void generatePaymentDaoImplWireData() {
		LetterCode code3 = new LetterCode("c30", "code 30", LetterType.CLAIM_FORM);
		code3.persist();
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
				newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), newCheck("1000010", 100),
				newCheck("1000021", 200));
		
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		
		c.setIdentificatonNo("100001");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		claimant.persist();
		
		Payment wire = c.reissueCheckAsWire(new java.math.BigDecimal(10));
		wire.setReissueOf(c);
		if (wire == c) {
			c = savePayment(wire);
		} else {
			wire.setPayTo(c.entityManager().getReference(Claimant.class, wire
					.getPayTo().getId()));
			wire.persist();
			c = savePayment(c);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	private static Payment savePayment(Payment c_) {
		Claimant claimant = Claimant.findClaimant(c_.getPayTo().getId(),
				Claimant.ASSOCIATION_ADDRESSES);
		return (Payment) c_.merge();
	}

	
	public static void generateTranchDaoImplTestData() {
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
				newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), newCheck("1000010", 100),
				newCheck("1000021", 200));
		claimant.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
		claimant.persist();
		
		claimant = newClaimant(
				"Nicholas",
				"Barrington",
				"100001",
				"200001",
				"30002",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MA", "49470"),
				newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), newCheck("2000010", 110),
				newCheck("2000021", 220));
		claimant.setMailingAddressByType(AddressType.ADDRESS_OF_RECORD);
		claimant.persist();

	}
	
	/**
	 * Added one extra check to test GitHub Issue #729,with Payment_Status:Stop_Confirmed
	 */
	public static void generateCheckForStopLiftData() {
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
				newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), newCheck("1000010", 100),
				newCheck("1002221", 200));
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setIdentificatonNo("11111111");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c=Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.STOP_STOP_CONFIRMED_S_SC);
		c.setIdentificatonNo("11111112");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c=Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_CONFIRMED_P_PC);
		c.setIdentificatonNo("11111113");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		claimant.persist();
	}
	
	public static void paymentDaoProcessManualChecksReleaseTestData() {
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
				newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), newCheck("1000010", 100),
				newCheck("1000127", 200));
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);		
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		c.setIdentificatonNo("100111");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		c.setIdentificatonNo("100112");
		c.setPaymentAmount(new BigDecimal(200));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		c.setIdentificatonNo("100113");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		claimant.persist();
	}
	
	public static void paymentDaoProcessManualWireReleaseTestData() {
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"), null, null, null);
		
		Payment c = Payment.newPayment(PaymentCode.WIRE_REQUESTED_W_W);
		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.WIRE_REQUESTED_W_W);
		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentAmount(new BigDecimal(200));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		claimant.persist();
		claimant.flush();
		claimant.clear();
	}

	public static void generateStopVoidReversalTestData() {
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"),
				newAddress(AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), newCheck("1000010", 100),
				newCheck("1000023", 200));
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1001");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		c.setIdentificatonNo("1002");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		c.setIdentificatonNo("1003");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_VOIDED_V_V);
		c.setIdentificatonNo("1004");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO);
		c.setIdentificatonNo("1005");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
		c.setIdentificatonNo("1006");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);	
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF);
		c.setIdentificatonNo("1007");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
		c.setIdentificatonNo("1008");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);		

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_HOLD_VOIDED_VH_VH);
		c.setIdentificatonNo("1009");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		//c.setPaymentCode(PaymentCode.VH);
		c.setIdentificatonNo("1010");
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);		
		
		claimant.persist();
		claimant.flush();

	}	
	public static void deleteData() {
		List<Claimant> list = Claimant.findAllClaimants();
		for (Claimant c : list) {
			c.remove();
		}
	}

	public static Claimant newClaimant(String registration1,
			String registration2, String fundAccountNo, String bin,
			String brokerId, String ssn, String ein, String tin,
			 boolean marketTimer,
			ClaimantAddress primaryAddress, ClaimantAddress seasonalAddress,
			Payment payment1, Payment payment2) {
		Claimant p = new Claimant();
		p.setFundAccountNo(fundAccountNo);
		p.setBrokerId(brokerId);
		p.setBin(bin);
		p.setSsn(ssn);
		p.setEin(ein);
		p.setTin(tin);
		p.setFundAccountNo(fundAccountNo);
		p.setMarketTimer(marketTimer);

		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1(registration1);
		p.getClaimantRegistration().setRegistration2(registration2);

		if (primaryAddress != null) {
			primaryAddress.setClaimant(p);
			p.addAddress(primaryAddress);
		}
		if (seasonalAddress != null) {
			seasonalAddress.setClaimant(p);
			p.addAddress(seasonalAddress);
		}
		if (payment1 != null) {
			p.addCheck(payment1);
		}
		if (payment2 != null) {
			p.addCheck(payment2);
		}

		return p;
	}

	public static Payment newCheck(String identificatioNo, double amount) {
		return newCheck(identificatioNo, amount, SaecDateUtils.getDaysFromCurrent(3));
	}
	
	public static Payment newCheck(String identificatioNo, double amount,Date paymentdate) {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
		c.setPaymentDate(paymentdate);
		c.setIdentificatonNo(identificatioNo);
		c.setPaymentAmount(new BigDecimal(amount));
		return c;
	}

	public static ClaimantAddress newAddress(AddressType type, String addr1,
			String city, String state, String zip) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(type);
		a.setAddress1(addr1);
		a.setCity(city);
		a.setStateCode(state);
		a.setCountryCode("US");
		if (zip != null) {
			a.setZipCode(new ZipCode(zip, null));
		}
		return a;
	}
	
	public static ProcessError newProcessError(Long id,String refNo, ProcessName processName,
			ErrorSeverity errorSeverity, String message, boolean isBatch, ErrorType errorType,Date date) {
		ProcessError processError=new ProcessError();
		processError.setId(id);
		processError.setReferenceNo(refNo);
		processError.setProcessName(processName);
		processError.setSeverity(errorSeverity);
		processError.setMessage(message);
		processError.setIsBatch(isBatch);
		processError.setErrorType(errorType);
		processError.setDate(date);
		
		return processError;
	}

	//Creating a new wire object for the check,as a part of the checkReissuing process.	
	public static void createWireToRelease() {
		Claimant claimant = newClaimant(
				"Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				newAddress(AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"), null, null, null);

		
		Payment c = Payment.newPayment(PaymentCode.WIRE_REQUESTED_W_W);
		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentAmount(new BigDecimal(200));
		c.setPayTo(claimant);

		Payment wire = c.reissueCheckAsWire(new java.math.BigDecimal(10));
		wire.setCheckAddress(new MailObjectAddress());
		wire.persist();
		Contact contact = new Contact();
		contact.setEmail("EmailAddress");
		contact.setPhoneNo("8099195347");
		contact.persist();
		claimant.setPrimaryContact(contact);
		claimant.persist();

	}
	
	private static Payment paymentWithGroupMailCode() {
		Payment payment_ = newCheck("1000010", 100);
		GroupMailCode groupMailCode = new GroupMailCode();
		groupMailCode.setCode("101");
		groupMailCode.persist();
		payment_.setGroupMailCode(groupMailCode);
		return payment_;
	}

}
