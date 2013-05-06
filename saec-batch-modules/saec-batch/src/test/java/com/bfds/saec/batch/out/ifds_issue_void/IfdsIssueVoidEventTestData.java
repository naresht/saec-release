package com.bfds.saec.batch.out.ifds_issue_void;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.batch.util.DataGenerator;
@Component
public class IfdsIssueVoidEventTestData extends DataGenerator {
	
	@Transactional
	public void create() {
		createEvent();
		Claimant claimant = newClaimant();

		final Payment cc = Payment.newPayment(PaymentCode.VOID_RE_ISSUE_COMPLETED_XX_VX);
		cc.setPaymentType(PaymentType.CHECK);
		cc.setIdentificatonNo("12347");
		cc.setPaymentAmount(new BigDecimal(100));
		cc.setPayTo(claimant);
		claimant.addCheck(cc);

		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		c.setIdentificatonNo("12345");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("55555555");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPaymentCode(PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO);
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
		c.setPaymentType(PaymentType.CHECK);
		c.setReissueOf(cc);
		c.setIdentificatonNo("12346");
		c.setPaymentCode(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		c.setPaymentAmount(new BigDecimal(143));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("66666666");
		c.setPaymentAmount(new BigDecimal(120));
		c.setPaymentCode(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
		c.setPayTo(claimant);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("777777");
		c.setPaymentAmount(new BigDecimal(120));
		c.setPaymentCode(PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO);
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("777771");
		c.setPaymentAmount(new BigDecimal(120));
		c.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		c = Payment.newPayment(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("777772");
		c.setPaymentAmount(new BigDecimal(120));
		c.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		c.setPayTo(claimant);
		claimant.addCheck(c);
		
		
		c = Payment.newPayment(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		c.setPaymentType(PaymentType.CHECK);
		c.setIdentificatonNo("88888888");
		c.setPaymentAmount(new BigDecimal(345));
		c.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		c.setPayTo(claimant);
		claimant.addCheck(c);
		

        claimant.persist();
        claimant.flush();
        claimant.clear();

	}
}
