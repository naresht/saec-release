package com.bfds.saec.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.dao.util.JpaUtil;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.audit.EntityWithAuditVo;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.CheckSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.SaecDateUtils;
import com.google.common.base.Preconditions;

@Repository("paymentDao")
public class PaymentDaoImpl implements PaymentDao {
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;

	@Autowired
	private transient EntityAuditDao entityAuditDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public List<CheckSearchRecordDto> getChecksForRelease(
			CheckReleaseSearchCriteriaDto criteria) {
		List<CheckSearchRecordDto> ret = new ArrayList<CheckSearchRecordDto>();
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Payment> check = cq.from(Payment.class);
		cq.select(getChecksForReleaseSelection(check,criteria));
		buildChecksForReleaseWhereClause(cb, cq, check, criteria);
		buildChecksForReleaseOrderByClause(cb, cq, check, criteria);
		List<Tuple> results = this.getEntityManager().createQuery(cq)
				.setFirstResult(criteria.getFirstResult())
				.setMaxResults(criteria.getMaxResults()).getResultList();
		for (Tuple t : results) {
			CheckSearchRecordDto rec = CheckSearchRecordDto.CheckSearchRecordVoBuilder
					.build(t.toArray());
			if (criteria.isBulk() && !criteria.isExcludeAllByDefault()) {
				rec.setApproved(true);
			}
			ret.add(rec);
		}
		return ret;
	}

	private void buildChecksForReleaseOrderByClause(CriteriaBuilder cb,
			CriteriaQuery<Tuple> cq, Root<Payment> check,
			CheckReleaseSearchCriteriaDto criteria) {
		List<Order> orderBys = new ArrayList<Order>();
		if (criteria.emptySearchFields()) {
			orderBys.add(cb.asc(check.get("statusChangeDate")));
			return;
		}
		if (criteria.hasSortFields()) {
			for (String field : criteria.getSortFields()) {
				for (Path param : JpaUtil.<Object> getPaths(check, field)) {
					if (criteria.getSortOrder() == CheckReleaseSearchCriteriaDto.SORT_ORDER_ASC) {
						orderBys.add(cb.asc(param));
					} else {
						orderBys.add(cb.desc(param));
					}
				}
			}
		}
	
		// Removed Extra Orederby conditions,due to those conditions the query is having duplicate properties. 
		
		
		if (orderBys.size() > 0) {
			cq.orderBy(orderBys);
		}
	}

	private <T> void buildChecksForReleaseWhereClause(CriteriaBuilder cb,
			CriteriaQuery<T> cq, Root<Payment> check,
			CheckReleaseSearchCriteriaDto criteria) {
		List<Predicate> expressions = new ArrayList<Predicate>();
		if (!criteria.isValid()) {
			throw new IllegalArgumentException("Invalid search criteria.");
		}
		expressions.add(check.get("paymentCode").in(
				CheckReleaseSearchCriteriaDto.getApprovalRequestCodes(criteria
						.getPaymentType())));
		expressions.add(cb.equal(check.get("paymentType"),
				criteria.getPaymentType()));
		if (criteria.getFromAmount() > 0) {
			expressions.add(cb.ge(check.<PaymentCalc> get("paymentCalc")
					.<Number> get("nettAmount"),
					new BigDecimal(criteria.getFromAmount())));
		}
		if (criteria.getToAmount() > 0) {
			expressions.add(cb.le(check.<PaymentCalc> get("paymentCalc")
					.<Number> get("nettAmount"),
					new BigDecimal(criteria.getToAmount())));
		}
		if (criteria.hasDateFilter()) {
			criteria.setToDate(getDateWithMaxTimeForDay(criteria));
			expressions.add(cb.between(check.<Date> get("statusChangeDate"),
					SaecDateUtils.getMinIfNull(criteria.getFromDate()),
					SaecDateUtils.getMaxIfNull(criteria.getToDate())));
		}

		if (!criteria.getFilters().isEmpty()) {
			for (Map.Entry<String, Object> entry : criteria
					.getResolvedFilters().entrySet()) {
				Object value = entry.getValue();
				if (value instanceof String) {
					expressions.add(cb.like(
							cb.lower(JpaUtil.concat(
									cb,
									JpaUtil.<String> getPaths(check,
											entry.getKey()))), (String) "%"
									+ value + "%"));
				} else if (value instanceof Number) {
					expressions.add(cb.equal(
							JpaUtil.<Object> getPaths(check, entry.getKey())
									.get(0), value));
				} else if (value instanceof int[]) { // TODO: int[] is assumed
														// to be date in [m, d,
														// y]

					final Expression<String> dateAsString = cb.function(
							"concat",
							String.class,
							cb.function("month", String.class,
									check.get(entry.getKey())),
							cb.function("day", String.class,
									check.get(entry.getKey())),
							cb.function("year", String.class,
									check.get(entry.getKey())));
					expressions.add(cb.like(dateAsString,
							asDateString((int[]) value) + "%"));
				}

			}
		}
		if (!expressions.isEmpty()) {
			cq.where((Predicate[]) expressions
					.toArray(new Predicate[expressions.size()]));
		}
	}

	private String asDateString(int[] value) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length; i++) {
			if (value[i] > 0) {
				sb.append(value[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * @param check
	 * @param criteria
	 * to determine the replaced check# on the UI,verifying whether a WIRE has the reissue and then we will take the check# of the reissued check,
	 * In the case of a check we will take the orignal check# of the parent check. 
	 */
	private CompoundSelection<Tuple> getChecksForReleaseSelection(
			Root<Payment> check,CheckReleaseSearchCriteriaDto criteria) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		
		From<Payment, MailObjectAddress> address = check.join("checkAddress",JoinType.LEFT);
		
		From<Payment, Payment> payments;
		if(criteria.getPaymentType().equals(PaymentType.WIRE)){
		payments = check.join("reissueOf",JoinType.LEFT);
		}
		else{
			payments = check;
		}
		return cb.tuple(check.get("id"),payments.get("identificatonNo"),
				check.<PaymentCode> get("paymentCode"),
				check.get("payTo").get("id"),
				check.get("payTo").get("referenceNo"),
				check.<PaymentCalc> get("paymentCalc").get("nettAmount"), 
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
				address.get("address").get("address1"), address.get("address")
						.get("address2"), address.get("address")
						.get("address3"), address.get("address")
						.get("address4"), address.get("address")
						.get("address5"), address.get("address")
						.get("address6"), address.get("address").get("city"),
				address.get("address").get("stateCode"), address.get("address")
						.get("zipCode").get("zip"),
				address.get("address").get("zipCode").get("ext"),
				address.get("address").get("countryCode"), check
						.get("statusChangeDate"));
	}

	@Override
	public Long getCheckCountForRelease(CheckReleaseSearchCriteriaDto criteria) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Payment> check = cq.from(Payment.class);
		cq.select(cb.function("count", Long.class, check.get("id")));
		buildChecksForReleaseWhereClause(cb, cq, check, criteria);
		return this.getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	@Transactional
	public void processManualRelease(List<CheckSearchRecordDto> list,
			final PaymentType paymentType) {
		this.processManualRelease(list, paymentType,
				java.util.Collections.<Long> emptyList(), null);

	}
	
	
	@Transactional
	public void processManualRelease(final List<CheckSearchRecordDto> list,
			final PaymentType paymentType, final Collection<Long> excludes,
			final String defaultComments) {
		Preconditions.checkArgument(paymentType == PaymentType.CHECK || paymentType == PaymentType.WIRE, "paymentType must be either CHECK or WIRE");
		
		List<Long> checkIdList = new ArrayList<Long>();
		for (CheckSearchRecordDto record : list) {
			if (excludes.contains(record.getId())) {
				continue;
			}
			final Payment payment = Payment.findPayment(record.getId());
			if (record.isApproved()) {
				payment.setPaymentCode(CheckReleaseSearchCriteriaDto.getApprovedCode(record.getPaymentCode(), paymentType));
			} else if (record.isRejected()) {
				payment.setPaymentCode(CheckReleaseSearchCriteriaDto.getRejectedCode(record.getPaymentCode(), paymentType));
				
			} else {
				throw new IllegalArgumentException(
						"Check approval must be either approved or rejected.");
			}
			payment.setIfdsSent(Boolean.FALSE);
			payment.setComments(StringUtils.hasText(record.getComments()) ? record.getComments() : defaultComments);
			payment.setReleaseRejectResponseCode(record.getRejectReasonCode());
			checkIdList.add(record.getId());
			
		}		
		generateCheckReleaseActivity(checkIdList);
	}


	@Override
	@Transactional
	public void processBulkRelease(
			final CheckReleaseSearchCriteriaDto criteria,
			final Collection<Long> excludes, final String comments) {
		final Long paymentCpuntForBulkRelease = getCheckCountForRelease(criteria);
		// TODO: Fix this. Batch process asynchronously.
		Preconditions
				.checkArgument(
						paymentCpuntForBulkRelease <= 100,
						"Cannot Bulk release more than 100 records at a time. Please refine criteria. ");
		criteria.setMaxResults(1000);
		criteria.setFirstResult(0);
		final List<CheckSearchRecordDto> list = getChecksForRelease(criteria);

		this.processManualRelease(list, criteria.getPaymentType(), excludes,
				comments);
	}

	@Transactional
	public void generateCheckReleaseActivity(List<Long> checkIds) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
		Root<Payment> check = cq.from(Payment.class);
		for (Long id : checkIds) {
			cq.where(cb.equal(check.get("id"), id));
			Payment c = this.getEntityManager().createQuery(cq)
					.getSingleResult();
			Activity a = newCheckReleaseActivity(c);
			c.getPayTo().addActivity(a);
		}
	}

	private Activity newCheckReleaseActivity(final Payment c) {

		c.getIdentificatonNo();
		c.getId();
		c.getReleaseRejectResponseCode();

		Activity activity = new Activity();
		StringBuilder sb = new StringBuilder();
		if (c.getPaymentType() == PaymentType.CHECK) {
			activity.setActivityTypeDescription("Check Management");
			sb.append("Check#").append(c.getIdentificatonNo());
		} else if (c.getPaymentType() == PaymentType.WIRE) {
			activity.setActivityTypeDescription("Wire Management");
			sb.append("Wire");
		}
		if (c.isReIssueApproved()) {
			sb.append(" Approved.");
		} else if (c.isReIssueRejected()) {
			sb.append(" Rejected.");
		}
		if (c.isWireApproved()) {
			sb.append(" Approved.");
		} else if (c.isWireRejected()) {
			sb.append(" Rejected.");
		}
		if (StringUtils.hasText(c.getComments())) {
			sb.append("Comments:").append(c.getComments());
		} else if (StringUtils.hasText(c.getReleaseRejectResponseCode())) {
			sb.append("Comments:").append(c.getReleaseRejectResponseCode());
		}
		activity.setDescription(sb.toString());
		activity = Activity.setActivityDefaults(activity);
		return activity;
	}

	public List<Payment> loadPaymentsForReturnOfFunding(String referenceNo,
			String paymentIdentificationNo) {

		List<Payment> ret = loadPaymentForReturnOfFunding(paymentIdentificationNo);
		if (ret == null || ret.size() == 0) {
			ret = loadPaymentsForReturnOfFunding(referenceNo);
		}
		return ret;
	}

	private List<Payment> loadPaymentForReturnOfFunding(
			String paymentIdentificationNo) {
		if (!StringUtils.hasText(paymentIdentificationNo)) {
			return null;
		}
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
		Root<Payment> check = cq.from(Payment.class);
		cq.select(check);
		cq.where(
				cb.equal(check.get("identificatonNo"), paymentIdentificationNo),
				check.get("paymentCode").in(PaymentCodeUtil.getCashedCodes()));
		List<Payment> list = this.getEntityManager().createQuery(cq)
				.getResultList();
		return list;
	}

	private List<Payment> loadPaymentsForReturnOfFunding(String referenceNo) {
		if (!StringUtils.hasText(referenceNo)) {
			return java.util.Collections.<Payment> emptyList();
		}
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();
		Root<Payment> check = cq.from(Payment.class);
		cq.select(check);
		cq.where(cb.equal(check.get("payTo").get("referenceNo"), referenceNo),
				check.get("paymentCode").in(PaymentCodeUtil.getCashedCodes()));
		return this.getEntityManager().createQuery(cq).getResultList();
	}

	public List<Payment> loadPaymentsForRPO(String referenceNo,
			String paymentIdentificationNo) {

		List<Payment> ret = loadPaymentForRPO(paymentIdentificationNo);
		if (ret == null || ret.size() == 0) {
			ret = loadPaymentsForRPO(referenceNo);
		}
		return ret;
	}

	private List<Payment> loadPaymentForRPO(String paymentIdentificationNo) {
		if (!StringUtils.hasText(paymentIdentificationNo)) {
			return null;
		}
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
		Root<Payment> check = cq.from(Payment.class);
		cq.select(check);
		cq.where(
				cb.equal(check.get("identificatonNo"), paymentIdentificationNo),
				cb.not(check.get("paymentCode").in(
						PaymentCodeUtil.getCashedCodes())));
		List<Payment> list = this.getEntityManager().createQuery(cq)
				.getResultList();
		return list;
	}

	private List<Payment> loadPaymentsForRPO(String referenceNo) {
		if (!StringUtils.hasText(referenceNo)) {
			return java.util.Collections.<Payment> emptyList();
		}
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();
		Root<Payment> check = cq.from(Payment.class);
		cq.select(check);
		cq.where(
				cb.equal(check.get("payTo").get("referenceNo"), referenceNo),
				cb.not(check.get("paymentCode").in(
						PaymentCodeUtil.getCashedCodes())));
		return this.getEntityManager().createQuery(cq).getResultList();
	}

	/**
	 * @deprecated use {@link Payment#findReissueApprovedChecksOlderThan(int)}
	 */
	@Override
	@Deprecated
	public List<Payment> findReissueApprovedChecksOlderThan(final int days) {
		return Payment.findReissueApprovedChecksOlderThan(days);
	}

	/**
	 * @deprecated use {@link Payment#findAllCreatedStatusChecks()}
	 */
	@Override
	@Deprecated
	public List<Payment> findAllCreatedStatusChecks() {
		return Payment.findAllCreatedStatusChecks();
	}

	/**
	 * @deprecated use
	 *             {@link Payment#findCheckByNumberAndAmount(String, double)}
	 */
	@Override
	@Deprecated
	public Payment findCheckByNumberAndAmount(final String identificatonNo,
			double amount) {
		return Payment.findCheckByNumberAndAmount(identificatonNo, amount);
	}

	@Override
	public Payment getCheckForStopLift(String paymentIdentificationNo) {
		if (!StringUtils.hasText(paymentIdentificationNo)) {
			return null;
		}
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
		Root<Payment> check = cq.from(Payment.class);
		cq.select(check);
		cq.where(
				cb.equal(check.get("identificatonNo"), paymentIdentificationNo),
				cb.or(check.get("paymentCode").in(
						PaymentCodeUtil.getStopRequestedCodes()),
						check.get("paymentCode").in(
								PaymentCode.STOP_STOP_CONFIRMED_S_SC,
								PaymentCode.STOP_DAMASCO_STOP_CONFIRMED_P_PC)));
		List<Payment> list = this.getEntityManager().createQuery(cq)
				.getResultList();
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * Method to count all Outstanding Checks
	 */
	@Override
	public Long getOutstandingCheckCount() {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Payment> check = cq.from(Payment.class);
		cq.select(cb.function("count", Long.class, check.get("id")));
		cq.where(check.get("paymentCode").in(
				PaymentCodeUtil.getOutstandingCodes()));
		return this.getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Method to count Stale Dated Checks
	 */
	@Override
	public Long getStaleDatedCheckCount() {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Payment> check = cq.from(Payment.class);
		cq.select(cb.function("count", Long.class, check.get("id")));
		cq.where(check.get("paymentCode").in(
				PaymentCode.STALE_DATE_OUTSTANDING_X_X));
		return this.getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public Payment getCheckForStopVoidReversal(
			final String paymentIdentificationNo) {
		if (!StringUtils.hasText(paymentIdentificationNo)) {
			return null;
		}
		final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
		final Root<Payment> check = cq.from(Payment.class);
		cq.select(check);
		cq.where(
				cb.equal(check.get("identificatonNo"), paymentIdentificationNo),
				cb.or(cb.notEqual(check.get("sentOnBankStopFile"), Boolean.TRUE), 
					  cb.isNull(check.get("sentOnBankStopFile"))),
				cb.or(check.get("paymentCode").in(
						PaymentCodeUtil.getStopRequestedCodes()),
						check.get("paymentCode").in(
								PaymentCodeUtil.getVoidedCodes())));

		try {
			return this.getEntityManager().createQuery(cq)
					.getSingleResult();
		} catch (final EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public PaymentCode getPreviousPaymentCode(Long paymentId,
			PaymentCode paymentCode) {
		Preconditions.checkArgument(paymentCode != null,
				"currentPaymentCode is null");

		List<EntityWithAuditVo<Payment>> payments = entityAuditDao
				.getEntityVersionList(Payment.class, paymentId);

		Collections.sort(payments,
				new Comparator<EntityWithAuditVo<Payment>>() {
					public int compare(EntityWithAuditVo<Payment> o1,
							EntityWithAuditVo<Payment> o2) {
						return o1.getRevisionInfo().getId()
								- o2.getRevisionInfo().getId();
					}
				});

		PaymentCode ret = null;
		for (int i = 0; i < payments.size(); i++) {
			EntityWithAuditVo<Payment> payment = payments.get(i);
			if (payment.getEntity().getPaymentCode().equals(paymentCode)) {
				if (i > 0) {
					Payment paymentAuditRec = payments.get(i - 1).getEntity();
					ret = paymentAuditRec.getPaymentCode();
				}
			}
		}

		return ret;
	}

	public EntityAuditDao getEntityAuditDao() {
		return entityAuditDao;
	}

	public void setEntityAuditDao(EntityAuditDao entityAuditDao) {
		this.entityAuditDao = entityAuditDao;
	}

	/*
	 * Changes "toDate" time part with the maximum time for the day to support &
	 * for the same date given as toDate range value. If the Check's
	 * statusChangeDate = "Aug 06 01:59:59 IST 2012" in database and range value
	 * passed from screen is "Aug 06 2012 00:00:00" data is not getting
	 * displayed for check/wire release screen.So after change we are able to
	 * display all checks comes under that range.
	 */

	private Date getDateWithMaxTimeForDay(CheckReleaseSearchCriteriaDto criteria) {

		Date toDate = criteria.getToDate();

		if (criteria.getToDate() != null) {
			toDate.setHours(23);
			toDate.setMinutes(59);
			toDate.setSeconds(59);

		}
		return toDate;
	}

	@Override
	public List<Payment> getPaymentsByClaimantIdAndStatus(
			final Long claimantId, List<PaymentCode> paymentCodes) {

		Preconditions.checkNotNull(claimantId, "claimantId is null");

		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
		Root<Payment> check = cq.from(Payment.class);
		cq.select(check);
		cq.where(cb.equal(check.get("payTo").get("id"), claimantId),
				check.get("paymentCode").in(paymentCodes));
		final List<Payment> list = this.getEntityManager().createQuery(cq)
				.getResultList();
		return list;
	}

}