// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Contact;
import com.bfds.saec.domain.Name;

privileged aspect Contact_Roo_JavaBean {
    
    public void Contact.setName(Name name) {
        this.name = name;
    }
    
    public String Contact.getPhoneNo() {
        return this.phoneNo;
    }
    
    public void Contact.setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    
    public String Contact.getWorkPhoneNo() {
        return this.workPhoneNo;
    }
    
    public void Contact.setWorkPhoneNo(String workPhoneNo) {
        this.workPhoneNo = workPhoneNo;
    }
    
    public String Contact.getCellPhoneNo() {
        return this.cellPhoneNo;
    }
    
    public void Contact.setCellPhoneNo(String cellPhoneNo) {
        this.cellPhoneNo = cellPhoneNo;
    }
    
    public String Contact.getEmail() {
        return this.email;
    }
    
    public void Contact.setEmail(String email) {
        this.email = email;
    }
    
    public String Contact.getComments() {
        return this.comments;
    }
    
    public void Contact.setComments(String comments) {
        this.comments = comments;
    }
    
    public Claimant Contact.getClaimant() {
        return this.claimant;
    }
    
    public void Contact.setClaimant(Claimant claimant) {
        this.claimant = claimant;
    }
    
    public Claimant Contact.getPrimaryContactOf() {
        return this.primaryContactOf;
    }
    
    public void Contact.setPrimaryContactOf(Claimant primaryContactOf) {
        this.primaryContactOf = primaryContactOf;
    }
    
}