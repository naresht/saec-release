// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.dto;

import com.bfds.saec.domain.dto.TranchAssignmentSearchCriteriaDto;
import com.bfds.saec.domain.reference.PaymentType;
import java.util.List;

privileged aspect TranchAssignmentSearchCriteriaDto_Roo_JavaBean {
    
    public String[] TranchAssignmentSearchCriteriaDto.getStateCode() {
        return this.stateCode;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setStateCode(String[] stateCode) {
        this.stateCode = stateCode;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getFromCheckNo() {
        return this.fromCheckNo;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setFromCheckNo(String fromCheckNo) {
        this.fromCheckNo = fromCheckNo;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getToCheckNo() {
        return this.toCheckNo;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setToCheckNo(String toCheckNo) {
        this.toCheckNo = toCheckNo;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getFromAccountNo() {
        return this.fromAccountNo;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getToAccountNo() {
        return this.toAccountNo;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getBrokerId() {
        return this.brokerId;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getTranchCode() {
        return this.tranchCode;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setTranchCode(String tranchCode) {
        this.tranchCode = tranchCode;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getTranchMode() {
        return this.tranchMode;
    }
    
    public String TranchAssignmentSearchCriteriaDto.getAccountNo() {
        return this.accountNo;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    
    public List<Long> TranchAssignmentSearchCriteriaDto.getExcludes() {
        return this.excludes;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setExcludes(List<Long> excludes) {
        this.excludes = excludes;
    }
    
    public List<Long> TranchAssignmentSearchCriteriaDto.getIncludes() {
        return this.includes;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setIncludes(List<Long> includes) {
        this.includes = includes;
    }
    
    public boolean TranchAssignmentSearchCriteriaDto.isExcludeAll() {
        return this.excludeAll;
    }
    
    public void TranchAssignmentSearchCriteriaDto.setExcludeAll(boolean excludeAll) {
        this.excludeAll = excludeAll;
    }
    
}
