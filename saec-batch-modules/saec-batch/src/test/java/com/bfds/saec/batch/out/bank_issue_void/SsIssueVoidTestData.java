package com.bfds.saec.batch.out.bank_issue_void;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Bank;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.SaecDateUtils;

@Component
public class SsIssueVoidTestData extends com.bfds.saec.batch.util.DataGenerator {
	  @Transactional
	    public void create() {
	        createEvent1();
	        Claimant claimant = newClaimant();
	        claimant.setReferenceNo("100000001");

	        createNewChecks(claimant);

	        createVoidChecks(claimant);
	        createVoidReissueRequestedChecks(claimant);
	        createReissueApprovedChecks(claimant);
	        claimant.persist();

	        claimant = newClaimant();
	        claimant.setReferenceNo("100000002");
	        claimant.persist();
	        claimant.flush();
	        claimant.clear();
	    }
	  
	  public static void createEvent1() {
			Event e = new Event();
			e.setDda("DDA-1");
			e.setBankCode("SS");
			Bank bank = new Bank();
			bank.setAbaNo("2234");
			e.setBank(bank);
			e.setDeutscheBankUserId("USER-123");
			e.persist();
			e.setUseBottomLineForCheckNoAssignment(true);
			e.flush();

		}

	    /**
	     * Creates Checks in the Created state/status. The check will not have a check #.
	     *
	     * @param claimant The {@link Claimant} to which the checks are assigned.
	     */
	    private void createNewChecksWithoutCheckNo(Claimant claimant) {
	        newPayment(claimant, null, PaymentCode.NEW_ISSUE_CREATED_NI_NI);
	        newPayment(claimant, null, PaymentCode.ISSUANCE_CREATED_IS_IS);
	        newPayment(claimant, null, PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
	        newPayment(claimant, null, PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);

	    }


	    /**
	     * Creates Checks in the void state/status.
	     *
	     * @param claimant The {@link Claimant} to which the checks are assigned.
	     */
	    private void createVoidChecks(Claimant claimant) {

	        newPayment(claimant, "30001", PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
	        newPayment(claimant, "30002", PaymentCode.VOID_HOLD_VOIDED_VH_VH);
	        newPayment(claimant, "30003", PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF);
	        newPayment(claimant, "30004", PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
	        newPayment(claimant, "30005", PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO);
	        newPayment(claimant, "30006", PaymentCode.VOID_VOIDED_V_V);

	    }

	    /**
	     * Creates Checks in the void re-issue requested state/status.
	     *
	     * @param claimant The {@link Claimant} to which the checks are assigned.
	     */
	    private void createVoidReissueRequestedChecks(Claimant claimant) {

	        newPayment(claimant, "40001", PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR);
	        newPayment(claimant, "40002", PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR);
	        newPayment(claimant, "40003", PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR);
	        newPayment(claimant, "40004", PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR);
	        newPayment(claimant, "40005", PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
	    }


	    /**
	     * Creates Checks in the rCreated state/status.
	     *
	     * @param claimant The {@link Claimant} to which the checks are assigned.
	     */
	    private void createNewChecks(Claimant claimant) {
	        newPayment(claimant, "20001", PaymentCode.NEW_ISSUE_CREATED_NI_NI);
	        newPayment(claimant, "20002", PaymentCode.ISSUANCE_CREATED_IS_IS);
	        newPayment(claimant, "20003", PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
	        newPayment(claimant, "20004", PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);         
	    	newPayment(claimant, null, PaymentCode.FIRST_ISSUE_CREATED_FI_FI);

	    }
	    
	    /**
	     * Creates Checks in the re-issue approved state/status. None of these check must be processed if bottom line is used.
	     * Any only those that are 2 days or older must be processed if bottom line is used.
	     *
	     * @param claimant The {@link Claimant} to which the checks are assigned.
	     */
	    private void createReissueApprovedChecks(Claimant claimant) {
	        newPayment(claimant, "10001", PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA).setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
	        newPayment(claimant, "10002", PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA).setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
	        newPayment(claimant, "10003", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA).setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
	        newPayment(claimant, "10004", PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA).setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));
	        newPayment(claimant, "10005", PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA).setStatusChangeDate(SaecDateUtils.getDaysFromCurrent(-3));

	        newPayment(claimant, "10006", PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
	        newPayment(claimant, "10007", PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA);
	        newPayment(claimant, "10008", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
	        newPayment(claimant, "10009", PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA);
	        newPayment(claimant, "10010", PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);

	    }

	    private Payment newPayment(Claimant claimant, String checkNo, PaymentCode paymentCode) {
	        PaymentCalc calc = new PaymentCalc();
	        calc.setNettAmount(new BigDecimal(100.5).setScale(2));
	        calc.setGrossAmount(new BigDecimal(150).setScale(2));
	        calc.setFedWithholding(new BigDecimal(49.5).setScale(2));
	        return newPayment(claimant, checkNo, paymentCode, calc);
	    }


	    private Payment newPayment(Claimant claimant, String checkNo, PaymentCode paymentCode, PaymentCalc paymentCalc) {
	        Payment c = Payment.newPayment(paymentCode);
	        c.setPaymentType(PaymentType.CHECK);
	        c.setIdentificatonNo(checkNo);
	        c.setPaymentCalc(paymentCalc);
	        c.setPayTo(claimant);
	        claimant.addCheck(c);
	        return c;
	    }

}
