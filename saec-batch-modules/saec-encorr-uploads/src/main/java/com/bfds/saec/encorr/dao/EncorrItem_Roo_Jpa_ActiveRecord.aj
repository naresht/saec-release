// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.encorr.dao;

import com.bfds.saec.encorr.dao.EncorrItem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EncorrItem_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager EncorrItem.entityManager;
    
    public static long EncorrItem.countEncorrItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EncorrItem o", Long.class).getSingleResult();
    }
    
    public static List<EncorrItem> EncorrItem.findAllEncorrItems() {
        return entityManager().createQuery("SELECT o FROM EncorrItem o", EncorrItem.class).getResultList();
    }
    
    public static EncorrItem EncorrItem.findEncorrItem(Long id) {
        if (id == null) return null;
        return entityManager().find(EncorrItem.class, id);
    }
    
    public static List<EncorrItem> EncorrItem.findEncorrItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EncorrItem o", EncorrItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public EncorrItem EncorrItem.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EncorrItem merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
