// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.RequestType;
import com.bfds.saec.domain.RequestTypeDataOnDemand;
import com.bfds.saec.domain.reference.MailType;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect RequestTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: RequestTypeDataOnDemand: @Component;
    
    private Random RequestTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<RequestType> RequestTypeDataOnDemand.data;
    
    public RequestType RequestTypeDataOnDemand.getNewTransientRequestType(int index) {
        RequestType obj = new RequestType();
        setMailType(obj, index);
        setName(obj, index);
        return obj;
    }
    
    public void RequestTypeDataOnDemand.setMailType(RequestType obj, int index) {
        MailType mailType = null;
        obj.setMailType(mailType);
    }
    
    public void RequestTypeDataOnDemand.setName(RequestType obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public RequestType RequestTypeDataOnDemand.getSpecificRequestType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        RequestType obj = data.get(index);
        Long id = obj.getId();
        return RequestType.findRequestType(id);
    }
    
    public RequestType RequestTypeDataOnDemand.getRandomRequestType() {
        init();
        RequestType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return RequestType.findRequestType(id);
    }
    
    public boolean RequestTypeDataOnDemand.modifyRequestType(RequestType obj) {
        return false;
    }
    
    public void RequestTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = RequestType.findRequestTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'RequestType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<RequestType>();
        for (int i = 0; i < 10; i++) {
            RequestType obj = getNewTransientRequestType(i);
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
