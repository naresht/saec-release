package com.bfds.saec.rip.domain;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooConfigurable
@RooToString
public class FollowupRemindersRipObject extends RipObject {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String referenceNo;

	private String reminderType;

}