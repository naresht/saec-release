// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.GroupMailCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect GroupMailCode_Roo_Jpa_Entity {
    
    declare @type: GroupMailCode: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long GroupMailCode.id;
    
    @Version
    @Column(name = "version")
    private Integer GroupMailCode.version;
    
    public Long GroupMailCode.getId() {
        return this.id;
    }
    
    public void GroupMailCode.setId(Long id) {
        this.id = id;
    }
    
    public Integer GroupMailCode.getVersion() {
        return this.version;
    }
    
    public void GroupMailCode.setVersion(Integer version) {
        this.version = version;
    }
    
}
