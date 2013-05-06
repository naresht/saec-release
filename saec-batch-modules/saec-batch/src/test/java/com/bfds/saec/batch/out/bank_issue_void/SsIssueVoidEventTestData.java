package com.bfds.saec.batch.out.bank_issue_void;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecDateUtils;

@Component
public class SsIssueVoidEventTestData extends com.bfds.saec.batch.util.DataGenerator{
	@Transactional
	public void create() {
		createEvent();
		Claimant claimant = newClaimant();
		Payment c;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("12345");
		c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("1234");
		c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
		c.setPaymentAmount(new BigDecimal(200));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("12346");
		c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
		c.setPaymentAmount(new BigDecimal(305));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.VOID_HOLD_VOIDED_VH_VH);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("12348");
		c.setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
		c.setPaymentAmount(new BigDecimal(305));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		claimant.persist();
        claimant.flush();
        claimant.clear();

	}

}
