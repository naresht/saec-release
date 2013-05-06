package com.bfds.saec.domain;

import java.io.Serializable;

import javax.persistence.Column;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 *  A code that indicates if/why account should not be included in distribution tranche. 
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findGroupMailCodesByCode"})
@RooJavaBean
@RooToString
public final class GroupMailCode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(unique = true, nullable = false)
	private String code;
	
	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String address5;

	private String address6;
	
	private String address7;

}
