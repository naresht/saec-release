// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.ReportCode;
import com.bfds.saec.domain.reference.ReportCategory;

privileged aspect ReportCode_Roo_JavaBean {
    
    public String ReportCode.getCode() {
        return this.code;
    }
    
    public void ReportCode.setCode(String code) {
        this.code = code;
    }
    
    public String ReportCode.getDescription() {
        return this.description;
    }
    
    public void ReportCode.setDescription(String description) {
        this.description = description;
    }
    
    public ReportCategory ReportCode.getReportCategory() {
        return this.reportCategory;
    }
    
    public void ReportCode.setReportCategory(ReportCategory reportCategory) {
        this.reportCategory = reportCategory;
    }
    
    public Boolean ReportCode.getActiveForEvent() {
        return this.activeForEvent;
    }
    
    public void ReportCode.setActiveForEvent(Boolean activeForEvent) {
        this.activeForEvent = activeForEvent;
    }
    
}