package com.bfds.wss.domain.reference;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.saec.domain.Claimant;
import com.bfds.wss.domain.ClaimantReminder;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class ReminderType implements java.io.Serializable {
	private String description;

    /**
     * Should the reminder be sent.
     */
    private Boolean sendReminder;

    /**
     * A +ve or -ve offset that must be added to {@link #dueDateReference} to get the due date of a reminder - {@link com.bfds.wss.domain.ClaimantReminder}.
     *
     */
    private Integer dueDateOffset;

    private Date dueDateReference;

    private BigDecimal threshold;
    
    public static ReminderType findReminderTypeByDescription(String description) {
        if (description == null) throw new IllegalArgumentException("The description argument is required");
        EntityManager em = ReminderType.entityManager();
        TypedQuery<ReminderType> q = em.createQuery("SELECT o FROM ReminderType AS o WHERE o.description = :description", ReminderType.class);
        q.setParameter("description", description);
        return q.getSingleResult();
    }

}
