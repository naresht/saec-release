// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantDistribution;
import com.bfds.wss.domain.reference.ClaimantDistributionType;
import java.math.BigDecimal;
import java.util.Date;

privileged aspect ClaimantDistribution_Roo_JavaBean {
    
    public ClaimantClaim ClaimantDistribution.getClaimantClaim() {
        return this.claimantClaim;
    }
    
    public void ClaimantDistribution.setClaimantClaim(ClaimantClaim claimantClaim) {
        this.claimantClaim = claimantClaim;
    }
    
    public String ClaimantDistribution.getCategory1() {
        return this.category1;
    }
    
    public void ClaimantDistribution.setCategory1(String category1) {
        this.category1 = category1;
    }
    
    public String ClaimantDistribution.getCategory2() {
        return this.category2;
    }
    
    public void ClaimantDistribution.setCategory2(String category2) {
        this.category2 = category2;
    }
    
    public String ClaimantDistribution.getCategory3() {
        return this.category3;
    }
    
    public void ClaimantDistribution.setCategory3(String category3) {
        this.category3 = category3;
    }
    
    public ClaimantDistributionType ClaimantDistribution.getDistributionType() {
        return this.distributionType;
    }
    
    public void ClaimantDistribution.setDistributionType(ClaimantDistributionType distributionType) {
        this.distributionType = distributionType;
    }
    
    public BigDecimal ClaimantDistribution.getDistributionAmount() {
        return this.distributionAmount;
    }
    
    public void ClaimantDistribution.setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
    }
    
    public Date ClaimantDistribution.getDistributionCalculatedDate() {
        return this.distributionCalculatedDate;
    }
    
    public void ClaimantDistribution.setDistributionCalculatedDate(Date distributionCalculatedDate) {
        this.distributionCalculatedDate = distributionCalculatedDate;
    }
    
    public String ClaimantDistribution.getSource() {
        return this.source;
    }
    
    public void ClaimantDistribution.setSource(String source) {
        this.source = source;
    }
    
    public String ClaimantDistribution.getStatus() {
        return this.status;
    }
    
    public void ClaimantDistribution.setStatus(String status) {
        this.status = status;
    }
    
}