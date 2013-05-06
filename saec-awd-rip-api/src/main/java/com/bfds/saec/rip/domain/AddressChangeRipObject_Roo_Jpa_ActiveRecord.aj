// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.rip.domain;

import com.bfds.saec.rip.domain.AddressChangeRipObject;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AddressChangeRipObject_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "entityManagerFactory")
    transient EntityManager AddressChangeRipObject.entityManager;
    
    public static long AddressChangeRipObject.countAddressChangeRipObjects() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AddressChangeRipObject o", Long.class).getSingleResult();
    }
    
    public static List<AddressChangeRipObject> AddressChangeRipObject.findAllAddressChangeRipObjects() {
        return entityManager().createQuery("SELECT o FROM AddressChangeRipObject o", AddressChangeRipObject.class).getResultList();
    }
    
    public static AddressChangeRipObject AddressChangeRipObject.findAddressChangeRipObject(Long id) {
        if (id == null) return null;
        return entityManager().find(AddressChangeRipObject.class, id);
    }
    
    public static List<AddressChangeRipObject> AddressChangeRipObject.findAddressChangeRipObjectEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AddressChangeRipObject o", AddressChangeRipObject.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public AddressChangeRipObject AddressChangeRipObject.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AddressChangeRipObject merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}