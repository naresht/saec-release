package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.rip.domain.StopReplaceCheckRipObject;
import com.bfds.saec.rip.service.RipEventListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class PaymentTest {

	@Autowired
	ClaimantDataOnDemand claimantDod;

	@Autowired
	PaymentDataOnDemand paymentDod;

	@Test
	public void assignCheckToClaimant() {
		Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		claimant.flush();

		Payment payment = Payment
				.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		claimant.addCheck(payment);
		assertEquals(payment.getCheckAddress().getAddress(), claimant
				.getPaymentAddress().getAddress());
	}

	@Test
	public void saveClaimantWithCheck() {
		Claimant claimant = claimantDod.getNewTransientClaimant(100);

		Payment payment = Payment
				.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		claimant.addCheck(payment);

		claimant.persist();
		claimant.flush();

		claimant = Claimant.findClaimant(claimant.getId(),
				Claimant.ASSOCIATION_PAYMENTS);

		assertEquals(1, claimant.getPayments().size());
		assertEquals(PaymentCode.FIRST_ISSUE_CREATED_FI_FI, claimant
				.getPayments().iterator().next().getPaymentCode());
	}

	@Test
	public void checkShouldCloneItsAddress() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		MailObjectAddress a = new MailObjectAddress();
		c.setCheckAddress(a);
		assertTrue(a != c.getCheckAddress());
	}

	@Test
	public void checkShouldCopyRegistrationFromItsPayt() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		Claimant claimant = claimantDod.getNewTransientClaimant(10);
		Payment payment = Payment
				.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		claimant.addCheck(payment);
		assertTrue(payment.getPayTo() != null);
		assertThat(
				payment.getPayToRegistration().getRegistrationLinesAsString())
				.isEqualTo(
						claimant.getClaimantRegistration()
								.getRegistrationLinesAsString());
	}

	/**
	 * Commenting this test temporarily,due to the changes made for the {@link Payment} objects setCheckAddress method. 
	 * There we are setting AddressType based on the selection from the UI.
	 */
	@Test
	@Ignore
	public void theCheckMustCheangeItsAddressType() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		MailObjectAddress a = new MailObjectAddress();
		c.setCheckAddress(a);
		assertEquals(AddressType.PAYMENT_ADDRESS, c.getCheckAddress()
				.getAddressType());
	}

	@Test
	public void cashedCheckCannotBeVoided() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		assertTrue(!c.isVoidable());
	}

	@Test
	public void voidedCheckCannotBeVoided() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentCode(PaymentCode.VOID_VOIDED_V_V);
		assertTrue(!c.isVoidable());
	}

	@Test
	public void stoppedCheckCannotBeVoided() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		assertTrue(!c.isVoidable());
	}

	@Test
	public void outstandingCheckCanBeVoided() {

		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		assertTrue(c.isVoidable());

		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		assertTrue(c.isVoidable());

		c.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		assertTrue(c.isVoidable());

		c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		assertTrue(c.isVoidable());

		c.setPaymentCode(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
		assertTrue(c.isVoidable());

		c.setPaymentCode(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		// assertTrue(c.isVoidable());
	}

	@Test
	public void doPartialReturnOfFunding() {
		Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		claimant.flush();

		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		PaymentCalc calc = new PaymentCalc();
		calc.setNettAmount(new BigDecimal(100).setScale(2, BigDecimal.ROUND_DOWN));
		calc.setGrossAmount(new BigDecimal(200).setScale(2, BigDecimal.ROUND_DOWN));
		c.setPaymentCalc(calc);
		c.setPayTo(claimant);
		Payment rof = c.addReturnOfFunding(50, 0, true, "Blah Blah Blah",
				new Date()).get(0);

		PaymentCalc expectedRofPaymantCalc = new PaymentCalc();
		expectedRofPaymantCalc.setNettAmount(new BigDecimal(50));//.setScale(2, BigDecimal.ROUND_HALF_UP));
		expectedRofPaymantCalc.setGrossAmount(new BigDecimal(100).setScale(2, BigDecimal.ROUND_DOWN));
		assertTrue(rof != null);
		assertEquals(rof.getPaymentCalc(), expectedRofPaymantCalc);
		assertTrue(rof.isRofHasResidualMonies() == true);
		assertEquals(rof.getComments(), "Blah Blah Blah");
		assertEquals(PaymentType.ROF, rof.getPaymentType());
		assertTrue(rof.getPaymentDate() != null);
		assertEquals(c.getReturnOfFundings().size(), 1);
		assertEquals(PaymentCode.ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR,
				rof.getPaymentCode());
	}

	@Test
	public void doFullReturnOfFunding() {
		Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		claimant.flush();
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		PaymentCalc calc = new PaymentCalc();
		calc.setNettAmount(new BigDecimal(100));
		calc.setGrossAmount(new BigDecimal(200));
		c.setPaymentCalc(calc);
		c.setPayTo(claimant);
		Payment rof = c.addReturnOfFunding(100, 0, true, "Blah Blah Blah",
				new Date()).get(0);

		assertTrue(rof != null);
		assertEquals(rof.getPaymentCalc(), c.getPaymentCalc());
		assertTrue(rof.isRofHasResidualMonies() == true);
		assertEquals(rof.getComments(), "Blah Blah Blah");
		assertEquals(PaymentType.ROF, rof.getPaymentType());
		assertTrue(rof.getPaymentDate() != null);
		assertEquals(c.getReturnOfFundings().size(), 1);
		assertEquals(PaymentCode.ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR,
				rof.getPaymentCode());
	}

	@Test
	public void addReturnOfInterestFunding() {
		Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		claimant.flush();

		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		Payment rof = c.addReturnOfFunding(100, 10, true, "Blah Blah Blah",
				new Date()).get(0);

		Set<Payment> payments = c.getReturnOfFundings();

		assertEquals(2, payments.size());
	}

	@Test
	public void voidCheck() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.voidCheck();
		assertThat(c.isVoid()).isTrue();

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		c.voidCheck();
		assertThat(c.isVoid()).isTrue();

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		c.voidCheck();
		assertThat(c.isVoid()).isTrue();

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		c.voidCheck();
		assertThat(c.isVoid()).isTrue();

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
		c.voidCheck();
		assertThat(c.isVoid()).isTrue();

	}

	@Test(expected = IllegalStateException.class)
	public void staleDatedCheckCannotBeVoided() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		Event.getCurrentEvent().setCanChangeStausOfStaleCheck(false);
		c.voidCheck();
		assertThat(c.isVoid()).isTrue();
	}

	@Test
	public void reissueCheck() {
		Payment c = new Payment() {
			protected StopReplaceCheckRipObject createStopReplaceCheckRipObject() {
				return null;
			}
		};
		RipEventListener mock = Mockito.mock(RipEventListener.class);
		c.setRipEventListener(mock);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.voidCheck();
		c.reissueCheck(new MailObjectAddress());
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);

		c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		c.stopCheck();
		c.reissueCheck(new MailObjectAddress());
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.STOP_REPLACE_REQUESTED_R_SRR);
	}

	@Test
	public void reverseVoid() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.voidCheck();
		c.doStopVoidReverse(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
	}
	
	@Test
	public void reverseStop() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.stopCheck();
		c.doStopVoidReverse(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
	}
	
	@Test(expected = IllegalStateException.class)
	public void reverseStopCannotBeDoneIfCheckSentOnStopFile() {
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.stopCheck();
		c.setSentOnBankStopFile(Boolean.TRUE);
		c.doStopVoidReverse(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
	}

	@Test
	public void getNewCheckForReissue() {
		final Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();

		c = Claimant.findClaimant(claimant.getId()).getPayments().iterator()
				.next();

		Payment c1 = c.getNewCheckForReissue();

		assertThat(c1.getPaymentCode()).isEqualTo(
				PaymentCode.ISSUANCE_CREATED_IS_IS);
		assertThat(c1.getReissueOf().getIdentificatonNo()).isEqualTo(
				c.getIdentificatonNo());
	}

	@Test
	@ExpectedException(IllegalStateException.class)
	public void cannotGetNewCheckForReissueIfStatusInvalid() {
		final Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();

		c = Claimant.findClaimant(claimant.getId()).getPayments().iterator()
				.next();

		c.getNewCheckForReissue();
	}

	@Test
	@ExpectedException(IllegalStateException.class)
	public void cannotGetNewCheckForReissueIfPaymentNotCheck() {
		final Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();

		c = Claimant.findClaimant(claimant.getId()).getPayments().iterator()
				.next();

		c.getNewCheckForReissue();
	}

	@Test
	public void canChangeToWire() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		assertThat(c.canChangeToWire()).isTrue();

		c.setPaymentType(null);
		assertThat(c.canChangeToWire()).isTrue();

		c.setPaymentType(PaymentType.WIRE);
		assertThat(c.canChangeToWire()).isFalse();

		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);

		assertThat(c.canChangeToWire()).isFalse();

	}

	@Test
	public void canSplit() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setSplitOf(null);
		assertThat(c.canSplit()).isFalse();

		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setPaymentType(PaymentType.CHECK);
		assertThat(c.canSplit()).isTrue();

		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		assertThat(c.canSplit()).isFalse();
	}

    @Test
    public void canSplitRof() {
        Payment c = paymentDod.getNewTransientPayment(10001);
        c.setSplitOf(null);
        c.setPaymentCode(PaymentCode.ROF_FULL_PROCESSED_RF_RF);
        c.setPaymentType(PaymentType.ROF);
        assertThat(c.canSplit()).isTrue();
    }

    @Test
    public void cannotSplitRofInterest() {
        Payment c = paymentDod.getNewTransientPayment(10001);
        c.setSplitOf(null);
        c.setPaymentCode(PaymentCode.ROF_INTEREST_PROCESSED_INT_INT);
        c.setPaymentType(PaymentType.ROF);
        assertThat(c.canSplit()).isFalse();
    }

	@Test
	public void canDoReturnOfFunding() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		assertThat(c.canDoReturnOfFunding()).isTrue();

		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		assertThat(c.canDoReturnOfFunding()).isFalse();

		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		assertThat(c.canDoReturnOfFunding()).isFalse();
	}

	@Test
	public void isWireApproved() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.WIRE_APPROVED_WA_WA);
		assertThat(c.isWireApproved()).isTrue();

		c.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		assertThat(c.isWireApproved()).isFalse();

	}

	@Test
	public void isWireRejected() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.WIRE_REJECTED_WR_WR);
		assertThat(c.isWireRejected()).isTrue();

		c.setPaymentCode(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		assertThat(c.isWireRejected()).isFalse();
	}

	@Test
	public void canUpdateWireInfo() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
		assertThat(c.canUpdateWireInfo()).isTrue();

		c.setPaymentCode(PaymentCode.WIRE_APPROVED_WA_WA);
		assertThat(c.canUpdateWireInfo()).isFalse();
	}

	@Test
	public void verifyToString() {
		// paymentDod.getNewTransientPayment(101).toString();
	}

	@Test
	public void getPayToAsText() {
		final Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.WIRE);
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();

		c = Claimant.findClaimant(claimant.getId()).getPayments().iterator()
				.next();

		assertThat(claimant.getRegistrationLinesAsString("")).isEqualTo(
				c.getPayToAsText(""));
	}

	@Test
	public void doStopLift() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setStopLifted(true);
		c.doStopLift();
		assertThat(c.isStopLifted()).isTrue();
	}

	@Test
	public void stopToWire() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		Payment wire = c.reissueCheckAsWire(new java.math.BigDecimal(10));
		assertThat(c.getPaymentCode()).isEqualTo(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);
		assertThat(c.getPaymentStatus()).isNotNull();
		assertThat(wire.getPaymentCode()).isEqualTo(PaymentCode.WIRE_REQUESTED_W_W);
		assertThat(wire.getPaymentStatus()).isEqualTo(PaymentStatus.WIRE_REQUESTED);
		assertThat(wire.getPaymentType()).isEqualTo(PaymentType.WIRE);
	}
	
	@Test
	public void createdToWire() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		Payment wire = c.reissueCheckAsWire(new java.math.BigDecimal(10));
		assertThat(c == wire).isTrue();
		assertThat(wire.getPaymentCode()).isEqualTo(PaymentCode.WIRE_REQUESTED_W_W);
		assertThat(wire.getPaymentStatus()).isEqualTo(PaymentStatus.WIRE_REQUESTED);
		assertThat(wire.getPaymentType()).isEqualTo(PaymentType.WIRE);
	}
	
	@Test
	public void voidToWire() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_VOIDED_V_V);
		Payment wire = c.reissueCheckAsWire(new java.math.BigDecimal(10));
		assertThat(c.getPaymentCode()).isEqualTo(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
		assertThat(c.getPaymentStatus()).isNotNull();
		assertThat(wire.getPaymentCode()).isEqualTo(PaymentCode.WIRE_REQUESTED_W_W);
		assertThat(wire.getPaymentStatus()).isEqualTo(PaymentStatus.WIRE_REQUESTED);
		assertThat(wire.getPaymentType()).isEqualTo(PaymentType.WIRE);
	}
	
	/**
	 * Verifies that all properties that have to be set or the newly created wire object are set correctly.
	 */
	@Test
	public void checkToWireCoppiedProperties() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_VOIDED_V_V);
		
		final WireInfo w = new WireInfo();		
		w.setAuthorizedApprover("aprover");
		Bank b = new Bank();
		b.setAbaNo("12345");
		w.setReceivingBank(b);		
		c.setWireInfo(w);
		
		PaymentCalc calc = new PaymentCalc();
		calc.setNettAmount(new BigDecimal(100));
		calc.setGrossAmount(new BigDecimal(150));
		
		c.setPaymentCalc(calc);
		
		Payment wire = c.reissueCheckAsWire(new java.math.BigDecimal(100));
		assertThat(wire.getPayTo().getReferenceNo()).isEqualTo(c.getPayTo().getReferenceNo());
		assertThat(wire.getReissueOf().getIdentificatonNo()).isEqualTo(c.getIdentificatonNo());
		assertThat(wire.getPaymentCalc()).isEqualTo(c.getPaymentCalc());	
		assertThat(wire.getWireInfo()).isEqualTo(w);	
		assertThat(wire.getPaymentCalc()).isEqualTo(calc);
		assertThat(c.getPaymentCalc()).isEqualTo(calc);
		assertThat(wire.getCheckAddress()).isNull();
		
	}

	@Test
	public void findByMailingControlNo() {

		final Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.WIRE);
		c.setIdentificatonNo("123456");
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();
		claimant.clear();

		Payment payment = Payment.findByMailingControlNo("123456");

		assertThat(payment.getIdentificatonNo()).isEqualTo("123456");

	}
	
	/**
	 * Test to count the number of {@link Payment} belonging to a {@link Claimant} that have been returned RPO_NON_FORWARDABLE.
	 */
	@Test
	public void countPaymentsForAddressResaerchOfClaimant() {

		final Claimant claimant = claimantDod.getNewTransientClaimant(100);
		claimant.persist();
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentType(PaymentType.WIRE);
		c.setIdentificatonNo("123456");
		c.setPaymentCode(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();
		claimant.clear();

		Long count = Payment.countPaymentsForAddressResaerchOfClaimant(claimant.getId());

		assertThat(count).isEqualTo(1);

	}

	@Test
	public void verifyStopTypes() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.damascoStopCheck();
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);

		c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.stopCheck();
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.STOP_STOP_REQUESTED_S_SR);
	}

	@Test
	public void verifyVoidTypes() {
		Payment c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.voidCheck();
		assertThat(c.getPaymentCode()).isEqualTo(PaymentCode.VOID_VOIDED_V_V);

		c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.noReissueVoidCheck();
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO);

		c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.damascoVoidCheck();
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);

		c = paymentDod.getNewTransientPayment(10001);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.holdVoidCheck();
		assertThat(c.getPaymentCode()).isEqualTo(
				PaymentCode.VOID_HOLD_VOIDED_VH_VH);
	}

	@Test
	public void testPaymentInitialState() {
		Payment p = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		assertThat(p.getInitialState()).isNotNull();
		assertThat(p.getInitialState().getPaymentCode()).isEqualTo(
				PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		assertThat(p.getInitialState().getPaymentStatus()).isEqualTo(
				p.getPaymentStatus());
		assertThat(p.getInitialState().getStatusChangeDate()).isEqualTo(
				p.getStatusChangeDate());
	}

	@Test
	public void testOutstandToCashedStateChange() {
		Payment p = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		p.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		p.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.FIRST_ISSUE_CASHED_C_FIC);

		p = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		p.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		p.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.NEW_ISSUE_CASHED_C_NIC);

		p = Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
		p.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.ISSUANCE_CASHED_C_ISC);
		p.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.ISSUANCE_CASHED_C_ISC);

		p = Payment.newPayment(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);
		p.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.TRANCHE_ITEM_CASHED_C_TIC);
		p.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.TRANCHE_ITEM_CASHED_C_TIC);

		p = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		p.setPaymentCode(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		assertThat(
				PaymentCodeUtil.getCashedCodeForGivenOutstandingCode(p,
						p.getPaymentCode())).isEqualTo(
				PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
	}

	/**
	 * A Check has been sent on IFDS file as an Outstanding. If the same check
	 * is marked void it must be sent again on IFDS file as an void.For That
	 * whenever the paymentCode changes,the value for IfdsSent will get
	 * reset.
	 */
	@Test
	public void testFileFlagsOnPaymentCodeChange() {
		Payment p = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		p.setIfdsSent(true);
		assertThat(p.getIfdsSent()).isEqualTo(Boolean.TRUE);
		p.setPaymentCode(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		assertThat(p.getIfdsSent()).isEqualTo(Boolean.FALSE);

	}

    /**
     A check can be re sent on the bank issue void file only when it goes from one on the "outstanding" states to
     one of the void or stop states. A cashed check is anyways not eligible to be sent on the issue void file.
     so setting the flag sentOnBankIssueVoidFile= false when a check goes from one on the "outstanding" states to
     a cashed state is harmless.
    */
    @Test
    public void testSentOnBankIssueVoidFileFlagReset() {
        Payment p = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);

        p.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
        p.setSentOnBankIssueVoidFile(Boolean.TRUE);

        p.setPaymentCode(PaymentCode.VOID_VOIDED_V_V);
        // A voided check must have the flag reset.
        assertThat(p.getSentOnBankIssueVoidFile()).isEqualTo(Boolean.FALSE);

        p.setSentOnBankIssueVoidFile(Boolean.TRUE);
        p.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
        //The flag must not be reset. see https://bostonfinancial.atlassian.net/browse/PII-230
        p.setSentOnBankIssueVoidFile(Boolean.TRUE);
    }
}
