// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Letter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Letter_Roo_Jpa_Entity {
    
    declare @type: Letter: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Letter.id;
    
    @Version
    @Column(name = "version")
    private Integer Letter.version;
    
    public Long Letter.getId() {
        return this.id;
    }
    
    public void Letter.setId(Long id) {
        this.id = id;
    }
    
    public Integer Letter.getVersion() {
        return this.version;
    }
    
    public void Letter.setVersion(Integer version) {
        this.version = version;
    }
    
}