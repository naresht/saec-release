// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.activity.PhoneCall;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect PhoneCall_Roo_Jpa_ActiveRecord {
    
    public static long PhoneCall.countPhoneCalls() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PhoneCall o", Long.class).getSingleResult();
    }
    
    public static List<PhoneCall> PhoneCall.findAllPhoneCalls() {
        return entityManager().createQuery("SELECT o FROM PhoneCall o", PhoneCall.class).getResultList();
    }
    
    public static PhoneCall PhoneCall.findPhoneCall(Long id) {
        if (id == null) return null;
        return entityManager().find(PhoneCall.class, id);
    }
    
    public static List<PhoneCall> PhoneCall.findPhoneCallEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PhoneCall o", PhoneCall.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public PhoneCall PhoneCall.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PhoneCall merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
