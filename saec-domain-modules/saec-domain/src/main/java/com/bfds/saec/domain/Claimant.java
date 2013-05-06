package com.bfds.saec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantClaimId;
import com.bfds.wss.domain.ClaimantReminder;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.integration.api.UpdateListener;
import com.bfds.saec.rip.domain.AddressChangeRipObject;
import com.bfds.saec.rip.service.RipEventListener;
import com.google.common.base.Preconditions;

/**
 * aka Account. A Claimant represents an individual or an organization from the harm population. A Claimant has the following associations
 * 
 * Address - A claimant has a minimum of one address 
 * Payment - One or more Payment(s)
 * Letter -  One or more Letter(s)
 * Contact - One Primary and any number of additional
 * PhoneCall - Any number of PhoneCall(s) can be made or received.
 * Activity - All transactions performed are recored as Activity - address change, Check void etc.
 * Parent Claimant - The Claimant for which this is an alternate payee. 
 * 
 */
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findClaimantsByReferenceNo", "findClaimantsByCreatedByEquals"})
@Audited
@NamedQueries({
	@NamedQuery(name="getSplitAccounts", query="from Claimant o left join fetch o.addresses where o.parentClaimant.id = :id"),
	@NamedQuery(name="getAlternatePayee", query="from Claimant as c where c.parentClaimant.id = :id")
} )
public class Claimant implements Serializable {
	private static final long serialVersionUID = 1L;

    public static final String ASSOCIATION_ACTIVITY = "activity";
    public static final String ASSOCIATION_CONTACTS = "contacts";
    public static final String ASSOCIATION_ADDRESSES = "addresses";
    public static final String ASSOCIATION_PAYMENTS = "payments";
    public static final String ASSOCIATION_CALLS = "phoneCall";
    public static final String ASSOCIATION_LETTERS = "letters";

    public static final String[] ASSOCIATION_ALL = {ASSOCIATION_ACTIVITY,
            ASSOCIATION_ADDRESSES, ASSOCIATION_CONTACTS, ASSOCIATION_LETTERS,
            ASSOCIATION_PAYMENTS };
    
    @Autowired
    private transient UpdateListener updateListener;
    
    @Autowired
    private transient RipEventListener ripEventListener;

    /**
     * The reference# of the claimant. This is a candidate key across the event.
     */
    @NotNull
    @Index(name = "referenceNo_ind")
    @Column(unique=true, nullable=false)
    private String referenceNo;

    /**
     * The Name of the Claimant. In most events the name is available as part of {@link ClaimantRegistration}, and this field is left empty. 
     */
    @Embedded
    private Name name;

    private String bin;

    private String fundAccountNo;

    /**
     * TODO: convert to enum if possible.
     */
    private String accountStatus;
    /**
     * TODO: convert to enum if possible.
     */
    private String accountType;

    private String accountTypeOtherDescription;

    private String brokerId;

    /**
     * Is this {@link Claimant} a market timer. If yes, the {@link Claimant} is usually not eligible for payments. 
     */
    private Boolean marketTimer;

    @NotNull
    private Boolean doAudit;

    private String specialPull;

    private Boolean omniBus;

    /**
     * The User that created this Claimant. If the Claimant was created from a data import funtion then the user is system!
     */
    private String createdBy;

    /**
     * The date the Claimant was created.
     */
    private Date createdDate;

    /**
     * Is this claimant an organization. If false the claimant is an individual.
     */
    private Boolean organization;

    /**
     * If true, the account is not eligible for receiving payments. This is also
     * called the pay indicator.
     */
    private Boolean notEligibleForPayment;
    
    /**
     * Reason for why the account is not eligible for receiving payments. 
     */
    private String ineligibilityReason;
    
    /**
     * If this is an alternate payee then this code tell why this was created as
     * an alternate payee.
     */
    private String alternatePayeeResonCode;

    /**
     * Is this Claimant an individual or a Corporation. TODO: How is this different form {@link #organization}
     */
    private Boolean corporate = Boolean.FALSE;

    /**
     * Has the claimant reached its send count limit for address research.
     */
    private boolean isSendCountLimit = false;

    /**
     * The number of times this claimant's address has been submitted for research.
     * @deprecated - Use one of {@link #addressResearchSentCountForLetters}, {@link #addressResearchSentCountForChecks} or {@link #addressResearchSentCountForLettersAndChecks}
     */
    @Deprecated
    private int addressResearchSentCount;
    
    /**
     * The number of times this claimant's address has been submitted for research for a RPO {@link Letter}.
     */
    private int addressResearchSentCountForLetters;
    
    /**
     * The number of times this claimant's address has been submitted for research for a RPO {@link Payment}.
     */
    private int addressResearchSentCountForChecks;
    
    /**
     * The number of times this claimant's address has been submitted for research for a RPO {@link Letter} and {@link Payment}. 
     * This when both RPO {@link Payment} and RPO {@link Letter} exist at the time of sending the research file.
     */
    private int addressResearchSentCountForLettersAndChecks;
    
    /**
     * A flag to indicate that this {@link Claimant} has been sent for address research processing. This flag should be used to exclude this {@link Claimant} from being 
     * sent again for address research before receiving an update from the earlier address research processing. However we should be able to send this {@link Claimant} again for
     * address research after receiving an update from the earlier address research processing. 
     */
    private Boolean addressResearchSent;
    
    /**
     * @see HoldCode 
     * This field will be manually set via the 'Account Detail' screen. It also may need to be set by the system as part of the tranche logic.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private HoldCode holdCode;
    
    /**
     * The registration details of the claimant.
     */    
    @OneToOne(cascade = CascadeType.ALL, optional=false, fetch=FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private ClaimantRegistration claimantRegistration;

    /**
     * The tax details of the Claimant - {@link ClaimantTaxInfo}
     */
    private @Embedded
    ClaimantTaxInfo taxInfo = new ClaimantTaxInfo();
    
    /**
     * The Employment History of the Claimant - {@link ClaimantEmploymentHistory}
     */
    private @Embedded
    ClaimantEmploymentHistory employmentHistory = new ClaimantEmploymentHistory();
       
    /**
     * The Primary {@link Contact} details of the Claimant. Usually the Claimant contact details. 
     */
    @OneToOne(mappedBy = "primaryContactOf", cascade = CascadeType.ALL)
    @JoinColumn(name = "primaryContact")
    private Contact primaryContact;

    /**
     *  The {@link Claimant} from which this {@link Claimant} has been created as an alternate payee. 
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.DETACH)
    private Claimant parentClaimant;
    
    /**
     * The additional {@link Contact}s of the Claimant. Note that this does not contain the Primary contact - {@link #primaryContact}
     */
    @OneToMany(mappedBy = "claimant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Set<Contact> contacts = new HashSet<Contact>();

    /**
     * The {@link Address} of the Claimant. Usually there are at most two {@link Address} - one primary and one seasonal. 
     * Note that there must be a minimum of one address. 
     */
    @OneToMany(mappedBy = "claimant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClaimantAddress> addresses = new HashSet<ClaimantAddress>();

    /**
     * All the {@link PhoneCall}s made to and received from this Claimant.
     */
    @OneToMany(mappedBy = "claimant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotAudited
    private Set<PhoneCall> phoneCall = new HashSet<PhoneCall>();

    /**
     * All the {@link Payment}s of this Claimant.
     */
    @OneToMany(mappedBy = "payTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotAudited
    private Set<Payment> payments = new HashSet<Payment>();

    /**
     * All the {@link Letter}s sent to and received from this Claimant. 
     */
    @OneToMany(mappedBy = "claimant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotAudited
    private Set<Letter> letters = new HashSet<Letter>();

    
    /**
     * All changes made to this Claimant are recored as activity. This is a {@link Collection} of read-only objects.
     */
    @OneToMany(mappedBy = "claimant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotAudited
    private Set<Activity> activity = new HashSet<Activity>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "claimant", cascade = CascadeType.ALL)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Set<ClaimantClaim> claimantClaims = new HashSet<ClaimantClaim>(0);


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "claimant")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @NotAudited
    private Set<ClaimantReminder> reminders = new HashSet<ClaimantReminder>(0);
    
    /** 
     *  Get all {@link ClaimantClaimId}s which are associated with this claimant, There is case when a
     *  Class Member prints the Claim form without submitting it online.    
     */
    @OneToMany(mappedBy = "claimant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @NotAudited
    private Set<ClaimantClaimId> claimantClaimIds = new HashSet<ClaimantClaimId>();
    
    
    /**
     * @return - The address of record of the {@link Claimant}.
     */
    public ClaimantAddress getAddressOfRecord() {
        return this.getAddressByType(AddressType.ADDRESS_OF_RECORD);
    }

    /**
     * @param adr - The {@link ClaimantAddress} to set as the address of record - See {@link AddressType#ADDRESS_OF_RECORD}
     * @throws - {@link IllegalStateException} if address of record already present
     */
    public void setAddressOfRecord(ClaimantAddress adr) {
        this.setAddressByType(adr, AddressType.ADDRESS_OF_RECORD);
    }

    /**
     * @return - The seasonal address of the {@link Claimant}.
     */
    public ClaimantAddress getSeasonalAddress() {
        return this.getAddressByType(AddressType.SEASONAL_ADDRESS);
    }

    /**
     * @param adr - The {@link ClaimantAddress} to set as the seasonal address - See {@link AddressType#SEASONAL_ADDRESS}
     * @throws - {@link IllegalStateException} if seasonal address already present
     */    
    public void setSeasonalAddress(ClaimantAddress adr) {
        this.setAddressByType(adr, AddressType.SEASONAL_ADDRESS);
    }

    private ClaimantAddress getAddressByType(AddressType type) {
        for (ClaimantAddress a : this.getAddresses()) {
            if (a.getAddressType() == type) {
                return a;
            }
        }
        return null;
    }

    private void setAddressByType(ClaimantAddress address, AddressType type) {
        Preconditions.checkNotNull(address, "address cannot be null");
        Preconditions.checkNotNull(type, "addressType cannot be null");
        for (Iterator<ClaimantAddress> itr = this.getAddresses().iterator(); itr.hasNext();) {
            ClaimantAddress a = itr.next();
            if (a.getAddressType() == type) {
                throw new IllegalStateException(type + " address already set.");
            }
        }
        address.setAddressType(type);
        this.getAddresses().add(address);
        address.setClaimant(this);
    }

    /**
     * @param type - The {@link AddressType}. 
     * 
     * Set the {@link ClaimantAddress} with the given {@link AddressType} as the mailing address.
     */
    public void setMailingAddressByType(AddressType type) {
        if (type == null) {
            return;
        }
        for (ClaimantAddress addr : this.getAddresses()) {
            if (type == addr.getAddressType()) {
                addr.setMailingAddress(true);
            } else {
                addr.setMailingAddress(false);
            }
        }

    }

    public ClaimantAddress getMailingAddress() {
        ClaimantAddress address = null;
        for (ClaimantAddress adr : this.getAddresses()) {
            if (adr.isMailingAddress()) {
                address = adr;
            }
        }
        return address == null ? getAddressOfRecord() : address;
    }

    /**
     * @return - True if ths {@link Claimant} has an Address of record.
     */
    public boolean hasAddressOfRecord() {
        return this.getAddressOfRecord() != null
                && !this.getAddressOfRecord().isEmpty();
    }

    /**
     * @return - True if ths {@link Claimant} has a seasonal of record.
     */
    public boolean hasSeasonalAddress() {
        return this.getSeasonalAddress() != null
                && !this.getSeasonalAddress().isEmpty();
    }

    /**
     * @return - All the {@link PhoneCall}s of this {@link Claimant} as a {@link List}
     */
    public List<PhoneCall> getAllCalls() {
        List<PhoneCall> logs = new ArrayList<PhoneCall>();
        for (PhoneCall call : this.getPhoneCall()) {
            if (!call.isShapshot()) {
                logs.add(call);
            }
        }
        return logs;
    }

    /**
     * @param userName - The user who has made or taken the phone call.
     * @return - A new {@link PhoneCall} is created for the user, persisted and returned.
     */
    public PhoneCall startCallLog(String userName) {
        PhoneCall call = PhoneCall.startCallLog(userName);
        this.addPhoneCall(call);
        call.persist();
        return call;
    }

    /**
     * @return All the {@link Activity} of this {@link Claimant} as a {@link List}
     */
    public List<Activity> getAllActivity() {
        List<Activity> ret = new ArrayList<Activity>();
        for(Activity activity : this.getActivity()) {
        	if(activity instanceof PhoneCall && ((PhoneCall)activity).isShapshot()) {
        		continue;
        	}
        	 ret.add(activity);
        }
        Collections.sort(ret, new ActivityComparator());
        return ret;
    }

    /**
     * @return - All {@link Payment}s of this {@link Claimant} that can be reissued.
     */
    public List<Payment> getReissuablePayments() {
        List<Payment> ret = new ArrayList<Payment>();
        for (Payment c : this.getPayments()) {
            if (c.isReissueable()) {
                ret.add(c);
            }
        }
        return ret;
    }
    
    /**
     * @return - All {@link Payment}s of this {@link Claimant} that are candidates for the next mailing.
     */
	public List<Payment> getPaymentsEligibleForMailing() {
		List<Payment> payments_ = new ArrayList<Payment>();
		for (Payment payment : this.getPayments()) {
			if (PaymentCodeUtil.getCreatedCodes().contains(payment.getPaymentCode())) {
				payments_.add(payment);
			}
		}
		return payments_;
	}
	
	 /**
     * @return - All {@link Letter}s of this {@link Claimant} that are candidates for the next mailing.
     * TODO: Need to make changes once the Letter :dstoPrintFileSentFlag type
	 * will be changed to Boolean.
     */
	public List<Letter> getLettersEligibleForMailing() {
		List<Letter> letters_ = new ArrayList<Letter>();
		for (Letter letter : this.getMails()) {
			if (letter.getDstoPrintFileSentFlag()==Boolean.FALSE) {
				letters_.add(letter);
			}
		}
		return letters_;
	}


    /**
     * A Comparator used to sort {@link Activity} by activity date.
     *
     */
    public static class ActivityComparator implements
            java.util.Comparator<Activity> {
        @Override
        public int compare(Activity o1, Activity o2) {
            return 0 - o1.getActivityDate().compareTo(o2.getActivityDate());
        }
    }

    /**
     * @return - The payment address of this {@link Claimant}. The payment address is the mailing address.
     */
    public ClaimantAddress getPaymentAddress() {
        return this.getMailingAddress();
    }

    /**
     * @param letter - The {@link Letter} to add to the Collection of {@link Letter}s
     */
    public void addLetter(final Letter letter) {
        letter.setClaimant(this);
        //TODO: Why is this set.
        letter.setAuditable(true);
        /**
         * Set the address only if the letter is not having Address.
         * It is possible a letter can have one time mailing address.
         */
        if(letter.getAddress()==null){
        	MailObjectAddress address = new MailObjectAddress();
            this.getMailingAddress().copyTo(address);  
            letter.setAddress(address);            
        }
       
        /**
         * Set the Registration Lines only if the letter is not having Registration Lines.
         * It is possible a letter can have one time Registration Lines.
         */        
        if(letter.getPayToRegistration()==null){
        	RegistrationLines regLines=new RegistrationLines();
        	this.getClaimantRegistration().getLines().copyTo(regLines);
        	letter.setPayToRegistration(regLines);
        }
        
        this.getLetters().add(letter);
    }

    /**
     * @param check - Add a new {@link Payment} to this {@link Claimant}.
     * The new {@link Payment} is assigned a clone of the {@link Claimant}s payment address - {@link Claimant#getPaymentAddress()}
     * and the {@link Claimant}s registration {@link Claimant#claimantRegistration}
     */
    public void addCheck(final Payment check) {
        check.setPayTo(this);
        MailObjectAddress address = new MailObjectAddress();
        this.getPaymentAddress().copyTo(address);
        check.setCheckAddress(address);       
        this.getPayments().add(check);
    }
    
  
    /**
     * @param check
     * This method is used to add a new {@link Payment} to this {@link Claimant}. 
     * The new check which is created from the check reissue process. 
     */
    public void addCheckforReisue(final Payment check) {
        check.setPayTo(this);
        this.getPayments().add(check);
    }

    /**
     * @param check - The {@link Payment} to remove.
     * @return - First try to remove by java identity. If that fails, remove by Hibernate/DB identity. 
     * TODO : This operation could fail with DB constraint violations. 
     */
    public boolean removeCheck(Payment check) {
        if (this.getPayments().remove(check)) {
            check.setPayTo(null);
            return true;
        }
        if(removePaymentById(check.getId())) {
        	return true;
        	
        }
        return removePaymentByIdentificationNo(check.getIdentificatonNo());
    }

	/**
	 * @param id - The PK of the {@link Payment} to remove.
	 * @return - True if remove succeeded.
	 */
	public boolean removePaymentById(Long id) {
		for (Payment c : this.getPayments()) {
            if (c.getId() != null) {
                if (c.getId().equals(id)) {
                    c.setPayTo(null);
                    return this.getPayments().remove(c);
                } 
            }
        }
		return false;
	}
	
	/**
	 * @param identificationNo - The identificationNo of the {@link Payment} to remove. identificationNo is a candidate key.
	 * @return - True if remove succeed.
	 */
	public boolean removePaymentByIdentificationNo(String identificationNo) {
		for (Payment c : this.getPayments()) {
            if (c.getIdentificatonNo() != null) {
                if (c.getIdentificatonNo().equals(identificationNo)) {
                    c.setPayTo(null);
                    return this.getPayments().remove(c);
                }
            }
        }
		return false;
	}

    /**
     * @param addressChange - The {@link AddressChange} event.
     */
    private void triggerAddressUpdatedEvent(final AddressChange addressChange) {
    	
    	if(AccountContext.USER_SYSTEM.equals(AccountContext.getCurrentUsername())) {
    		return;
    	}
    	AddressChangeRipObject ripObject = addressChange.getAddressChangeRipObject();
    	ripObject.setReferenceNo(this.getReferenceNo());
    	PhoneCall call = findCallInProgess();
    	if(call != null && call.getUserId().equals(ripObject.getCreatedByUser())) {
    		ripObject.setCorrelationId(call.getId());
    		ripEventListener.addressChanged(ripObject);
    	}
        //updateListener.notifyUpdate(addressChange.getUpdateEvent());
    }
    
    /**
     * @param addressChange - The {@link AddressChange} event.
     */
    public void triggerAddressCreatedEvent(final ClaimantAddress claimantAddress) {
    	if(AccountContext.USER_SYSTEM.equals(AccountContext.getCurrentUsername())) {
    		return;
    	}
    	AddressChange addressChange = new AddressChange();
    	
    	addressChange.setFrom(new Address());
    	addressChange.setTo(claimantAddress.getAddress());
    	

    	AddressChangeRipObject ripObject = addressChange.getAddressChangeRipObject();
    	ripObject.setReferenceNo(this.getReferenceNo());
    	PhoneCall call = findCallInProgess();
    	if(call != null && call.getUserId().equals(ripObject.getCreatedByUser())) {
    		ripObject.setCorrelationId(call.getId());
    		ripEventListener.addressChanged(ripObject);
    	}
        //updateListener.notifyUpdate(addressChange.getUpdateEvent());
    }
    
    /**
     * @return The Active call for this {@link Claimant}. The most recent call snapShot is considered the active call. Can we do better ?
     */
    public PhoneCall findCallInProgess() {
        PhoneCall ret = null;
        Date callDate = null;
        for (PhoneCall call : this.getPhoneCall()) {
            if (call.isShapshot()) {
            	if(callDate == null) {
            		ret = call;
            		callDate = call.getActivityDate();
            	} else if(call.getActivityDate().after(callDate)) {
            		ret = call;
            		callDate = call.getActivityDate();            		
            	}
            
            }
        }
        return ret;
    }
    

    /**
     * @param activity - An {@link AddressChange} activity. The activity indicates that an address change has happened. 
     */
    public void addAddressChangeActivity(AddressChange activity) {
        this.addActivity(activity);
        this.triggerAddressUpdatedEvent(activity);

    }

    /**
     * @return - True of the mailing address is the {@link AddressType#ADDRESS_OF_RECORD}.
     */
    public boolean isAorMailingAddress() {
        ClaimantAddress a = this.getMailingAddress();
        if (a != null) {
            return this.getMailingAddress().getAddressType()
                    .equals(AddressType.ADDRESS_OF_RECORD);
        }
        return false;
    }
    /**
     * @return - True of the mailing address is the {@link AddressType#SEASONAL_ADDRESS}.
     */
    public boolean isSeasonalMailingAddress() {
        ClaimantAddress a = this.getMailingAddress();
        if (a != null) {
            return this.getMailingAddress().getAddressType()
                    .equals(AddressType.SEASONAL_ADDRESS);
        }
        return false;
    }

    /**
     * @param alternatePayee - The new {@link Claimant} thats has to be added as an alternate payee.
     */
    public void addAlternatePayee(final Claimant alternatePayee) {
        alternatePayee.setFundAccountNo(this.getFundAccountNo());
        alternatePayee.setCreatedBy(AccountContext.getCurrentUsername());
        alternatePayee.setCreatedDate(new Date());
        alternatePayee.setParentClaimant(this);
    }

    /**
     * @param list - {@link List} of contacts to add to {@link Claimant#contacts}
     */
    public void addContacts(final Collection<Contact> list) {
        for (Contact c : list) {
            this.addContact(c);
        }
    }
    /**
     * @param list - {@link List} of contacts to remove from {@link Claimant#contacts}.
     * Note: This removes the {@link Contact}s from DB as well!!.
     */
    @Transactional
    public void removeContacts(Collection<Contact> list) {
        if (list == this.getContacts()) {
            list = new ArrayList<Contact>();
            list.addAll(this.getContacts());
        }
        for (Contact c : list) {
            this.removeContact(c);           
        }
    }
    
    /**
     * @param contact - A {@link Contact} to add as an additional contact - {@link Claimant#contacts}
     */
    public void addContact(final Contact contact) {
        this.getContacts().add(contact);
        contact.setClaimant(this);
    }

    /**
     * @param contact - Remove the {@link Contact} from additional contacts - {@link Claimant#contacts}
     * Note: This removes the {@link Contact} from DB as well!!.
     */
    @Transactional
    public void removeContact(final Contact c) {   
    	c.setClaimant(null);
        this.getContacts().remove(c);
        if(c.getId() != null) {
            c.remove();
        }
    }
    
    /**
     * @param call - A {@link PhoneCall} to add to the collection of phone calls - {@link Claimant#phoneCall}
     */    
    public void addPhoneCall(final PhoneCall call) {
        this.getPhoneCall().add(call);
        call.setClaimant(this);
    }

    /**
     * @param call - Remove the {@link PhoneCall} from the collection of phone calls - {@link Claimant#phoneCall}
     */ 
    public void removePhoneCall(final PhoneCall call) {
        call.setClaimant(null);
        this.getPhoneCall().remove(call);
    }    

    public List<Contact> getContactsAsList() {
        final List<Contact> ret = new ArrayList<Contact>();
        ret.addAll(this.getContacts());
        return ret;
    }

   
    public void setPrimaryContact(Contact primaryContact) {
        if (this.primaryContact != null) {
            this.primaryContact.setPrimaryContactOf(null);
        }
        if (primaryContact != null) {
            primaryContact.setPrimaryContactOf(this);
        }
        this.primaryContact = primaryContact;
    }

    /** 
     * @see IRegistrationLines#getRegistrationLinesAsString(String)
     */
    public String getRegistrationLinesAsString(String lineSeperator) {
        return this.getClaimantRegistration() != null ? this
                .getClaimantRegistration().getRegistrationLinesAsString(
                        lineSeperator) : null;
    }

    /**
     * @return - The {@link Letter} count for this {@link Claimant}
     */
    public Long getMailCount() {
        if (this.getId() != null) {
            return Letter.getLetterCountOfClaimant(this.getId());
        }
        return 0L;
    }
    
    
    /**
     * @return - All the {@link Letter}s of this {@link Claimant} as a {@link List}
     */
    public List<Letter> getMails() {
        if (this.getId() != null) {
            return Letter.findLetterOfClaimant(this.getId());
        }
        return new ArrayList<Letter>();
    }

    public void addActivity(final Activity activity) {
        this.getActivity().add(activity);
        activity.setClaimant(this);
    }

    public void removeActivity(final Activity activity) {
        activity.setClaimant(null);
        this.getActivity().remove(activity);
    }

    public void addAddress(final ClaimantAddress address) {
        Preconditions.checkNotNull(address, "address cannot be null");
        this.setAddressByType(address, address.getAddressType());
    }

    public void removeAddress(final ClaimantAddress address) {
        address.setClaimant(null);
        this.getAddresses().remove(address);
    }

    /**
     * A claimant can have zero or more split accounts(claimants).
     * @return List of all split accounts.
     */
    public final List<Claimant> getAlternatePayees() {
        return entityManager()
                .createNamedQuery("getSplitAccounts", Claimant.class).setParameter("id", this.getId())
                .getResultList();
    }
    
    public final List<Claimant> getAlternatePayee() {
    	return entityManager()
                .createNamedQuery("getAlternatePayee", Claimant.class).setParameter("id", this.getId())
                .getResultList();
    }

    /**
     * @param entity - The claimant to be saved.
     */
    @Transactional
    public static void save(final Claimant entity) {
        Validate.notNull(entity, "The entity to save cannot be null element");
        // map brand new entity or entity whose primary is manually set.
        if (entity.getId() != null || !entityManager().contains(entity)) {
            entityManager().persist(entity);
        }
    }

    /**
     * @param id - The PK of the Claimant.
     * @param associations - The names of associations that have to be eagerly loaded.
     * @return - The Claimant
     */
    public static Claimant findClaimant(final Long id, String... associations) {
    	CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Claimant> cq = cb.createQuery(Claimant.class);
		Root<Claimant> claimant = cq.from(Claimant.class);
		claimant.fetch("primaryContact", JoinType.LEFT);
		claimant.fetch("claimantRegistration", JoinType.LEFT);
		cq.select(claimant);
		cq.where(cb.and(cb.equal(claimant.get("id"), id)));
		if (associations == null) {
            associations = ASSOCIATION_ALL;
        }
        for (String association : associations) {
        	claimant.fetch(association, JoinType.LEFT);
        }
        return entityManager().createQuery(cq).getSingleResult();
    }

	/**
	 * Method to select the {@link Claimant} based upon its referenceNo.
	 * 
	 * This is a hack for now to avoid the Exception
	 * {hibernate.AssertionFailure: collection was not processed by flush()}.
	 * This will be removed once the issue resolved.
	 * 
	 * @param referenceNo
	 *            : referenceNo of an Claimant.If the {@link Claimant} with the
	 *            given referenceNo is present in the database returns the same.
	 * @return
	 */
	public static Claimant findClaimant(final String referenceNo) {

		TypedQuery<Claimant> q = entityManager()
				.createQuery(
						"SELECT distinct c FROM Claimant AS c  WHERE c.referenceNo = :referenceNo",
						Claimant.class);
		q.setParameter("referenceNo", referenceNo);
		return q.getSingleResult();
	}
    /**
     * @param referenceNo - The unique referenceNo of the Claimant.
     * @param associations - The names of associations that have to be eagerly loaded.
     * @return - The Claimant
     */
    public static Claimant findClaimant(String referenceNo, String... associations) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Claimant> cq = cb.createQuery(Claimant.class);
        Root<Claimant> claimant = cq.from(Claimant.class);
        claimant.fetch("primaryContact", JoinType.LEFT);
        claimant.fetch("claimantRegistration", JoinType.LEFT);
        cq.select(claimant);
        cq.where(cb.and(cb.equal(claimant.get("referenceNo"), referenceNo)));
        if (associations == null) {
            associations = ASSOCIATION_ALL;
        }
        for (String association : associations) {
            claimant.fetch(association, JoinType.LEFT);
        }
        return entityManager().createQuery(cq).getSingleResult();
    }
    
    // IMPORTANT: Only used for Test Data
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    
    /**
     * Generate the unique reference# for this {@link Claimant} when it is created.
     */
    @PrePersist
    public void generateReferenceNumber() {
        if (!StringUtils.hasText(this.referenceNo)) {
            this.referenceNo = String
                    .valueOf(100000000 + (new ClaimantReferenceNumber()).getNextValue());
        }
    }
    
    /**
     * @see - {@link ClaimantTaxInfo#getSsn()}
     */
    public String getSsn() {    	
    	return this.taxInfo != null ? this.taxInfo.getSsn() : null;        
    }

    /**
     * @see - {@link ClaimantTaxInfo#setSsn(String)}
     */
    public void setSsn(String ssn) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}    		
    	this.taxInfo.setSsn(ssn);   	
    }
    
    /**
     * @see - {@link ClaimantTaxInfo#getEin()}
     */
    public String getEin() {
    	return this.taxInfo != null ? this.taxInfo.getEin() : null;
    }

    /**
     * @see - {@link ClaimantTaxInfo#setEin(String)}
     */
    public void setEin(String ein) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}
        this.taxInfo.setEin(ein);
    }

    /**
     * @see - {@link ClaimantTaxInfo#getTin()}
     */
    public String getTin() {
    	return this.taxInfo != null ? this.taxInfo.getTin() : null;
    }

    /**
     * @see - {@link ClaimantTaxInfo#setTin(String)}
     */    
    public void setTin(String tin) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}
        this.taxInfo.setTin(tin);
    }

    /**
     * @see - {@link ClaimantTaxInfo#getCertificationStatus()}
     */
    public String getCertificationStatus() {
    	return this.taxInfo != null ? this.taxInfo.getCertificationStatus() : null;
    }

    /**
     * @see - {@link ClaimantTaxInfo#setCertificationStatus(String)}
     */       
    public void setCertificationStatus(String certificationStatus) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}
        this.taxInfo.setCertificationStatus(certificationStatus);
    }

    /**
     * @see - {@link ClaimantTaxInfo#getCertificationType()}
     */
    public String getCertificationType() {
    	return this.taxInfo != null ? this.taxInfo.getCertificationType() : null;
    }

    /**
     * @see - {@link ClaimantTaxInfo#setCertificationType(String)}
     */      
    public void setCertificationType(String certificationType) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}
        this.taxInfo.setCertificationType(certificationType);
    }

    /**
     * @see - {@link ClaimantTaxInfo#getTaxCountryCode()}
     */
    public String getTaxCountryCode() {
    	return this.taxInfo != null ? this.taxInfo.getTaxCountryCode() : null;
    }

    /**
     * @see - {@link ClaimantTaxInfo#setTaxCountryCode(String)}
     */       
    public void setTaxCountryCode(String taxCountryCode) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}
        this.taxInfo.setTaxCountryCode(taxCountryCode);
    }

    /**
     * @see - {@link ClaimantTaxInfo#getUsCitizen()}
     */    
    public Boolean getUsCitizen() {
    	return this.taxInfo != null ? this.taxInfo.getUsCitizen() : null;
    }

    /**
     * @see - {@link ClaimantTaxInfo#setUsCitizen(Boolean)}
     */           
    public void setUsCitizen(Boolean usCitizen) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}
        this.taxInfo.setUsCitizen(usCitizen);
    }

    /**
     * @see - {@link ClaimantTaxInfo#getCertificationDate()}
     */        
    public Date getCertificationDate() {
    	return this.taxInfo != null ? this.taxInfo.getCertificationDate() : null;
    }
    
    /**
     * @see - {@link ClaimantTaxInfo#setCertificationDate(Date)}
     */  
    public void setCertificationDate(Date certificationDate) {
    	if(this.taxInfo == null){
    		this.taxInfo=new ClaimantTaxInfo();
    	}
        this.taxInfo.setCertificationDate(certificationDate);
    }
        
    /**
     * @see - {@link ClaimantRegistration}{@link #setRegistration1(String)}
     */
    public void setRegistration1(String registration1) {
        createClaimantRegistrationIfNull();
        claimantRegistration.setRegistration1(registration1);
    }
    
    /**
     * @see - {@link ClaimantRegistration}{@link #setRegistration2(String)}
     */    
    public void setRegistration2(String registration2) {
        createClaimantRegistrationIfNull();
        claimantRegistration.setRegistration2(registration2);
    }

    /**
     * @see - {@link ClaimantRegistration}{@link #setRegistration3(String)}
     */    
    public void setRegistration3(String registration3) {
        createClaimantRegistrationIfNull();
        claimantRegistration.setRegistration3(registration3);
    }

    /**
     * @see - {@link ClaimantRegistration}{@link #setRegistration4(String)}
     */    
    public void setRegistration4(String registration4) {
            createClaimantRegistrationIfNull();
            claimantRegistration.setRegistration4(registration4);
    }

    /**
     * @see - {@link ClaimantRegistration}{@link #setRegistration5(String)}
     */    
    public void setRegistration5(String registration5) {
        createClaimantRegistrationIfNull();
        claimantRegistration.setRegistration5(registration5);
    }

    /**
     * @see - {@link ClaimantRegistration}{@link #setRegistration6(String)}
     */    
    public void setRegistration6(String registration6) {
        createClaimantRegistrationIfNull();
        claimantRegistration.setRegistration6(registration6);
    }

	private void createClaimantRegistrationIfNull() {
		if (claimantRegistration == null) {
            claimantRegistration = new ClaimantRegistration();            
        }
	}
        
	public void setFirstName(String firstName) {
        createNameIfNull();
        this.name.setFirstName(firstName);
    }

    public void setMiddleName(String middleName) {
        createNameIfNull();
        this.name.setMiddleName(middleName);
    }

	private void createNameIfNull() {
		if (this.name == null) {
            this.name = new Name();
        }
	}
    
    public void setLastName(String lastName) {
       createNameIfNull();
       this.name.setLastName(lastName);
    }


    public ClaimantClaim getSingleClaimantClaim() {
    	if(this.getClaimantClaims().size()>0)
    	{
        return new ArrayList<ClaimantClaim>(this.getClaimantClaims()).get(0) ;
    	}
        else
        {
        	return null;
        }
    }

    public Payment getOutstandingPayment() {
        Payment returnPayment = null ;
        if(this.getPayments() == null || this.getPayments().size() == 0) {
            return new Payment() ;
        } else {
            if (this.getPayments().size() > 1) {
                for (Payment p : this.getPayments()) {
                    if (p.getId() > 0 && PaymentCodeUtil.getOutstandingCodes().contains(p.getPaymentCode())) {
                        returnPayment = p ;
                    }
                }
            } else if (this.getPayments().size() == 1) {
                returnPayment = new ArrayList<Payment>(this.getPayments()).get(0) ;
            }
        }
        return returnPayment ;
    }
    
    public RipEventListener getRipEventListener() {
		return ripEventListener;
	}

	public void setRipEventListener(RipEventListener ripEventListener) {
		this.ripEventListener = ripEventListener;
	}
	  /**
     * @return  The  most recent {@link ClaimantClaimId} of this {@link Claimant} 
     */
    public ClaimantClaimId getLatestClaimantClaimId() {
        
    	List<ClaimantClaimId> ret = new ArrayList<ClaimantClaimId>();
        
        for(ClaimantClaimId claimId : this.getClaimantClaimIds()) {
        	 ret.add(claimId);
        }
        Collections.sort(ret, new ClaimantClaimIdComparator());
        return ret.get(0);
    }
	
	
	
	/**
     * A Comparator used to sort {@link ClaimantClaimId} by claimIdentifier.
     *
     */
    public static class ClaimantClaimIdComparator implements
            java.util.Comparator<ClaimantClaimId> {
        @Override
        public int compare(ClaimantClaimId o1, ClaimantClaimId o2) {
        	return Integer.valueOf(o2.getControlNumber()).compareTo(Integer.valueOf(o1.getControlNumber()));
        }
    }
	

	@Override
    public final String toString() {
        return "Claimant [referenceNo=" + referenceNo + ", Account Type="
                + accountType + ", Account Status =" + accountStatus
                + ", Mailing Address=" + getMailingAddress() + ", Name=" + name
                + ", Organization=" + organization + ", Primary Contact="
                + primaryContact + ", ssn=" + getSsn() + ", tin=" + getTin()
                + ", corporate=" + corporate + ", OmniBus=" + omniBus + "]";
    }
}
