package com.bfds.saec.batch.in.ss_bottom_line;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

@Component
public class SsBottomlineInboundEventTestData extends com.bfds.saec.batch.util.DataGenerator {

    @Transactional
	public void create() {
        createEvent();
        Claimant claimant = newClaimant();
        

        Payment c1 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c1.setPaymentType(PaymentType.CHECK);
        c1.setStatusChangeDate(new Date(2011, 9, 1));
        c1.setPaymentAmount(new BigDecimal(100));
        claimant.addCheck(c1);

        Payment c2 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c2.setPaymentType(PaymentType.CHECK);
        c2.setStatusChangeDate(new Date(2011, 9, 1));
        c2.setPaymentAmount(new BigDecimal(200));
        claimant.addCheck(c2);
        
        Payment c3 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c3.setPaymentType(PaymentType.CHECK);
        c3.setStatusChangeDate(new Date(2011, 9, 1));
        c3.setPaymentAmount(new BigDecimal(300));
        claimant.addCheck(c3);
        
        claimant.persist();

	}
}
