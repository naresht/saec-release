// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.PaymentLetterCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect PaymentLetterCode_Roo_Jpa_Entity {
    
    declare @type: PaymentLetterCode: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long PaymentLetterCode.id;
    
    @Version
    @Column(name = "version")
    private Integer PaymentLetterCode.version;
    
    public Long PaymentLetterCode.getId() {
        return this.id;
    }
    
    public void PaymentLetterCode.setId(Long id) {
        this.id = id;
    }
    
    public Integer PaymentLetterCode.getVersion() {
        return this.version;
    }
    
    public void PaymentLetterCode.setVersion(Integer version) {
        this.version = version;
    }
    
}
