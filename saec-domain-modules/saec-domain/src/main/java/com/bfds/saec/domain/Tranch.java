package com.bfds.saec.domain;

import javax.persistence.Column;
import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findTranchesByCode"})
 @RooJavaBean
@RooToString
public class Tranch implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false, unique=true)
	@NotNull
	private String code;
	private LetterCode letterCode;
	private String status;
	
	/**
	 * If true, the assignment of {@link Payment}s to this {@link Tranch} is in progress.
	 */
	private boolean istranchAssignmentInProgress;
	
	public static Tranch findByCode(final String code) {
		try {
		 return entityManager().createQuery("SELECT o FROM Tranch o where code = :code", Tranch.class).setParameter("code", code).getSingleResult();
		}catch(final NoResultException e) {
			//Noop
		}catch(EmptyResultDataAccessException e) {
			//Noop
		}
		return null;
	}

	public boolean isIstranchAssignmentInProgress() {
		return istranchAssignmentInProgress;
	}

	public void setIstranchAssignmentInProgress(boolean istranchAssignmentInProgress) {
		this.istranchAssignmentInProgress = istranchAssignmentInProgress;
	} 
	
	
	public static boolean anyTranchAssignmentInProgress() {
			 final Long count =  entityManager().createQuery("SELECT count(o) FROM Tranch o where istranchAssignmentInProgress = :istranchAssignmentInProgress", Long.class)
					               .setParameter("istranchAssignmentInProgress", true).getSingleResult();

			return count > 0;
	}
}
