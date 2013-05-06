package com.bfds.saec.domain.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Utility methods for PaymentCode.
 */
public class PaymentCodeUtil {

	private static final List<PaymentCode> voidReissueRequestedCodes = Lists
			.newArrayList();
	static {
		voidReissueRequestedCodes
				.add(PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR);
		voidReissueRequestedCodes
				.add(PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR);
		voidReissueRequestedCodes.add(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		voidReissueRequestedCodes
				.add(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR);
		voidReissueRequestedCodes
				.add(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR);
	}

	private static final List<PaymentCode> voidReissueApprovedCodes = Lists
			.newArrayList();
	static {
		voidReissueApprovedCodes
				.add(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		voidReissueApprovedCodes
				.add(PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA);
		voidReissueApprovedCodes.add(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		voidReissueApprovedCodes
				.add(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA);
		voidReissueApprovedCodes
				.add(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
	}

	private static final List<PaymentCode> stopReplaceRequestedCodes = Lists
			.newArrayList();
	static {
		stopReplaceRequestedCodes
				.add(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR);
		stopReplaceRequestedCodes.add(PaymentCode.STOP_REPLACE_REQUESTED_R_SRR);
	}

	private static final List<PaymentCode> stopReplaceApprovedCodes = Lists
			.newArrayList();
	static {
		stopReplaceApprovedCodes
				.add(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		stopReplaceApprovedCodes.add(PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
	}
	public static final List<PaymentCode> PAYMENTS_ELIGIBLE_FOR_DAMASCO_PROCESSING =

	new ImmutableList.Builder<PaymentCode>().add(
			PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO,
			PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO,
			PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO,
			PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO,
			PaymentCode.STOP_LIFT_OUTSTANDING_L_L,
			PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL,
			PaymentCode.STALE_DATE_OUTSTANDING_X_X,
			PaymentCode.VOID_DAMASCO_VOIDED_VD_VD,
			PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR).build();

	public static Set<PaymentCode> getOutstandingCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		ret.add(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		ret.add(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		ret.add(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
		ret.add(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		ret.add(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		ret.add(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
		return ret;
	}

	public static boolean isCreated(final PaymentCode paymentCode) {
		return paymentCode == PaymentCode.FIRST_ISSUE_CREATED_FI_FI
				|| paymentCode == PaymentCode.ISSUANCE_CREATED_IS_IS
				|| paymentCode == PaymentCode.NEW_ISSUE_CREATED_NI_NI
				|| paymentCode == PaymentCode.TRANCHE_ITEM_CREATED_TI_TI;
	}

	/**
	 * @return A {@link Set} of {@link PaymentCode}s that are used to put a
	 *         {@link Payment} in a CASHED status.
	 */
	public static Set<PaymentCode> getCashedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		ret.add(PaymentCode.ISSUANCE_CASHED_C_ISC);
		ret.add(PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		ret.add(PaymentCode.TRANCHE_ITEM_CASHED_C_TIC);
		return ret;
	}

	/**
	 * @return A {@link Set} of {@link PaymentCode}s that are used to put a
	 *         {@link Payment} in a CREATED status.
	 */
	public static Set<PaymentCode> getCreatedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		ret.add(PaymentCode.ISSUANCE_CREATED_IS_IS);
		ret.add(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		ret.add(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);
		return ret;
	}

	/**
	 * @return A {@link Set} of {@link PaymentCode}s that are used to put a
	 *         {@link Payment} in a STOP_REQUESTED status.
	 */
	public static Set<PaymentCode> getStopRequestedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
		ret.add(PaymentCode.STOP_STOP_REQUESTED_S_SR);
		return ret;
	}

	/**
	 * @return A {@link Set} of {@link PaymentCode}s that are used to put a
	 *         {@link Payment} in a STOP_REPLACE_REQUESTED status.
	 */
	public static Set<PaymentCode> getStopReplaceRequestedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR);
		ret.add(PaymentCode.STOP_REPLACE_REQUESTED_R_SRR);
		return ret;
	}

	/**
	 * @return A {@link Set} of {@link PaymentCode}s that are used to put a
	 *         {@link Payment} in a STOP_REPLACE_APPROVED status.
	 */
	public static Set<PaymentCode> getStopReplaceApprovedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);
		ret.add(PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
		return ret;
	}

	/**
	 * @return A {@link Set} of {@link PaymentCode}s that are used to put a
	 *         {@link Payment} in a VOID status.
	 */
	public static Set<PaymentCode> getVoidedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
		ret.add(PaymentCode.VOID_HOLD_VOIDED_VH_VH);
		ret.add(PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF);
		ret.add(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
		ret.add(PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO);
		ret.add(PaymentCode.VOID_VOIDED_V_V);
		return ret;
	}

	public static Set<PaymentCode> getVoidReissueApprovedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		ret.add(PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA);
		ret.add(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA);
		ret.add(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
		ret.add(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		return ret;
	}

	public static Set<PaymentCode> getVoidReissueRequestedCodes() {
		// TODO: make static
		final Set<PaymentCode> ret = Sets.newHashSet();
		ret.add(PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR);
		ret.add(PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR);
		ret.add(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR);
		ret.add(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR);
		ret.add(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		return ret;
	}

	public static boolean isReIssueRejected(final PaymentCode paymentCode) {

		return paymentCode == PaymentCode.VOID_DAMASCO_RE_ISSUE_REJECTED_IR_VDJ
				|| paymentCode == PaymentCode.VOID_HOLD_RE_ISSUE_REJECTED_IR_VHJ
				|| paymentCode == PaymentCode.VOID_RE_ISSUE_REJECTED_IR_VJ
				|| paymentCode == PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REJECTED_IR_VFJ
				|| paymentCode == PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REJECTED_IR_VNJ;
	}

	public static boolean isReIssueCompleted(final PaymentCode paymentCode) {
		return paymentCode == PaymentCode.VOID_DAMASCO_RE_ISSUE_COMPLETED_XX_VDX
				|| paymentCode == PaymentCode.VOID_HOLD_RE_ISSUE_COMPLETED_XX_VHX
				|| paymentCode == PaymentCode.VOID_RE_ISSUE_COMPLETED_XX_VX
				|| paymentCode == PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_COMPLETED_XX_VFX
				|| paymentCode == PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_COMPLETED__VNX;
	}
	

	/**
	 * Has a Void been placed on this Payment.
	 * 
	 * @return
	 */
	public static boolean isVoid(final PaymentCode paymentCode) {
		return getVoidedCodes().contains(paymentCode);
	}

	/**
	 * Has a Stop been placed on this Payment.
	 * 
	 * @return
	 */
	public static boolean isStop(final PaymentCode paymentCode) {
		return paymentCode == PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR
				|| paymentCode == PaymentCode.STOP_STOP_REQUESTED_S_SR;
	}

	public static List<PaymentCode> getCheckVoidReissueRequestedCodes() {
		return voidReissueRequestedCodes;
	}

	public static List<PaymentCode> getCheckStopReplaceRequestedCodes() {
		return stopReplaceRequestedCodes;
	}

	public static List<PaymentCode> getCheckVoidReissueApprovedCodes() {
		return voidReissueApprovedCodes;
	}

	public static List<PaymentCode> getCheckStopReplaceApproveCodes() {
		return stopReplaceApprovedCodes;
	}

	public static final Map<PaymentCode, PaymentCode> createdToOutstandingLookup = Maps
			.newHashMap();

	static {
		createdToOutstandingLookup.put(PaymentCode.FIRST_ISSUE_CREATED_FI_FI,
				PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
		createdToOutstandingLookup.put(PaymentCode.ISSUANCE_CREATED_IS_IS,
				PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
		createdToOutstandingLookup.put(PaymentCode.NEW_ISSUE_CREATED_NI_NI,
				PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
		createdToOutstandingLookup.put(PaymentCode.TRANCHE_ITEM_CREATED_TI_TI,
				PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a PaymentStatus.CREATED
	 *            status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.OUTSTANDING
	 */
	public static PaymentCode getOutStandingCodeForGivenCreatedCode(
			final PaymentCode createdPaymentCode) {
		Preconditions.checkArgument(
				getCreatedCodes().contains(createdPaymentCode), String.format(
						"%s is not one of the CREATD codes %s",
						createdPaymentCode, getCreatedCodes()));
		PaymentCode ret = createdToOutstandingLookup.get(createdPaymentCode);
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find OUTSTANDING code for CREATED code %s in lookpMap %s",
								createdPaymentCode, createdToOutstandingLookup));
		return ret;
	}

	public static final Map<PaymentCode, PaymentCode> outstandingToCashedLookup = Maps
			.newHashMap();

	static {
		outstandingToCashedLookup.put(
				PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO,
				PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		outstandingToCashedLookup.put(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO,
				PaymentCode.ISSUANCE_CASHED_C_ISC);
		outstandingToCashedLookup.put(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO,
				PaymentCode.NEW_ISSUE_CASHED_C_NIC);
		outstandingToCashedLookup.put(
				PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO,
				PaymentCode.TRANCHE_ITEM_CASHED_C_TIC);

	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.OUTSTANDING status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.CASHED
	 */
	public static PaymentCode getCashedCodeForGivenOutstandingCode(
			final Payment payment, final PaymentCode outstandingPaymentCode) {
		Preconditions.checkArgument(
				getOutstandingCodes().contains(outstandingPaymentCode), String
						.format("%s is not one of the OUTSTANDING codes %s",
								outstandingPaymentCode, getOutstandingCodes()));
		PaymentCode ret = null;
		if (outstandingPaymentCode == PaymentCode.STOP_LIFT_OUTSTANDING_L_L
				|| outstandingPaymentCode == PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL
				|| outstandingPaymentCode == PaymentCode.STALE_DATE_OUTSTANDING_X_X) {
			if (PaymentCode.FIRST_ISSUE_CREATED_FI_FI == payment
					.getInitialState().getPaymentCode()) {
				ret = outstandingToCashedLookup
						.get(PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
			} else if (PaymentCode.NEW_ISSUE_CREATED_NI_NI == payment
					.getInitialState().getPaymentCode()) {
				ret = outstandingToCashedLookup
						.get(PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
			} else if (PaymentCode.ISSUANCE_CREATED_IS_IS == payment
					.getInitialState().getPaymentCode()) {
				ret = outstandingToCashedLookup
						.get(PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO);
			} else if (PaymentCode.TRANCHE_ITEM_CREATED_TI_TI == payment
					.getInitialState().getPaymentCode()) {
				ret = outstandingToCashedLookup
						.get(PaymentCode.TRANCHE_ITEM_OUTSTANDING_TI_TIO);
			} else {
				throw new IllegalStateException(
						String.format(
								"The initial payment code %s, of the payment %s of is not one of the CREATED codes",
								payment.getInitialState().getPaymentCode(),
								payment.getIdentificatonNo()));
			}
		} else {
			ret = outstandingToCashedLookup.get(outstandingPaymentCode);
		}
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find CASHED code for OUTSTANDING code %s in lookpMap %s",
								outstandingPaymentCode,
								outstandingToCashedLookup));
		return ret;
	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.STOP_REQUESTED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.STOP_CONFIRMED
	 */
	public static PaymentCode getStopConfirmCodeForGivenStopRequestedCode(
			final PaymentCode stopRequestedPaymentCode) {
		Preconditions.checkArgument(
				getStopRequestedCodes().contains(stopRequestedPaymentCode),
				String.format("%s is not one of the STOP_REQUESTED codes %s",
						stopRequestedPaymentCode, getStopRequestedCodes()));
		PaymentCode ret = null;
		if (stopRequestedPaymentCode == PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR) {
			ret = PaymentCode.STOP_DAMASCO_STOP_CONFIRMED_P_PC;
		} else if (stopRequestedPaymentCode == PaymentCode.STOP_STOP_REQUESTED_S_SR) {
			ret = PaymentCode.STOP_STOP_CONFIRMED_S_SC;
		}
		Preconditions.checkState(ret != null, String.format(
				"Cannot find STOP_CONFIRMED code for STOP_REQUESTED code %s",
				stopRequestedPaymentCode));
		return ret;
	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.STOP_REPLACE_REQUESTED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.STOP_REPLACE_APPROVED
	 */
	public static PaymentCode getStopReplaceApprovedCodeForGivenStopRequestedCode(
			final PaymentCode stopReplaceRequestedPaymentCode) {
		Preconditions.checkArgument(
				getStopReplaceRequestedCodes().contains(
						stopReplaceRequestedPaymentCode), String.format(
						"%s is not one of the STOP_REPLACE_REQUESTED codes %s",
						stopReplaceRequestedPaymentCode,
						getStopReplaceRequestedCodes()));
		PaymentCode ret = null;
		if (stopReplaceRequestedPaymentCode == PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR) {
			ret = PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA;
		} else if (stopReplaceRequestedPaymentCode == PaymentCode.STOP_REPLACE_REQUESTED_R_SRR) {
			ret = PaymentCode.STOP_REPLACE_APPROVED_R_SRA;
		}
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find STOP_REPLACE_APPROVED code for STOP_REPLACE_REQUESTED code %s",
								stopReplaceRequestedPaymentCode));
		return ret;
	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.STOP_REPLACE_REQUESTED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.STOP_REPLACE_REJECTED
	 */
	public static PaymentCode getStopReplaceRejectedCodeForGivenStopRequestedCode(
			final PaymentCode stopReplaceRequestedPaymentCode) {
		Preconditions.checkArgument(
				getStopReplaceRequestedCodes().contains(
						stopReplaceRequestedPaymentCode), String.format(
						"%s is not one of the STOP_REPLACE_REQUESTED codes %s",
						stopReplaceRequestedPaymentCode,
						getStopReplaceRequestedCodes()));
		PaymentCode ret = null;
		if (stopReplaceRequestedPaymentCode == PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR) {
			ret = PaymentCode.STOP_DAMASCO_REPLACE_REJECTED_RR_PRJ;
		} else if (stopReplaceRequestedPaymentCode == PaymentCode.STOP_REPLACE_REQUESTED_R_SRR) {
			ret = PaymentCode.STOP_REPLACE_REJECTED_RR_SRJ;
		}
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find STOP_REPLACE_REJECTED code for STOP_REPLACE_REQUESTED code %s",
								stopReplaceRequestedPaymentCode));
		return ret;
	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.STOP_REQUESTED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.STOP_REJECTED
	 */
	public static PaymentCode getStopRejectedCodeForGivenStopRequestedCode(
			final PaymentCode stopRequestedPaymentCode) {
		Preconditions.checkArgument(
				getStopRequestedCodes().contains(stopRequestedPaymentCode),
				String.format("%s is not one of the STOP_REQUESTED codes %s",
						stopRequestedPaymentCode, getStopRequestedCodes()));
		PaymentCode ret = null;
		if (stopRequestedPaymentCode == PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR) {
			ret = PaymentCode.STOP_DAMASCO_STOP_REJECTED_SR_PJ;
		} else if (stopRequestedPaymentCode == PaymentCode.STOP_STOP_REQUESTED_S_SR) {
			ret = PaymentCode.STOP_STOP_REJECTED_SR_SJ;
		}
		Preconditions.checkState(ret != null, String.format(
				"Cannot find STOP_REJECTED code for STOP_REQUESTED code %s ",
				stopRequestedPaymentCode));
		return ret;
	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.STOP_REPLACE_APPROVED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.STOP_REPLACE_COMPLETED
	 */
	public static PaymentCode getStopReplaceCompletedCodeForGivenStopApprovedCode(
			final PaymentCode stopApprovedPaymentCode) {
		Preconditions.checkArgument(
				getCheckStopReplaceApproveCodes().contains(
						stopApprovedPaymentCode), String.format(
						"%s is not one of the STOP_REPLACE_APPROVED codes %s",
						stopApprovedPaymentCode,
						getCheckStopReplaceApproveCodes()));
		PaymentCode ret = null;
		if (stopApprovedPaymentCode == PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA) {
			ret = PaymentCode.STOP_DAMASCO_REPLACE_COMPLETED_XX_PRX;
		} else if (stopApprovedPaymentCode == PaymentCode.STOP_REPLACE_APPROVED_R_SRA) {
			ret = PaymentCode.STOP_REPLACE_COMPLETED_XX_SRX;
		}
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find STOP_REPLACE_COMPLETED code for STOP_REPLACE_APPROVED code %s ",
								stopApprovedPaymentCode));
		return ret;
	}

	public static final Map<PaymentCode, PaymentCode> voidReissueApprovedToVoidReissueCompletedLookup = Maps
			.newHashMap();

	static {
		voidReissueApprovedToVoidReissueCompletedLookup.put(
				PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA,
				PaymentCode.VOID_DAMASCO_RE_ISSUE_COMPLETED_XX_VDX);
		voidReissueApprovedToVoidReissueCompletedLookup.put(
				PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA,
				PaymentCode.VOID_HOLD_RE_ISSUE_COMPLETED_XX_VHX);
		voidReissueApprovedToVoidReissueCompletedLookup.put(
				PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA,
				PaymentCode.VOID_RE_ISSUE_COMPLETED_XX_VX);
		voidReissueApprovedToVoidReissueCompletedLookup.put(
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA,
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_COMPLETED_XX_VFX);
		voidReissueApprovedToVoidReissueCompletedLookup.put(
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA,
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_COMPLETED__VNX);

	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.VOID_RE_ISSUE_APPROVED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.VOID_RE_ISSUE_COMPLETED
	 */
	public static PaymentCode getVoidReissueCompletedCodeForGivenVoidReissueApprovedCode(
			final PaymentCode reissueApprovedPaymentCode) {
		Preconditions.checkArgument(
				getVoidReissueApprovedCodes().contains(
						reissueApprovedPaymentCode), String.format(
						"%s is not one of the VOID_RE_ISSUE_APPROVED codes %s",
						reissueApprovedPaymentCode,
						getVoidReissueApprovedCodes()));
		PaymentCode ret = voidReissueApprovedToVoidReissueCompletedLookup
				.get(reissueApprovedPaymentCode);
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find VOID_RE_ISSUE_COMPLETED code for VOID_RE_ISSUE_APPROVED code %s in lookpMap %s",
								reissueApprovedPaymentCode,
								voidReissueApprovedToVoidReissueCompletedLookup));
		return ret;
	}

	
	public static final Map<PaymentCode, PaymentCode> voidToVoidReissueRequestedLookup = Maps
			.newHashMap();

	static {
		voidToVoidReissueRequestedLookup.put(
				PaymentCode.VOID_DAMASCO_VOIDED_VD_VD,
				PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR);
		voidToVoidReissueRequestedLookup.put(
				PaymentCode.VOID_HOLD_VOIDED_VH_VH,
				PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR);
		voidToVoidReissueRequestedLookup.put(
				PaymentCode.VOID_VOIDED_V_V,
				PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		voidToVoidReissueRequestedLookup.put(
				PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF,
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR);
		voidToVoidReissueRequestedLookup.put(
				PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN,
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR);

	}
	

	public static PaymentCode getVoidReissueRequestedCodeForGivenVoidCode(final PaymentCode voidPaymentCode) {
		Preconditions.checkArgument(getVoidedCodes().contains(voidPaymentCode),
						String.format("%s is not one of the VOID codes %s", voidPaymentCode, getVoidedCodes()));
		PaymentCode ret = voidToVoidReissueRequestedLookup.get(voidPaymentCode);
		Preconditions.checkState(ret != null,
						String.format("Cannot find VOID_RE_ISSUE_REQUESTED code for VOID code %s in lookpMap %s",
								voidPaymentCode,
								voidToVoidReissueRequestedLookup));
		return ret;
	}
	
	public static final Map<PaymentCode, PaymentCode> voidReissueRequestedToVoidReissueApprovedLookup = Maps
			.newHashMap();

	static {
		voidReissueRequestedToVoidReissueApprovedLookup.put(
				PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR,
				PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		voidReissueRequestedToVoidReissueApprovedLookup.put(
				PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR,
				PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA);
		voidReissueRequestedToVoidReissueApprovedLookup.put(
				PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR,
				PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		voidReissueRequestedToVoidReissueApprovedLookup.put(
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR,
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA);
		voidReissueRequestedToVoidReissueApprovedLookup.put(
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR,
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);

	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.VOID_RE_ISSUE_REQUESTED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.VOID_RE_ISSUE_APPROVED
	 */
	public static PaymentCode getVoidReissueApprovedCodeForGivenVoidReissueRequestedCode(
			final PaymentCode reissueRequestedPaymentCode) {
		Preconditions
				.checkArgument(
						getVoidReissueRequestedCodes().contains(
								reissueRequestedPaymentCode),
						String.format(
								"%s is not one of the VOID_RE_ISSUE_REQUESTED codes %s",
								reissueRequestedPaymentCode,
								getVoidReissueRequestedCodes()));
		PaymentCode ret = voidReissueRequestedToVoidReissueApprovedLookup
				.get(reissueRequestedPaymentCode);
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find VOID_RE_ISSUE_APPROVED code for VOID_RE_ISSUE_REQUESTED code %s in lookpMap %s",
								reissueRequestedPaymentCode,
								voidReissueRequestedToVoidReissueApprovedLookup));
		return ret;
	}

	public static final Map<PaymentCode, PaymentCode> voidReissueRequestedToVoidReissueRejectedLookup = Maps
			.newHashMap();

	static {
		voidReissueRequestedToVoidReissueRejectedLookup.put(
				PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR,
				PaymentCode.VOID_DAMASCO_RE_ISSUE_REJECTED_IR_VDJ);
		voidReissueRequestedToVoidReissueRejectedLookup.put(
				PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR,
				PaymentCode.VOID_HOLD_RE_ISSUE_REJECTED_IR_VHJ);
		voidReissueRequestedToVoidReissueRejectedLookup.put(
				PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR,
				PaymentCode.VOID_RE_ISSUE_REJECTED_IR_VJ);
		voidReissueRequestedToVoidReissueRejectedLookup.put(
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR,
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REJECTED_IR_VFJ);
		voidReissueRequestedToVoidReissueRejectedLookup.put(
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR,
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REJECTED_IR_VNJ);

	}

	/**
	 * @param paymentCode
	 *            - A {@link PaymentCode} that indicates a
	 *            PaymentStatus.VOID_RE_ISSUE_REQUESTED status.
	 * @return - The corresponding {@link PaymentCode} that indicates a
	 *         PaymentStatus.VOID_RE_ISSUE_REJECTED
	 */
	public static PaymentCode getVoidReissueRejectedCodeForGivenVoidReissueRequestedCode(
			final PaymentCode reissueRequestedPaymentCode) {
		Preconditions
				.checkArgument(
						getVoidReissueRequestedCodes().contains(
								reissueRequestedPaymentCode),
						String.format(
								"%s is not one of the VOID_RE_ISSUE_REQUESTED codes %s",
								reissueRequestedPaymentCode,
								getVoidReissueRequestedCodes()));
		PaymentCode ret = voidReissueRequestedToVoidReissueRejectedLookup
				.get(reissueRequestedPaymentCode);
		Preconditions
				.checkState(
						ret != null,
						String.format(
								"Cannot find VOID_RE_ISSUE_REJECTED code for VOID_RE_ISSUE_REQUESTED code %s in lookpMap %s",
								reissueRequestedPaymentCode,
								voidReissueRequestedToVoidReissueRejectedLookup));
		return ret;
	}
	
	/**
	 * Can a request for re-issue be placed on an Payment with the given paymentCode. 
	 * @param paymentCode
	 * @return
	 */
	public static boolean canRequestReissue(final PaymentCode paymentCode) {
		if(getVoidedCodes().contains(paymentCode) && paymentCode != PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO) {
			return true;
		} 
		return false;
	}

}
