package com.bfds.saec.batch.in.db_stop_confirmation;

import java.math.BigDecimal;
import java.util.Date;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DbStopConfirmationEventTestData extends com.bfds.saec.batch.util.DataGenerator {

    @Transactional
	public void create() {
        createEvent();
        Claimant claimant = newClaimant();
        Payment c;

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("4881918444");
        c.setPaymentAmount(new BigDecimal(100.00));
        c.setPayTo(claimant);
        claimant.addCheck(c);

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("3608287156");
        c.setPaymentAmount(new BigDecimal(25.25));
        c.setPayTo(claimant);
        claimant.addCheck(c);
        //claimant.persist();

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("3608287157");
        c.setPaymentAmount(new BigDecimal(25.25));
        c.setPayTo(claimant);
        claimant.addCheck(c);
        
        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("3608287158");
        c.setPaymentAmount(new BigDecimal(12.25));
        c.setPayTo(claimant);
        claimant.addCheck(c);
        
        

        claimant.persist();
	}
}
