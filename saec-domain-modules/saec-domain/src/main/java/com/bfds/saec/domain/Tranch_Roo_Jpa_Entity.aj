// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Tranch;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Tranch_Roo_Jpa_Entity {
    
    declare @type: Tranch: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Tranch.id;
    
    @Version
    @Column(name = "version")
    private Integer Tranch.version;
    
    public Long Tranch.getId() {
        return this.id;
    }
    
    public void Tranch.setId(Long id) {
        this.id = id;
    }
    
    public Integer Tranch.getVersion() {
        return this.version;
    }
    
    public void Tranch.setVersion(Integer version) {
        this.version = version;
    }
    
}
