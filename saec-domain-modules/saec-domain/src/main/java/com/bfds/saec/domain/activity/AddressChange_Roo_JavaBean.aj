// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.reference.ChangeSource;

privileged aspect AddressChange_Roo_JavaBean {
    
    public Address AddressChange.getFrom() {
        return this.from;
    }
    
    public void AddressChange.setFrom(Address from) {
        this.from = from;
    }
    
    public Address AddressChange.getTo() {
        return this.to;
    }
    
    public void AddressChange.setTo(Address to) {
        this.to = to;
    }
    
    public ChangeSource AddressChange.getSource() {
        return this.source;
    }
    
    public void AddressChange.setSource(ChangeSource source) {
        this.source = source;
    }
    
}
