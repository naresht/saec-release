// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimUserResponseGroup;
import com.bfds.wss.domain.ClaimantUserResponses;

privileged aspect ClaimantUserResponses_Roo_JavaBean {
    
    public ClaimUserResponseGroup ClaimantUserResponses.getClaimUserResponseGroup() {
        return this.claimUserResponseGroup;
    }
    
    public void ClaimantUserResponses.setClaimUserResponseGroup(ClaimUserResponseGroup claimUserResponseGroup) {
        this.claimUserResponseGroup = claimUserResponseGroup;
    }
    
    public ClaimProof ClaimantUserResponses.getProof() {
        return this.proof;
    }
    
    public void ClaimantUserResponses.setProof(ClaimProof proof) {
        this.proof = proof;
    }
    
}
