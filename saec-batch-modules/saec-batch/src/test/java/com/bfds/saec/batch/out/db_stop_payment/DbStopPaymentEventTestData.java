package com.bfds.saec.batch.out.db_stop_payment;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

@Component
public class DbStopPaymentEventTestData extends com.bfds.saec.batch.util.DataGenerator{
	
	@Transactional
	public void create() {
		createEvent();
		Claimant claimant = newClaimant();
		Payment c;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("123456");
		c.setPaymentCode(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);
		c.setPaymentAmount(new BigDecimal(100));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setIdentificatonNo("1234567");
		c.setPaymentAmount(new BigDecimal(10));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_REPLACE_REQUESTED_R_SRR);
		c.setIdentificatonNo("12345");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
		c.setIdentificatonNo("1234568");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR);
		c.setIdentificatonNo("1234569");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);

		claimant.persist();
        claimant.flush();
        claimant.clear();

	}
}
