// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.CorrespondenceDocument;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CorrespondenceDocument_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager CorrespondenceDocument.entityManager;
    
    public static final EntityManager CorrespondenceDocument.entityManager() {
        EntityManager em = new CorrespondenceDocument().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long CorrespondenceDocument.countCorrespondenceDocuments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CorrespondenceDocument o", Long.class).getSingleResult();
    }
    
    public static List<CorrespondenceDocument> CorrespondenceDocument.findAllCorrespondenceDocuments() {
        return entityManager().createQuery("SELECT o FROM CorrespondenceDocument o", CorrespondenceDocument.class).getResultList();
    }
    
    public static CorrespondenceDocument CorrespondenceDocument.findCorrespondenceDocument(Long id) {
        if (id == null) return null;
        return entityManager().find(CorrespondenceDocument.class, id);
    }
    
    public static List<CorrespondenceDocument> CorrespondenceDocument.findCorrespondenceDocumentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CorrespondenceDocument o", CorrespondenceDocument.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void CorrespondenceDocument.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void CorrespondenceDocument.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            CorrespondenceDocument attached = CorrespondenceDocument.findCorrespondenceDocument(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void CorrespondenceDocument.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void CorrespondenceDocument.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public CorrespondenceDocument CorrespondenceDocument.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CorrespondenceDocument merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}