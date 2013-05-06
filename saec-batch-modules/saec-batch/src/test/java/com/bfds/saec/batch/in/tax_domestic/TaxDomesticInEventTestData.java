package com.bfds.saec.batch.in.tax_domestic;

import com.bfds.saec.domain.*;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentComponentType;
import com.bfds.saec.domain.reference.PaymentType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class TaxDomesticInEventTestData extends com.bfds.saec.batch.util.DataGenerator {

    @Transactional
    public void create() {
        createEvent();
        createEventPaymentConfig(PaymentComponentType.paymentComp1, "Settlement Amount");
        createEventPaymentConfig(PaymentComponentType.paymentComp2, "Fees");
        createEventPaymentConfig(PaymentComponentType.paymentComp3, "Other Monies");

        Claimant claimant = newClaimant();
        claimant.setReferenceNo("101");

        Payment c1 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c1.setPaymentCode(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
        c1.setPaymentType(PaymentType.CHECK);
        c1.setIdentificatonNo("100001");
        c1.setPaymentAmount(new BigDecimal(100));
        claimant.addCheck(c1);
        claimant.persist();

        claimant = newClaimant();
        claimant.setReferenceNo("102");

        c1 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c1.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
        c1.setPaymentType(PaymentType.CHECK);
        c1.setIdentificatonNo("100002");
        c1.setPaymentAmount(new BigDecimal(100));
        claimant.addCheck(c1);
        claimant.persist();

        claimant = newClaimant();
        claimant.setReferenceNo("103");

        c1 = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c1.setPaymentCode(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
        c1.setPaymentType(PaymentType.CHECK);
        c1.setIdentificatonNo("100003");
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

    public Claimant getAlternatePayeeOf(String claimantReferenceNo) {
        try {
        return   Claimant.entityManager().createQuery("select c from Claimant as c where c.parentClaimant.referenceNo = :claimantReferenceNo", Claimant.class)
                .setParameter("claimantReferenceNo", claimantReferenceNo).getSingleResult();
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
