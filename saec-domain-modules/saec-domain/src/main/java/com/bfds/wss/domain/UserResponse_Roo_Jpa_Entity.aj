// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.UserResponse;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;

privileged aspect UserResponse_Roo_Jpa_Entity {
    
    declare @type: UserResponse: @Entity;
    
    declare @type: UserResponse: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long UserResponse.id;
    
    @Version
    @Column(name = "version")
    private Integer UserResponse.version;
    
    public Long UserResponse.getId() {
        return this.id;
    }
    
    public void UserResponse.setId(Long id) {
        this.id = id;
    }
    
    public Integer UserResponse.getVersion() {
        return this.version;
    }
    
    public void UserResponse.setVersion(Integer version) {
        this.version = version;
    }
    
}