// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.WorkType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect WorkType_Roo_Jpa_Entity {
    
    declare @type: WorkType: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long WorkType.id;
    
    @Version
    @Column(name = "version")
    private Integer WorkType.version;
    
    public Long WorkType.getId() {
        return this.id;
    }
    
    public void WorkType.setId(Long id) {
        this.id = id;
    }
    
    public Integer WorkType.getVersion() {
        return this.version;
    }
    
    public void WorkType.setVersion(Integer version) {
        this.version = version;
    }
    
}
