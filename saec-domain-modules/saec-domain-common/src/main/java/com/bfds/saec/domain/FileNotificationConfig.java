package com.bfds.saec.domain;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.reference.BatchFileLov;
import com.bfds.saec.domain.reference.PermissionType;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 public class FileNotificationConfig implements Serializable, Cloneable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7317705578628760867L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_fk", nullable = true)
    private Event event;

	private String fileNotificationType ;
	
	private String mailId ; 
	
	PermissionType permType ;
	
}
