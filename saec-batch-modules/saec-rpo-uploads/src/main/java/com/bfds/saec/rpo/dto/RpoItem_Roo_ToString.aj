// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.rpo.dto;

import java.lang.String;

privileged aspect RpoItem_Roo_ToString {
    
    public String RpoItem.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BusinessArea: ").append(getBusinessArea()).append(", ");
        sb.append("CheckAmount: ").append(getCheckAmount()).append(", ");
        sb.append("CheckNo: ").append(getCheckNo()).append(", ");
        sb.append("MailControlNo: ").append(getMailControlNo()).append(", ");
        sb.append("ReferenceNo: ").append(getReferenceNo()).append(", ");
        sb.append("Ssn: ").append(getSsn()).append(", ");
        sb.append("WorkType: ").append(getWorkType());
        return sb.toString();
    }
    
}