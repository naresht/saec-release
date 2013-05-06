// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.activity.PhoneCallDataOnDemand;
import com.bfds.saec.domain.reference.CallType;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect PhoneCallDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PhoneCallDataOnDemand: @Component;
    
    private Random PhoneCallDataOnDemand.rnd = new SecureRandom();
    
    private List<PhoneCall> PhoneCallDataOnDemand.data;
    
    public PhoneCall PhoneCallDataOnDemand.getNewTransientPhoneCall(int index) {
        PhoneCall obj = new PhoneCall();
        setActivityCode(obj, index);
        setActivityDate(obj, index);
        setActivityTypeDescription(obj, index);
        setCallType(obj, index);
        setClaimant(obj, index);
        setComments(obj, index);
        setDescription(obj, index);
        setPhoneExtension(obj, index);
        setShapshot(obj, index);
        setUserId(obj, index);
        return obj;
    }
    
    public void PhoneCallDataOnDemand.setActivityCode(PhoneCall obj, int index) {
        ActivityCode activityCode = null;
        obj.setActivityCode(activityCode);
    }
    
    public void PhoneCallDataOnDemand.setActivityDate(PhoneCall obj, int index) {
        Date activityDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setActivityDate(activityDate);
    }
    
    public void PhoneCallDataOnDemand.setActivityTypeDescription(PhoneCall obj, int index) {
        String activityTypeDescription = "activityTypeDescription_" + index;
        obj.setActivityTypeDescription(activityTypeDescription);
    }
    
    public void PhoneCallDataOnDemand.setCallType(PhoneCall obj, int index) {
        CallType callType = CallType.class.getEnumConstants()[0];
        obj.setCallType(callType);
    }
    
    public void PhoneCallDataOnDemand.setClaimant(PhoneCall obj, int index) {
        Claimant claimant = null;
        obj.setClaimant(claimant);
    }
    
    public void PhoneCallDataOnDemand.setComments(PhoneCall obj, int index) {
        String comments = "comments_" + index;
        if (comments.length() > 1024) {
            comments = comments.substring(0, 1024);
        }
        obj.setComments(comments);
    }
    
    public void PhoneCallDataOnDemand.setDescription(PhoneCall obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void PhoneCallDataOnDemand.setPhoneExtension(PhoneCall obj, int index) {
        String phoneExtension = "phoneExtension_" + index;
        obj.setPhoneExtension(phoneExtension);
    }
    
    public void PhoneCallDataOnDemand.setShapshot(PhoneCall obj, int index) {
        Boolean shapshot = true;
        obj.setShapshot(shapshot);
    }
    
    public void PhoneCallDataOnDemand.setUserId(PhoneCall obj, int index) {
        String userId = "userId_" + index;
        obj.setUserId(userId);
    }
    
    public PhoneCall PhoneCallDataOnDemand.getSpecificPhoneCall(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        PhoneCall obj = data.get(index);
        Long id = obj.getId();
        return PhoneCall.findPhoneCall(id);
    }
    
    public PhoneCall PhoneCallDataOnDemand.getRandomPhoneCall() {
        init();
        PhoneCall obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return PhoneCall.findPhoneCall(id);
    }
    
    public boolean PhoneCallDataOnDemand.modifyPhoneCall(PhoneCall obj) {
        return false;
    }
    
    public void PhoneCallDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = PhoneCall.findPhoneCallEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'PhoneCall' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<PhoneCall>();
        for (int i = 0; i < 10; i++) {
            PhoneCall obj = getNewTransientPhoneCall(i);
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