// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.wss.domain;

import com.bfds.wss.domain.ClaimantUserResponses;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ClaimantUserResponses_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager ClaimantUserResponses.entityManager;
    
    public static long ClaimantUserResponses.countClaimantUserResponseses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ClaimantUserResponses o", Long.class).getSingleResult();
    }
    
    public static List<ClaimantUserResponses> ClaimantUserResponses.findAllClaimantUserResponseses() {
        return entityManager().createQuery("SELECT o FROM ClaimantUserResponses o", ClaimantUserResponses.class).getResultList();
    }
    
    public static ClaimantUserResponses ClaimantUserResponses.findClaimantUserResponses(Long id) {
        if (id == null) return null;
        return entityManager().find(ClaimantUserResponses.class, id);
    }
    
    public static List<ClaimantUserResponses> ClaimantUserResponses.findClaimantUserResponsesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ClaimantUserResponses o", ClaimantUserResponses.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ClaimantUserResponses ClaimantUserResponses.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ClaimantUserResponses merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}