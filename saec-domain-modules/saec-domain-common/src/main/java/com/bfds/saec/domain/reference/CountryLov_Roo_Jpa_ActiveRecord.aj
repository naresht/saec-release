// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.reference;

import com.bfds.saec.domain.reference.CountryLov;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CountryLov_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager CountryLov.entityManager;
    
    public static long CountryLov.countCountryLovs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CountryLov o", Long.class).getSingleResult();
    }
    
    public static List<CountryLov> CountryLov.findAllCountryLovs() {
        return entityManager().createQuery("SELECT o FROM CountryLov o", CountryLov.class).getResultList();
    }
    
    public static CountryLov CountryLov.findCountryLov(Long id) {
        if (id == null) return null;
        return entityManager().find(CountryLov.class, id);
    }
    
    public static List<CountryLov> CountryLov.findCountryLovEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CountryLov o", CountryLov.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public CountryLov CountryLov.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CountryLov merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
