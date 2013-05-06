// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.activity.CheckActivity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CheckActivity_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager CheckActivity.entityManager;
    
    public static long CheckActivity.countCheckActivitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CheckActivity o", Long.class).getSingleResult();
    }
    
    public static List<CheckActivity> CheckActivity.findAllCheckActivitys() {
        return entityManager().createQuery("SELECT o FROM CheckActivity o", CheckActivity.class).getResultList();
    }
    
    public static CheckActivity CheckActivity.findCheckActivity(Long id) {
        if (id == null) return null;
        return entityManager().find(CheckActivity.class, id);
    }
    
    public static List<CheckActivity> CheckActivity.findCheckActivityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CheckActivity o", CheckActivity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public CheckActivity CheckActivity.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CheckActivity merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
