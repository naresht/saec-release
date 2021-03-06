// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.reference;

import com.bfds.saec.domain.reference.CallCode;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CallCode_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager CallCode.entityManager;
    
    public static final EntityManager CallCode.entityManager() {
        EntityManager em = new CallCode().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long CallCode.countCallCodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CallCode o", Long.class).getSingleResult();
    }
    
    public static List<CallCode> CallCode.findAllCallCodes() {
        return entityManager().createQuery("SELECT o FROM CallCode o", CallCode.class).getResultList();
    }
    
    public static CallCode CallCode.findCallCode(Long id) {
        if (id == null) return null;
        return entityManager().find(CallCode.class, id);
    }
    
    public static List<CallCode> CallCode.findCallCodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CallCode o", CallCode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void CallCode.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void CallCode.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            CallCode attached = CallCode.findCallCode(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void CallCode.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void CallCode.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public CallCode CallCode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CallCode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
