package com.bfds.wss.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")

public class SecurityQuestion implements java.io.Serializable {

	@Column
	private String securityQuestionTxt;

}
