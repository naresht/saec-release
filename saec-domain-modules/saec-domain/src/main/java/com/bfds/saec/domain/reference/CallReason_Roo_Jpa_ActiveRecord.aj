// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.reference;

import com.bfds.saec.domain.reference.CallReason;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CallReason_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager CallReason.entityManager;
    
    public static final EntityManager CallReason.entityManager() {
        EntityManager em = new CallReason().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long CallReason.countCallReasons() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CallReason o", Long.class).getSingleResult();
    }
    
    public static List<CallReason> CallReason.findAllCallReasons() {
        return entityManager().createQuery("SELECT o FROM CallReason o", CallReason.class).getResultList();
    }
    
    public static CallReason CallReason.findCallReason(Long id) {
        if (id == null) return null;
        return entityManager().find(CallReason.class, id);
    }
    
    public static List<CallReason> CallReason.findCallReasonEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CallReason o", CallReason.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void CallReason.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void CallReason.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            CallReason attached = CallReason.findCallReason(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void CallReason.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void CallReason.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public CallReason CallReason.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CallReason merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
