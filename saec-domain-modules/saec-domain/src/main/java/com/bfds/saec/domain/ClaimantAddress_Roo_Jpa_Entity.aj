// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.ClaimantAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect ClaimantAddress_Roo_Jpa_Entity {
    
    declare @type: ClaimantAddress: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ClaimantAddress.id;
    
    @Version
    @Column(name = "version")
    private Integer ClaimantAddress.version;
    
    public Long ClaimantAddress.getId() {
        return this.id;
    }
    
    public void ClaimantAddress.setId(Long id) {
        this.id = id;
    }
    
    public Integer ClaimantAddress.getVersion() {
        return this.version;
    }
    
    public void ClaimantAddress.setVersion(Integer version) {
        this.version = version;
    }
    
}
