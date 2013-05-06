// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.AddressResearchReturn;
import com.bfds.saec.domain.Claimant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect AddressResearchReturn_Roo_Finder {
    
    public static TypedQuery<AddressResearchReturn> AddressResearchReturn.findAddressResearchReturnsByClaimant(Claimant claimant) {
        if (claimant == null) throw new IllegalArgumentException("The claimant argument is required");
        EntityManager em = AddressResearchReturn.entityManager();
        TypedQuery<AddressResearchReturn> q = em.createQuery("SELECT o FROM AddressResearchReturn AS o WHERE o.claimant = :claimant", AddressResearchReturn.class);
        q.setParameter("claimant", claimant);
        return q;
    }
    
}
