package com.bfds.wss.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.saec.domain.dto.ClaimantPositionCriteriaDto;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.reference.SecurityFund;
import com.google.common.collect.Lists;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit = "entityManagerFactory")
@Audited
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "claimant_claim_fk",
		"security_fund_fk", "position_type", "position_date", "method" }) })
public class ClaimantPosition implements java.io.Serializable {
	
	
	/**
	 * A value that can be assigned to {@link #status}.
	 */
	public static final String STATUS_OUT_OF_BALANCE = "Out Of Balance";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimant_claim_fk", nullable = false)
    private ClaimantClaim claimantClaim;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "security_fund_fk")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private SecurityFund securityFund;

	@Column(name = "position_type", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private PositionType positionType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "position_date", nullable = false, length = 10)
    @DateTimeFormat(style = "S-")
	private Date positionDate;

	/**
	 * The date the position was calculated. Applicable only if {@link #method}
	 * = {@link Method#CALCULATED}
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "calculated_date", length = 10)
	private Date calculatedDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "claim_proof_fk", nullable = true)
	private ClaimProof proof;

	/**
	 * The value of the position.
	 */
	@Column(nullable = false, scale = 4)
	private BigDecimal shareBalance;

	@Column(scale = 4)
	private BigDecimal accountBalance;

	@Column(name = "method")
	@Enumerated(EnumType.STRING)
	private Method method;

	@Column(name = "comments")
	private String comments;

	/**
	 * 'Out Of Balance' etc.
	 */
	@Column(name = "status")
	private String status;
	
	/**
	 * Flag to identify deleted positions.
	 */
	private Boolean deleted;

	public static enum PositionType {
		EOD("End Of Day"), ME("Month End"), QE("Quarter End"), YE("Year End");

		private final String name;

		private PositionType(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	public static enum Method {
		PROVIDED("Provided"), CALCULATED("Calculated");
		
		private final String name;

		private Method(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	public List<ClaimantPosition> findClaimantPositionByClaimant(
			ClaimantPositionCriteriaDto criteria) {

		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ClaimantPosition> cq = cb
				.createQuery(ClaimantPosition.class);
		Root<ClaimantPosition> claimantPosition = cq
				.from(ClaimantPosition.class);

		final List<Predicate> expressions = new ArrayList<Predicate>();

		buildClaimantPositionWhereClause(cb, cq, claimantPosition,
				criteria, expressions);

		setWhereConditions(cq, expressions);

		buildClaimantPositionsOrderByClause(cb, cq, claimantPosition, criteria);
		return this.entityManager.createQuery(cq)
				.setFirstResult(criteria.getFirstResult())
				.setMaxResults(criteria.getMaxResults()).getResultList();
	}

	private <T> void buildClaimantPositionWhereClause(CriteriaBuilder cb,
			CriteriaQuery<T> cq, Root<ClaimantPosition> claimantPosition,
			ClaimantPositionCriteriaDto criteria,
			final List<Predicate> expressions) {

		expressions.add(cb.equal(claimantPosition.<String> get("claimantClaim"),
				criteria.getClaimantClaim()));
		//expressions.add(cb.equal(claimantPosition.<String> get("method"),Method.PROVIDED));

		if (!criteria.getFilters().isEmpty()) {
			updateFilters(cb, claimantPosition, expressions,
					criteria.getResolvedClaimantFilters());
		}

	}

	private void updateFilters(CriteriaBuilder cb, From from,
			List<Predicate> expressions, Map<String, Object> filters) {
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String) {
				expressions.add(like(cb,
						this.<String> getPath(from, entry.getKey()),
						(String) value));
			} else if (value instanceof Number) {
				expressions.add(cb.ge(
						this.<Number> getPath(from, entry.getKey()),
						(Number) value));
			} else if (value instanceof PositionType || value instanceof Method) {
				expressions.add(equal(cb,
						this.<String> getPath(from, entry.getKey()), value));
			}
		}
	}

	private Predicate equal(final CriteriaBuilder cb, Path<String> path,
			final Object expression) {
		return cb.equal(cb.lower(path), expression);
	}

	private Predicate like(final CriteriaBuilder cb, Path<String> path,
			final String expression) {
		return cb.like(cb.lower(path), "%" + expression.toLowerCase() + "%");
	}

	private <T> Path<T> getPath(From r, String path) {
		if (path.contains(".")) {
			String[] paths = path.split("\\.");
			Path p = r.get(paths[0]);
			for (int i = 1; i < paths.length; i++) {
				p = p.get(paths[i]);
			}
			return p;
		} else {
			return r.get(path);
		}
	}

	private void setWhereConditions(CriteriaQuery<?> cq,
			final List<Predicate> expressions) {
		if (!expressions.isEmpty()) {
			cq.where((Predicate[]) expressions
					.toArray(new Predicate[expressions.size()]));
		}
	}

	private void buildClaimantPositionsOrderByClause(CriteriaBuilder cb,
			CriteriaQuery<ClaimantPosition> cq,
			Root<ClaimantPosition> claimantPosition,
			ClaimantPositionCriteriaDto criteria) {
		List<Order> orderBys = new ArrayList<Order>();

		if (criteria.hasSortFields()) {
			buildOrderBy(cb, claimantPosition, criteria, orderBys,
					new String[] { criteria.getSortField() });
		}
		if (orderBys.size() > 0) {
			cq.orderBy(orderBys);
		} else {
			orderBys.add(cb.desc(claimantPosition.get("calculatedDate")));
		}
		cq.orderBy(orderBys);
	}

	private void buildOrderBy(CriteriaBuilder cb, From root,
			ClaimantPositionCriteriaDto criteria, List<Order> orderBys,
			String[] fields) {
		for (String field : fields) {
			final Path param = getPath(root, field);
			if (criteria.getSortOrder() == ClaimantPositionCriteriaDto.SORT_ORDER_ASC) {

				Expression<String> p = cb.upper(param);
				orderBys.add(cb.asc(p));
			} else {
				orderBys.add(cb.desc(param));
			}
		}
	}

	public Long getClaimantPositionCountForClaimant(
			ClaimantPositionCriteriaDto criteria) {
		final List<Predicate> expressions = new ArrayList<Predicate>();
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ClaimantPosition> position = cq.from(ClaimantPosition.class);
		cq.select(cb.countDistinct(position.get("id")));
		buildClaimantPositionWhereClause(cb, cq, position, criteria,
				expressions);
		setWhereConditions(cq, expressions);
		return this.entityManager.createQuery(cq).getSingleResult();
	}
	
	public static long countDailyPositionsWhereStatus(ClaimantClaim claimantClaim, Collection<String> status) {
		EntityManager em = ClaimantPosition.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(o) FROM ClaimantPosition AS o WHERE o.claimantClaim = :claimantClaim and o.status = :status", Long.class);
        q.setParameter("claimantClaim", claimantClaim);
        q.setParameter("status", status);
        return q.getSingleResult();
	}
	
	public static ClaimProof getProofOfClaimantPositionById(Long id) {
		if (id == null) throw new IllegalArgumentException("The Claimant Position Id argument is required");
		EntityManager em = ClaimantTransactions.entityManager();
        TypedQuery<ClaimProof> q = em.createQuery("SELECT o.proof FROM ClaimantPosition AS o WHERE o.id = :id ", ClaimProof.class);
        q.setParameter("id", id);
        return q.getSingleResult();
	}
	
	public static List<ClaimProof> getProofsOfClaimantPosition(final ClaimantClaim claimantClaim) {
		if (claimantClaim == null) throw new IllegalArgumentException("The claimantClaim argument is required");
		EntityManager em = ClaimantTransactions.entityManager();
        TypedQuery<ClaimProof> q = em.createQuery("SELECT distinct o.proof FROM ClaimantPosition AS o WHERE o.claimantClaim = :claimantClaim and o.proof != null ", ClaimProof.class);
        q.setParameter("claimantClaim", claimantClaim);
        return q.getResultList();
	}
	
	public static List<ClaimProof> getClaimProofs(final ClaimantClaim claimantClaim,
			Collection<ProofStatus> proofStatus) {
		List<ClaimProof> retClaimProofs = Lists.newArrayList();
		List<ClaimProof> claimProofs = getProofsOfClaimantPosition(claimantClaim);
		for (ClaimProof claimProof : claimProofs) {
			if (proofStatus.contains(claimProof.getStatus())) {
				retClaimProofs.add(claimProof);
			}
		}
		return retClaimProofs;
	}

}
