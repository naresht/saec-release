// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain.reference;

import com.bfds.wss.domain.reference.SecurityFund;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SecurityFund_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager SecurityFund.entityManager;
    
    public static final EntityManager SecurityFund.entityManager() {
        EntityManager em = new SecurityFund().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long SecurityFund.countSecurityFunds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SecurityFund o", Long.class).getSingleResult();
    }
    
    public static List<SecurityFund> SecurityFund.findAllSecurityFunds() {
        return entityManager().createQuery("SELECT o FROM SecurityFund o", SecurityFund.class).getResultList();
    }
    
    public static SecurityFund SecurityFund.findSecurityFund(Long id) {
        if (id == null) return null;
        return entityManager().find(SecurityFund.class, id);
    }
    
    public static List<SecurityFund> SecurityFund.findSecurityFundEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SecurityFund o", SecurityFund.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void SecurityFund.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void SecurityFund.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SecurityFund attached = SecurityFund.findSecurityFund(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void SecurityFund.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void SecurityFund.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public SecurityFund SecurityFund.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SecurityFund merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}