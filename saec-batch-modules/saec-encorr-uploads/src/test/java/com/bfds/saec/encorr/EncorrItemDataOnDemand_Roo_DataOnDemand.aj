// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.encorr;

import com.bfds.saec.batch.domain.InRecordStatus;
import com.bfds.saec.encorr.EncorrItemDataOnDemand;
import com.bfds.saec.encorr.dao.EncorrItem;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect EncorrItemDataOnDemand_Roo_DataOnDemand {
    
    declare @type: EncorrItemDataOnDemand: @Component;
    
    private Random EncorrItemDataOnDemand.rnd = new SecureRandom();
    
    private List<EncorrItem> EncorrItemDataOnDemand.data;
    
    public EncorrItem EncorrItemDataOnDemand.getNewTransientEncorrItem(int index) {
        EncorrItem obj = new EncorrItem();
        setBusinessArea(obj, index);
        setJobExecutionId(obj, index);
        setMailControlNo(obj, index);
        setReferenceNo(obj, index);
        setSsn(obj, index);
        setStatus(obj, index);
        setWorkType(obj, index);
        return obj;
    }
    
    public void EncorrItemDataOnDemand.setBusinessArea(EncorrItem obj, int index) {
        String businessArea = "businessArea_" + index;
        obj.setBusinessArea(businessArea);
    }
    
    public void EncorrItemDataOnDemand.setJobExecutionId(EncorrItem obj, int index) {
        Long jobExecutionId = new Integer(index).longValue();
        obj.setJobExecutionId(jobExecutionId);
    }
    
    public void EncorrItemDataOnDemand.setMailControlNo(EncorrItem obj, int index) {
        String mailControlNo = "mailControlNo_" + index;
        obj.setMailControlNo(mailControlNo);
    }
    
    public void EncorrItemDataOnDemand.setReferenceNo(EncorrItem obj, int index) {
        String referenceNo = "referenceNo_" + index;
        obj.setReferenceNo(referenceNo);
    }
    
    public void EncorrItemDataOnDemand.setSsn(EncorrItem obj, int index) {
        String ssn = "ssn_" + index;
        obj.setSsn(ssn);
    }
    
    public void EncorrItemDataOnDemand.setStatus(EncorrItem obj, int index) {
        InRecordStatus status = null;
        obj.setStatus(status);
    }
    
    public void EncorrItemDataOnDemand.setWorkType(EncorrItem obj, int index) {
        String workType = "workType_" + index;
        obj.setWorkType(workType);
    }
    
    public EncorrItem EncorrItemDataOnDemand.getSpecificEncorrItem(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        EncorrItem obj = data.get(index);
        Long id = obj.getId();
        return EncorrItem.findEncorrItem(id);
    }
    
    public EncorrItem EncorrItemDataOnDemand.getRandomEncorrItem() {
        init();
        EncorrItem obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return EncorrItem.findEncorrItem(id);
    }
    
    public boolean EncorrItemDataOnDemand.modifyEncorrItem(EncorrItem obj) {
        return false;
    }
    
    public void EncorrItemDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = EncorrItem.findEncorrItemEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'EncorrItem' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<EncorrItem>();
        for (int i = 0; i < 10; i++) {
            EncorrItem obj = getNewTransientEncorrItem(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}