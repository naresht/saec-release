// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.batch.domain;

import com.bfds.saec.batch.domain.InRecord;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;

privileged aspect InRecord_Roo_Jpa_Entity {
    
    declare @type: InRecord: @Entity;
    
    declare @type: InRecord: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long InRecord.id;
    
    @Version
    @Column(name = "version")
    private Integer InRecord.version;
    
    public Long InRecord.getId() {
        return this.id;
    }
    
    public void InRecord.setId(Long id) {
        this.id = id;
    }
    
    public Integer InRecord.getVersion() {
        return this.version;
    }
    
    public void InRecord.setVersion(Integer version) {
        this.version = version;
    }
    
}
