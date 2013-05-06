// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.WorkType;
import com.bfds.saec.domain.reference.LetterType;

privileged aspect LetterCode_Roo_JavaBean {
    
    public String LetterCode.getCode() {
        return this.code;
    }
    
    public void LetterCode.setCode(String code) {
        this.code = code;
    }
    
    public String LetterCode.getDescription() {
        return this.description;
    }
    
    public void LetterCode.setDescription(String description) {
        this.description = description;
    }
    
    public LetterType LetterCode.getLetterType() {
        return this.letterType;
    }
    
    public void LetterCode.setLetterType(LetterType letterType) {
        this.letterType = letterType;
    }
    
    public Boolean LetterCode.getActiveForEvent() {
        return this.activeForEvent;
    }
    
    public void LetterCode.setActiveForEvent(Boolean activeForEvent) {
        this.activeForEvent = activeForEvent;
    }
    
    public WorkType LetterCode.getWorkType() {
        return this.workType;
    }
    
    public void LetterCode.setWorkType(WorkType workType) {
        this.workType = workType;
    }
    
}