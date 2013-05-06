package com.bfds.wss.domain.reference;

// Generated Jul 18, 2012 1:10:30 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;


@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class SecurityNavRef implements java.io.Serializable {

	@Column
	private Integer assetId;
	
	@Column
	private String source;

	@Temporal(TemporalType.DATE)
	@Column(name = "Date", length = 10)
	private Date date;
	
	@Column
	private BigDecimal lowValue;
	
	@Column
	private BigDecimal highValue;
	
	@Column
	private BigDecimal averageValue;


}
