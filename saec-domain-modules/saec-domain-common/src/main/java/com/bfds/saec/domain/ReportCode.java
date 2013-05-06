package com.bfds.saec.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.reference.ReportCategory;
import com.google.common.base.Preconditions;

@RooJavaBean
@RooToString (excludeFields = { "id", "version"})
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findReportCodesByReportCategory", "findReportCodesByCode"})
public class ReportCode implements java.io.Serializable {
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
    private ReportCategory reportCategory;
    
    @NotNull
    @Column(nullable=false) 
    private Boolean activeForEvent = false ; 
    
//    @Enumerated(EnumType.STRING)
//    @NotNull
//    @Column(nullable=false)        
//    private MailType mailType;
    
    /**
     * @deprecated - Required by JPA. Use {@link #ReportCode(String, String, ReportCategory)} always.
     */
    @Deprecated    
    public ReportCode() {}
    
    public ReportCode(final String code, final String description, final ReportCategory reportCategory) {
    	this.code = code;
    	this.description = description;
    	this.reportCategory = reportCategory;
    	//this.mailType = mailType;
    }

    public static List<ReportCode> findByReportCategory(ReportCategory ReportCategory) {
    	Preconditions.checkNotNull(ReportCategory, "ReportCategory cannot be null");
        return findReportCodesByReportCategory(ReportCategory).getResultList();
    }

    public static ReportCode findByCode(String code) {
    	Preconditions.checkNotNull(code, "code cannot be null");
    	try {
        return findReportCodesByCode(code).getSingleResult();
    	}catch(EmptyResultDataAccessException e) {
    		return null;
    	}
    }
    
	public static ReportCode findByDescription(String description) {
		Preconditions.checkNotNull(description, "description cannot be null");
		description = description.trim().toLowerCase();
		try {
			EntityManager em = ReportCode.entityManager();
			TypedQuery<ReportCode> q = em
					.createQuery(
							"SELECT o FROM ReportCode AS o WHERE lower(o.description) = :description",
							ReportCode.class);
			q.setParameter("description", description);

			return q.getSingleResult();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
    
    private static TypedQuery<ReportCode> findReportCodesByCode(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = ReportCode.entityManager();
        TypedQuery<ReportCode> q = em.createQuery("SELECT o FROM ReportCode AS o WHERE o.code = :code", ReportCode.class);
        q.setParameter("code", code);
        return q;
    }
    
    private static TypedQuery<ReportCode> findReportCodesByReportCategory(ReportCategory ReportCategory) {
        if (ReportCategory == null) throw new IllegalArgumentException("The ReportCategory argument is required");
        EntityManager em = ReportCode.entityManager();
        TypedQuery<ReportCode> q = em.createQuery("SELECT o FROM ReportCode AS o WHERE o.ReportCategory = :ReportCategory", ReportCode.class);
        q.setParameter("ReportCategory", ReportCategory);
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
		ReportCode other = (ReportCode) obj;
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
