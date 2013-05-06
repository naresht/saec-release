// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.dto;

import com.bfds.saec.domain.ZipCode;
import com.bfds.saec.domain.dto.TranchAssignmentSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentType;
import java.util.Date;

privileged aspect TranchAssignmentSearchRecordDto_Roo_JavaBean {
    
    public Long TranchAssignmentSearchRecordDto.getId() {
        return this.id;
    }
    
    public void TranchAssignmentSearchRecordDto.setId(Long id) {
        this.id = id;
    }
    
    public String TranchAssignmentSearchRecordDto.getCheckNo() {
        return this.checkNo;
    }
    
    public void TranchAssignmentSearchRecordDto.setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }
    
    public Long TranchAssignmentSearchRecordDto.getClaimantId() {
        return this.claimantId;
    }
    
    public void TranchAssignmentSearchRecordDto.setClaimantId(Long claimantId) {
        this.claimantId = claimantId;
    }
    
    public Date TranchAssignmentSearchRecordDto.getRequestedDate() {
        return this.requestedDate;
    }
    
    public void TranchAssignmentSearchRecordDto.setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }
    
    public String TranchAssignmentSearchRecordDto.getReferenceNo() {
        return this.referenceNo;
    }
    
    public void TranchAssignmentSearchRecordDto.setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    
    public String TranchAssignmentSearchRecordDto.getName() {
        return this.name;
    }
    
    public void TranchAssignmentSearchRecordDto.setName(String name) {
        this.name = name;
    }
    
    public String TranchAssignmentSearchRecordDto.getAddress1() {
        return this.address1;
    }
    
    public void TranchAssignmentSearchRecordDto.setAddress1(String address1) {
        this.address1 = address1;
    }
    
    public String TranchAssignmentSearchRecordDto.getAddress2() {
        return this.address2;
    }
    
    public void TranchAssignmentSearchRecordDto.setAddress2(String address2) {
        this.address2 = address2;
    }
    
    public String TranchAssignmentSearchRecordDto.getCity() {
        return this.city;
    }
    
    public void TranchAssignmentSearchRecordDto.setCity(String city) {
        this.city = city;
    }
    
    public String TranchAssignmentSearchRecordDto.getState() {
        return this.state;
    }
    
    public void TranchAssignmentSearchRecordDto.setState(String state) {
        this.state = state;
    }
    
    public ZipCode TranchAssignmentSearchRecordDto.getZipCode() {
        return this.zipCode;
    }
    
    public void TranchAssignmentSearchRecordDto.setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }
    
    public double TranchAssignmentSearchRecordDto.getPaymentAmount() {
        return this.paymentAmount;
    }
    
    public void TranchAssignmentSearchRecordDto.setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    public boolean TranchAssignmentSearchRecordDto.isInclude() {
        return this.include;
    }
    
    public void TranchAssignmentSearchRecordDto.setInclude(boolean include) {
        this.include = include;
    }
    
    public PaymentType TranchAssignmentSearchRecordDto.getPaymentType() {
        return this.paymentType;
    }
    
    public void TranchAssignmentSearchRecordDto.setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    
}