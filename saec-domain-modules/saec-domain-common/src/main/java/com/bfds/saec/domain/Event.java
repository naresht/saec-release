package com.bfds.saec.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.AccountType;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.BatchFileLov;
import com.bfds.saec.domain.reference.PermissionType;
import com.bfds.saec.domain.reference.RpoEligibleOption;
import com.bfds.saec.util.SaecDateUtils;

@RooJavaBean
@RooToString(excludeFields = { "id", "version", "bankName" })
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Configurable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({ @NamedQuery(name = "getDda", query = "select e.dda FROM com.bfds.saec.domain.Event AS e") })

@SecondaryTable(name="EVENT_SMALL_CHECK_CONFIG", pkJoinColumns=@PrimaryKeyJoinColumn(name="event_fk"))
public class Event implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public static Event newEvent() {
        final Event event = new Event();
        event.initializePaymentConfig();
        event.initializeFileNotificationConfig();
    	event.setHideReminders(Boolean.TRUE);
        return event;
    }

    /*
      * -----------------------------General event Start
      * ------------------------------
      */
    
    
    private Boolean hideReminders;

    /**
     * General Event: Event Short Code
     */
    @NotNull
    private String code;

    /**
     * General Event: Event Name
     */
    private String name;

    private String description;

    /**
     * Event Type
     */
    private String eventType;

    private String issuingBankCode;

    /**
     * Start Date
     */
    private Date startDate;

    /**
     * Target End Date
     */
    private Date targetEndDate;

    /**
     * Issuing Bank Code
     *
     * @link BankLov
     *
     */
    private String bankCode;

    /**
     * Issuing Bank ABA #
     */
    private int bankABANumber;

    /**
     * General Event: DDA. Should we move this to {@link Bank}
     */
    private String dda;

    /**
     * dBUserId is used to display the User Filed in Header record as provided
     * by Deustche Bank
     */
    @Column(length = 8)
    private String deutscheBankUserId;
    
    /**
	 * checkNameforBottomlineOutFile is used for displaying field position 427-464 in SSC BottomLine Outbound File.
	 * This is a standard field for all the checks being produced for a particular Event.
	 */    
    @Column(length = 38)
	private String checkNameforBottomlineOutFile;
    

    /**
     * Tax Administrator
     */
    private Boolean taxAdministrator;

    /**
     *  Tax vendor  
     */
    private String taxVendor;

    /**
     * Foreign Account Solicitation
     */
    private Boolean foreignAccountSolicitation;

    /**
     * library ID
     */
    private String libraryId;

    /**
     * Closed Event
     */
    private Boolean closedEvent;

    /**
     * Closed Date
     */
    private Date closedDate;

    /**
     * Omnibus outreach
     */
    private Boolean omnibusOutreach;

    /**
     * General Event: Stale date days
     *
     * A newly created Check will be stale if {@link Payment#getPaymentCode()}
     * == {@link PaymentCode} is one of the Outstanding codes and the
     * checkStaleByDays + 1 has reached.
     */
    private int checkStaleInDays;

    /**
     * SAEC issued ID
     */
    private String saecIssueId;
    
    /**
     *  Claim Cut Off  flag will be set prior to the Final Distribution 
     *  calculations being performed.
     */
    private Boolean claimCuttOff;

    /**
     * Comments section used when Other is selected
     */
    private String comments;

    /*
      * ----------------------------- General Event Configuration End
      * ------------------------------
      */

    /*
      * ----------------------------- TAX Reporting Configuration Start
      * ------------------------------
      */

    /**
     *
     */
    private Boolean requiresTaxInfo;

    /**
     * Taxable Distribution
     */
    private Boolean taxableDistribution;

    /**
     * TaxType Primary Code. e.g. 1098
     *
     * @link TaxTypeLov
     */
    private String taxTypePrimary;

    /**
     * TaxType Primary Code. e.g. 1098-C
     */
    private String taxTypeSecondary;

    /**
     * Taxable Threshold
     */
    private int taxableThreshold;

    /*
      * ----------------------------- TAX Reporting Configuration End
      * ------------------------------
      */

    /*
      * ----------------------------- Payment Configuration Start
      * ------------------------------
      */
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @LazyCollection(LazyCollectionOption.FALSE)
    private Set<EventPaymentConfig> paymentConfigs;

    /*
      * ----------------------------- Payment Configuration End
      * ------------------------------
      */

    /*
      * ----------------------------- Mail Object Configuration Start
      * ------------------------------
      */

    /**
     * Cure Letter Limit
     */
    private Boolean cureLetterLimit;

    /**
     * Cure Letter Limit #
     */
    private int cureLetterLimitNumber;

    /**
     * Event Threshold
     */
    private BigDecimal eventThreshold;

    /**
     * Cure Letter Range
     */
    private int cureLetterRangeStart;

    private int cureLetterRangeEnd;

    /*
      * ----------------------------- Mail Object Configuration End
      * ------------------------------
      */

    /*
      * ----------------------------- Address Research Configuration Start
      * ------------------------------
      */

    /**
     * Address Research
     */
    private Boolean addressResearch;

    private Boolean preScrub;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private RpoEligibleOption rpoEligible;

    private Boolean sendCountLimit;

    private int mailSendCountLimit;

    private int paymentSendCountLimit;
    /*
      * ----------------------------- Address Research Configuration End
      * ------------------------------
      */

    /*
      * ----------------------------- File Notification Configuration Start
      * ------------------------------
      */

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FileNotificationConfig> fileNotificationConfigs = new HashSet<FileNotificationConfig>();

    /*
      * ----------------------------- File Notification Configuration End
      * ------------------------------
      */
    /**
     *
     */
    private String notificationEmailTo;

    /**
     *
     */
    private Long checkStartingNo;

    /**
     *
     */
    private String mailDistributionList;

    /**
     * Address Research : Pre-scrub
     */
    private boolean requiresAddressPrescrub;

    /**
     * File Notifications : InfoAge Processing
     */
    private String infoAgeReviewMailDistributionList;

    /**
     * Mail Object Configuration: Event $ Threshold
     *
     * The threshold limit that will determine the document list for
     * correspondence processing.
     */
    private double correspondenceThresholdLimit;

    /**
     * Issuing Bank
     */
    @Embedded
    private Bank bank = new Bank();

    /**
     * A newly created Check will be stale if {@link Payment#getPaymentCode()}
     * == {@link PaymentCode} is one of the Outstanding codes and this date has
     * passed by a day.
     */
    private Date checkStaleByDate;

    /**
     * Can a stale dated check's status be updated.
     */
    private boolean canChangeStausOfStaleCheck;

    /**
     * Unique mailing control no. This is temporary and will be replaced by
     * actual mail control generation/configuration as and when specified.
     *
     * This is not being used anymore.
     */
    @Deprecated
    private Long mailingControlSequence;

    /**
     *  To store accumulated bank interest for the event.
     *
     *  User in 'Check Summary Dollar Band' report.
     */
    private BigDecimal bankInterest;


    /**
     * Use the bottom line process to assign check# when State Street is the issueing bank.
     * If this is false then the Issue Void batch process will assign the check#.
     */
    private Boolean useBottomLineForCheckNoAssignment;

    @Embedded
    @AttributeOverrides({
    	
        @AttributeOverride(name="eventNameAddress1", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),
        @AttributeOverride(name="eventNameAddress2", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),
        @AttributeOverride(name="eventNameAddress3", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),
        @AttributeOverride(name="eventNameAddress4", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")), 	
        @AttributeOverride(name="eventNameAddress5", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),	
        @AttributeOverride(name="eventNameAddress6", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),
        @AttributeOverride(name="variableVerbiage", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),	
        @AttributeOverride(name="bankInfo1", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),	
        @AttributeOverride(name="bankInfo2", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")), 	
        @AttributeOverride(name="bankInfo3", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),  	
        @AttributeOverride(name="distributionText", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),  	
        @AttributeOverride(name="abaTop", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),	
        @AttributeOverride(name="abaBottom", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")),	
        @AttributeOverride(name="voidAfterDays", column=@Column(table="EVENT_SMALL_CHECK_CONFIG")) })
    private SmallCheckConfig smallCheckConfig;
    /**
     * The current event is the first event found. The DB is expected to have
     * only one Event.
     *
     * @return The current Event.
     */
    public static Event getCurrentEvent() {
    	List<Event> allEvents = Event.findAllEvents();
    	if (allEvents.size() > 0) {
    		return Event.findAllEvents().get(0);
    	}
    	return new Event();
    }

     
    
    public void setupEvent() {
        System.err.println("Inside Event:SetupEvent");

    }

    public static String getCurrentEventCode() {
        return getCurrentEvent().getCode();
    }

    public static String getCurrentEventDescription() {
        return getCurrentEvent().getName();
    }

    public Date getStaleDate() {
        if (checkStaleByDate != null) {
            return checkStaleByDate;
        }
        ;
        if (checkStaleInDays != 0) {
            return SaecDateUtils.getDaysFromCurrent(checkStaleInDays);
        }
        return SaecDateUtils.getMax();
    }

    @Transactional
    public String getNextCheckSequenceNumber() {
        if (this.checkStartingNo == null) {
            this.checkStartingNo = new Long(1000);
        }
        this.checkStartingNo = this.checkStartingNo + 1;
        this.merge();
        return String.valueOf(this.checkStartingNo);
    }

    private void initializePaymentConfig() {
        this.paymentConfigs = new HashSet<EventPaymentConfig>();
        List<PaymentComponentTypeLov> pclovs = PaymentComponentTypeLov
                .findAllPaymentComponentTypeLovs();
        Iterator iter = pclovs.iterator();
        while (iter.hasNext()) {
            PaymentComponentTypeLov pctylov = (PaymentComponentTypeLov) iter
                    .next();
            EventPaymentConfig epc = new EventPaymentConfig();
            epc.setActive(true);
            epc.setEvent(this);
            epc.setDefaultDescription(pctylov.getDescription());
            epc.setPaymentComponentType(pctylov);
            // char objCode = pctylov.getDescription2().charAt(0) ;
            epc.setObjectCode('A');
            this.paymentConfigs.add(epc);
        }

    }

    private void initializeFileNotificationConfig() {
        this.fileNotificationConfigs = new HashSet<FileNotificationConfig>();
        List<BatchFileLov> pclovs = BatchFileLov.findAllBatchFileLovs();
        Iterator iter = pclovs.iterator();
        while (iter.hasNext()) {
            BatchFileLov pctylov = (BatchFileLov) iter.next();
            FileNotificationConfig epc = new FileNotificationConfig();
            epc.setEvent(this);
            epc.setFileNotificationType(pctylov.getCode());
            epc.setPermType(PermissionType.VIEW);
            this.fileNotificationConfigs.add(epc);
        }

    }

    public static String getCurrentEventDda() {
        TypedQuery<String> query = entityManager().createNamedQuery("getDda",
                String.class);
        try {
            return query.getSingleResult();
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public String getBankName(){
        BankLov bankLov=BankLov.findBankLovsByCode(Event.getCurrentEvent().getBankCode()).getSingleResult();
        return bankLov.getDescription();
    }
}
