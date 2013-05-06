package com.bfds.wss.domain.reference;

// Generated Jul 13, 2012 2:27:59 PM by Hibernate Tools 4.0.0

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import javax.persistence.Column;

/**
 * Type of transaction (Buy, Sell, etc)
 */
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class TransactionType implements java.io.Serializable {
    @Column(nullable = false, unique = true)
	private String code;

}
