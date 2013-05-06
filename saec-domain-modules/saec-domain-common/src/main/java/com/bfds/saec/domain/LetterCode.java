package com.bfds.saec.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.reference.LetterType;
import com.google.common.base.Preconditions;

@RooJavaBean
@RooToString (excludeFields = { "id", "version"})
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findLetterCodesByLetterType", "findLetterCodesByCode"})
public class LetterCode implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(nullable=false, unique=true)
    private String code;

    @NotNull
    @Column(nullable=false, unique=true)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable=false)    
    private LetterType letterType;
    
    @NotNull
    @Column(nullable=false) 
    private Boolean activeForEvent = false ; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_type", nullable = true)
    private WorkType workType;
    
//    @Enumerated(EnumType.STRING)
//    @NotNull
//    @Column(nullable=false)        
//    private MailType mailType;
    
    /**
     * @deprecated - Required by JPA. Use {@link #LetterCode(String, String, LetterType)} always.
     */
    @Deprecated    
    public LetterCode() {}
    
    public boolean insCureLetterCode() {
    	return LetterType.CLAIM_FORM_CURE_LETTER == this.letterType
    			|| LetterType.OPTOUT_CURE_LETTER== this.letterType
    			|| LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER == this.letterType;
    }
    
    public LetterCode(final String code, final String description, final LetterType letterType) {
    	this.code = code;
    	this.description = description;
    	this.letterType = letterType;
    }

    public static List<LetterCode> findByLetterType(LetterType letterType) {
    	Preconditions.checkNotNull(letterType, "letterType cannot be null");
        return findLetterCodesByLetterType(letterType).getResultList();
    }

    public static LetterCode findByCode(String code) {
    	Preconditions.checkNotNull(code, "code cannot be null");
    	try {
        return findLetterCodesByCode(code).getSingleResult();
    	}catch(EmptyResultDataAccessException e) {
    		return null;
    	}
    }
    
	public static LetterCode findByDescription(String description) {
		Preconditions.checkNotNull(description, "description cannot be null");
		description = description.trim().toLowerCase();
		try {
			EntityManager em = LetterCode.entityManager();
			TypedQuery<LetterCode> q = em
					.createQuery(
							"SELECT o FROM LetterCode AS o WHERE lower(o.description) = :description",
							LetterCode.class);
			q.setParameter("description", description);

			return q.getSingleResult();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
    
    private static TypedQuery<LetterCode> findLetterCodesByCode(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = LetterCode.entityManager();
        TypedQuery<LetterCode> q = em.createQuery("SELECT o FROM LetterCode AS o WHERE o.code = :code", LetterCode.class);
        q.setParameter("code", code);
        return q;
    }
    
    private static TypedQuery<LetterCode> findLetterCodesByLetterType(LetterType letterType) {
        if (letterType == null) throw new IllegalArgumentException("The letterType argument is required");
        EntityManager em = LetterCode.entityManager();
        TypedQuery<LetterCode> q = em.createQuery("SELECT o FROM LetterCode AS o WHERE o.letterType = :letterType", LetterCode.class);
        q.setParameter("letterType", letterType);
        return q;
    }   
    

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LetterCode other = (LetterCode) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}
        
}
