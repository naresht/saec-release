// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.saec.domain.Claimant;
import com.bfds.wss.domain.ClaimantClaimId;

privileged aspect ClaimantClaimId_Roo_JavaBean {
    
    public String ClaimantClaimId.getClaimIdentifier() {
        return this.claimIdentifier;
    }
    
    public void ClaimantClaimId.setClaimIdentifier(String claimIdentifier) {
        this.claimIdentifier = claimIdentifier;
    }
    
    public int ClaimantClaimId.getControlNumber() {
        return this.controlNumber;
    }
    
    public void ClaimantClaimId.setControlNumber(int controlNumber) {
        this.controlNumber = controlNumber;
    }
    
    public Claimant ClaimantClaimId.getClaimant() {
        return this.claimant;
    }
    
    public void ClaimantClaimId.setClaimant(Claimant claimant) {
        this.claimant = claimant;
    }
    
}
