package com.bfds.wss.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.wss.domain.reference.SecurityRef;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class ClaimEntryPosition implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
    @ManyToOne
    @JoinColumn(name = "Claim_Entry_fk")
    private ClaimEntry claimEntry;
    
    @NotNull
    @Column(nullable = false)
    private String fund ;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Asset_ID_fk", nullable=true)
    private SecurityRef securityRef;
    
    @NotNull
    @Column(nullable = false)
    private String positionType ;

    @Null
    @Column(nullable = true)
    private Date positionDate ;

    @NotNull
    @Column(nullable = false)
    private BigDecimal shareBalance ;
    
    @Null
    @Column(nullable = true)
    private BigDecimal accountBalance ;
            
}
