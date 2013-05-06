// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimFileLob;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ClaimFileLob_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager ClaimFileLob.entityManager;
    
    public static final EntityManager ClaimFileLob.entityManager() {
        EntityManager em = new ClaimFileLob().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long ClaimFileLob.countClaimFileLobs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ClaimFileLob o", Long.class).getSingleResult();
    }
    
    public static List<ClaimFileLob> ClaimFileLob.findAllClaimFileLobs() {
        return entityManager().createQuery("SELECT o FROM ClaimFileLob o", ClaimFileLob.class).getResultList();
    }
    
    public static ClaimFileLob ClaimFileLob.findClaimFileLob(Long id) {
        if (id == null) return null;
        return entityManager().find(ClaimFileLob.class, id);
    }
    
    public static List<ClaimFileLob> ClaimFileLob.findClaimFileLobEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ClaimFileLob o", ClaimFileLob.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void ClaimFileLob.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void ClaimFileLob.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ClaimFileLob attached = ClaimFileLob.findClaimFileLob(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void ClaimFileLob.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void ClaimFileLob.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public ClaimFileLob ClaimFileLob.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ClaimFileLob merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
