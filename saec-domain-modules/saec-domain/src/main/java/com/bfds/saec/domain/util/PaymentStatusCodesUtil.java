package com.bfds.saec.domain.util;

import java.util.Map;

import com.bfds.saec.domain.reference.PaymentCode;
import com.google.common.collect.Maps;

public class PaymentStatusCodesUtil {

	private static final Map<PaymentCode, String> codeDescriptions = Maps.newHashMap();
	
	static {
		codeDescriptions.put(PaymentCode.FIRST_ISSUE_CREATED_FI_FI, "First Issue Created"); 
		codeDescriptions.put(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO, "Outstanding"); 
		codeDescriptions.put(PaymentCode.FIRST_ISSUE_CASHED_C_FIC, "Cashed"); 
		codeDescriptions.put(PaymentCode.ISSUANCE_CREATED_IS_IS, "Re-Issue Created"); 
		codeDescriptions.put(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO, "Outstanding"); 
		codeDescriptions.put(PaymentCode.ISSUANCE_CASHED_C_ISC, "Cashed"); 
		codeDescriptions.put(PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO, "Voided"); 
		codeDescriptions.put(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD, "Void Damasco"); 
		codeDescriptions.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR, "Void Damasco Re-Issue Req"); 
		codeDescriptions.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA, "Void Damasco Re-Issue App"); 
		codeDescriptions.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_REJECTED_IR_VDJ, "Void Damasco Re-Issue Rej"); 
		codeDescriptions.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_COMPLETED_XX_VDX, "Void Damasco Re-Issue Completed"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF, "Void Forwardable"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR, "Void Forwardable Re-Issue Req"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA, "Void Forwardable Re-Issue App"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REJECTED_IR_VFJ, "Void Forwardable Re-Issue Rej"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_COMPLETED_XX_VFX, "Void Forwardable Re-Issue Completed"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN, "Void Non-Forward"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR, "Void Non-Forward Re-Issue Req"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA, "Void Non-Forward Re-Issue App"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REJECTED_IR_VNJ, "Void Non-Forward Re-Issue Rej"); 
		codeDescriptions.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_COMPLETED__VNX, "Void Non-Forward Re-Issue Completed"); 
		codeDescriptions.put(PaymentCode.VOID_HOLD_VOIDED_VH_VH, "Void Hold"); 
		codeDescriptions.put(PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR, "Void Hold Re-Issue Req"); 
		codeDescriptions.put(PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA, "Void Hold Re-Issue App"); 
		codeDescriptions.put(PaymentCode.VOID_HOLD_RE_ISSUE_REJECTED_IR_VHJ, "Void Hold Re-Issue Rej"); 
		codeDescriptions.put(PaymentCode.VOID_HOLD_RE_ISSUE_COMPLETED_XX_VHX, "Void Hold Re-Issue Completed"); 
		codeDescriptions.put(PaymentCode.VOID_VOIDED_V_V, "Voided"); 
		codeDescriptions.put(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR, "Void Re-Issue Req"); 
		codeDescriptions.put(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA, "Void Re-Issue App"); 
		codeDescriptions.put(PaymentCode.VOID_RE_ISSUE_REJECTED_IR_VJ, "Void Re-Issue Rej"); 
		codeDescriptions.put(PaymentCode.VOID_RE_ISSUE_COMPLETED_XX_VX, "Void Re-Issue Completed"); 
		codeDescriptions.put(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL, "Outstanding"); 
		codeDescriptions.put(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW, "Void To Wire"); 
		codeDescriptions.put(PaymentCode.STOP_STOP_REQUESTED_S_SR, "Stop Requested"); 
		codeDescriptions.put(PaymentCode.STOP_STOP_CONFIRMED_S_SC, "Stop Confirmed"); 
		codeDescriptions.put(PaymentCode.STOP_STOP_REJECTED_SR_SJ, "Stop Rejected"); 
		codeDescriptions.put(PaymentCode.STOP_REPLACE_REQUESTED_R_SRR, "Stop Replace Requested"); 
		codeDescriptions.put(PaymentCode.STOP_REPLACE_APPROVED_R_SRA, "Stop Replace Approved"); 
		codeDescriptions.put(PaymentCode.STOP_REPLACE_REJECTED_RR_SRJ, "Stop Replace Rejected"); 
		codeDescriptions.put(PaymentCode.STOP_REPLACE_COMPLETED_XX_SRX, "Stop Replace Completed"); 
		codeDescriptions.put(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR, "Stop Damasco"); 
		codeDescriptions.put(PaymentCode.STOP_DAMASCO_STOP_CONFIRMED_P_PC, "Stop Damasco Confirmed"); 
		codeDescriptions.put(PaymentCode.STOP_DAMASCO_STOP_REJECTED_SR_PJ, "Stop Damasco Rejected"); 
		codeDescriptions.put(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR, "Stop Damasco Replace Requested"); 
		codeDescriptions.put(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA, "Stop Damasco Replace Approved"); 
		codeDescriptions.put(PaymentCode.STOP_DAMASCO_REPLACE_REJECTED_RR_PRJ, "Stop Damasco Replace Rejected"); 
		codeDescriptions.put(PaymentCode.STOP_DAMASCO_REPLACE_COMPLETED_XX_PRX, "Stop Damasco Replace Completed"); 
		codeDescriptions.put(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW, "Stop To Wire"); 
		codeDescriptions.put(PaymentCode.STOP_LIFT_OUTSTANDING_L_L, "Outstanding"); 
		codeDescriptions.put(PaymentCode.ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR, "ROF Full - Residual "); 
		codeDescriptions.put(PaymentCode.ROF_FULL_PROCESSED_RF_RF, "ROF Full"); 
		codeDescriptions.put(PaymentCode.ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR, "ROF Partial - Residual "); 
		codeDescriptions.put(PaymentCode.ROF_PARTIAL_PROCESSED_RP_RP, "ROF Partial"); 
		codeDescriptions.put(PaymentCode.ROF_INTEREST_PROCESSED_INT_INT, "ROF Interest"); 
		codeDescriptions.put(PaymentCode.NEW_ISSUE_CREATED_NI_NI, "New Issue Created"); 
		codeDescriptions.put(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO, "Outstanding"); 
		codeDescriptions.put(PaymentCode.NEW_ISSUE_CASHED_C_NIC, "Cashed"); 
		codeDescriptions.put(PaymentCode.WIRE_REQUESTED_W_W, "Wire Requested"); 
		codeDescriptions.put(PaymentCode.WIRE_APPROVED_WA_WA, "Wire Approved"); 
		codeDescriptions.put(PaymentCode.WIRE_REJECTED_WR_WR, "Wire Rejected"); 
		codeDescriptions.put(PaymentCode.STALE_DATE_OUTSTANDING_X_X, "Stale Date"); 
		codeDescriptions.put(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI, "Tranch Item Created"); 
		codeDescriptions.put(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO, "Outstanding"); 
		codeDescriptions.put(PaymentCode.TRANCHE_ITEM_CASHED_C_TIC, "Cashed"); 
	}
	
	public static String getDescription(final PaymentCode paymentCode) {
		return codeDescriptions.get(paymentCode);
	}
}
