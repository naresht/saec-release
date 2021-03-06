// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain.reference;

import com.bfds.wss.domain.reference.QuestionGroup;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect QuestionGroup_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager QuestionGroup.entityManager;
    
    public static final EntityManager QuestionGroup.entityManager() {
        EntityManager em = new QuestionGroup().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long QuestionGroup.countQuestionGroups() {
        return entityManager().createQuery("SELECT COUNT(o) FROM QuestionGroup o", Long.class).getSingleResult();
    }
    
    public static List<QuestionGroup> QuestionGroup.findAllQuestionGroups() {
        return entityManager().createQuery("SELECT o FROM QuestionGroup o", QuestionGroup.class).getResultList();
    }
    
    public static QuestionGroup QuestionGroup.findQuestionGroup(Long id) {
        if (id == null) return null;
        return entityManager().find(QuestionGroup.class, id);
    }
    
    public static List<QuestionGroup> QuestionGroup.findQuestionGroupEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM QuestionGroup o", QuestionGroup.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void QuestionGroup.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void QuestionGroup.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            QuestionGroup attached = QuestionGroup.findQuestionGroup(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void QuestionGroup.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void QuestionGroup.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public QuestionGroup QuestionGroup.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        QuestionGroup merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
