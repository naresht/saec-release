package com.bfds.saec.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.rip.domain.CureLetterRipObject;
import com.bfds.saec.rip.service.RipEventListener;
import com.bfds.saec.service.DataFieldMaxValueIncrementerService;
import com.bfds.wss.domain.ClaimantPosition;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@RooJavaBean
@RooToString(excludeFields = { "id", "version" })
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = { "findLettersByMailingControlNo", "findLettersByRpoType",
		"findLettersByRpoTypeAndClaimant" })
@NamedQueries({
		@NamedQuery(name = "findLetterOfClaimant", query = "from Letter o where claimant.id = :claimantId"),
		@NamedQuery(name = "findFormsOfClaimantByLetterTypeAndCureLetterType", query = "from Letter o where claimant.id = :claimantId and (letterCode.letterType = :letterType1 or letterCode.letterType = :letterType2)"),
		@NamedQuery(name="countLettersForAddressResaerchOfClaimant", query="select count(l.id) from com.bfds.saec.domain.Letter AS l "+
		                                                                   " where l.claimant.id = :claimantId and l.rpoType =:letterRpoType"+
				                                                           " and ( l.letterCode.code < :cureLimitStart or l.letterCode.code > :cureLimitEnd ) "),
		@NamedQuery(name="getLastRpoLetterDateOfClaimant", query="select max(l.mailDate) from com.bfds.saec.domain.Letter AS l"+
				                                                 " where l.claimant.id = :claimantId and l.rpoType =:letterRpoType"+
				                                                 " and ( l.letterCode.code < :cureLimitStart or l.letterCode.code > :cureLimitEnd ) ")})
public class Letter implements IMail, java.io.Serializable {

	public static final String ASSOCIATION_NIGO_CORRESPONDENCE_DOCUMENTS = "nigoCorrespondenceDocuments";
	public static final String ASSOCIATION_IGO_CORRESPONDENCE_DOCUMENTS = "igoCorrespondenceDocuments";

	public static final String[] ASSOCIATION_ALL = {
			ASSOCIATION_NIGO_CORRESPONDENCE_DOCUMENTS,
			ASSOCIATION_IGO_CORRESPONDENCE_DOCUMENTS };

	public static final String THRESHOLD_YES = "Threshold = Y ";

	public static final String THRESHOLD_NO = "Threshold = N ";

	private static final long serialVersionUID = 1L;
	
    @Autowired
    private transient RipEventListener ripEventListener;
    
	@Autowired
	private transient DataFieldMaxValueIncrementerService dataFieldMaxValueIncrementerService;

	private Date mailDate;
	@Enumerated(EnumType.STRING)
	private MailType mailType;

	private Boolean dstoPrintFileSentFlag;

	/**
	 * A unique # for all outgoing {@link Letter}s - {@link MailType#OUTGOING}
	 */
	private String mailingControlNo;
	private String description;
	private String requestType;

	@ManyToOne(optional=false)
	//@JoinColumn//(name="letterCode_fk", nullable=false)
	@NotNull
	private LetterCode letterCode;

	private boolean auditable;
	
	private boolean specialPull;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private GroupMailCode groupMailCode;

	@Enumerated(EnumType.STRING)
	private LetterStatus letterStatus;

	@Enumerated(EnumType.STRING)
	private RPOType rpoType;

	// Flag to indicate if an RPO forwardable required a re-issue.
	private boolean rpoForwardableReissueRequired;

	private Date rpoDate;
	private String comments;
	/**
	 * User who las updated the {@link Letter}
	 */
	private String userId;

	// Used By Address Research
	@Deprecated
	boolean isSendCountLimit = false;
	@Deprecated
	int addressResearchSentCount;

	@ManyToOne(fetch = FetchType.EAGER, optional=true)
	private Letter inResponseTo;

	@ManyToOne
	@JoinColumn(name = "claimant_fk")
	private Claimant claimant;

	private String letterCodeString;

	@OneToOne(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	@BatchSize(size=10)
	@JoinColumn(name="address_id") 
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private MailObjectAddress address;
	
	@Embedded
	private RegistrationLines payToRegistration;
	
	@ManyToMany
	@BatchSize(size = 10)
	private Set<CorrespondenceDocument> igoCorrespondenceDocuments = new HashSet<CorrespondenceDocument>();

	@ManyToMany
	@BatchSize(size = 10)
	private Set<CorrespondenceDocument> nigoCorrespondenceDocuments = new HashSet<CorrespondenceDocument>();

	private Boolean correspondenceHasAwdObject;
	
	public void sendCureLetterCreatedRip() {
		/**
		 * Boolean check is done to create AWD Object based on True or False.
		 * Awd Object is to be created when this is TRUE only i.e., when the user clicks "No" in "Are you working for AWD Rip Object?" dialogue box in front-end
		 */
		if(MailType.OUTGOING == this.mailType && !(Boolean.TRUE.equals(getCorrespondenceHasAwdObject()))) {
			final CureLetterRipObject ripObject = newCureLetterRipObject();
			if(ripObject != null) {
				ripEventListener.cureLetterCreated(ripObject);
			}
		}
	}
	
	
	protected CureLetterRipObject newCureLetterRipObject() {
		PhoneCall activeCall = this.getClaimant().findCallInProgess();
		if(activeCall == null) {
			return null;
		}
		final CureLetterRipObject ripObject = new CureLetterRipObject();
		/**
		 * Here address should be MailingObjectAddress not the ClaimantAddress. 
		 */
		if(this.address != null){
		final MailObjectAddress address = this.address;
		ripObject.setAddress1(address.getAddress1());
		ripObject.setAddress2(address.getAddress2());
		ripObject.setAddress3(address.getAddress3());
		ripObject.setAddress4(address.getAddress4());
		ripObject.setAddress5(address.getAddress5());
		ripObject.setAddress6(address.getAddress6());
		ripObject.setCity(address.getCity());
		ripObject.setStateCode(address.getStateCode());
		ripObject.setZipCode(address.getZipCode().getZip());
		ripObject.setZipExt(address.getZipCode().getExt());
		ripObject.setCreatedByUser(AccountContext.getCurrentUsername());
		ripObject.setReferenceNo(this.claimant.getReferenceNo());
		ripObject.setCorrelationId(activeCall.getId());
		
		ripObject.setRegistration1(this.payToRegistration.getRegistration1());
		ripObject.setRegistration2(this.payToRegistration.getRegistration2());
		ripObject.setRegistration3(this.payToRegistration.getRegistration3());
		ripObject.setRegistration4(this.payToRegistration.getRegistration4());
		ripObject.setRegistration5(this.payToRegistration.getRegistration5());
		ripObject.setRegistration6(this.payToRegistration.getRegistration6());
		//TODO: 
		ripObject.setSpecialInstructions("");
		ripObject.setLetterCode(this.letterCode.getCode());
		ripObject.setWorkType(this.letterCode.getWorkType().getDescription());
		
		return ripObject;
		}else{
			return null;
		}
	}

	/**
	 * @param claimantId
	 * @return - Get all {@link Letter}s of given {@link Claimant} id.
	 */
	public static List<Letter> findLetterOfClaimant(Long claimantId) {
		return entityManager()
				.createNamedQuery("findLetterOfClaimant", Letter.class)
				.setParameter("claimantId", claimantId).getResultList();
	}

	public static List<Letter> findClaimFormsOfClaimant(Long claimantId) {
		return entityManager()
				.createNamedQuery(
						"findFormsOfClaimantByLetterTypeAndCureLetterType",
						Letter.class).setParameter("claimantId", claimantId)
				.setParameter("letterType1", LetterType.CLAIM_FORM)
				.setParameter("letterType2", LetterType.CLAIM_FORM_CURE_LETTER)
				.getResultList();
	}

	public static List<Letter> findOptoutFormsOfClaimant(Long claimantId) {
		return entityManager()
				.createNamedQuery(
						"findFormsOfClaimantByLetterTypeAndCureLetterType",
						Letter.class).setParameter("claimantId", claimantId)
				.setParameter("letterType1", LetterType.OPTOUT_FORM)
				.setParameter("letterType2", LetterType.OPTOUT_CURE_LETTER)
				.getResultList();
	}
	
	/**
	 * @param claimantId - The PK of the {@link Claimant}
	 * @return - The total number of {@link Letter}s belonging to the {@link Claimant} that have returned RPO_NON_FORWARDABLE and are not in cure letter range. 
	 * and require an address research.
	 */
	public static Long countLettersForAddressResaerchOfClaimant(final Long claimantId) {
		TypedQuery<Long> query = entityManager().createNamedQuery("countLettersForAddressResaerchOfClaimant", Long.class);
		query.setParameter("claimantId", claimantId);
		query.setParameter("letterRpoType", RPOType.NONFORWARDABLE);
		
		final int cureLimitStart = Event.getCurrentEvent().getCureLetterRangeStart();
		final int cureLimitEnd   = Event.getCurrentEvent().getCureLetterRangeEnd();
		
		if(cureLimitEnd > 0) {
			query.setParameter("cureLimitStart", String.valueOf(cureLimitStart));
			query.setParameter("cureLimitEnd", String.valueOf(cureLimitEnd));
		} else {
			query.setParameter("cureLimitStart", "0");
			query.setParameter("cureLimitEnd", "0");
		}
		
		return query.getSingleResult();
	}
	
	/**
	 * @param claimantId - The PK of the {@link Claimant}
	 * @return - The Date of the Last RPO Non Forwardable Letter of the claimant that is not in cure letter range. 
	 */
	public static Date getLastRpoLetterDateOfClaimant(final Long claimantId) {
		TypedQuery<Date> query = entityManager().createNamedQuery("getLastRpoLetterDateOfClaimant", Date.class);
		query.setParameter("claimantId", claimantId);
		query.setParameter("letterRpoType", RPOType.NONFORWARDABLE);
		
		final int cureLimitStart = Event.getCurrentEvent().getCureLetterRangeStart();
		final int cureLimitEnd   = Event.getCurrentEvent().getCureLetterRangeEnd();
		
		if(cureLimitEnd > 0) {
			query.setParameter("cureLimitStart", String.valueOf(cureLimitStart));
			query.setParameter("cureLimitEnd", String.valueOf(cureLimitEnd));
		} else {
			query.setParameter("cureLimitStart", "0");
			query.setParameter("cureLimitEnd", "0");
		}
		
		return query.getSingleResult();
	}
	
	public static List<Letter> findCorrespondenceOfClaimant(Long claimantId,
			String... associations) {

		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Letter> cq = cb.createQuery(Letter.class);
		Root<Letter> letter = cq.from(Letter.class);
		cq.select(letter).distinct(true);
		cq.where(
				cb.and(cb.equal(letter.<Claimant> get("claimant").<Long> get("id"), claimantId)), 
				cb.or(
						cb.equal(letter.<LetterCode> get("letterCode").<LetterType> get("letterType"), LetterType.GENERAL_CORRESPONDENCE),
						cb.equal(letter.<LetterCode> get("letterCode").<LetterType> get("letterType"), LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)
					 )
				);
		if (associations == null) {
			associations = ASSOCIATION_ALL;
		}
		for (String association : associations) {
			letter.fetch(association, JoinType.LEFT);
		}
		return entityManager().createQuery(cq).getResultList();
	}
	
	public static boolean cureLetterSendLimitExceeded(final Long claimantId) {
		final int cureLetterSendLimit = getCureLetterSendLimit();
		final int sentCureLetterCout =  getSentCureLetterCountOfClaimant(claimantId).intValue();
		return cureLetterSendLimit < sentCureLetterCout ;
	}
    
	private static int getCureLetterSendLimit() {
		// TODO : Get from event.
		return 6;
	}	
	
	public static Long getSentCureLetterCountOfClaimant(Long claimantId) {
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Letter> letter = cq.from(Letter.class);
		cq.select(cb.function("count", Long.class, letter.get("id")));
		cq.where(
				cb.equal(letter.<MailType> get("mailType"), MailType.OUTGOING),
				cb.and(cb.equal(letter.get("claimant").get("id"), claimantId)), 
				cb.and(cb.or(cb.equal(letter.<LetterCode> get("letterCode").<LetterType>get("letterType"), LetterType.CLAIM_FORM_CURE_LETTER),
						cb.equal(letter.<LetterCode> get("letterCode").<LetterType>get("letterType"), LetterType.OPTOUT_CURE_LETTER),
						cb.equal(letter.<LetterCode> get("letterCode").<LetterType>get("letterType"), LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER))));

		return entityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * @param claimantId
	 * @return - The # of {@link Letter}s for the given {@link Claimant} id.
	 */
	public static Long getLetterCountOfClaimant(Long claimantId) {
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Letter> letter = cq.from(Letter.class);
		cq.select(cb.function("count", Long.class, letter.get("id")));
		cq.where(cb.equal(letter.get("claimant").get("id"), claimantId));
		return entityManager().createQuery(cq).getSingleResult();
	}

	public static Letter findByMailingControlNo(final String mailingControlNo) {
		List<Letter> list = findLettersByMailingControlNo(mailingControlNo)
				.getResultList();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public String getStatusDescription() {
		return this.getLetterStatus() == null ? "" : this.getLetterStatus()
				.toString();
	}

	/**
	 * @param id
	 *            - The PK of the Letter.
	 * @param associations
	 *            - The names of associations that have to be eagerly loaded.
	 * @return - The Letter
	 */
	public static Letter findLetter(final Long id, String... associations) {
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Letter> cq = cb.createQuery(Letter.class);
		Root<Letter> letter = cq.from(Letter.class);
		cq.select(letter);
		cq.where(cb.and(cb.equal(letter.get("id"), id)));
		if (associations == null) {
			associations = ASSOCIATION_ALL;
		}
		for (String association : associations) {
			letter.fetch(association, JoinType.LEFT);
		}
		return entityManager().createQuery(cq).getSingleResult();
	}

	public static Letter newCorrespondence() {
		final Letter ret = new Letter();
		ret.setMailDate(new Date());
		ret.setUserId(AccountContext.getCurrentUsername());
		ret.setDescription("Correspondence");
		return ret;
	}

	/**
	 * Generate an outgoing correspondence {@link Letter} for an incoming
	 * correspondence {@link Letter} if the incoming {@link Letter} is NIGO.
	 * 
	 * @return - An outgoing correspondence {@link Letter}
	 * @throws - {@link IllegalStateException} if the {@link Letter} is not
	 *         incoming and NIGO
	 */
	public Letter newOutgoingCorrespondenceForIncomingNigo() {
		Preconditions
				.checkState(
						LetterStatus.NIGO == this.getLetterStatus(),
						"Only NIGO incoming correspondence can have an out going correspondence item. Not NIGO.");
		Preconditions
				.checkState(
						MailType.INCOMING == this.getMailType(),
						"Only NIGO incoming correspondence can have an out going correspondence item. Not Incoming.");
		Letter let = new Letter();
		let.setInResponseTo(this);
		copyCorrespondenceDocument(this, let);
		let.setMailType(MailType.OUTGOING);
		let.setUserId(AccountContext.getCurrentUsername());
		let.setMailDate(null);
		let.setMailingControlNo(this.getNextMailingControlNumber());
		let.setDescription("Cure Letter");
		return let;
	}

	private void copyCorrespondenceDocument(Letter from, Letter to) {
		to.setIgoCorrespondenceDocuments(from.getIgoCorrespondenceDocuments());
		to.setNigoCorrespondenceDocuments(from.getNigoCorrespondenceDocuments());
	}

	public String getDerivedComments() {
		String comments = "";
		if (this.getMailType() == MailType.OUTGOING) {
			comments = this.getLetterCode().getDescription() == null ? ""
					: this.getLetterCode().getDescription();
		}
		return comments;
	}

	/*
	 * The user ID  of the user who has made the change to this Letter should be saved. 
	 * The user ID can also be obtained form the audit tables. 
	 */

	@PreUpdate
	public void updateUserId() {
		this.userId = AccountContext.getCurrentUsername();
	}

	/**
	 * Method return a List of Strings which are selected as IGO Received or
	 * NIGO Missing Documents.
	 * 
	 * @param selected
	 *            IGO Correspondence Documents or NIGO Correspondence Documents
	 * @return selected IGO or NIGO Correspondence Documents in List of String
	 */

	public Collection<String> getCorrespondenceDocumentNames(
			final Set<CorrespondenceDocument> correspondenceDocuments) {
		if (correspondenceDocuments == null) {
			return null;
		}
		final List<String> ret = new ArrayList<String>();
		int lineCount = 1;
		for (CorrespondenceDocument doc : correspondenceDocuments) {
			ret.add((lineCount++) + ". " + doc.getName());
		}
		return ret;
	}

	public void setMailTypeStr(String mailType) {
		if (StringUtils.hasLength(mailType)) {
			setMailType(MailType.valueOf(mailType));
		}
	}

	public void setLetterStatusStr(String letterStatus) {
		if (StringUtils.hasLength(letterStatus)) {
			setLetterStatus(LetterStatus.valueOf(letterStatus));
		}
	}


	public void setRpoTypeStr(String rpoType) {
		if (StringUtils.hasLength(rpoType)) {
			setRpoType(RPOType.valueOf(rpoType));
		}
	}

	/**
	 * Letter Two Process If the item is still NIGO then generate a incoming
	 * mail activity object as well as an additional outgoing mail activity
	 * object
	 * 
	 * @param doc
	 * @return
	 */
	public Letter newOutgoingMailObjectForNIGO() {
		Letter letter = new Letter();
		letter.setMailType(MailType.OUTGOING);
		letter.setMailDate(null);
		letter.setMailingControlNo(this.getNextMailingControlNumber());
		letter.setInResponseTo(this);
		letter.setUserId(AccountContext.getCurrentUsername());
		letter.setIgoCorrespondenceDocuments(igoCorrespondenceDocuments);
		letter.setNigoCorrespondenceDocuments(nigoCorrespondenceDocuments);
		return letter;

	}

	public boolean isCureLetter() {
		Preconditions.checkState(this.getLetterCode() != null, "LetterCode is null");
		return this.getLetterCode().insCureLetterCode();
	}
	
	public Boolean getDstoPrintFileSentFlag() {
		return dstoPrintFileSentFlag;
	}

	public void setDstoPrintFileSentFlag(Boolean dstoPrintFileSentFlag) {
		this.dstoPrintFileSentFlag = dstoPrintFileSentFlag;
	}

	public String getMailTypeDescription() {
		return this.getMailType() == null ? "" : this.getMailType().toString();
	}

	public String getRpoTypeDescription() {
		return this.getRpoType() == null ? "" : this.getRpoType().toString();
	}

	public boolean canLetterRpoed() {
		return this.getRpoType() == null ? true : false;
	}
	
	public void markLetterRpo(final RPOType rpoType) {
    	Preconditions.checkArgument(rpoType != null, "RpoType is null");
    	if(rpoType == RPOType.FORWARDABLE ) {
    		this.setRpoType(RPOType.FORWARDABLE);
			this.setRpoDate(new Date());			
    	} else if(rpoType == RPOType.NONFORWARDABLE ) {
    		this.setRpoType(RPOType.NONFORWARDABLE);
			this.setRpoDate(new Date());			
    	}
    }
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public String getNextMailingControlNumber() {
		return dataFieldMaxValueIncrementerService.getNextMailingControlNumber();
	}

	public static long countLetter(Long claimantId,String description, LetterStatus letterStatus) {
		/*CriteriaBuilder qb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		final Root<Letter> letter = cq.from(Letter.class);
		cq.select(qb.count(letter));
		cq.where(letter.get("claimant.id").in(claimantId));
		cq.where(letter.get("letterStatus").in(letterStatus));
		cq.where(letter.get("description").in(description));
		return entityManager().createQuery(cq).getSingleResult();*/
		EntityManager em = Letter.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(o) FROM Letter AS o WHERE o.claimant.id = :claimantId and o.letterStatus = :letterStatus and o.description = :description", Long.class);
        q.setParameter("claimantId", claimantId);
        q.setParameter("letterStatus", letterStatus);
        q.setParameter("description", description);
        return q.getSingleResult();
		

	}

}
