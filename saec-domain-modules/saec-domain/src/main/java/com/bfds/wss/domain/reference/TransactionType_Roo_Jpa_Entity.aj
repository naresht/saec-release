// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain.reference;

import com.bfds.wss.domain.reference.TransactionType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect TransactionType_Roo_Jpa_Entity {
    
    declare @type: TransactionType: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long TransactionType.id;
    
    @Version
    @Column(name = "version")
    private Integer TransactionType.version;
    
    public Long TransactionType.getId() {
        return this.id;
    }
    
    public void TransactionType.setId(Long id) {
        this.id = id;
    }
    
    public Integer TransactionType.getVersion() {
        return this.version;
    }
    
    public void TransactionType.setVersion(Integer version) {
        this.version = version;
    }
    
}