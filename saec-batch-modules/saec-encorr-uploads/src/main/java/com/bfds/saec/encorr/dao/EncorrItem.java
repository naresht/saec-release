package com.bfds.saec.encorr.dao;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.batch.domain.InRecord;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", table="batch_encorr_item")
public class EncorrItem extends InRecord {

	private String businessArea;
	private String workType;
	private String referenceNo;
	private String ssn;
	private String mailControlNo;
}
