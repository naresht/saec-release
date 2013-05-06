// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantTransactions;
import com.bfds.wss.domain.reference.SecurityFund;
import com.bfds.wss.domain.reference.TransactionType;
import java.math.BigDecimal;
import java.util.Date;

privileged aspect ClaimantTransactions_Roo_JavaBean {
    
    public ClaimantClaim ClaimantTransactions.getClaimantClaim() {
        return this.claimantClaim;
    }
    
    public void ClaimantTransactions.setClaimantClaim(ClaimantClaim claimantClaim) {
        this.claimantClaim = claimantClaim;
    }
    
    public SecurityFund ClaimantTransactions.getSecurityFund() {
        return this.securityFund;
    }
    
    public void ClaimantTransactions.setSecurityFund(SecurityFund securityFund) {
        this.securityFund = securityFund;
    }
    
    public TransactionType ClaimantTransactions.getTransactionType() {
        return this.transactionType;
    }
    
    public void ClaimantTransactions.setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    
    public ClaimProof ClaimantTransactions.getProof() {
        return this.proof;
    }
    
    public void ClaimantTransactions.setProof(ClaimProof proof) {
        this.proof = proof;
    }
    
    public String ClaimantTransactions.getTransactionCode() {
        return this.transactionCode;
    }
    
    public void ClaimantTransactions.setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
    
    public String ClaimantTransactions.getSourceTransactionCode() {
        return this.sourceTransactionCode;
    }
    
    public void ClaimantTransactions.setSourceTransactionCode(String sourceTransactionCode) {
        this.sourceTransactionCode = sourceTransactionCode;
    }
    
    public String ClaimantTransactions.getSourceTransactionType() {
        return this.sourceTransactionType;
    }
    
    public void ClaimantTransactions.setSourceTransactionType(String sourceTransactionType) {
        this.sourceTransactionType = sourceTransactionType;
    }
    
    public Date ClaimantTransactions.getTradeDate() {
        return this.tradeDate;
    }
    
    public void ClaimantTransactions.setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }
    
    public Date ClaimantTransactions.getSettlementDate() {
        return this.settlementDate;
    }
    
    public void ClaimantTransactions.setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }
    
    public BigDecimal ClaimantTransactions.getQuantity() {
        return this.quantity;
    }
    
    public void ClaimantTransactions.setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal ClaimantTransactions.getUnitPrice() {
        return this.unitPrice;
    }
    
    public void ClaimantTransactions.setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal ClaimantTransactions.getGrossAmount() {
        return this.grossAmount;
    }
    
    public void ClaimantTransactions.setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }
    
    public BigDecimal ClaimantTransactions.getNetAmount() {
        return this.netAmount;
    }
    
    public void ClaimantTransactions.setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }
    
    public BigDecimal ClaimantTransactions.getCommission() {
        return this.commission;
    }
    
    public void ClaimantTransactions.setCommission(BigDecimal commission) {
        this.commission = commission;
    }
    
    public BigDecimal ClaimantTransactions.getFees() {
        return this.fees;
    }
    
    public void ClaimantTransactions.setFees(BigDecimal fees) {
        this.fees = fees;
    }
    
    public BigDecimal ClaimantTransactions.getGainLoss() {
        return this.gainLoss;
    }
    
    public void ClaimantTransactions.setGainLoss(BigDecimal gainLoss) {
        this.gainLoss = gainLoss;
    }
    
    public BigDecimal ClaimantTransactions.getTotalCost() {
        return this.totalCost;
    }
    
    public void ClaimantTransactions.setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public BigDecimal ClaimantTransactions.getCostBasis() {
        return this.costBasis;
    }
    
    public void ClaimantTransactions.setCostBasis(BigDecimal costBasis) {
        this.costBasis = costBasis;
    }
    
    public BigDecimal ClaimantTransactions.getWitholding() {
        return this.witholding;
    }
    
    public void ClaimantTransactions.setWitholding(BigDecimal witholding) {
        this.witholding = witholding;
    }
    
    public BigDecimal ClaimantTransactions.getBeginningBalance() {
        return this.beginningBalance;
    }
    
    public void ClaimantTransactions.setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }
    
    public BigDecimal ClaimantTransactions.getEndingBalance() {
        return this.endingBalance;
    }
    
    public void ClaimantTransactions.setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }
    
    public boolean ClaimantTransactions.isProofProvidedInd() {
        return this.proofProvidedInd;
    }
    
    public void ClaimantTransactions.setProofProvidedInd(boolean proofProvidedInd) {
        this.proofProvidedInd = proofProvidedInd;
    }
    
    public boolean ClaimantTransactions.isDiscrepencyInd() {
        return this.discrepencyInd;
    }
    
    public void ClaimantTransactions.setDiscrepencyInd(boolean discrepencyInd) {
        this.discrepencyInd = discrepencyInd;
    }
    
    public String ClaimantTransactions.getComments() {
        return this.comments;
    }
    
    public void ClaimantTransactions.setComments(String comments) {
        this.comments = comments;
    }
    
    public Boolean ClaimantTransactions.getDeleted() {
        return this.deleted;
    }
    
    public void ClaimantTransactions.setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
}
