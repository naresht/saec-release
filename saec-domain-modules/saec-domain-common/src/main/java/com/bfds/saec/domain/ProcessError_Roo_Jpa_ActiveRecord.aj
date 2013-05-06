// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.ProcessError;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ProcessError_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager ProcessError.entityManager;
    
    public static final EntityManager ProcessError.entityManager() {
        EntityManager em = new ProcessError().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long ProcessError.countProcessErrors() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProcessError o", Long.class).getSingleResult();
    }
    
    public static List<ProcessError> ProcessError.findAllProcessErrors() {
        return entityManager().createQuery("SELECT o FROM ProcessError o", ProcessError.class).getResultList();
    }
    
    public static ProcessError ProcessError.findProcessError(Long id) {
        if (id == null) return null;
        return entityManager().find(ProcessError.class, id);
    }
    
    public static List<ProcessError> ProcessError.findProcessErrorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProcessError o", ProcessError.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void ProcessError.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void ProcessError.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ProcessError attached = ProcessError.findProcessError(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void ProcessError.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void ProcessError.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public ProcessError ProcessError.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ProcessError merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}