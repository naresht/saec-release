// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.ClaimantCalculationAttributes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ClaimantCalculationAttributes_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager ClaimantCalculationAttributes.entityManager;
    
    public static final EntityManager ClaimantCalculationAttributes.entityManager() {
        EntityManager em = new ClaimantCalculationAttributes().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long ClaimantCalculationAttributes.countClaimantCalculationAttributeses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ClaimantCalculationAttributes o", Long.class).getSingleResult();
    }
    
    public static List<ClaimantCalculationAttributes> ClaimantCalculationAttributes.findAllClaimantCalculationAttributeses() {
        return entityManager().createQuery("SELECT o FROM ClaimantCalculationAttributes o", ClaimantCalculationAttributes.class).getResultList();
    }
    
    public static ClaimantCalculationAttributes ClaimantCalculationAttributes.findClaimantCalculationAttributes(Long id) {
        if (id == null) return null;
        return entityManager().find(ClaimantCalculationAttributes.class, id);
    }
    
    public static List<ClaimantCalculationAttributes> ClaimantCalculationAttributes.findClaimantCalculationAttributesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ClaimantCalculationAttributes o", ClaimantCalculationAttributes.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void ClaimantCalculationAttributes.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void ClaimantCalculationAttributes.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ClaimantCalculationAttributes attached = ClaimantCalculationAttributes.findClaimantCalculationAttributes(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void ClaimantCalculationAttributes.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void ClaimantCalculationAttributes.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public ClaimantCalculationAttributes ClaimantCalculationAttributes.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ClaimantCalculationAttributes merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}