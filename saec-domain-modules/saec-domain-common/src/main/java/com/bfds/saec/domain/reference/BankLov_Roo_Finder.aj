// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain.reference;

import com.bfds.saec.domain.reference.BankLov;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect BankLov_Roo_Finder {
    
    public static TypedQuery<BankLov> BankLov.findBankLovsByCode(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = BankLov.entityManager();
        TypedQuery<BankLov> q = em.createQuery("SELECT o FROM BankLov AS o WHERE o.code = :code", BankLov.class);
        q.setParameter("code", code);
        return q;
    }
    
}
