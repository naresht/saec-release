// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Name;

privileged aspect Name_Roo_JavaBean {
    
    public String Name.getFirstName() {
        return this.firstName;
    }
    
    public void Name.setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String Name.getLastName() {
        return this.lastName;
    }
    
    public void Name.setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String Name.getMiddleName() {
        return this.middleName;
    }
    
    public void Name.setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
}