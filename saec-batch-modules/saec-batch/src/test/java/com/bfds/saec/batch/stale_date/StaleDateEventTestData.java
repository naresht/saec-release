package com.bfds.saec.batch.stale_date;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecDateUtils;

@Component
public class StaleDateEventTestData  extends com.bfds.saec.batch.util.DataGenerator {

	@Transactional
	public static void create() {
		createEvent();
		Claimant claimant = newClaimant();
		Payment c;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setIdentificatonNo("1000");
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentAmount(new BigDecimal(50));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1001");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		c.setIdentificatonNo("1002");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(200));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1003");
		c.setStaleDated(Boolean.FALSE);
		c.setStaleByDate(new Date());
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("1004");
		c.setStaleDated(Boolean.FALSE);
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		c.setIdentificatonNo("1005");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
		c.setIdentificatonNo("1006");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		c.setIdentificatonNo("1007");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		c.setIdentificatonNo("1008");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		c.setIdentificatonNo("1009");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-2));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		

		claimant.persist();

	}
}
