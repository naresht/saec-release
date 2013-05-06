package com.bfds.saec.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import javax.persistence.criteria.Selection;

import org.hibernate.ejb.criteria.expression.LiteralExpression;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.GroupMailCode;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchRecordDto;
import com.bfds.wss.domain.ClaimantClaim;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.bfds.wss.domain.ClaimantClaimId;

@Repository
public class ClaimantDaoImpl implements ClaimantDao {
	private EntityManager entityManager;

	@PersistenceContext(unitName="entityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<ClaimantSearchRecordDto> getClaimantSearchResults(
			final ClaimantSearchCriteriaDto criteria) {
		
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Claimant> claimant = cq.from(Claimant.class);
		
		final List<Predicate> expressions = new ArrayList<Predicate>();
		
		cq.select(getClaimantSearchResultsSelection(claimant, cq, expressions));
		buildClaimantSearchResultsWhereClause(cb, cq, claimant, criteria, expressions);
		
		setWhereConditions(cq, expressions);
		
		buildChecksForReleaseOrderByClause(cb, cq, claimant, criteria);
		List<Tuple> results = this.getEntityManager().createQuery(cq)
				.setFirstResult(criteria.getFirstResult())
				.setMaxResults(criteria.getMaxResults()).getResultList();
		final Collection<Object[]> records = removeDuplicateClaimants(results);		
		return new ClaimantSearchRecordDto.Builder().build(addFirstCheckInfo(records,criteria));
	}

	/**
	 * @param records - A {@link List} of {@link Object} arrays. 
	 * Each Array represents a claimant. 
	 * This method adds the first Check# and amount of the claimant to each array.
	 * TODO : This method has a public access modifier only for testing. Find a better way.
	 */
	public Collection<Object[]> addFirstCheckInfo(final Collection<Object[]> records, ClaimantSearchCriteriaDto criteria) {
		
		final Map<Long, Object[]> claimantIdRecords = Maps.newLinkedHashMap();
		if(records!=null && !records.isEmpty()){
		for(Iterator<Object[]> itr = records.iterator(); itr.hasNext(); ) {			
			final Object[] recordWithPaymentInfo = new Object[7];
			System.arraycopy(itr.next(), 0, recordWithPaymentInfo, 0, 5);
			claimantIdRecords.put((Long) recordWithPaymentInfo[0], recordWithPaymentInfo);
		}
		
		final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		final Root<Payment> payment = cq.from(Payment.class);
		
		cq.select(cb.tuple(payment.get("payTo").get("id"), payment.get("identificatonNo"), payment.get("paymentCalc").get("grossAmount"), payment.get("paymentDate")));		
		
		cq.where(payment.get("payTo").get("id").in(claimantIdRecords.keySet()));
		if(criteria.getCheckNo()!=null && StringUtils.hasText(criteria.getCheckNo()))
		{
			cq.where(payment.get("identificatonNo").in(criteria.getCheckNo()));
		}
		cq.orderBy(cb.asc(payment.get("paymentDate")));
		
		final List<Tuple> results = this.getEntityManager().createQuery(cq).getResultList();
		final Set<Long> updatedClaimantIds = Sets.newHashSet();
		for(final Tuple record : results) { 
			
			if(!updatedClaimantIds.add((Long)record.get(0))) {
				continue;
			}
			final Object[] recordWithPaymentInfo = claimantIdRecords.get(record.get(0));
			recordWithPaymentInfo[5] = record.get(1);
			recordWithPaymentInfo[6] = record.get(2);			
		}
		
		
		}
		
		
		
		
		return claimantIdRecords.values();
	}

	/**
	 * @param records - A {@link List} of {@link Tuple} where {@link Tuple#get(0)} is the claimantId.
	 * 
	 * Removes all duplicate records. A record is a duplicate of another if they have the same claimantId
	 */
	private Collection<Object[]> removeDuplicateClaimants(final List<Tuple> records) {
		Map<Long, Object[]> ObjArrayMap = new LinkedHashMap<Long, Object[]>();
		
		for(final Tuple record : records) {
			final Object[] duplicate = ObjArrayMap.get((Long)record.get(0));
			if(duplicate == null) {
				ObjArrayMap.put((Long)record.get(0), record.toArray());
			}
		}
		return ObjArrayMap.values();		
	}

	private void setWhereConditions(CriteriaQuery<?> cq,
			final List<Predicate> expressions) {
		if (!expressions.isEmpty()) {
			cq.where((Predicate[]) expressions
					.toArray(new Predicate[expressions.size()]));
		}
	}

	private void buildChecksForReleaseOrderByClause(CriteriaBuilder cb,
			CriteriaQuery<Tuple> cq, Root<Claimant> claimant,
			ClaimantSearchCriteriaDto criteria) {
		List<Order> orderBys = new ArrayList<Order>();

		
		if (criteria.hasSortFields()) {
			buildOrderBy(cb, claimant, criteria, orderBys,
					criteria.getClaimantSortFields());
			
			buildOrderBy(cb, claimant.join("addresses", JoinType.LEFT),
					criteria, orderBys, criteria.getAddressSortFields());
			
			if (criteria.getSortOrder() == CheckReleaseSearchCriteriaDto.SORT_ORDER_ASC) {
				orderBys.add(cb.asc(getClaimIdentifierSelectionExpression(cq)));
			}
			else{
				orderBys.add(cb.desc(getClaimIdentifierSelectionExpression(cq)));
			}
			

		}
		if (orderBys.size() > 0) {
			cq.orderBy(orderBys);
		}
		else {
			orderBys.add(cb.asc(claimant.get("referenceNo")));
		}
		cq.orderBy(orderBys);
	}

	private Expression<?> getClaimIdentifierSelectionExpression(CriteriaQuery<Tuple> cq) {
		for(Selection<?> selection :  cq.getSelection().getCompoundSelectionItems()) {
			if("claimIdentifier".equals(selection.getAlias())) {
				return (Expression<?>) selection;
			}
		}
		return null;
	}

	private void buildOrderBy(CriteriaBuilder cb, From root,
			ClaimantSearchCriteriaDto criteria, List<Order> orderBys,
			String[] fields) {
		for (String field : fields) {
			final Path param = getPath(root, field);
			if (criteria.getSortOrder() == CheckReleaseSearchCriteriaDto.SORT_ORDER_ASC) {
				
				Expression<String> p=cb.upper(param);
				orderBys.add(cb.asc(p));
			}
			else {
				orderBys.add(cb.desc(param));
			}
		}
	}

	private <T> Path<T> getPath(From r, String path) {
		if (path.contains(".")) {
			String[] paths = path.split("\\.");
			Path p = r.get(paths[0]);
			for (int i = 1; i < paths.length; i++) {
				p = p.get(paths[i]);
			}
			return p;
		}
		else {
			return r.get(path);
		}
	}

	private <T> void  buildClaimantSearchResultsWhereClause(CriteriaBuilder cb,
			CriteriaQuery<T> cq, Root<Claimant> claimant,
			ClaimantSearchCriteriaDto criteria, final List<Predicate> expressions) {

		if (StringUtils.hasText(criteria.getReferenceNo())) {
			expressions.add(equal(cb, claimant.<String> get("referenceNo"),
					criteria.getReferenceNo()));
		}
		if (StringUtils.hasText(criteria.getTaxId())
				&& criteria.getIdentificationType().equalsIgnoreCase("ssn")) {
			expressions.add(cb.equal(claimant.<String> get("taxInfo")
					.get("ssn"), criteria.getTaxId()));
		}

		if (StringUtils.hasText(criteria.getTaxId())
				&& criteria.getIdentificationType().equalsIgnoreCase("tin")) {
			expressions.add(cb.equal(claimant.<String> get("taxInfo")
					.get("tin"), criteria.getTaxId()));

		}

		if (StringUtils.hasText(criteria.getTaxId())
				&& criteria.getIdentificationType().equalsIgnoreCase("ein")) {
			expressions.add(cb.equal(claimant.<String> get("taxInfo")
					.get("ein"), criteria.getTaxId()));

		}

		if (StringUtils.hasText(criteria.getBin())) {
			expressions.add(equal(cb, claimant.<String> get("bin"),
					criteria.getBin()));
		}
		if (StringUtils.hasText(criteria.getFundAccountNo())) {
			expressions.add(equal(cb, claimant.<String> get("fundAccountNo"),
					criteria.getFundAccountNo()));
		}
		if (StringUtils.hasText(criteria.getBrokerId())) {
			expressions.add(equal(cb, claimant.<String> get("brokerId"),
					criteria.getBrokerId()));
		}
		if (criteria.isMarketTimer()) {
			expressions.add(cb.equal(claimant.<Boolean> get("marketTimer"),
					Boolean.TRUE));
		}		
		if (criteria.getParentClaimantId() != null) {
			expressions.add(cb.equal(claimant.<Claimant> get("parentClaimant").<Long> get("id"),
					criteria.getParentClaimantId()));
		}
		if (StringUtils.hasText(criteria.getName())) {
			expressions.add(like(
					cb,
					claimant.<String> get("claimantRegistration").<String> get(
							"concatinatedLines"), criteria.getName()));
		}

		From<Claimant, Payment> payments = claimant.join("payments",JoinType.LEFT);
		if (StringUtils.hasText(criteria.getCheckNo())) {
			expressions.add(cb.equal(payments.<String> get("identificatonNo"),criteria.getCheckNo()));
		}
		
		if (StringUtils.hasText(criteria.getGroupMailCode())) {
			expressions.add(cb.equal(payments.<GroupMailCode> get("groupMailCode").get("code"),
			criteria.getGroupMailCode()));
		}

		From<Claimant, ClaimantAddress> address = claimant.join("addresses",
				JoinType.LEFT);
		expressions.add(cb.equal(address.<ClaimantAddress> get("address")
				.<Boolean> get("mailingAddress"), Boolean.TRUE));

		if (StringUtils.hasText(criteria.getCity())) {
			expressions.add(like(cb, address.<ClaimantAddress> get("address")
					.<String> get("city"), criteria.getCity()));
		}
		if (StringUtils.hasText(criteria.getState())) {
			expressions.add(like(cb, address.<ClaimantAddress> get("address")
					.<String> get("stateCode"), criteria.getState()));
		}
		if (StringUtils.hasText(criteria.getZip())) {
			expressions.add(like(cb, address.<ClaimantAddress> get("address")
					.<ZipCode> get("zipCode").<String> get("zip"),
					criteria.getZip()));
		}

		if (!criteria.getFilters().isEmpty()) {
			updateFilters(cb, claimant, expressions,
					criteria.getResolvedClaimantFilters());
			updateFilters(cb, claimant.join("addresses", JoinType.LEFT),
					expressions, criteria.getResolvedAddressFilters());
		}
		From<Claimant, ClaimantClaim> claimantClaims = claimant.join("claimantClaims",JoinType.LEFT);
		From<Claimant, ClaimantClaimId> claimantClaimIds = claimant.join("claimantClaimIds",JoinType.LEFT);
		
		if (StringUtils.hasText(criteria.getClaimIdentifier())) {
			expressions.add(cb.or(cb.equal(claimantClaims.<String> get("claimIdentifier"),criteria.getClaimIdentifier()),
					              cb.equal(claimantClaimIds.<String> get("claimIdentifier"),criteria.getClaimIdentifier())));
			
		}

	}

	private Predicate like(final CriteriaBuilder cb, Path<String> path,
			final String expression) {
		return cb.like(cb.lower(path), "%" + expression.toLowerCase() + "%");
	}

	private Predicate equal(final CriteriaBuilder cb, Path<String> path,
			final String expression) {
		return cb.equal(cb.lower(path), expression.toLowerCase());
	}

	private void updateFilters(CriteriaBuilder cb, From from,
			List<Predicate> expressions, Map<String, Object> filters) {
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String) {
				expressions.add(like(cb,
						this.<String> getPath(from, entry.getKey()),
						(String) value));
			}
			else if (value instanceof Number) {
				expressions.add(cb.ge(
						this.<Number> getPath(from, entry.getKey()),
						(Number) value));
			}

		}
	}

	private CompoundSelection<Tuple> getClaimantSearchResultsSelection(  
			Root<Claimant> claimant, CriteriaQuery<Tuple> cq, final List<Predicate> expressions) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		From<Claimant, ClaimantAddress> address = claimant.join("addresses", JoinType.LEFT);
		From<Claimant, ClaimantRegistration> registration = claimant.join("claimantRegistration");
		From<Claimant, ClaimantClaim> claimantClaims = claimant.join("claimantClaims", JoinType.LEFT);
		From<Claimant, ClaimantClaimId> claimantClaimIds = claimant.join("claimantClaimIds", JoinType.LEFT);
		Predicate isMailingAddress = cb.equal(
					address.<ClaimantAddress> get("address").<Boolean> get(
						"mailingAddress"), Boolean.TRUE);
		expressions.add(isMailingAddress);
		//cq.where(isMailingAddress);
		return cb.tuple(claimant.get("id"), claimant.get("referenceNo"), registration.get("lines"), 
						address.get("address"), cb. 
						coalesce(claimantClaims.get("claimIdentifier"), 
								 claimantClaimIds.get("claimIdentifier")).alias("claimIdentifier"));
	}
	
	
	@Override
	public Long getClaimantSearchResultsCount(ClaimantSearchCriteriaDto criteria) {
		final List<Predicate> expressions = new ArrayList<Predicate>();
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Claimant> check = cq.from(Claimant.class);
		cq.select(cb.countDistinct(check.get("id")));
		buildClaimantSearchResultsWhereClause(cb, cq, check, criteria, expressions);
		setWhereConditions(cq, expressions);
		return this.getEntityManager().createQuery(cq).getSingleResult();
	}
}