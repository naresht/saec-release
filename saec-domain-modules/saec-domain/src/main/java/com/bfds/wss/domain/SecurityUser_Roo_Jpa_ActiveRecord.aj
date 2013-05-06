// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.SecurityUser;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SecurityUser_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager SecurityUser.entityManager;
    
    public static final EntityManager SecurityUser.entityManager() {
        EntityManager em = new SecurityUser().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long SecurityUser.countSecurityUsers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SecurityUser o", Long.class).getSingleResult();
    }
    
    public static List<SecurityUser> SecurityUser.findAllSecurityUsers() {
        return entityManager().createQuery("SELECT o FROM SecurityUser o", SecurityUser.class).getResultList();
    }
    
    public static SecurityUser SecurityUser.findSecurityUser(Long id) {
        if (id == null) return null;
        return entityManager().find(SecurityUser.class, id);
    }
    
    public static List<SecurityUser> SecurityUser.findSecurityUserEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SecurityUser o", SecurityUser.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void SecurityUser.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void SecurityUser.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SecurityUser attached = SecurityUser.findSecurityUser(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void SecurityUser.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void SecurityUser.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public SecurityUser SecurityUser.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SecurityUser merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}