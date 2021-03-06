// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.EventPaymentConfig;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EventPaymentConfig_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager EventPaymentConfig.entityManager;
    
    public static final EntityManager EventPaymentConfig.entityManager() {
        EntityManager em = new EventPaymentConfig().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EventPaymentConfig.countEventPaymentConfigs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EventPaymentConfig o", Long.class).getSingleResult();
    }
    
    public static List<EventPaymentConfig> EventPaymentConfig.findAllEventPaymentConfigs() {
        return entityManager().createQuery("SELECT o FROM EventPaymentConfig o", EventPaymentConfig.class).getResultList();
    }
    
    public static EventPaymentConfig EventPaymentConfig.findEventPaymentConfig(Long id) {
        if (id == null) return null;
        return entityManager().find(EventPaymentConfig.class, id);
    }
    
    public static List<EventPaymentConfig> EventPaymentConfig.findEventPaymentConfigEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EventPaymentConfig o", EventPaymentConfig.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EventPaymentConfig.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EventPaymentConfig.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EventPaymentConfig attached = EventPaymentConfig.findEventPaymentConfig(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EventPaymentConfig.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EventPaymentConfig.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EventPaymentConfig EventPaymentConfig.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EventPaymentConfig merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
