// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimActivity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect ClaimActivity_Roo_Jpa_Entity {
    
    declare @type: ClaimActivity: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ClaimActivity.id;
    
    @Version
    @Column(name = "version")
    private Integer ClaimActivity.version;
    
    public Long ClaimActivity.getId() {
        return this.id;
    }
    
    public void ClaimActivity.setId(Long id) {
        this.id = id;
    }
    
    public Integer ClaimActivity.getVersion() {
        return this.version;
    }
    
    public void ClaimActivity.setVersion(Integer version) {
        this.version = version;
    }
    
}