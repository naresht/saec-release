package com.bfds.saec.batch.stale_date;

import java.math.BigDecimal;
import java.util.Date;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecDateUtils;


public class DataGenerator extends com.bfds.saec.batch.util.DataGenerator {
	
	public static void createStaleDateProcessingData() {
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
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1002");
		c.setStaleByDate(SaecDateUtils.getDaysFromCurrent(-1));
		c.setPaymentAmount(new BigDecimal(200));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("1003");
		c.setStaleByDate(new Date());
		c.setPaymentAmount(new BigDecimal(300));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("1004");
		c.setPaymentAmount(new BigDecimal(400));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		claimant.persist();

	}


}
