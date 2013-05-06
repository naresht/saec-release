package com.bfds.wss.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.rip.domain.FollowupRemindersRipObject;
import com.bfds.saec.rip.service.RipEventListener;
import com.bfds.wss.domain.reference.ReminderType;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory",finders = {"findClaimantRemindersByClaimant"})
public class ClaimantReminder implements java.io.Serializable {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claim_entry_fk", nullable=true)
	private ClaimEntry claimEntry; 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimant_claim_fk", nullable = true)
	private ClaimantClaim claimantClaim;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimant_fk", nullable = true)
	private Claimant claimant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_type", nullable = true)
    private ReminderType reminderType;

    
    @Autowired
    private transient RipEventListener ripEventListener;
    /**
     * When the reminder does not have a {@link ReminderType} a textual description of the reminder
     * is stored in this filed.
     */
	@Column
	private String reminderDescription;

    /**
     * The calculated (or edited) date that the reminder is scheduled to be sent on.
     * See {@link ReminderType} on how to calculate this field.
     */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date reminderDueDate ;

    /**
     * The date the Reminder was either sent or resolved (will be blank for ‘pending’ items).
     */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
    private Date actionDate ;
	
	@Column(nullable = false)
    @Enumerated(EnumType.STRING)
	private ReminderStatus reminderStatus ;

    @Enumerated(EnumType.STRING)
    private ContactMethod contactMethod ;

    public static enum ReminderStatus {
        COMPLETE("Complete"), 
        REJECT("Reject"),
        PENDING("Pending"),
        AUTO_RESOLVED("AutoResolved"),
        MAN_RESOLVED("ManResolved"),
        NO_ADDRESS_AVAILABLE("No Address Available"),
        INSUFFICIENT_AMOUNT("InSufficient Amount");

        private final String name;

        private ReminderStatus(String name) {
            this.name = name ;
        }

        public String toString() {
            return name;
        }
    }

    public static enum ContactMethod {
        DIALER, EMAIL;
    }
    
    public void sendFollowupReminderRip()
    {
    	final FollowupRemindersRipObject followupRemindersRipObject=newfollowupRemindersRipObject();
    	ripEventListener.followupreminderCreated(followupRemindersRipObject);
    }
    protected FollowupRemindersRipObject newfollowupRemindersRipObject()
    {
    	String clidentifier=this.claimantClaim !=null? this.claimantClaim.getClaimIdentifier() : "";
    	final FollowupRemindersRipObject followupRemindersRipObject=new FollowupRemindersRipObject();
    	followupRemindersRipObject.setReferenceNo(clidentifier); 
    	followupRemindersRipObject.setReminderType(this.reminderType.getDescription().toString());
    	followupRemindersRipObject.setCreatedByUser(AccountContext.getCurrentUsername());
    	followupRemindersRipObject.setWorkType("RJTEMAIL");
    	return followupRemindersRipObject;
    }
    
    
    public List<ClaimantReminder> findClaimantReminderByClaimant(Claimant claimant) {
        if (claimant == null) throw new IllegalArgumentException("The claimant argument is required");
        EntityManager em = ClaimantReminder.entityManager();
        TypedQuery<ClaimantReminder> q = em.createQuery("SELECT o FROM ClaimantReminder AS o WHERE o.claimant = :claimant and o.reminderStatus <> :reminderstatus1 and o.reminderStatus <> :reminderstatus2", ClaimantReminder.class);
        q.setParameter("claimant", claimant);
        q.setParameter("reminderstatus1", ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
        q.setParameter("reminderstatus2", ClaimantReminder.ReminderStatus.MAN_RESOLVED);
        return q.getResultList();
    }
    
	public static List<ClaimantReminder> findClaimantReminderByClaimantClaim(
			ClaimantClaim claimantClaim) {
		return findClaimantReminders(claimantClaim, "Submit Documents");
	}
    
    public static List<ClaimantReminder> findClaimantReminders(ClaimantClaim claimantClaim, String reminderDesc) {
    	List<ClaimantReminder> claimantReminders = new ArrayList<ClaimantReminder>();
        EntityManager em = ClaimantReminder.entityManager();
        TypedQuery<ClaimantReminder> q = em.createQuery("SELECT o FROM ClaimantReminder AS o WHERE o.claimantClaim = :claimantClaim and o.reminderType.description = :reminderdesc and o.reminderStatus <> :reminderstatus", ClaimantReminder.class);
        q.setParameter("claimantClaim", claimantClaim);
        q.setParameter("reminderdesc", reminderDesc);
        q.setParameter("reminderstatus", ClaimantReminder.ReminderStatus.COMPLETE);
        claimantReminders = q.getResultList();
        return claimantReminders;
    }
    
    public static List<ClaimantReminder> findClaimantReminders(Claimant claimant, String reminderDesc) {
        if (claimant == null) throw new IllegalArgumentException("The claimant argument is required");
        EntityManager em = ClaimantReminder.entityManager();
        TypedQuery<ClaimantReminder> q = em.createQuery("SELECT o FROM ClaimantReminder AS o WHERE o.claimant = :claimant and o.reminderType.description = :reminderdesc  and o.reminderStatus <> :reminderstatus", ClaimantReminder.class);
        q.setParameter("claimant", claimant);
        q.setParameter("reminderdesc", reminderDesc);
        q.setParameter("reminderstatus", ClaimantReminder.ReminderStatus.COMPLETE);
        return q.getResultList();
    }
    
    
    public static List<ClaimantReminder> findClaimantRemindersWithReminderStatus(Claimant claimant, String reminderDesc) {
    	List<ClaimantReminder> claimantReminders = new ArrayList<ClaimantReminder>();
        EntityManager em = ClaimantReminder.entityManager();
        TypedQuery<ClaimantReminder> q = em.createQuery("SELECT o FROM ClaimantReminder AS o WHERE o.claimant = :claimant and o.reminderType.description = :reminderdesc ", ClaimantReminder.class);
        q.setParameter("claimant", claimant);
        q.setParameter("reminderdesc", reminderDesc);
        claimantReminders = q.getResultList();
        return claimantReminders;
    }
    
    
   
}
