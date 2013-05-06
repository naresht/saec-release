// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.activity.Activity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Activity_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager Activity.entityManager;
    
    public static final EntityManager Activity.entityManager() {
        EntityManager em = new Activity().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Activity.countActivitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Activity o", Long.class).getSingleResult();
    }
    
    public static List<Activity> Activity.findAllActivitys() {
        return entityManager().createQuery("SELECT o FROM Activity o", Activity.class).getResultList();
    }
    
    public static Activity Activity.findActivity(Long id) {
        if (id == null) return null;
        return entityManager().find(Activity.class, id);
    }
    
    public static List<Activity> Activity.findActivityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Activity o", Activity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Activity.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Activity.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Activity attached = Activity.findActivity(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Activity.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Activity.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Activity Activity.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Activity merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
