package com.bfds.saec.tranch.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.bfds.saec.domain.PaymentLetterCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.dao.TranchDao;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Tranch;
import com.bfds.saec.domain.dto.TranchAssignmentSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchRecordDto;
import com.google.common.base.Preconditions;

@Repository("tranchService")
public class TranchServiceImpl implements TranchService {

	@Autowired
	private TranchDao tranchDao;
	
	 private TaskExecutor executorService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bfds.saec.dao.TranchDao#getpaymentsForTranchAssignment(com.bfds.saec
	 * .domain.dto.TranchAssignmentSearchCriteriaDto)
	 */
	@Override
	public List<TranchAssignmentSearchRecordDto> getpaymentsForTranchAssignment(
			final TranchAssignmentSearchCriteriaDto criteria) {
		checkTranchAssignmentNotInProgress();
		return tranchDao.getpaymentsForTranchAssignment(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bfds.saec.dao.TranchDao#getPaymentCountForTranchAssignment(com.bfds
	 * .saec.domain.dto.TranchAssignmentSearchCriteriaDto)
	 */
	@Override
	public Long getPaymentCountForTranchAssignment(
			final TranchAssignmentSearchCriteriaDto criteria) {
		checkTranchAssignmentNotInProgress();
		return tranchDao.getPaymentCountForTranchAssignment(criteria);
	}

	@Override
	public BigDecimal getpaymentDistributionTotalForTranchAssignment(
			final TranchAssignmentSearchCriteriaDto criteria) {
		checkTranchAssignmentNotInProgress();
		return tranchDao.getpaymentDistributionTotalForTranchAssignment(criteria);
	}

	@Override
	public void executePaymentTranchAssignment(PaymentLetterCode letterCode,Tranch tranch,
			final TranchAssignmentSearchCriteriaDto... criterias) {
		Preconditions.checkArgument(StringUtils.hasText(tranch.getCode()),
				"Tranch code cannot be empty/null");
		checkTranchAssignmentNotInProgress();		
		tranchDao.executePaymentTranchAssignment(letterCode,tranch, criterias);
		
	}


	
	private void checkTranchAssignmentNotInProgress() {
		if(Tranch.anyTranchAssignmentInProgress()) {
			throw new TranchException("Exception in Tranch Assignment", "Tranch assignment in progress. Try after some time.");
		}
	}

	@Override
	@Transactional
	public void removePaymentsFromTranch(String tranchCode,
			Set<Long> paymentsToRemove) {
		tranchDao.removePaymentsFromTranch(tranchCode, paymentsToRemove);
		
	}
		

}