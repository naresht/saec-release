// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.LetterCode;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LetterCode_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager LetterCode.entityManager;
    
    public static final EntityManager LetterCode.entityManager() {
        EntityManager em = new LetterCode().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long LetterCode.countLetterCodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LetterCode o", Long.class).getSingleResult();
    }
    
    public static List<LetterCode> LetterCode.findAllLetterCodes() {
        return entityManager().createQuery("SELECT o FROM LetterCode o", LetterCode.class).getResultList();
    }
    
    public static LetterCode LetterCode.findLetterCode(Long id) {
        if (id == null) return null;
        return entityManager().find(LetterCode.class, id);
    }
    
    public static List<LetterCode> LetterCode.findLetterCodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LetterCode o", LetterCode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void LetterCode.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void LetterCode.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LetterCode attached = LetterCode.findLetterCode(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void LetterCode.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void LetterCode.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public LetterCode LetterCode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LetterCode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
