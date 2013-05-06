package com.bfds.wss.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.saec.domain.util.AccountContext;
import com.bfds.wss.domain.reference.ClaimActivityType;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class ClaimActivity implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Claimant_Claim_fk", nullable = true)
    private ClaimantClaim claimantClaim;
    
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimActivityType activityType ;

    /**
     * Date on which the activity occurred
     */
    @NotNull
    @Column(nullable = false)
    private Date activityDate ;

    /**
     * System in which the activity occurred (SASEC or WSS)
     */
    @Null
    @Column(nullable = true)
    private String system ;
    /**
     * User who made the change
     */
    @Null
    @Column(nullable = true)
    private String userId ;

    /**
     * TODO: What is this ?
     */
    @NotNull
    @Column(nullable = false)
    private String status ;
    
    @Null
    @Column(nullable = true)
    private String comments ;
    
    public static ClaimActivity setActivityDefaults(final ClaimActivity activity) {
		activity.setActivityDate(new Date());
		activity.setUserId(AccountContext.getCurrentUsername());
		return activity;
	}
        
}
