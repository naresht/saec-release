package com.bfds.wss.domain.reference;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import javax.persistence.Column;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class ProofType implements java.io.Serializable {

    @Column(nullable = false, unique = true)
    private String code;
}
