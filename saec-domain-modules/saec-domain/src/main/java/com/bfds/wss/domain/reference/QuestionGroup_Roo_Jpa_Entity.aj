// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain.reference;

import com.bfds.wss.domain.reference.QuestionGroup;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect QuestionGroup_Roo_Jpa_Entity {
    
    declare @type: QuestionGroup: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long QuestionGroup.id;
    
    @Version
    @Column(name = "version")
    private Integer QuestionGroup.version;
    
    public Long QuestionGroup.getId() {
        return this.id;
    }
    
    public void QuestionGroup.setId(Long id) {
        this.id = id;
    }
    
    public Integer QuestionGroup.getVersion() {
        return this.version;
    }
    
    public void QuestionGroup.setVersion(Integer version) {
        this.version = version;
    }
    
}