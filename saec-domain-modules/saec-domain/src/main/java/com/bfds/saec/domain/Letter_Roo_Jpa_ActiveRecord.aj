// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Letter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Letter_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager Letter.entityManager;
    
    public static final EntityManager Letter.entityManager() {
        EntityManager em = new Letter().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Letter.countLetters() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Letter o", Long.class).getSingleResult();
    }
    
    public static List<Letter> Letter.findAllLetters() {
        return entityManager().createQuery("SELECT o FROM Letter o", Letter.class).getResultList();
    }
    
    public static Letter Letter.findLetter(Long id) {
        if (id == null) return null;
        return entityManager().find(Letter.class, id);
    }
    
    public static List<Letter> Letter.findLetterEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Letter o", Letter.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Letter.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Letter.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Letter attached = Letter.findLetter(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Letter.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Letter.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Letter Letter.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Letter merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}