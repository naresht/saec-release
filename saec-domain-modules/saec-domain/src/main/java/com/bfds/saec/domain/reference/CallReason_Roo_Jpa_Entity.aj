// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.reference;

import com.bfds.saec.domain.reference.CallReason;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect CallReason_Roo_Jpa_Entity {
    
    declare @type: CallReason: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long CallReason.id;
    
    @Version
    @Column(name = "version")
    private Integer CallReason.version;
    
    public Long CallReason.getId() {
        return this.id;
    }
    
    public void CallReason.setId(Long id) {
        this.id = id;
    }
    
    public Integer CallReason.getVersion() {
        return this.version;
    }
    
    public void CallReason.setVersion(Integer version) {
        this.version = version;
    }
    
}
