package com.bfds.saec.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * Tracks User Logins as a security/audit measure
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @RooJavaBean
public class UserLoginAudit implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="user_fk")
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(length = 0)
	private Date datetime;

	// is successful or not
	private Boolean isSuccessful;

	public UserLoginAudit(User user, Date datetime) {
		this.user = user;
		this.datetime = datetime;
	}
}
