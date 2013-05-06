// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimEntry;
import com.bfds.wss.domain.ClaimEntryTransactions;
import com.bfds.wss.domain.reference.SecurityRef;
import java.math.BigDecimal;
import java.util.Date;

privileged aspect ClaimEntryTransactions_Roo_JavaBean {
    
    public ClaimEntry ClaimEntryTransactions.getClaimEntry() {
        return this.claimEntry;
    }
    
    public void ClaimEntryTransactions.setClaimEntry(ClaimEntry claimEntry) {
        this.claimEntry = claimEntry;
    }
    
    public String ClaimEntryTransactions.getFund() {
        return this.fund;
    }
    
    public void ClaimEntryTransactions.setFund(String fund) {
        this.fund = fund;
    }
    
    public SecurityRef ClaimEntryTransactions.getSecurityRef() {
        return this.securityRef;
    }
    
    public void ClaimEntryTransactions.setSecurityRef(SecurityRef securityRef) {
        this.securityRef = securityRef;
    }
    
    public String ClaimEntryTransactions.getTransactionType() {
        return this.transactionType;
    }
    
    public void ClaimEntryTransactions.setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public String ClaimEntryTransactions.getTransactionCode() {
        return this.transactionCode;
    }
    
    public void ClaimEntryTransactions.setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
    
    public String ClaimEntryTransactions.getTransactionCodeSource() {
        return this.transactionCodeSource;
    }
    
    public void ClaimEntryTransactions.setTransactionCodeSource(String transactionCodeSource) {
        this.transactionCodeSource = transactionCodeSource;
    }
    
    public Date ClaimEntryTransactions.getTradeDate() {
        return this.tradeDate;
    }
    
    public void ClaimEntryTransactions.setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }
    
    public Date ClaimEntryTransactions.getSettlementDate() {
        return this.settlementDate;
    }
    
    public void ClaimEntryTransactions.setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }
    
    public BigDecimal ClaimEntryTransactions.getQuantity() {
        return this.quantity;
    }
    
    public void ClaimEntryTransactions.setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal ClaimEntryTransactions.getUnitPrice() {
        return this.unitPrice;
    }
    
    public void ClaimEntryTransactions.setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal ClaimEntryTransactions.getGrossAmount() {
        return this.grossAmount;
    }
    
    public void ClaimEntryTransactions.setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }
    
    public BigDecimal ClaimEntryTransactions.getNetAmount() {
        return this.netAmount;
    }
    
    public void ClaimEntryTransactions.setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }
    
    public BigDecimal ClaimEntryTransactions.getCommission() {
        return this.commission;
    }
    
    public void ClaimEntryTransactions.setCommission(BigDecimal commission) {
        this.commission = commission;
    }
    
    public BigDecimal ClaimEntryTransactions.getFees() {
        return this.fees;
    }
    
    public void ClaimEntryTransactions.setFees(BigDecimal fees) {
        this.fees = fees;
    }
    
    public BigDecimal ClaimEntryTransactions.getGainLoss() {
        return this.gainLoss;
    }
    
    public void ClaimEntryTransactions.setGainLoss(BigDecimal gainLoss) {
        this.gainLoss = gainLoss;
    }
    
    public BigDecimal ClaimEntryTransactions.getCostBasis() {
        return this.costBasis;
    }
    
    public void ClaimEntryTransactions.setCostBasis(BigDecimal costBasis) {
        this.costBasis = costBasis;
    }
    
    public BigDecimal ClaimEntryTransactions.getWitholding() {
        return this.witholding;
    }
    
    public void ClaimEntryTransactions.setWitholding(BigDecimal witholding) {
        this.witholding = witholding;
    }
    
    public BigDecimal ClaimEntryTransactions.getBeginningBalance() {
        return this.beginningBalance;
    }
    
    public void ClaimEntryTransactions.setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }
    
    public BigDecimal ClaimEntryTransactions.getEndingBalance() {
        return this.endingBalance;
    }
    
    public void ClaimEntryTransactions.setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }
    
}