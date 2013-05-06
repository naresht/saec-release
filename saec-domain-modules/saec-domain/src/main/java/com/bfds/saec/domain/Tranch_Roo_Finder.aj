// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Tranch;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Tranch_Roo_Finder {
    
    public static TypedQuery<Tranch> Tranch.findTranchesByCode(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Tranch.entityManager();
        TypedQuery<Tranch> q = em.createQuery("SELECT o FROM Tranch AS o WHERE o.code = :code", Tranch.class);
        q.setParameter("code", code);
        return q;
    }
    
}
