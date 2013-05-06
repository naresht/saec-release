// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimEntry;
import com.bfds.wss.domain.ClaimEntryPosition;
import com.bfds.wss.domain.reference.SecurityRef;
import java.math.BigDecimal;
import java.util.Date;

privileged aspect ClaimEntryPosition_Roo_JavaBean {
    
    public ClaimEntry ClaimEntryPosition.getClaimEntry() {
        return this.claimEntry;
    }
    
    public void ClaimEntryPosition.setClaimEntry(ClaimEntry claimEntry) {
        this.claimEntry = claimEntry;
    }
    
    public String ClaimEntryPosition.getFund() {
        return this.fund;
    }
    
    public void ClaimEntryPosition.setFund(String fund) {
        this.fund = fund;
    }
    
    public SecurityRef ClaimEntryPosition.getSecurityRef() {
        return this.securityRef;
    }
    
    public void ClaimEntryPosition.setSecurityRef(SecurityRef securityRef) {
        this.securityRef = securityRef;
    }
    
    public String ClaimEntryPosition.getPositionType() {
        return this.positionType;
    }
    
    public void ClaimEntryPosition.setPositionType(String positionType) {
        this.positionType = positionType;
    }
    
    public Date ClaimEntryPosition.getPositionDate() {
        return this.positionDate;
    }
    
    public void ClaimEntryPosition.setPositionDate(Date positionDate) {
        this.positionDate = positionDate;
    }
    
    public BigDecimal ClaimEntryPosition.getShareBalance() {
        return this.shareBalance;
    }
    
    public void ClaimEntryPosition.setShareBalance(BigDecimal shareBalance) {
        this.shareBalance = shareBalance;
    }
    
    public BigDecimal ClaimEntryPosition.getAccountBalance() {
        return this.accountBalance;
    }
    
    public void ClaimEntryPosition.setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }
    
}
