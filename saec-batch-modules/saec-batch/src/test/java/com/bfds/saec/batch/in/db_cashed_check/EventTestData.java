package com.bfds.saec.batch.in.db_cashed_check;

import java.math.BigDecimal;
import java.util.Date;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventTestData extends com.bfds.saec.batch.util.DataGenerator {

    @Transactional
	public void create() {
        createEvent();
        Claimant claimant = newClaimant();
        Payment c;

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("4881918444");
        c.setPaymentAmount(new BigDecimal(100));
        c.setPayTo(claimant);
        c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);

        claimant.getTaxInfo().setCertificationDate(new Date());
        claimant.addCheck(c);

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("3608287156");
        c.setPaymentAmount(new BigDecimal(25));
        c.setPayTo(claimant);
        c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);

        claimant.getTaxInfo().setCertificationDate(new Date());
        claimant.addCheck(c);
        
        
        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("3608287157");
        c.setPaymentAmount(new BigDecimal(25));
        c.setPayTo(claimant);
        c.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);

        claimant.getTaxInfo().setCertificationDate(new Date());
        claimant.addCheck(c);
        
        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("3608287158");
        c.setPaymentAmount(new BigDecimal(25));
        c.setPayTo(claimant);
        c.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);

        claimant.getTaxInfo().setCertificationDate(new Date());
        claimant.addCheck(c);
        
        
        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("3608287159");
        c.setPaymentAmount(new BigDecimal(25));
        c.setPayTo(claimant);
        c.setPaymentCode(PaymentCode.STALE_DATE_OUTSTANDING_X_X);

        claimant.getTaxInfo().setCertificationDate(new Date());
        claimant.addCheck(c);

        claimant.persist();

	}
}
