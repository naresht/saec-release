// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.rip.domain;

import com.bfds.saec.rip.domain.RipObject;
import com.bfds.saec.rip.domain.RipObjectStatus;
import java.util.List;

privileged aspect RipObject_Roo_JavaBean {
    
    public String RipObject.getWorkType() {
        return this.workType;
    }
    
    public void RipObject.setWorkType(String workType) {
        this.workType = workType;
    }
    
    public RipObjectStatus RipObject.getStatus() {
        return this.status;
    }
    
    public void RipObject.setStatus(RipObjectStatus status) {
        this.status = status;
    }
    
    public Long RipObject.getCorrelationId() {
        return this.correlationId;
    }
    
    public void RipObject.setCorrelationId(Long correlationId) {
        this.correlationId = correlationId;
    }
    
    public String RipObject.getCreatedByUser() {
        return this.createdByUser;
    }
    
    public void RipObject.setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }
    
    public String RipObject.getBusinessUnit() {
        return this.businessUnit;
    }
    
    public void RipObject.setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }
    
    public String RipObject.getHostName() {
        return this.hostName;
    }
    
    public void RipObject.setHostName(String hostName) {
        this.hostName = hostName;
    }
    
    public List<String> RipObject.getCreatedByRoles() {
        return this.createdByRoles;
    }
    
    public void RipObject.setCreatedByRoles(List<String> createdByRoles) {
        this.createdByRoles = createdByRoles;
    }
    
}
