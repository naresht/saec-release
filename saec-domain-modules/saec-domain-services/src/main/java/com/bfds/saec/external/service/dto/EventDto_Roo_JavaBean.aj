// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.external.service.dto;

import com.bfds.saec.external.service.dto.EventDto;
import java.util.Date;

privileged aspect EventDto_Roo_JavaBean {
    
    public String EventDto.getCode() {
        return this.code;
    }
    
    public void EventDto.setCode(String code) {
        this.code = code;
    }
    
    public Date EventDto.getTargetEndDate() {
        return this.targetEndDate;
    }
    
    public void EventDto.setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }
    
    public Boolean EventDto.getClosed() {
        return this.closed;
    }
    
    public void EventDto.setClosed(Boolean closed) {
        this.closed = closed;
    }
    
}
