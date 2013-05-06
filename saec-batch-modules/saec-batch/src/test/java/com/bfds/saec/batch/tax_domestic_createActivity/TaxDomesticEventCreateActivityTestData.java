package com.bfds.saec.batch.tax_domestic_createActivity;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.EventPaymentConfig;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentComponentTypeLov;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentComponentType;
import com.bfds.saec.domain.reference.PaymentType;

@Component
public class TaxDomesticEventCreateActivityTestData extends com.bfds.saec.batch.util.DataGenerator {

    @Transactional
    public void create() {
        createEvent();
        createEventPaymentConfig(PaymentComponentType.paymentComp1, "Settlement Amount");
        createEventPaymentConfig(PaymentComponentType.paymentComp2, "Fees");
        createEventPaymentConfig(PaymentComponentType.paymentComp3, "Other Monies");

        Claimant claimant = newClaimant();
        claimant.setReferenceNo("1001");

        Payment c1 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c1.setPaymentCode(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
        c1.setPaymentType(PaymentType.CHECK);
        c1.setIdentificatonNo("1000001");
        c1.setPaymentAmount(new BigDecimal(100));
        claimant.addCheck(c1);
        claimant.persist();

        
    }

    private void createEventPaymentConfig(PaymentComponentType paymentComponentType, String description) {
        EventPaymentConfig config = new EventPaymentConfig();
        config.setDefaultDescription(description);
        PaymentComponentTypeLov paymentComponentTypeLov = new PaymentComponentTypeLov();
        paymentComponentTypeLov.setCode(paymentComponentType.name());
        paymentComponentTypeLov.setDescription("NA");
        paymentComponentTypeLov.persist();
        config.setPaymentComponentType(paymentComponentTypeLov);
        config.setEvent(Event.getCurrentEvent());
        config.persist();
    }

}
