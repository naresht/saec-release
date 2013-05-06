// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.rip.domain;

import com.bfds.saec.rip.domain.RipObject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect RipObject_Roo_Finder {
    
    public static TypedQuery<RipObject> RipObject.findRipObjectsByCorrelationId(Long correlationId) {
        if (correlationId == null) throw new IllegalArgumentException("The correlationId argument is required");
        EntityManager em = RipObject.entityManager();
        TypedQuery<RipObject> q = em.createQuery("SELECT o FROM RipObject AS o WHERE o.correlationId = :correlationId", RipObject.class);
        q.setParameter("correlationId", correlationId);
        return q;
    }
    
}
