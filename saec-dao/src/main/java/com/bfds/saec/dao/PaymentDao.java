package com.bfds.saec.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.CheckSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

public interface PaymentDao extends Serializable{
	
	Long getCheckCountForRelease(CheckReleaseSearchCriteriaDto criteria);

	List<CheckSearchRecordDto> getChecksForRelease(
			CheckReleaseSearchCriteriaDto criteria);

	void processManualRelease(List<CheckSearchRecordDto> list,
			PaymentType paymentType);

	void processBulkRelease(final CheckReleaseSearchCriteriaDto criteria,
			Collection<Long> excludes, String comments);

	List<Payment> loadPaymentsForReturnOfFunding(String referenceNo,
			String paymentIdentificationNo);

	List<Payment> loadPaymentsForRPO(String referenceNo,
			String paymentIdentificationNo);

	/**
	 * @deprecated use {@link Payment#findReissueApprovedChecksOlderThan(int)}
	 */
	@Deprecated
	List<Payment> findReissueApprovedChecksOlderThan(int days);

	/**
	 * @deprecated use {@link Payment#findAllCreatedStatusChecks()}
	 */
	@Deprecated
	List<Payment> findAllCreatedStatusChecks();

	/**
	 * @deprecated use
	 *             {@link Payment#findCheckByNumberAndAmount(String, double)}
	 */
	@Deprecated
	Payment findCheckByNumberAndAmount(String identificatonNo, double amount);

	Payment getCheckForStopLift(String paymentIdentificationNo);

	Payment getCheckForStopVoidReversal(String paymentIdentificationNo);

	Long getOutstandingCheckCount();

	Long getStaleDatedCheckCount();

	/**
	 * Method to get the PaymentCode of Payment's Previous state for given
	 * Payment's :paymentId and current :paymentCode
	 */

	PaymentCode getPreviousPaymentCode(Long paymentId, PaymentCode paymentCode);

	public List<Payment> getPaymentsByClaimantIdAndStatus(
			final Long claimantId, List<PaymentCode> paymentCodes);
}
