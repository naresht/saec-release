// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

privileged aspect CheckActivity_Roo_JavaBean {
    
    public String CheckActivity.getIdentificationNo() {
        return this.identificationNo;
    }
    
    public void CheckActivity.setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }
    
    public PaymentType CheckActivity.getFromPaymentType() {
        return this.fromPaymentType;
    }
    
    public void CheckActivity.setFromPaymentType(PaymentType fromPaymentType) {
        this.fromPaymentType = fromPaymentType;
    }
    
    public PaymentType CheckActivity.getToPaymentType() {
        return this.toPaymentType;
    }
    
    public void CheckActivity.setToPaymentType(PaymentType toPaymentType) {
        this.toPaymentType = toPaymentType;
    }
    
    public PaymentCode CheckActivity.getFromPaymentCode() {
        return this.fromPaymentCode;
    }
    
    public void CheckActivity.setFromPaymentCode(PaymentCode fromPaymentCode) {
        this.fromPaymentCode = fromPaymentCode;
    }
    
    public PaymentCode CheckActivity.getToPaymentCode() {
        return this.toPaymentCode;
    }
    
    public void CheckActivity.setToPaymentCode(PaymentCode toPaymentCode) {
        this.toPaymentCode = toPaymentCode;
    }
    
    public String CheckActivity.getComments() {
        return this.comments;
    }
    
    public void CheckActivity.setComments(String comments) {
        this.comments = comments;
    }
    
    public Payment CheckActivity.getPayment() {
        return this.payment;
    }
    
    public void CheckActivity.setPayment(Payment payment) {
        this.payment = payment;
    }
    
}