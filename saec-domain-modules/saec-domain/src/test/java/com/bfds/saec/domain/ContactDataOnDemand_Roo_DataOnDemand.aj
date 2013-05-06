// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Contact;
import com.bfds.saec.domain.ContactDataOnDemand;
import com.bfds.saec.domain.Name;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect ContactDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ContactDataOnDemand: @Component;
    
    private Random ContactDataOnDemand.rnd = new SecureRandom();
    
    private List<Contact> ContactDataOnDemand.data;
    
    public Contact ContactDataOnDemand.getNewTransientContact(int index) {
        Contact obj = new Contact();
        setCellPhoneNo(obj, index);
        setClaimant(obj, index);
        setComments(obj, index);
        setEmail(obj, index);
        setName(obj, index);
        setPhoneNo(obj, index);
        setPrimaryContactOf(obj, index);
        setWorkPhoneNo(obj, index);
        return obj;
    }
    
    public void ContactDataOnDemand.setCellPhoneNo(Contact obj, int index) {
        String cellPhoneNo = "cellPhoneNo_" + index;
        obj.setCellPhoneNo(cellPhoneNo);
    }
    
    public void ContactDataOnDemand.setClaimant(Contact obj, int index) {
        Claimant claimant = null;
        obj.setClaimant(claimant);
    }
    
    public void ContactDataOnDemand.setComments(Contact obj, int index) {
        String comments = "comments_" + index;
        obj.setComments(comments);
    }
    
    public void ContactDataOnDemand.setEmail(Contact obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }
    
    public void ContactDataOnDemand.setName(Contact obj, int index) {
        Name name = null;
        obj.setName(name);
    }
    
    public void ContactDataOnDemand.setPhoneNo(Contact obj, int index) {
        String phoneNo = "phoneNo_" + index;
        obj.setPhoneNo(phoneNo);
    }
    
    public void ContactDataOnDemand.setPrimaryContactOf(Contact obj, int index) {
        Claimant primaryContactOf = null;
        obj.setPrimaryContactOf(primaryContactOf);
    }
    
    public void ContactDataOnDemand.setWorkPhoneNo(Contact obj, int index) {
        String workPhoneNo = "workPhoneNo_" + index;
        obj.setWorkPhoneNo(workPhoneNo);
    }
    
    public Contact ContactDataOnDemand.getSpecificContact(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Contact obj = data.get(index);
        Long id = obj.getId();
        return Contact.findContact(id);
    }
    
    public Contact ContactDataOnDemand.getRandomContact() {
        init();
        Contact obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Contact.findContact(id);
    }
    
    public boolean ContactDataOnDemand.modifyContact(Contact obj) {
        return false;
    }
    
    public void ContactDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Contact.findContactEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Contact' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Contact>();
        for (int i = 0; i < 10; i++) {
            Contact obj = getNewTransientContact(i);
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
