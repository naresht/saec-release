// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Tranch;

privileged aspect Tranch_Roo_JavaBean {
    
    public String Tranch.getCode() {
        return this.code;
    }
    
    public void Tranch.setCode(String code) {
        this.code = code;
    }
    
    public LetterCode Tranch.getLetterCode() {
        return this.letterCode;
    }
    
    public void Tranch.setLetterCode(LetterCode letterCode) {
        this.letterCode = letterCode;
    }
    
    public String Tranch.getStatus() {
        return this.status;
    }
    
    public void Tranch.setStatus(String status) {
        this.status = status;
    }
    
}
