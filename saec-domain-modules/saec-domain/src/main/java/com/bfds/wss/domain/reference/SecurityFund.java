package com.bfds.wss.domain.reference;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")

public class SecurityFund implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
    private String fund;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "security_ref_fk", nullable = true)
    private SecurityRef securityRef;
}
