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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.dto.ClaimantTransactionCriteriaDto;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimantPosition.Method;
import com.bfds.wss.domain.ClaimantPosition.PositionType;
import com.bfds.wss.domain.reference.SecurityFund;
import com.bfds.wss.domain.reference.TransactionType;
import com.google.common.collect.Lists;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Audited
public class ClaimantTransactions implements java.io.Serializable {
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimant_claim_fk", nullable = false)
	private ClaimantClaim claimantClaim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_fund_fk")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private SecurityFund securityFund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_fk", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private TransactionType transactionType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "claim_proof_fk", nullable = true)
    private ClaimProof proof;

    /**
     *  The A/S/N attribute for the internal (i.e. SASEC) transaction type; indicates if we should add, subtract, or ignore the transaction amount when calculating the daily share balance.
     */
	@Column(name = "transaction_code", length = 50)
	private String transactionCode;

    /**
     * The A/S/N attribute for the source transaction type
     */
	@Column(name = "source_transaction_code", length = 50)
	private String sourceTransactionCode;

    @Column(name = "source_transaction_type", length = 50)
    private String sourceTransactionType;

    /**
     * Date on which trade was executed
     */
    @Temporal(TemporalType.TIMESTAMP)
   	@Column(name = "Trade_Date", nullable = false, length = 10)
    @DateTimeFormat(style = "S-")
	private Date tradeDate;

    /**
     * Trade settlement date
     */
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Settlement_Date", nullable = false, length = 10)
	@DateTimeFormat(style = "S-")
	private Date settlementDate;

    /**
     * Number of shares traded
     */
	@Column(nullable=false, scale = 4)
	private BigDecimal quantity;

    /**
     * Price of shares
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal unitPrice;

    /**
     * Gross amount of trade
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal grossAmount;

    /**
     * Net amount of trade
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal netAmount;

    /**
     * Commission for trade
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal commission;

    /**
     * Fees associated with trade
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal fees;

    /**
     * Gain or loss in the transaction
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal gainLoss;

    /**
     *  The total cost of a transaction taking into account # shares, share price, commission, other fees, etc.
     */
    @Column(nullable=true, scale = 4)
    private BigDecimal totalCost;

    /**
     * Cost basis of the assent
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal costBasis;

	@Column(nullable=true, scale = 4)
	private BigDecimal witholding;

    /**
     * Balance before the transaction
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal beginningBalance;

    /**
     * Balance after the transaction
     */
	@Column(nullable=true, scale = 4)
	private BigDecimal endingBalance;

    /**
     * Indicates if proof has been provided
     */
	@Column(nullable=true, scale = 4)
	private boolean proofProvidedInd;

    /**
     * Indiacates if there is a discrepency in the transaction
     */
	@Column(nullable=true, scale = 4)
	private boolean discrepencyInd;

    @Column(name = "comments")
    private String comments;
    
    /**
	 * Flag to identify deleted transactions.
	 */
	private Boolean deleted;

	/**
	 * Loading the {@link ClaimantTransactions} by a {@link Claimant}
	 * @param criteria
	 * @return
	 */
	public List<ClaimantTransactions> findClaimantTransactionByClaimant(ClaimantTransactionCriteriaDto criteria) {

		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ClaimantTransactions> cq = cb.createQuery(ClaimantTransactions.class);
		Root<ClaimantTransactions> claimantTransactions = cq.from(ClaimantTransactions.class);

		final List<Predicate> expressions = new ArrayList<Predicate>();

		buildClaimantTransactionsWhereClause(cb, cq, claimantTransactions,criteria, expressions);
		setWhereConditions(cq, expressions);

		buildClaimantPositionsOrderByClause(cb, cq, claimantTransactions, criteria);
		
		return this.entityManager.createQuery(cq)
				.setFirstResult(criteria.getFirstResult())
				.setMaxResults(criteria.getMaxResults()).getResultList();
	}

	private <T> void buildClaimantTransactionsWhereClause(CriteriaBuilder cb,
			CriteriaQuery<T> cq, Root<ClaimantTransactions> claimantTransactions,
			ClaimantTransactionCriteriaDto criteria,
			final List<Predicate> expressions) {

		expressions.add(cb.equal(claimantTransactions.<String> get("claimantClaim"),
				criteria.getClaimantClaim()));
		
		if (!criteria.getFilters().isEmpty()) {
			updateFilters(cb, claimantTransactions, expressions,
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

	private void setWhereConditions(CriteriaQuery<?> cq,final List<Predicate> expressions) {
		if (!expressions.isEmpty()) {
			cq.where((Predicate[]) expressions
					.toArray(new Predicate[expressions.size()]));
		}
	}

	private void buildClaimantPositionsOrderByClause(CriteriaBuilder cb,
			CriteriaQuery<ClaimantTransactions> cq,
			Root<ClaimantTransactions> claimantTransactions,
			ClaimantTransactionCriteriaDto criteria) {
		List<Order> orderBys = new ArrayList<Order>();

		if (criteria.hasSortFields()) {
			buildOrderBy(cb, claimantTransactions, criteria, orderBys,
					new String[] { criteria.getSortField() });
		}
		if (orderBys.size() > 0) {
			cq.orderBy(orderBys);
		} else {
			orderBys.add(cb.desc(claimantTransactions.get("id")));
		}
		cq.orderBy(orderBys);
	}

	private void buildOrderBy(CriteriaBuilder cb, From root,
			ClaimantTransactionCriteriaDto criteria, List<Order> orderBys,
			String[] fields) {
		for (String field : fields) {
			final Path param = getPath(root, field);
			if (criteria.getSortOrder() == ClaimantTransactionCriteriaDto.SORT_ORDER_ASC) {

				Expression<String> p = cb.upper(param);
				orderBys.add(cb.asc(p));
			} else {
				orderBys.add(cb.desc(param));
			}
		}
	}

	/**
	 * Count of a {@link ClaimantTransactions} for a {@link Claimant}
	 * @param criteria
	 * @return
	 */
	public Long getClaimantTransactionsCountForClaimant(
			ClaimantTransactionCriteriaDto criteria) {
		final List<Predicate> expressions = new ArrayList<Predicate>();
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ClaimantTransactions> check = cq.from(ClaimantTransactions.class);
		cq.select(cb.countDistinct(check.get("id")));
		buildClaimantTransactionsWhereClause(cb, cq, check, criteria,
				expressions);
		setWhereConditions(cq, expressions);
		return this.entityManager.createQuery(cq).getSingleResult();
	}
    
    
	/**
	 * Loading the all the {@link ClaimantTransactions}
	 * @return
	 */
	public static List<ClaimantTransactions> findAllClaimantTransaction() {
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<ClaimantTransactions> cq = cb
				.createQuery(ClaimantTransactions.class);
		Root<ClaimantTransactions> claimantTransactions = cq
				.from(ClaimantTransactions.class);
		claimantTransactions.fetch("claimantClaim", JoinType.LEFT);
		claimantTransactions.fetch("securityFund", JoinType.LEFT);
		cq.select(claimantTransactions);
		return entityManager().createQuery(cq).getResultList();
	}
	
	
	/**
	 * Loading the {@link ClaimantTransactions} by a {@link ClaimantClaim}
	 * @param claimantClaim
	 * @return
	 */
	public static List<ClaimantTransactions> findClaimantTransactionByClaimantClaim(final ClaimantClaim claimantClaim) {
		if (claimantClaim == null) throw new IllegalArgumentException("The ClaimantClaim argument is required");
		EntityManager em = ClaimantTransactions.entityManager();
        TypedQuery<ClaimantTransactions> q = em.createQuery("SELECT o FROM ClaimantTransactions AS o WHERE o.claimantClaim = :claimantClaim ", ClaimantTransactions.class);
        q.setParameter("claimantClaim", claimantClaim);
        return q.getResultList();
	}
	
	public static List<ClaimProof> getProofsOfClaimantTransactions(final ClaimantClaim claimantClaim) {
		if (claimantClaim == null) throw new IllegalArgumentException("The claimantClaim argument is required");
		EntityManager em = ClaimantTransactions.entityManager();
        TypedQuery<ClaimProof> q = em.createQuery("SELECT distinct o.proof FROM ClaimantTransactions AS o WHERE o.claimantClaim = :claimantClaim and o.proof != null ", ClaimProof.class);
        q.setParameter("claimantClaim", claimantClaim);
        return q.getResultList();
	}
	
	public static List<ClaimProof> getClaimProofs(final ClaimantClaim claimantClaim,
			Collection<ProofStatus> proofStatus) {
		List<ClaimProof> retClaimProofs = Lists.newArrayList();
		List<ClaimProof> claimProofs = getProofsOfClaimantTransactions(claimantClaim);
		for (ClaimProof claimProof : claimProofs) {
			if (proofStatus.contains(claimProof.getStatus())) {
				retClaimProofs.add(claimProof);
			}
		}
		return retClaimProofs;
	}
	
	

}
