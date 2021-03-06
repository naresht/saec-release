// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.HoldCode;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect HoldCode_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager HoldCode.entityManager;
    
    public static long HoldCode.countHoldCodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM HoldCode o", Long.class).getSingleResult();
    }
    
    public static List<HoldCode> HoldCode.findAllHoldCodes() {
        return entityManager().createQuery("SELECT o FROM HoldCode o", HoldCode.class).getResultList();
    }
    
    public static HoldCode HoldCode.findHoldCode(Long id) {
        if (id == null) return null;
        return entityManager().find(HoldCode.class, id);
    }
    
    public static List<HoldCode> HoldCode.findHoldCodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM HoldCode o", HoldCode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public HoldCode HoldCode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        HoldCode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
