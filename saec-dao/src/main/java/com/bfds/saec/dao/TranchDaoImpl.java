package com.bfds.saec.dao;

import com.bfds.saec.dao.util.JpaUtil;
import com.bfds.saec.domain.*;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchRecordDto;
import com.bfds.saec.util.SaecArrayUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

@Repository("tranchDao")
public class TranchDaoImpl implements TranchDao {

	@PersistenceContext(unitName="entityManagerFactory")
	private EntityManager entityManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	
	@PersistenceContext(unitName="entityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

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
		final List<TranchAssignmentSearchRecordDto> ret = new ArrayList<TranchAssignmentSearchRecordDto>();
		final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		final Root<Payment> check = cq.from(Payment.class);
		cq.select(getChecksForReleaseSelection(check));
		buildPaymentsForTranchAssignmentWhereClause(cb, cq, check, criteria);
		buildPaymentsForTranchAssignmentOrderByClause(cb, cq, check, criteria);
		final List<Tuple> results = this.getEntityManager().createQuery(cq)
				.setFirstResult(criteria.getFirstResult())
				.setMaxResults(criteria.getMaxResults()).getResultList();
		for (final Tuple t : results) {
			TranchAssignmentSearchRecordDto rec = TranchAssignmentSearchRecordDto.TranchAssignmentSearchRecordDtoBuilder
					.build(t.toArray());
			ret.add(rec);
		}
		return ret;
	}

	private void buildPaymentsForTranchAssignmentOrderByClause(
			final CriteriaBuilder cb, CriteriaQuery<Tuple> cq,
			final Root<Payment> check,
			final TranchAssignmentSearchCriteriaDto criteria) {
		final List<Order> orderBys = Lists.newArrayList();

		if (criteria.hasSortFields()) {
			for (final String field : criteria.getSortFields()) {
				for (final Path param : JpaUtil.<Object> getPaths(check, field)) {
					if (criteria.getSortOrder() == CheckReleaseSearchCriteriaDto.SORT_ORDER_ASC) {
						orderBys.add(cb.asc(param));
					}
					else {
						orderBys.add(cb.desc(param));
					}
				}
			}
		}
		if (criteria.hasAmountFilter()) {
			orderBys.add(cb.asc(check.<PaymentCalc>get("paymentCalc").get("nettAmount")));
		}
		if (orderBys.size() > 0) {
			cq.orderBy(orderBys);
		}
		else {
			// Default sort order.
			cq.orderBy(cb.asc(check.get("payTo").get("referenceNo")));
		}
	}

	private <T> void buildPaymentsForTranchAssignmentWhereClause(
			final CriteriaBuilder cb, final CriteriaQuery<T> cq,
			final Root<Payment> check,
			final TranchAssignmentSearchCriteriaDto criteria) {
		final List<Predicate> expressions = Lists.newArrayList();
		if (!criteria.isValid()) {
			throw new IllegalArgumentException("Invalid search criteria. " + criteria.validate());
		}
		if(StringUtils.hasText(criteria.getTranchCode())) { // This implies we want to get the Payments assigned to a tranch.
			expressions.add(cb.equal(check.get("tranch").get("code"), criteria.getTranchCode()));
		} else {
			expressions.add(cb.isNull(check.get("tranch")));
		}
		
		expressions.add(cb.equal(check.get("paymentType"),
				criteria.getPaymentType()));
		// expressions.add(cb.equal(check.get("paymentStatus"),
		// PaymentStatus.CREATED)); // TODO: What should the status be ?

		expressions.addAll(addSearchFieldFilters(cb, check, criteria));
		expressions.addAll(addExcludesFilters(cb, check, criteria));

		if (!criteria.getFilters().isEmpty()) {
			for (final Map.Entry<String, Object> entry : criteria
					.getResolvedFilters().entrySet()) {
				Object value = entry.getValue();
				if (value instanceof String) {
					expressions.add(cb.like(
							cb.lower(JpaUtil.concat(
									cb,
									JpaUtil.<String> getPaths(check,
											entry.getKey()))), (String) "%"
									+ value + "%"));
				}
				else if (value instanceof Number) {
					expressions.add(cb.equal(
							JpaUtil.<Object> getPaths(check, entry.getKey())
									.get(0), value));
				}
			}
		}
		if (!expressions.isEmpty()) {
			cq.where((Predicate[]) expressions
					.toArray(new Predicate[expressions.size()]));
		}
	}

	/**
	 * Exclude {@link Payment#id}s that have to be excluded from tranch.
	 * 
	 * @param cb - The {@link CriteriaBuilder} to build query
	 * @param check - The {@link Root} againts which the filers will be applied
	 * @param criteria - The searh criteria object -
	 * {@link TranchAssignmentSearchCriteriaDto}
	 * @return - A {@link List} of {@link Predicate} that represent the filters
	 * to be applied to the query.
	 */
	private Collection<? extends Predicate> addExcludesFilters(
			CriteriaBuilder cb, Root<Payment> check,
			TranchAssignmentSearchCriteriaDto criteria) {
		final List<Predicate> expressions = Lists.newArrayList();
		if (!criteria.isExcludeAll()) {
			if (criteria.getExcludes().size() > 0) {
				expressions.add(cb.not(check.get("id").in(
						criteria.getExcludes())));
			}
		}
		else {
			if (criteria.getIncludes().size() > 0) {
				expressions.add(check.get("id").in(criteria.getIncludes()));
			}
		}

		return expressions;
	}

	/**
	 * Apply search field filters on tranch.
	 * 
	 * @param cb - The {@link CriteriaBuilder} to build query
	 * @param check - The {@link Root} againts which the filers will be applied
	 * @param criteria - The searh criteria object -
	 * {@link TranchAssignmentSearchCriteriaDto}
	 * @return - A {@link List} of {@link Predicate} that represent the filters
	 * to be applied to the query.
	 */
	private List<Predicate> addSearchFieldFilters(final CriteriaBuilder cb,
			final Root<Payment> check,
			final TranchAssignmentSearchCriteriaDto criteria) {
		final List<Predicate> expressions = Lists.newArrayList();
		if (criteria.getFromAmount() > 0) {
			expressions.add(cb.ge(check.<PaymentCalc>get("paymentCalc").<Number>get("nettAmount"),
					new BigDecimal(criteria.getFromAmount())));
		}
		if (criteria.getToAmount() > 0) {
			expressions.add(cb.le(check.<PaymentCalc>get("paymentCalc").<Number>get("nettAmount"),
					new BigDecimal(criteria.getToAmount())));
		}
		if (StringUtils.hasText(criteria.getFromAccountNo())) {
			expressions.add(cb.between(check.<Claimant> get("payTo")
					.<String> get("referenceNo"), String.valueOf(criteria
					.getFromAccountNo()), String.valueOf(criteria
					.getToAccountNo())));
		}
		if (StringUtils.hasText(criteria.getFromCheckNo())) {
			expressions.add(cb.between(check.<String> get("identificatonNo"),
					String.valueOf(criteria.getFromCheckNo()),
					String.valueOf(criteria.getToCheckNo())));
		}
		if (criteria.getToAmount() > 0) {
			expressions.add(cb.le(check.<PaymentCalc>get("paymentCalc").<Number>get("nettAmount"),
					new BigDecimal(criteria.getToAmount())));
		}
		if (StringUtils.hasText(criteria.getAccountNo())) {
			expressions.add(cb.equal(check.<Claimant> get("payTo")
					.<String> get("referenceNo"), String.valueOf(criteria
					.getAccountNo())));
		}
		if (StringUtils.hasText(criteria.getBrokerId())) {
			expressions.add(cb.equal(check.<Claimant> get("payTo")
					.<String> get("brokerId"), criteria.getBrokerId().trim()));
		}
		if (SaecArrayUtils.getNonEmptyValues(criteria.getStateCode()).length > 0) {
			final From<Claimant, ClaimantAddress> address = check.join("payTo")
					.join("addresses");
			expressions.add(address.<ClaimantAddress> get("address")
					.<String> get("stateCode").in(criteria.getStateCode()));
			expressions.add(cb.equal(address.<ClaimantAddress> get("address")
					.<Boolean> get("mailingAddress"), Boolean.TRUE));

		}
		return expressions;
	}

	private CompoundSelection<Tuple> getChecksForReleaseSelection(
			final Root<Payment> check) {
		final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();

		return cb.tuple(
				check.get("id"),
				check.get("identificatonNo"),
				check.get("payTo").get("id"),
				check.get("payTo").get("referenceNo"),
				check.<PaymentCalc>get("paymentCalc").get("nettAmount"),
				check.get("payTo").get("claimantRegistration").get("lines")
						.get("registration1"),
				check.get("payTo").get("claimantRegistration").get("lines")
						.get("registration2"),
				check.get("payTo").get("claimantRegistration").get("lines")
						.get("registration3"),
				check.get("payTo").get("claimantRegistration").get("lines")
						.get("registration4"),
				check.get("payTo").get("claimantRegistration").get("lines")
						.get("registration5"),
				check.get("payTo").get("claimantRegistration").get("lines")
						.get("registration6"),
				check.get("checkAddress").get("address").get("address1"),
				check.get("checkAddress").get("address").get("address2"),
				check.get("checkAddress").get("address").get("address3"),
				check.get("checkAddress").get("address").get("address4"),
				check.get("checkAddress").get("address").get("address5"),
				check.get("checkAddress").get("address").get("address6"),
				check.get("checkAddress").get("address").get("city"),
				check.get("checkAddress").get("address").get("stateCode"),
				check.get("checkAddress").get("address").get("zipCode")
						.get("zip"), check.get("checkAddress").get("address")
						.get("zipCode").get("ext"), check.get("checkAddress")
						.get("address").get("countryCode"), check
						.get("paymentType"));
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
		final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		final Root<Payment> check = cq.from(Payment.class);
		cq.select(cb.function("count", Long.class, check.get("id")));
		buildPaymentsForTranchAssignmentWhereClause(cb, cq, check, criteria);
		return this.getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public BigDecimal getpaymentDistributionTotalForTranchAssignment(
			final TranchAssignmentSearchCriteriaDto criteria) {
		final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
		final Root<Payment> check = cq.from(Payment.class);
		cq.select(cb.function("sum", BigDecimal.class,
				check.<PaymentCalc>get("paymentCalc").<Number>get("nettAmount")));
		buildPaymentsForTranchAssignmentWhereClause(cb, cq, check, criteria);

		return this.getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	@Async
	public void executePaymentTranchAssignment(final PaymentLetterCode letterCode,final Tranch tranch_,
			final TranchAssignmentSearchCriteriaDto... criterias) {
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		final Tranch tranch = tt.execute(new TransactionCallback<Tranch>() {
			@Override
			public Tranch doInTransaction(TransactionStatus status) {
				Tranch newTranch = createTranchIfItDoesNotExisit(tranch_);	
				lockTranchAssignmentProcess(newTranch);
				return newTranch;
			}
		});
		try {			
			for (TranchAssignmentSearchCriteriaDto criteria : criterias) {
				TranchAssignmentSearchCriteriaDto newCriteria = criteria.clone();
				newCriteria.setMaxResults(1000);
				newCriteria.setFirstResult(0);
				List<TranchAssignmentSearchRecordDto> peymantsToUpdateList = getpaymentsForTranchAssignment(newCriteria);
				
				while (!peymantsToUpdateList.isEmpty()) {
					final List<TranchAssignmentSearchRecordDto> tempList = peymantsToUpdateList;
					tt.execute(new TransactionCallbackWithoutResult() {
						
						@Override
						protected void doInTransactionWithoutResult(TransactionStatus status) {
							addPaymentsToTranch(letterCode,tranch, tempList);
							tranch.flush();
							tranch.clear();
							
						}
					});
					peymantsToUpdateList = getpaymentsForTranchAssignment(newCriteria);
				}
			}
		} finally  {
			tt.execute(new TransactionCallbackWithoutResult() {
				
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					unlockTranchAssignmentProcess(tranch);					
				}
			});
		}
	}
	
	
	
	private Tranch createTranchIfItDoesNotExisit(final Tranch tranch) {
		Tranch ret = Tranch.findByCode(tranch.getCode());
		if (ret == null) {
			tranch.persist();
			tranch.flush();
			ret = tranch;
		}
		return ret;
	}	
	
	/**
	 * For unit testing only. To avoid @Async
	 * @param tranch
	 * @param criterias
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void executePaymentTranchAssignmentAsync(PaymentLetterCode letterCode,Tranch tranch,
			final TranchAssignmentSearchCriteriaDto... criterias) {
		this.executePaymentTranchAssignment(letterCode,tranch, criterias);
	}
	

	private void lockTranchAssignmentProcess(Tranch tranch) {
		tranch.setIstranchAssignmentInProgress(true);
		tranch.flush();
	}

	
	private void unlockTranchAssignmentProcess(Tranch tranch) {
		tranch.setIstranchAssignmentInProgress(false);
		tranch.merge();
		tranch.flush();
	}

	private void addPaymentsToTranch(PaymentLetterCode letterCode,Tranch tranch,
			List<TranchAssignmentSearchRecordDto> peymantsToUpdateList) {
		for (final TranchAssignmentSearchRecordDto tranchAssignmentSearchRecordDto : peymantsToUpdateList) {
			final Payment payment = Payment
					.findPayment(tranchAssignmentSearchRecordDto.getId());
			payment.setTranch(tranch);
			payment.setLetterCode(letterCode);
			payment.setSkipActivityGeneration(true);
			payment.merge();
		}
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void removePaymentsFromTranch(final String tranchCode,
			final Set<Long> paymentsToRemove) {
		
		for (final Long paymentId : paymentsToRemove) {
			final Payment payment = Payment.findPayment(paymentId);
			payment.setTranch(null);
			payment.merge();
		}
		entityManager.flush();
		entityManager.clear();
		
	}

}