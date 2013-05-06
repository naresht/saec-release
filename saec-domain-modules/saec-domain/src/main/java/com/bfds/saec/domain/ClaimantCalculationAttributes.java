package com.bfds.saec.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Container for holding various status and flags set/updated on a
 * {@link Claimant} by different calculation processes.
 */
@RooJpaActiveRecord(persistenceUnit = "entityManagerFactory")
@RooJavaBean
@RooToString
public class ClaimantCalculationAttributes implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Claimant claimant;

	/**
	 * Indicates that a recalculation of the claimant's daily positions is
	 * required. A recalculation is required when there is a change to the
	 * claimant's provided positions.
	 */
	private Boolean reCalculatePositions;

	private String processStatus;

	public static boolean isReCalcRequired(Claimant claimant) {
		if (claimant == null)
			throw new IllegalArgumentException(
					"The claimant argument is required");
		EntityManager em = ClaimantCalculationAttributes.entityManager();
		TypedQuery<ClaimantCalculationAttributes> q = em
				.createQuery(
						"SELECT o FROM ClaimantCalculationAttributes AS o WHERE o.claimant = :claimant",
						ClaimantCalculationAttributes.class);
		q.setParameter("claimant", claimant);
		List<ClaimantCalculationAttributes> list = q.getResultList();
		if (!list.isEmpty()) {
			return list.get(0).reCalculatePositions;
		}
		return false;

	}
	
	public static List<ClaimantCalculationAttributes> findClaimantCalculationAttributesByClaimant(Claimant claimant){
		if (claimant == null)
			throw new IllegalArgumentException(
					"The claimant argument is required");
		EntityManager em = ClaimantCalculationAttributes.entityManager();
		TypedQuery<ClaimantCalculationAttributes> q = em
				.createQuery("SELECT o FROM ClaimantCalculationAttributes AS o WHERE o.claimant = :claimant",ClaimantCalculationAttributes.class);
		q.setParameter("claimant", claimant);
		List<ClaimantCalculationAttributes> list = q.getResultList();
		return list;
	}
}
