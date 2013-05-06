package com.bfds.saec.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecDateUtils;

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
						"New York", "NY", "4470"), newCheck("1000010", 100),
				newCheck("1000020", 200));
		claimant.persist();
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
		claimant.persist();
		claimant = newClaimant(
				"David",
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
		claimant.persist();
	}

	public static void generatePaymentDaoImplTestData() {
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
		c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		c.setIdentificatonNo("100001");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		PaymentLetterCode code6 = new PaymentLetterCode("c6", "code 6");
		code6.persist();

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		c.setIdentificatonNo("100002");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setLetterCode(code6);
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

        PaymentLetterCode code7 = new PaymentLetterCode("c7", "code 7");
		code7.persist();
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.WIRE);
		c.setSpecialPullCode("SP-1");
		c.setLetterCode(code7);
		c.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
		// c.setIdentificatonNo(String.valueOf(25 + 20 * counter));
		c.setPaymentAmount(new BigDecimal(550));
		c.setStatusChangeDate(new Date(101, 7, 14));
		c.setPayTo(claimant);
		claimant.addCheck(c);

        PaymentLetterCode code8 = new PaymentLetterCode("c8", "code 8");
		code8.persist();
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		// c.setPaymentType(PaymentType.WIRE);
		c.setSpecialPullCode("SP-1");
		c.setLetterCode(code8);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		// c.setIdentificatonNo(String.valueOf(25 + 20 * counter));
		c.setPaymentAmount(new BigDecimal(600));
		c.setStatusChangeDate(new Date(101, 7, 14));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		claimant.persist();

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

		// c.setPaymentCode(PaymentCode.VH);
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
			// NOTE = Throws Integrity constraint violation if data already
			// exists
			c.remove();
		}
	}

	public static Claimant newClaimant(String registration1,
			String registration2, String fundAccountNo, String bin,
			String brokerId, String ssn, String ein, String tin, boolean marketTimer,
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
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
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

}
