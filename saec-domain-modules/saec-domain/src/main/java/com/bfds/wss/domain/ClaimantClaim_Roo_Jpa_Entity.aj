// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimantClaim;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect ClaimantClaim_Roo_Jpa_Entity {
    
    declare @type: ClaimantClaim: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ClaimantClaim.id;
    
    @Version
    @Column(name = "version")
    private Integer ClaimantClaim.version;
    
    public Long ClaimantClaim.getId() {
        return this.id;
    }
    
    public void ClaimantClaim.setId(Long id) {
        this.id = id;
    }
    
    public Integer ClaimantClaim.getVersion() {
        return this.version;
    }
    
    public void ClaimantClaim.setVersion(Integer version) {
        this.version = version;
    }
    
}