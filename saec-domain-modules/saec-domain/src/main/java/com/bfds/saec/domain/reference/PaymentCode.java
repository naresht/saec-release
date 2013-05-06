package com.bfds.saec.domain.reference;

import java.util.Map;

import com.google.common.collect.Maps;

public enum PaymentCode {	
	
	FIRST_ISSUE_CREATED_FI_FI ("FI"),
	FIRST_ISSUE_OUTSTANDING_FI_FIO ("FIO"),
	FIRST_ISSUE_CASHED_C_FIC ("FIC"),
	ISSUANCE_CREATED_IS_IS ("IS"),
	ISSUANCE_OUTSTANDING_IS_ISO ("ISO"),
	ISSUANCE_CASHED_C_ISC ("ISC"),
	VOID_NO_REISSUE_VOIDED_VO_VO ("VO"),
	VOID_DAMASCO_VOIDED_VD_VD ("VD"),
	VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR ("VDR"),
	VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA ("VDA"),
	VOID_DAMASCO_RE_ISSUE_REJECTED_IR_VDJ ("VDJ"),
	VOID_DAMASCO_RE_ISSUE_COMPLETED_XX_VDX ("VDX"),
	VOID_RPO_FORWARDABLE_VOIDED_VF_VF ("VF"),
	VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR ("VFR"),
	VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA ("VFA"),
	VOID_RPO_FORWARDABLE_RE_ISSUE_REJECTED_IR_VFJ ("VFJ"),
	VOID_RPO_FORWARDABLE_RE_ISSUE_COMPLETED_XX_VFX ("VFX"),
	VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN ("VN"),
	VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR ("VNR"),
	VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA ("VNA"),
	VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REJECTED_IR_VNJ ("VNJ"),
	VOID_RPO_NON_FORWARDABLE_RE_ISSUE_COMPLETED__VNX ("VNX"),
	VOID_HOLD_VOIDED_VH_VH ("VH"),
	VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR ("VHR"),
	VOID_HOLD_RE_ISSUE_APPROVED_I_VHA ("VHA"),
	VOID_HOLD_RE_ISSUE_REJECTED_IR_VHJ ("VHJ"),
	VOID_HOLD_RE_ISSUE_COMPLETED_XX_VHX ("VHX"),
	VOID_VOIDED_V_V ("V"),
	VOID_RE_ISSUE_REQUESTED_I_VR ("VR"),
	VOID_RE_ISSUE_APPROVED_I_VA ("VA"),
	VOID_RE_ISSUE_REJECTED_IR_VJ ("VJ"),
	VOID_RE_ISSUE_COMPLETED_XX_VX ("VX"),
	VOID_LIFT_OUTSTANDING_VL_VL ("VL"),
	VOID_VOID_TO_WIRE_CONFIRMED_VW_VW ("VW"),
	STOP_STOP_REQUESTED_S_SR ("SR"),
	STOP_STOP_CONFIRMED_S_SC ("SC"),
	STOP_STOP_REJECTED_SR_SJ ("SJ"),
	STOP_REPLACE_REQUESTED_R_SRR ("SRR"),
	STOP_REPLACE_APPROVED_R_SRA ("SRA"),
	STOP_REPLACE_REJECTED_RR_SRJ ("SRJ"),
	STOP_REPLACE_COMPLETED_XX_SRX ("SRX"),
	STOP_DAMASCO_STOP_REQUESTED_P_PR ("PR"),
	STOP_DAMASCO_STOP_CONFIRMED_P_PC ("PC"),
	STOP_DAMASCO_STOP_REJECTED_SR_PJ ("PJ"),
	STOP_DAMASCO_REPLACE_REQUESTED_R_PRR ("PRR"),
	STOP_DAMASCO_REPLACE_APPROVED_PRA ("PRA"),
	STOP_DAMASCO_REPLACE_REJECTED_RR_PRJ ("PRJ"),
	STOP_DAMASCO_REPLACE_COMPLETED_XX_PRX ("PRX"),
	STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW ("SW"),
	STOP_LIFT_OUTSTANDING_L_L ("L"),
	ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR ("RFR"),
	ROF_FULL_PROCESSED_RF_RF ("RF"),
	ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR ("RPR"),
	ROF_PARTIAL_PROCESSED_RP_RP ("RP"),
	ROF_INTEREST_PROCESSED_INT_INT ("INT"),
	NEW_ISSUE_CREATED_NI_NI ("NI"),
	NEW_ISSUE_OUTSTANDING_NI_NIO ("NIO"),
	NEW_ISSUE_CASHED_C_NIC ("NIC"),
	WIRE_REQUESTED_W_W ("W"),
	WIRE_APPROVED_WA_WA ("WA"),
	WIRE_REJECTED_WR_WR ("WR"),
	STALE_DATE_OUTSTANDING_X_X ("X"),
	TRANCHE_ITEM_CREATED_TI_TI ("TI"),
	TRANCHE_ITEM_OUTSTANDING_TI_TIO ("TIO"),
	TRANCHE_ITEM_CASHED_C_TIC ("TIC");
	
	private final String name;
	
	private PaymentCode(String name) {
		this.name=name;
	}		

	public String toString() {
		return name;
	}
	
	public PaymentStatus getPaymentStatus() {
		return null;
	}
	
	private static final Map<PaymentCode, PaymentStatus> codeToStatusMap = Maps.newHashMap();
	
	public static PaymentStatus getPaymentStatusForCode(final PaymentCode paymentCode) {
		return codeToStatusMap.get(paymentCode);
	}
	
	static {
		codeToStatusMap.put(PaymentCode.FIRST_ISSUE_CREATED_FI_FI, PaymentStatus.CREATED);
		codeToStatusMap.put(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO, PaymentStatus.OUTSTANDING);
		codeToStatusMap.put(PaymentCode.FIRST_ISSUE_CASHED_C_FIC, PaymentStatus.CASHED);
		codeToStatusMap.put(PaymentCode.ISSUANCE_CREATED_IS_IS, PaymentStatus.CREATED);
		codeToStatusMap.put(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO, PaymentStatus.OUTSTANDING);
		codeToStatusMap.put(PaymentCode.ISSUANCE_CASHED_C_ISC, PaymentStatus.CASHED);
		codeToStatusMap.put(PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO, PaymentStatus.VOIDED);
		codeToStatusMap.put(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD, PaymentStatus.VOIDED);
		codeToStatusMap.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR, PaymentStatus.REISSUE_REQUESTED);
		codeToStatusMap.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA, PaymentStatus.REISSUE_APPROVED);
		codeToStatusMap.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_REJECTED_IR_VDJ, PaymentStatus.REISSUE_REJECTED);
		codeToStatusMap.put(PaymentCode.VOID_DAMASCO_RE_ISSUE_COMPLETED_XX_VDX, PaymentStatus.REISSUE_COMPLETED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF, PaymentStatus.VOIDED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR, PaymentStatus.REISSUE_REQUESTED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA, PaymentStatus.REISSUE_APPROVED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REJECTED_IR_VFJ, PaymentStatus.REISSUE_REJECTED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_COMPLETED_XX_VFX, PaymentStatus.REISSUE_COMPLETED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN, PaymentStatus.VOIDED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR, PaymentStatus.REISSUE_REQUESTED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA, PaymentStatus.REISSUE_APPROVED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REJECTED_IR_VNJ, PaymentStatus.REISSUE_REJECTED);
		codeToStatusMap.put(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_COMPLETED__VNX, PaymentStatus.REISSUE_COMPLETED);
		codeToStatusMap.put(PaymentCode.VOID_HOLD_VOIDED_VH_VH, PaymentStatus.VOIDED);
		codeToStatusMap.put(PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR, PaymentStatus.REISSUE_REQUESTED);
		codeToStatusMap.put(PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA, PaymentStatus.REISSUE_APPROVED);
		codeToStatusMap.put(PaymentCode.VOID_HOLD_RE_ISSUE_REJECTED_IR_VHJ, PaymentStatus.REISSUE_REJECTED);
		codeToStatusMap.put(PaymentCode.VOID_HOLD_RE_ISSUE_COMPLETED_XX_VHX, PaymentStatus.REISSUE_COMPLETED);
		codeToStatusMap.put(PaymentCode.VOID_VOIDED_V_V, PaymentStatus.VOIDED);
		codeToStatusMap.put(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR, PaymentStatus.REISSUE_REQUESTED);
		codeToStatusMap.put(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA, PaymentStatus.REISSUE_APPROVED);
		codeToStatusMap.put(PaymentCode.VOID_RE_ISSUE_REJECTED_IR_VJ, PaymentStatus.REISSUE_REJECTED);
		codeToStatusMap.put(PaymentCode.VOID_RE_ISSUE_COMPLETED_XX_VX, PaymentStatus.REISSUE_COMPLETED);
		codeToStatusMap.put(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL, PaymentStatus.OUTSTANDING);
		codeToStatusMap.put(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW, null); 
		codeToStatusMap.put(PaymentCode.STOP_STOP_REQUESTED_S_SR, PaymentStatus.STOP_REQUESTED);
		codeToStatusMap.put(PaymentCode.STOP_STOP_CONFIRMED_S_SC, PaymentStatus.STOPPED_CONFIRMED);
		codeToStatusMap.put(PaymentCode.STOP_STOP_REJECTED_SR_SJ, PaymentStatus.STOP_REJECTED);
		codeToStatusMap.put(PaymentCode.STOP_REPLACE_REQUESTED_R_SRR, PaymentStatus.REPLACE_REQUESTED);
		codeToStatusMap.put(PaymentCode.STOP_REPLACE_APPROVED_R_SRA, PaymentStatus.REPLACE_APPROVED);
		codeToStatusMap.put(PaymentCode.STOP_REPLACE_REJECTED_RR_SRJ, PaymentStatus.REPLACE_REJECTED);
		codeToStatusMap.put(PaymentCode.STOP_REPLACE_COMPLETED_XX_SRX, PaymentStatus.REPLACE_COMPLETED);
		codeToStatusMap.put(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR, PaymentStatus.STOP_REQUESTED);
		codeToStatusMap.put(PaymentCode.STOP_DAMASCO_STOP_CONFIRMED_P_PC, PaymentStatus.STOPPED_CONFIRMED);
		codeToStatusMap.put(PaymentCode.STOP_DAMASCO_STOP_REJECTED_SR_PJ, PaymentStatus.STOP_REJECTED);
		codeToStatusMap.put(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR, PaymentStatus.REPLACE_REQUESTED);
		codeToStatusMap.put(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA, PaymentStatus.REPLACE_APPROVED);
		codeToStatusMap.put(PaymentCode.STOP_DAMASCO_REPLACE_REJECTED_RR_PRJ, PaymentStatus.REPLACE_REJECTED);
		codeToStatusMap.put(PaymentCode.STOP_DAMASCO_REPLACE_COMPLETED_XX_PRX, PaymentStatus.REPLACE_COMPLETED);
		codeToStatusMap.put(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW, null);
		codeToStatusMap.put(PaymentCode.STOP_LIFT_OUTSTANDING_L_L, PaymentStatus.OUTSTANDING);
		codeToStatusMap.put(PaymentCode.ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR, PaymentStatus.ROF_FULL);
		codeToStatusMap.put(PaymentCode.ROF_FULL_PROCESSED_RF_RF, PaymentStatus.ROF_FULL);
		codeToStatusMap.put(PaymentCode.ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR, PaymentStatus.ROF_PARTIAL);
		codeToStatusMap.put(PaymentCode.ROF_PARTIAL_PROCESSED_RP_RP, PaymentStatus.ROF_PARTIAL);
		codeToStatusMap.put(PaymentCode.ROF_INTEREST_PROCESSED_INT_INT, PaymentStatus.ROF_INTEREST);
		codeToStatusMap.put(PaymentCode.NEW_ISSUE_CREATED_NI_NI, PaymentStatus.CREATED);
		codeToStatusMap.put(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO, PaymentStatus.OUTSTANDING);
		codeToStatusMap.put(PaymentCode.NEW_ISSUE_CASHED_C_NIC, PaymentStatus.CASHED);
		codeToStatusMap.put(PaymentCode.WIRE_REQUESTED_W_W, PaymentStatus.WIRE_REQUESTED);
		codeToStatusMap.put(PaymentCode.WIRE_APPROVED_WA_WA, PaymentStatus.WIRE_APPROVED);
		codeToStatusMap.put(PaymentCode.WIRE_REJECTED_WR_WR, PaymentStatus.WIRE_REJECTED);
		codeToStatusMap.put(PaymentCode.STALE_DATE_OUTSTANDING_X_X, PaymentStatus.OUTSTANDING);
		codeToStatusMap.put(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI, PaymentStatus.CREATED);
		codeToStatusMap.put(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO, PaymentStatus.OUTSTANDING);
		codeToStatusMap.put(PaymentCode.TRANCHE_ITEM_CASHED_C_TIC, PaymentStatus.CASHED);
	}		
}