// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.EventPaymentConfig;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect EventPaymentConfig_Roo_Jpa_Entity {
    
    declare @type: EventPaymentConfig: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long EventPaymentConfig.id;
    
    @Version
    @Column(name = "version")
    private Integer EventPaymentConfig.version;
    
    public Long EventPaymentConfig.getId() {
        return this.id;
    }
    
    public void EventPaymentConfig.setId(Long id) {
        this.id = id;
    }
    
    public Integer EventPaymentConfig.getVersion() {
        return this.version;
    }
    
    public void EventPaymentConfig.setVersion(Integer version) {
        this.version = version;
    }
    
}