// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.UserLoginAudit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect UserLoginAudit_Roo_Jpa_Entity {
    
    declare @type: UserLoginAudit: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long UserLoginAudit.id;
    
    @Version
    @Column(name = "version")
    private Integer UserLoginAudit.version;
    
    public UserLoginAudit.new() {
        super();
    }

    public Long UserLoginAudit.getId() {
        return this.id;
    }
    
    public void UserLoginAudit.setId(Long id) {
        this.id = id;
    }
    
    public Integer UserLoginAudit.getVersion() {
        return this.version;
    }
    
    public void UserLoginAudit.setVersion(Integer version) {
        this.version = version;
    }
    
}
