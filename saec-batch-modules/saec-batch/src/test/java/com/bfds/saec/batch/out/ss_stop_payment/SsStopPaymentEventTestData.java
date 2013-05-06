package com.bfds.saec.batch.out.ss_stop_payment;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

@Component
public class SsStopPaymentEventTestData extends com.bfds.saec.batch.util.DataGenerator{
	@Transactional
	public void create() {
		createEvent();
		Claimant claimant = newClaimant();
		Payment c;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setIdentificatonNo("123456");
		c.setPaymentAmount(new BigDecimal(100));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setIdentificatonNo("1234567");
		c.setPaymentAmount(new BigDecimal(10));
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
		c.setPaymentCode(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR);
		c.setIdentificatonNo("1001");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		c.setIdentificatonNo("1002");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
		c.setIdentificatonNo("1003");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);
		c.setIdentificatonNo("1004");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_STOP_REJECTED_SR_SJ);
		c.setIdentificatonNo("1005");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		c.setIdentificatonNo("1006");
		c.setPaymentAmount(new BigDecimal(1050));
		claimant.getTaxInfo().setCertificationDate(new Date());
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentAmount(new BigDecimal("101.75"));
		c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		c.setDstoCheckFileSentFlag(Boolean.FALSE);
		c.setPayTo(claimant);
		c.setAuditable(Boolean.TRUE);
		
		
		claimant.persist();
        claimant.flush();
        claimant.clear();

	}

}
