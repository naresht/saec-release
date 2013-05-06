package com.bfds.saec.dao;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentLetterCode;
import com.bfds.saec.domain.Tranch;
import com.bfds.saec.domain.dto.TranchAssignmentSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchRecordDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface TranchDao {
	/**
	 * @param criteria - Search Parameters for querying {@link Payment}s that will be included in a tranch. 
	 * @return
	 */
	Long getPaymentCountForTranchAssignment(TranchAssignmentSearchCriteriaDto criteria);

	List<TranchAssignmentSearchRecordDto> getpaymentsForTranchAssignment(TranchAssignmentSearchCriteriaDto criteria);
	
	/**
	 * @param criteria - Search Parameters for querying {@link Payment}s that will be included in a tranch.
	 * @return - Sum of all paymentAmounts of {@link Payment}s that will be selected by the specified {@link TranchAssignmentSearchCriteriaDto}
	 */
	BigDecimal  getpaymentDistributionTotalForTranchAssignment(TranchAssignmentSearchCriteriaDto criteria);
	
	/**
	 * Assigns the {@link Tranch} to the selected {@link Payment}s. The {@link Tranch} will be created if it does not exist. 
	 * 
	 * @param tranch - The tranchCode to apply to the {@link Payment}s specified by criteria - {@link TranchAssignmentSearchCriteriaDto}
	 * @param criteria - The criteria {@link TranchAssignmentSearchCriteriaDto} to select/filter {@link Payment}s.
	 * @throws - {@link IllegalArgumentException} - If the given tranchCode does not exist in DB.
	 */
	void executePaymentTranchAssignment(PaymentLetterCode letterCode,Tranch tranch, TranchAssignmentSearchCriteriaDto... criteria);
	
	void executePaymentTranchAssignmentAsync(PaymentLetterCode letterCode,Tranch tranch,
			final TranchAssignmentSearchCriteriaDto... criterias);
	
	void removePaymentsFromTranch(final String tranchCode, Set<Long> paymentsToRemove);

}
