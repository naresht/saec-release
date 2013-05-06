package com.bfds.wss.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.service.DataFieldMaxValueIncrementerService;


@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = "findClaimEntryByUser")
public class ClaimEntry implements Serializable {
	    
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private transient DataFieldMaxValueIncrementerService dataFieldMaxValueIncrementerService;
	
    @OneToMany(mappedBy = "claimEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClaimEntryPosition> claimEntryPositions = new HashSet<ClaimEntryPosition>();
    
    @OneToMany(mappedBy = "claimEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClaimEntryTransactions> claimEntryTransactions = new HashSet<ClaimEntryTransactions>();
 
    @OneToMany(mappedBy = "claimEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClaimEntryUserResponseGroup> claimEntryUserResponseGroups = new ArrayList<ClaimEntryUserResponseGroup>();
     
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "claimEntry")
	private Set<ClaimantReminder> reminders = new HashSet<ClaimantReminder>(0);

   // @OneToOne(fetch=FetchType.EAGER, optional=false)
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "security_user_fk", nullable=true)
    private SecurityUser securityUser ;
        
    @NotNull
    @Column(nullable = false)
	private String claimIdentifier ;
    
    @NotNull
    @Column(nullable = false)
    private int controlNumber ;

    @NotNull
    @Column(nullable = false)
    private String firstName ;

    @Null
    @Column(nullable = true)
    private String middleName ;

    @Null
    @Column(nullable = true)
    private String lastName ;
    
    @Null
    @Column(nullable = true)
    private String email ;

    @Null
    @Column(nullable = true)
    private String homePhone ;
    
    @Null
    @Column(nullable = true)
    private String workPhone ;

    @Null
    @Column(nullable = true)
    private String cellPhone ;

    @NotNull
    @Column(nullable = false)
    private String address1 ;
    
    @Null
    @Column(nullable = true)
    private String address2 ;
    
    @Null
    @Column(nullable = true)
    private String address3 ;
    
    @Null
    @Column(nullable = true)
    private String city ;
    
    @Null
    @Column(nullable = true)
    private String stateCode ;
    
    @Null
    @Column(nullable = true)
    private String countryCode ;
    
    @Null
    @Column(nullable = true)
    private String zip ;
    
    @Null
    @Column(nullable = true)
    private Date expiryDate ;
    
    @Null
    @Column(nullable = true)
    private short statementOfTruth ;
    
    @Null
    @Column(nullable = true)
    private String ssn;

    @Null
    @Column(nullable = true)
    private String taxId;
    
    @Null
    @Column(nullable = true)
    private String companyName;
    
    /**
     * Generate the unique reference# for this {@link Claimant} when it is created.
     */
    @PrePersist
    public void generateReferenceNumber() {
    	if (this.claimIdentifier == null) {
    		if (!StringUtils.hasText(this.claimIdentifier)) {
    			this.claimIdentifier = getNextClaimtIdentifierNumber() ;
    			this.controlNumber = 1 ;
    		}
    	}
    }
    
	@Transactional(propagation=Propagation.SUPPORTS)
	public String getNextClaimtIdentifierNumber() {
		return dataFieldMaxValueIncrementerService.getNextClaimtIdentifierNumber() ;
	}
    
    public static TypedQuery<ClaimEntry> findClaimEntryByUser(String argUserName) {
        if (argUserName == null || argUserName.length() == 0) throw new IllegalArgumentException("The userName argument is required");
        EntityManager em = ClaimEntry.entityManager();
        TypedQuery<ClaimEntry> q = em.createQuery("SELECT o FROM ClaimEntry AS o WHERE o.securityUser.email = :userName", ClaimEntry.class);
        q.setParameter("userName", argUserName);
        return q;
    }
}
