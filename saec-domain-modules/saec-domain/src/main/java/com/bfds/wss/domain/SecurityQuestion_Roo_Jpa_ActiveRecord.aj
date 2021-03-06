// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.SecurityQuestion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SecurityQuestion_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager SecurityQuestion.entityManager;
    
    public static final EntityManager SecurityQuestion.entityManager() {
        EntityManager em = new SecurityQuestion().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long SecurityQuestion.countSecurityQuestions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SecurityQuestion o", Long.class).getSingleResult();
    }
    
    public static List<SecurityQuestion> SecurityQuestion.findAllSecurityQuestions() {
        return entityManager().createQuery("SELECT o FROM SecurityQuestion o", SecurityQuestion.class).getResultList();
    }
    
    public static SecurityQuestion SecurityQuestion.findSecurityQuestion(Long id) {
        if (id == null) return null;
        return entityManager().find(SecurityQuestion.class, id);
    }
    
    public static List<SecurityQuestion> SecurityQuestion.findSecurityQuestionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SecurityQuestion o", SecurityQuestion.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void SecurityQuestion.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void SecurityQuestion.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SecurityQuestion attached = SecurityQuestion.findSecurityQuestion(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void SecurityQuestion.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void SecurityQuestion.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public SecurityQuestion SecurityQuestion.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SecurityQuestion merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
