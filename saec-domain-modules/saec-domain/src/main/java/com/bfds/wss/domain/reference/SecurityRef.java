package com.bfds.wss.domain.reference;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")

public class SecurityRef implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(unique = true, nullable = false)
	private Integer securityId;
    /**
     * Type of Asset (cusip, isin, etc)
     */
	@Column(length=20)
	private String securityIdType;
	@Column
	private String name;
    /**
     * Security type (eq, fi)
     */
	@Column
	private String securityType;
	@Column
	private String securityClass;
	@Column
	private BigDecimal rate;
	@Temporal(TemporalType.DATE)
	@Column(length = 10)
	private Date maturityDate;
    /**
     * Securities stock ticker
     */
	@Column
	private String ticker;

    @Column
    private String cusip;
}
