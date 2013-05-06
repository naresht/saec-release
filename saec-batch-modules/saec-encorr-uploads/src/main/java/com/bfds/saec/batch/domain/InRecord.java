package com.bfds.saec.batch.domain;

import javax.persistence.Column;

import org.springframework.batch.core.JobExecution;
import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", inheritanceType="TABLE_PER_CLASS")
@RooJavaBean
@RooConfigurable
@RooToString
public abstract class InRecord {
	
	/**
	 * The PK of the {@link JobExecution}
	 */
	@Column(nullable=false)
	private Long jobExecutionId;
	
	@Column(nullable=false)
	private InRecordStatus status;
	
}
