// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.PaymentState;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import java.util.Date;

privileged aspect PaymentState_Roo_JavaBean {
    
    public PaymentCode PaymentState.getPaymentCode() {
        return this.paymentCode;
    }
    
    public PaymentStatus PaymentState.getPaymentStatus() {
        return this.paymentStatus;
    }
    
    public Date PaymentState.getStatusChangeDate() {
        return this.statusChangeDate;
    }
    
}
