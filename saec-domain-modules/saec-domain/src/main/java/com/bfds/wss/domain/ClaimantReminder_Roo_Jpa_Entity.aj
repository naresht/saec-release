// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimantReminder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect ClaimantReminder_Roo_Jpa_Entity {
    
    declare @type: ClaimantReminder: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ClaimantReminder.id;
    
    @Version
    @Column(name = "version")
    private Integer ClaimantReminder.version;
    
    public Long ClaimantReminder.getId() {
        return this.id;
    }
    
    public void ClaimantReminder.setId(Long id) {
        this.id = id;
    }
    
    public Integer ClaimantReminder.getVersion() {
        return this.version;
    }
    
    public void ClaimantReminder.setVersion(Integer version) {
        this.version = version;
    }
    
}
