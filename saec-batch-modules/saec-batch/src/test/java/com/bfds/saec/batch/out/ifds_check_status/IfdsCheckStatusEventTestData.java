package com.bfds.saec.batch.out.ifds_check_status;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

@Component
public class IfdsCheckStatusEventTestData extends com.bfds.saec.batch.util.DataGenerator{
	
	@Transactional
	public void create() {
		createEvent();
		Claimant claimant = newClaimant();
        Payment c;

        c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
        c.setPaymentType(PaymentType.CHECK);
        c.setStatusChangeDate(new Date(2011, 9, 1));
        c.setIdentificatonNo("00001011");
        c.setPaymentAmount(new BigDecimal(550));
        c.setPayTo(claimant);
        c.setPaymentCode(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
        c.setIfdsSent(true);
        claimant.addCheck(c);

        Payment reIssueOf = c;
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.WIRE);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);
		c.setPaymentCode(PaymentCode.WIRE_APPROVED_WA_WA);
        c.setReissueOf(reIssueOf);
		claimant.addCheck(c);
		
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("00002233");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);
		c.setPaymentCode(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
		c.setIfdsSent(true);
		claimant.addCheck(c);

		Payment splitOf = c;
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.WIRE);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);
		c.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
		c.setSplitOf(splitOf);
		claimant.addCheck(c);


		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(null);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("55544433");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);
		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);

		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo(null);
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);
		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);

		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.ROF_INTEREST_PROCESSED_INT_INT);
		c.setPaymentType(PaymentType.ROF);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setRofOf(c);
		c.setIdentificatonNo("111121");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);

		claimant.addCheck(c);
		
				
		c = Payment.newPayment(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		c.setPaymentType(PaymentType.CHECK);
		c.setStatusChangeDate(new Date(2011, 9, 1));
		c.setIdentificatonNo("111122");
		c.setPaymentAmount(new BigDecimal(550));
		c.setPayTo(claimant);
		
		claimant.addCheck(c);

        claimant.persist();
        claimant.flush();
        claimant.clear();

	}
}
