// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.WireOriginationInfo;

privileged aspect WireOriginationInfo_Roo_JavaBean {
    
    public String WireOriginationInfo.getReferenceNo() {
        return this.referenceNo;
    }
    
    public void WireOriginationInfo.setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    
    public RegistrationLines WireOriginationInfo.getLines() {
        return this.lines;
    }
    
    public void WireOriginationInfo.setLines(RegistrationLines lines) {
        this.lines = lines;
    }
    
}
