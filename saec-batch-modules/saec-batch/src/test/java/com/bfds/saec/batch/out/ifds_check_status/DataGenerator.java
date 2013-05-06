package com.bfds.saec.batch.out.ifds_check_status;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

public class DataGenerator extends com.bfds.saec.batch.util.DataGenerator {

	public static void createTestStopChecksArePickedAfterTheyAreSentAsOutstangingItems() {
		createEvent();
		Claimant claimant = newClaimant();
		Payment c;

		c = Payment.newPayment(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100001");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100002");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100003");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100004");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100005");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100006");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100007");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100008");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100009");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100010");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100011");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100012");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR);
		c.setPaymentType(PaymentType.ROF);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setRofOf(c);
		c.setIdentificatonNo("100013");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.ROF_FULL_PROCESSED_RF_RF);
		c.setPaymentType(PaymentType.ROF);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setRofOf(c);
		c.setIdentificatonNo("100014");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR);
		c.setPaymentType(PaymentType.ROF);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setRofOf(c);
		c.setIdentificatonNo("100015");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.ROF_PARTIAL_PROCESSED_RP_RP);
		c.setPaymentType(PaymentType.ROF);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setRofOf(c);
		c.setIdentificatonNo("100016");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100017");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.WIRE_REQUESTED_W_W);
		c.setPaymentType(PaymentType.WIRE);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100018");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.WIRE_APPROVED_WA_WA);
		c.setPaymentType(PaymentType.WIRE);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100019");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.WIRE_REJECTED_WR_WR);
		c.setPaymentType(PaymentType.WIRE);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100020");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.ROF_INTEREST_PROCESSED_INT_INT);
		c.setPaymentType(PaymentType.ROF);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setRofOf(c);
		c.setIdentificatonNo("100021");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);
		
				
		c = Payment.newPayment(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100022");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.STOP_DAMASCO_STOP_REJECTED_SR_PJ);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100023");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.STOP_REPLACE_REQUESTED_R_SRR);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100024");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.STOP_STOP_REJECTED_SR_SJ);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100025");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("100026");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);

		claimant.persist();
	}
}
