package com.bfds.saec.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.domain.util.PaymentStatusCodesUtil;
import com.bfds.saec.rip.domain.StopReplaceCheckRipObject;
import com.bfds.saec.rip.service.RipEventListener;
import com.bfds.saec.util.ConverterUtils;
import com.bfds.saec.util.SaecDateUtils;
import com.google.common.base.Preconditions;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit = "entityManagerFactory", finders = {
		"findPaymentsByPayTo", "findPaymentsByMailingControlNo",
		"findPaymentsByPaymentCodeAndPayTo" })
@Audited
@NamedQueries({
		@NamedQuery(name = "findPaymentIdentificationNo", query = "FROM com.bfds.saec.domain.Payment AS c where c.identificatonNo = :identificatonNo"),
		@NamedQuery(name = "findCheckByAccountNoAmount", query = "FROM com.bfds.saec.domain.Payment AS c where c.paymentCalc.nettAmount = :nettAmount and c.payTo.referenceNo = :accountNo"),
		@NamedQuery(name = "findCheckByNumberAndAmount", query = "FROM com.bfds.saec.domain.Payment AS c where c.identificatonNo = :identificatonNo and c.paymentCalc.nettAmount = :nettAmount and c.paymentType = :paymentType "),
		@NamedQuery(name = "findAllCreatedStatusChecks", query = "FROM com.bfds.saec.domain.Payment AS c where (c.paymentCode = :fi or c.paymentCode = :is or c.paymentCode = :ni or c.paymentCode = :ti) and c.paymentType = :paymentType"),
		@NamedQuery(name = "findReissueApprovedChecksOlderThan", query = "FROM com.bfds.saec.domain.Payment AS c"
				+ " where (c.paymentCode =:vda or c.paymentCode = :vfa or c.paymentCode =:vna or c.paymentCode =:vha or c.paymentCode =:va or c.paymentCode =:sra or c.paymentCode =:prc)"
				+ " and c.paymentType = :paymentType and c.statusChangeDate <= :toDate"),
		@NamedQuery(name = "findAllPaymentsAssignedToTranch", query = "FROM com.bfds.saec.domain.Payment AS c where c.tranch.code = :tranchCode"),
		@NamedQuery(name = "countPaymentsForAddressResaerchOfClaimant", query = "select count(c.id) from com.bfds.saec.domain.Payment AS c where c.payTo.id = :claimantId and c.paymentCode = :vn"),
		@NamedQuery(name = "getLastRpoPaymentDateOfClaimant", query = "select max(c.paymentDate) from com.bfds.saec.domain.Payment AS c where c.payTo.id = :claimantId and c.paymentCode = :vn") })
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(Payment.class);

	@Autowired
	private transient RipEventListener ripEventListener;

	// @Column(unique=true,nullable=true)
	private String identificatonNo;

	@Embedded
	private PaymentCalc paymentCalc;

	private Boolean ifdsSent;

	private Boolean dstoCheckFileSentFlag;
	private Boolean dstoPrintFileSentFlag;

	private Date paymentDate;

	// Note: No process is setting this currently
	@Enumerated(EnumType.STRING)
	private RPOType rpoType;

	private Date rpoDate;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(nullable = false)
	private PaymentStatus paymentStatus;

	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	private Date statusChangeDate;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(nullable = false)
	private PaymentCode paymentCode;

	@ManyToOne
	// (cascade = CascadeType.MERGE)
	@JoinColumn(name = "claimant_fk")
	private Claimant payTo;

	@OneToOne(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	@BatchSize(size = 10)
	@JoinColumn(name = "address_id")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private MailObjectAddress checkAddress;

	/**
	 * The {@link Tranch} to which this {@link PaymentStatus} belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tranch_fk")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Tranch tranch;

	@Embedded
	private RegistrationLines payToRegistration;

	private String specialPullCode;

	@ManyToOne(optional = true)
    @JoinColumn(name = "payment_letter_code")
	@NotNull
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private PaymentLetterCode letterCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private GroupMailCode groupMailCode;

	private boolean auditable;

	private String comments;

	private String userId;

	private String releaseRejectResponseCode;

	// TODO: Move this out.
	private boolean rofHasResidualMonies;

	@Embedded
	private WireInfo wireInfo;

	@Transient
	private boolean skipActivityGeneration;

	/**
	 * The {@link Payment} of which this a split.
	 */
	@ManyToOne
	private Payment splitFromPayment;

	private String itemSequenceNumber;

	/**
	 * Flag to indicate a stop lift has been performed on this check. This flag
	 * is used in activity generation only.
	 */
	@Transient
	private boolean stopLifted;

	/**
	 * Flag to indicate a stop reverse has been performed on this check. This
	 * flag is used in activity generation only.
	 */
	@Transient
	private boolean stopReversed;

	/**
	 * Flag to indicate a void reverse has been performed on this check. This
	 * flag is used in activity generation only.
	 */
	@Transient
	private boolean voidReversed;

	/**
	 * A Check is stale if this date is reached and
	 * {@link Payment#getPaymentStatus() == PaymentStatus.OUTSTANDING}
	 */
	private Date staleByDate;

	/**
	 * The check is state dated.
	 */
	private Boolean staleDated;
	
	/**
	 * Flag to indicate if this {@link Payment} is sent on the tax file.
	 */
	private Boolean sentOnTaxFile;

	/**
	 * TODO: Remove this.
	 */
	private String mailingControlNo;

	// Used By Address Research
	private boolean isSendCountLimit = false;

	private int addressResearchSentCount;

	/**
	 * Flag to indicate if this {@link Payment} is sent on the banks issue-void
	 * file. This flag is not required for CREATED status {@link Payment}s. It
	 * is only required for VOIDED {@link Payment}s.
	 */
	private Boolean sentOnBankIssueVoidFile;
	
	
	/**
     * Flag to indicate if this {@link Payment} is sent on the bottom line
     */
    private Boolean sentOnBottomLineFile;

	/**
	 * Flag to indicate if this {@link Payment} is sent on the banks stop file.
	 * This flag is not required for STOP {@link Payment}s.
	 */
	private Boolean sentOnBankStopFile;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "paymentCode", column = @Column(name = "initial_paymentCode")),
			@AttributeOverride(name = "paymentStatus", column = @Column(name = "initial_paymentStatus")),
			@AttributeOverride(name = "statusChangeDate", column = @Column(name = "initial_statusChangeDate")) })
	private PaymentState initialState;
	/**
	 * The {@link Payment} for which this an ROF.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_fk", nullable = true)
	private Payment rofOf;

	/**
	 * The {@link Payment} for which this a re-issue.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_reissueof_fk", nullable = true)
	private Payment reissueOf;

	/**
	 * The {@link Payment} for which this an ROF.
	 */
	@OneToMany(mappedBy = "rofOf", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Set<Payment> returnOfFundings = new HashSet<Payment>();

	/**
	 * The {@link Payment} for which this a split.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_split_fk", nullable = true)
	private Payment splitOf;

	/**
	 * The {@link Payment} for which this an ROF.
	 */
	@OneToMany(mappedBy = "splitOf", fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Set<Payment> splits = new HashSet<Payment>();

	/**
	 * A derived field to tell that this Payment has been split. 
	 * This filed can be derived as follows
	 * 
	 * hasBeenSplit = {@link Payment#splits}.size() > 0
	 * 
	 * We have this filed even though we can derive it because we do not want to load {@link Payment#splits}
	 * every time we want to know if the Payment has been split.
	 */
	private Boolean hasBeenSplit;
	/*
	 * @deprecated - Use Check.newCheck() instead.
	 */
	@Deprecated
	public Payment() {
		this.wireInfo = new WireInfo();
		this.paymentCalc = new PaymentCalc();
	}

	/**
	 * Factory method that creates a new {@link Payment} with defaults.
	 * 
	 * @param paymentCode
	 *            - The initial value for {@link Payment#paymentCode}.
	 * @return A new {@link Payment}
	 */
	public static Payment newPayment(final PaymentCode paymentCode) {
		Preconditions.checkArgument(paymentCode != null, "paymentCode is null");
		Payment check = new Payment();
		check.setPaymentCode(paymentCode);
		check.staleByDate = Event.getCurrentEvent().getStaleDate();
		check.setInitialState(new PaymentState(check.getPaymentCode(), check
				.getPaymentStatus(), check.getStatusChangeDate()));
		return check;
	}

	public void setPaymentAmount(final BigDecimal nettAmount) {
		this.getPaymentCalc().setNettAmount(nettAmount);
	}

	public BigDecimal getPaymentAmount() {
		return this.getPaymentCalc().getNettAmount();
	}

	public void setPayTo(final Claimant payTo) {
		this.payTo = payTo;
		if (payTo == null) {
			this.setPayToRegistration(null);
		} else {
			final RegistrationLines payToRegistration = new RegistrationLines();
			payTo.getClaimantRegistration().getLines()
					.copyTo(payToRegistration);
			this.setPayToRegistration(payToRegistration);
		}
	}

	/**
	 * 
	 * @return A new {@link Payment} that can be issued in place of this
	 *         {@link Payment}.
	 */
	public Payment getNewCheckForReissue() {
		if(log.isInfoEnabled())
			log.info(String.format("Creating new Payment for reissue: id:%s, #:%s", this.getId(), this.getIdentificatonNo()));		

		if (!(canReissue() || canRplace())) {
			throw new IllegalStateException(
					String.format(
							"Cannot create a new Payment for a re-issue/replace on Payment %s with PaymentCode : %s. ",
							this.getIdentificatonNo(), this.getPaymentCode()));
		}

		if (PaymentType.CHECK != this.paymentType) {
			throw new IllegalStateException("Check must have PaymentType:"
					+ PaymentType.CHECK);
		}
		Payment newCheck = null;
		newCheck = Payment.newPayment(PaymentCode.ISSUANCE_CREATED_IS_IS);
		newCheck.setPaymentCalc(this.getPaymentCalc().clone());
		newCheck.setPaymentType(PaymentType.CHECK);
		newCheck.setPaymentDate(this.getPaymentDate());
		newCheck.setReissueOf(this);
		if (checkAddress != null) {
			newCheck.setCheckAddress(new MailObjectAddress());
			checkAddress.copyTo(newCheck.getCheckAddress());
		}
		this.getPayTo().addCheckforReisue(newCheck);
		return newCheck;

	}

	private boolean canReissue() {
		return this.paymentCode == PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA
				|| this.paymentCode == PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA
				|| this.paymentCode == PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA
				|| this.paymentCode == PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA
				|| this.paymentCode == PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA;
	}

	private boolean canRplace() {
		return this.paymentCode == PaymentCode.STOP_REPLACE_APPROVED_R_SRA
				|| this.paymentCode == PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA;
	}

	/**
	 * @param paymentStatus
	 * 
	 * @deprecated - Use {@link #setPaymentCode(PaymentCode)} instead.
	 */
	@Deprecated
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		throw new UnsupportedOperationException(
				"Payment#setPaymentStatus(PaymentStatus) should never be called. Use Payment#setPaymentCode(PaymentCode) instead.");
	}

	public void setPaymentCode(PaymentCode paymentCode) {
		Preconditions.checkArgument(paymentCode != null, "paymentCode is null");
        final PaymentCode prevPaymentCode = this.paymentCode;
		this.paymentCode = paymentCode;
		this.statusChangeDate = new Date();
		/*
		 * We do not have a PaymentStatus that corresponds to
		 * PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW and this.paymentCode ==
		 * PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW.
		 * 
		 * So we do not change Payment.paymentStatus.
		 */
		if (!(this.paymentCode == PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW || this.paymentCode == PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW)) {
			this.paymentStatus = PaymentCode
					.getPaymentStatusForCode(paymentCode);
		} else {
			Preconditions.checkState(this.paymentStatus != null, String.format(
					"PaymentStatus must not be null when settiing PaymentCode(s)"
							+ " %s and %s",
					PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW,
					PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW));
		}
		Preconditions.checkState(this.paymentStatus != null, String.format(
				"Cannot find PaymentStatus for PaymentCode : %s", paymentCode));
		updateFileFlagsOnPaymentCodeChange(prevPaymentCode, this.paymentCode);
	}

	/**
	 * When the {@link PaymentCode} changes we will have to reset flags that
	 * indicate the {@link Payment} has been sent on a certain files. This is
	 * because once the {@link PaymentCode} changes the {@link Payment} can
	 * become eligible for being sent on those files again.
	 */
	private void updateFileFlagsOnPaymentCodeChange(PaymentCode prevPaymentCode, PaymentCode newPaymentCode) {
		// As per the Bug PII-361 ifds_sent flag should not be reset if a check is reissue requested,applicable for stop checks also.
		if(!PaymentCodeUtil.getVoidReissueRequestedCodes().contains(newPaymentCode) && !PaymentCodeUtil.getStopReplaceRequestedCodes().contains(newPaymentCode))
		this.ifdsSent = false;
        // A check can be re sent on the bank issue void file only when it goes from one on the "outstanding" states to
        // one of the void or stop states. A cashed check is anyways not eligible to be sent on the issue void file.
        // so setting the flag sentOnBankIssueVoidFile= false when a check goes from one on the "outstanding" states to
        // a cashed state is harmless.
        if(PaymentCodeUtil.getOutstandingCodes().contains(prevPaymentCode)) {
            this.sentOnBankIssueVoidFile = false;
        }
	}

	/**
	 * @return true is we can void the check.
	 */
	public boolean isVoidable() {
		boolean ret = PaymentType.CHECK == this.paymentType;
		if (ret) {
			if (this.getPaymentCode() == PaymentCode.STALE_DATE_OUTSTANDING_X_X) {
				return Event.getCurrentEvent().isCanChangeStausOfStaleCheck();
			} else {
				return isOutstanding();
			}
		}
		return false;
	}

	public boolean isOutstanding() {
		return PaymentCodeUtil.getOutstandingCodes().contains(this.paymentCode);
	}

	/**
	 * @return true if this Payment is marked VOID RPO.
	 */
	public boolean isVoidRPORequested() {

		return this.getPaymentCode() == PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF
				|| this.getPaymentCode() == PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN;
	}

	public boolean isStoppable() {
		return isVoidable();
	}

	public boolean canChangeToWire() {
		return (PaymentType.CHECK == this.paymentType && this.isCreated())
				|| ((null == this.paymentType && this.isCreated()));
	}

	private boolean isCreated() {
		return PaymentCodeUtil.isCreated(this.getPaymentCode());
	}

	/**
	 * Is this payment eligible for a split.
	 * 
	 * @return
	 */
	public boolean canSplit() {
		if(this.getSplitOf() != null) {
			// Split of split not allowed.
			return false;
		}
		return (this.paymentType == PaymentType.CHECK 
				&& (this.isOutstanding() || this.isStop() || this.isVoid()))
				|| (this.paymentType == PaymentType.ROF && this.paymentCode != PaymentCode.ROF_INTEREST_PROCESSED_INT_INT);
	}

	/**
	 * Is this payment eligible for processing tax.
	 * 
	 * @return
	 */
	public boolean canProcessTax() {
		return this.paymentType == PaymentType.CHECK
				&& (this.isOutstanding() 
					|| (this.isVoid() && this.getPaymentCode() != PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO) 
					|| this.isStop())
				&& Boolean.TRUE.equals(Event.getCurrentEvent().getTaxAdministrator());
				
	}

	/**
	 * Can a request for re-issue be placed on this {@link Payment}
	 * 
	 * @return - true if a request for re-issue can be placed.
	 * 
	 *         TODO: rename method to canRequestReissue()
	 */
	public boolean isReissueable() {

		return PaymentType.CHECK == this.paymentType
				&& !Boolean.TRUE.equals(hasBeenSplit)
				&& ((isVoid() && this.getPaymentCode() != PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO) || isStop());
	}

	public boolean canDoReturnOfFunding() {
		return PaymentType.CHECK == this.paymentType && this.isCashed();
	}

	private boolean isCashed() {
		return PaymentCodeUtil.getCashedCodes().contains(this.getPaymentCode());
	}

	public boolean isReIssueApproved() {
		return canReissue();
	}

	public boolean isWireApproved() {
		return this.getPaymentCode() == PaymentCode.WIRE_APPROVED_WA_WA;
	}

	public boolean isWireRejected() {
		return this.getPaymentCode() == PaymentCode.WIRE_REJECTED_WR_WR;
	}

	public boolean isReIssueRejected() {
		return PaymentCodeUtil.isReIssueRejected(this.getPaymentCode());
	}

	public boolean canVoidOrStop() {
		return this.isVoidable() || this.isStoppable();
	}

	public boolean canUpdateWireInfo() {
		return PaymentType.WIRE == this.getPaymentType()
				&& this.getPaymentCode() == PaymentCode.WIRE_REQUESTED_W_W;
	}

	/**
	 * See {@link IRegistrationLines#getRegistrationLinesAsString(String)}
	 * 
	 */
	public String getPayToAsText(String lineSeperator) {
		StringBuilder sb = new StringBuilder();
		if (this.getPayTo() != null && this.getPayToRegistration() != null) {
			sb.append(this.getPayToRegistration().getRegistrationLinesAsString(
					lineSeperator));
		}
		sb.append(lineSeperator);
		return sb.toString();
	}

    /**
     * DO a Return-of-finding foe this {@link Payment}. ROF can be done only for Payments of type check ({@link com.bfds.saec.domain.Payment#getPaymentType()}) in one of the CASHED states.
     * @param amount - The ROF amount.
     * @param interest - The interest on ROF
     * @param residualMonies
     * @param comments
     * @param rofDate  - Date of ROF
     * @return A {@List} of ROF {@link Payment}s. The list will have either one or two Payments. The first is always the Payment for the ROF amount. The second is for the interest on ROF.
     * If there is not interest on ROF then the list will contain only one element which is the Payment for ROF amount.
     */
	public List<Payment> addReturnOfFunding(double amount, double interest,
			boolean residualMonies, String comments, Date rofDate) {
        final List<Payment> rofList = Lists.newArrayList();
		if(log.isInfoEnabled())
			log.info(String.format("Processing ROF for: id:%s, #:%s. ROF amount : %s, ROF Interest: %s", this.getId(), this.getIdentificatonNo(), amount, interest));		
		List<String> errors = canDoReturnOfFunding(amount);
		if (!errors.isEmpty()) {
			throw new IllegalStateException("Cannot perform return of funding.");
		}
		Payment c = newReturnOfFunding(amount, residualMonies, comments,
				rofDate);
		this.getReturnOfFundings().add(c);
		c.setRofOf(this);
        rofList.add(c);

		if (interest > 0) {
			c = newReturnOfFundingInterest(interest, comments, rofDate);
			this.getReturnOfFundings().add(c);
			c.setRofOf(this);
            rofList.add(c);
		}
		return rofList;
	}

	private Payment newReturnOfFunding(final double amount,
			final boolean residualMonies, final String comments,
			final Date rofDate) {
		PaymentCode paymentCode = null;
		if (amount == this.getPaymentAmount().doubleValue()) {
			if (residualMonies) {
				// c.setStatusType("RF R");
				paymentCode = PaymentCode.ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR;
			} else {
				// c.setStatusType("RF");
				paymentCode = PaymentCode.ROF_FULL_PROCESSED_RF_RF;
			}
		} else {
			if (residualMonies) {
				// c.setStatusType("RP R");
				paymentCode = PaymentCode.ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR;
			} else {
				// c.setStatusType("RP");
				paymentCode = PaymentCode.ROF_PARTIAL_PROCESSED_RP_RP;
			}
		}

		Payment c = Payment.newPayment(paymentCode);
		if (amount == this.getPaymentAmount().doubleValue()) {
			c.setPaymentCalc(this.getPaymentCalc().clone());
		} else {
			PaymentCalc newPaymentCalc = this.getPaymentCalc().clone();
			BigDecimal percentage = this.getPaymentCalc()
					.percentageOfNettAmount(new BigDecimal(amount));
			newPaymentCalc.multiply(percentage.divide(new BigDecimal(100)));
			c.setPaymentCalc(newPaymentCalc);
			// Explicitly set the payment amount again. We do not want the
			// payment amount to be coputed by %.
			c.setPaymentAmount(new BigDecimal(amount));
		}
		c.setPaymentAmount(new BigDecimal(amount));
		c.setPaymentType(PaymentType.ROF);
		c.setRofHasResidualMonies(residualMonies);
		c.setComments(comments);
		// c.setUserId(AccountContext.getCurrentUsername());
		c.setPaymentDate(new Date());
		c.setStatusChangeDate(rofDate == null ? new Date() : rofDate);

		MailObjectAddress address = new MailObjectAddress();
		if (this.getCheckAddress() != null) {
			this.getCheckAddress().copyTo(address);
		}
		c.setCheckAddress(address);
		c.setPayTo(this.getPayTo());
		return c;
	}

	private Payment newReturnOfFundingInterest(final double interest,
			final String comments, final Date rofDate) {

		Payment c = Payment
				.newPayment(PaymentCode.ROF_INTEREST_PROCESSED_INT_INT);
		c.setPaymentAmount(new BigDecimal(interest));
		c.setPaymentType(PaymentType.ROF);
		c.setComments(comments);
		// c.setUserId(AccountContext.getCurrentUsername());
		c.setPaymentDate(new Date());
		c.setStatusChangeDate(rofDate == null ? new Date() : rofDate);

		MailObjectAddress address = new MailObjectAddress();
		if (this.getCheckAddress() != null) {
			this.getCheckAddress().copyTo(address);
		}
		c.setCheckAddress(address);
		c.setPayTo(this.getPayTo());
		return c;
	}

	public List<String> canDoReturnOfFunding(double amount) {
		List<String> ret = new ArrayList<String>();
		if (amount <= 0) {
			ret.add("Please enter valid ROF Amount.");
			return ret;
		}
		double priorRofAmount = getPriorReturnOfFundingAmount();
		if (this.getPaymentAmount().doubleValue() - priorRofAmount < amount) {
			ret.add("Amount cannot be more than "
					+ (this.getPaymentAmount().doubleValue() - priorRofAmount));
		}
		return ret;
	}

	/**
	 * @return - Total of all previous ROF amounts.
	 */
	public double getPriorReturnOfFundingAmount() {
		double ret = 0;
		for (Payment c : this.getReturnOfFundings()) {
			ret += c.getPaymentAmount().doubleValue();
		}
		setPriorRofAmount(ret);
		return ret;
	}

	/**
	 * Adds a new {@link Payment} as a split of this {@link Payment}.
	 * 
	 * @param split
	 * @throws IllegalArgumentException
	 *             if the sum of all splits exceeds the actual value of this
	 *             {@link Payment}
	 */
	public void addSplit(Payment newSplit) {
		Preconditions.checkArgument(newSplit != null, "split is null");
		if (!canAddNewSplitPayment(newSplit.getPaymentCalc())) {
			throw new IllegalArgumentException(
					"Cannot add split payment. The sum of all splits exceeds the payment value.");
		}
		this.getSplits().add(newSplit);
		newSplit.setSplitOf(this);
	}

	/**
	 * Checks if the {@link PaymentCalc} can be added as a split {@link Payment}
	 * . A new split {@link Payment} can be added only if the sum of all split
	 * {@link Payment} including the new split {@link Payment} does not exceed
	 * the value of this {@link Payment}.
	 * 
	 * @param newSplitPaymentCalc
	 *            - The {@link PaymentCalc} of new split {@link PaymentCalc}
	 * @return true if newSplit can be added as a split {@link Payment} else
	 *         false.
	 */
	public boolean canAddNewSplitPayment(PaymentCalc newSplitPaymentCalc) {
		PaymentCalc sum = new PaymentCalc();
		for (Payment split : this.getSplits()) {
			sum.add(split.getPaymentCalc());
		}
		sum.add(newSplitPaymentCalc);
		return this.getPaymentCalc().compareTo(sum) >= 0;
	}

	/**
	 * @return - The last comments against this {@link PaymentStatus}. If no
	 *         comments are set {@link Payment#releaseRejectResponseCode}. TODO:
	 *         comments are overwritten. Do we need to retail a list of all
	 *         comments ?
	 */
	public String getComments() {
		return StringUtils.hasText(this.comments) ? this.comments
				: releaseRejectResponseCode;
	}

	/**
	 * The user ID of the user who has made the change to this Payment should be
	 * saved. The user ID can also be obtained form the audit tables.
	 * Set the payment date before create.
	 */
	@PrePersist
	public void prePersist() {
		this.userId = AccountContext.getCurrentUsername();
		if(paymentDate == null) {
			this.paymentDate = new Date();
		}
	}

	/**
	 * @param address
	 *            - The {@link Address} the {@link Payment} must be sent to.
	 */
	public void setCheckAddress(MailObjectAddress address) {
		this.checkAddress = new MailObjectAddress();
		address.copyTo(this.checkAddress);
		this.checkAddress.setMailingAddress(true);
	}

	public Payment doStopLift() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Stop Lift for check Id : %s", this.getId()));		
		this.setStopLifted(true);
		this.setPaymentCode(PaymentCode.STOP_LIFT_OUTSTANDING_L_L);
		return this;
	}

	public void voidCheck() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Void for check Id : %s", this.getId()));		
		if (!this.isVoidable()) {
			throw new IllegalStateException(String.format(
					"Cannot void Check : %s , with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		this.setPaymentCode(PaymentCode.VOID_VOIDED_V_V);
		// this.setUserId(AccountContext.getCurrentUsername());
	}

	public void noReissueVoidCheck() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Void No Re-issue for Check Id : %s", this.getId()));		
		if (!this.isVoidable()) {
			throw new IllegalStateException(String.format(
					"Cannot void Check : %s , with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		this.setPaymentCode(PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO);
		// this.setUserId(AccountContext.getCurrentUsername());
	}

	public void damascoVoidCheck() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Domasco Void for check Id : %s", this.getId()));				
		if (!this.isVoidable()) {
			throw new IllegalStateException(String.format(
					"Cannot void Check : %s , with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		this.setPaymentCode(PaymentCode.VOID_DAMASCO_VOIDED_VD_VD);
		// this.setUserId(AccountContext.getCurrentUsername());
	}

	public void holdVoidCheck() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Void Hold for check Id : %s ", this.getId()));				
		if (!this.isVoidable()) {
			throw new IllegalStateException(String.format(
					"Cannot void Check : %s , with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		this.setPaymentCode(PaymentCode.VOID_HOLD_VOIDED_VH_VH);
		// this.setUserId(AccountContext.getCurrentUsername());
	}

	/**
	 * Has a Void been placed on this Payment.
	 * 
	 * @return
	 */
	public boolean isVoid() {
		return PaymentCodeUtil.isVoid(this.getPaymentCode());
	}

	/**
	 * Has a Stop been placed on this Payment.
	 * 
	 * @return
	 */
	public boolean isStop() {
		return PaymentCodeUtil.isStop(this.getPaymentCode());
	}

	public void stopCheck() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Stop for check Id : %s", this.getId()));		
		if (!this.isStoppable()) {
			throw new IllegalStateException(String.format(
					"Cannot stop Check : %s, with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		this.setPaymentCode(PaymentCode.STOP_STOP_REQUESTED_S_SR);
	}

	public void damascoStopCheck() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Domasco Stop for check Id : %s", this.getId()));				
		if (!this.isStoppable()) {
			throw new IllegalStateException(String.format(
					"Cannot stop Check : %s, with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		this.setPaymentCode(PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR);
	}

	/**
	 * @param address
	 * @return
	 * 
	 *         TODO: Rename method to requestCheckReissue()
	 */
	public boolean reissueCheck(final MailObjectAddress address) {
		if(log.isInfoEnabled())
			log.info(String.format("Requesting reissue forcheck Id  :%s", this.getId()));		
		this.setCheckAddress(address);
		if (this.isStop()) {
			replaceCheck();
		} else if (this.isVoid()) {
			if (PaymentCode.VOID_NO_REISSUE_VOIDED_VO_VO == this
					.getPaymentCode()) {
				throw new IllegalStateException(
						String.format(
								"Cannot request ressiue for Check %s with paymentCode %s",
								this.getIdentificatonNo(),
								this.getPaymentCode()));
			} else if (PaymentCode.VOID_DAMASCO_VOIDED_VD_VD == this
					.getPaymentCode()) {
				this.setPaymentCode(PaymentCode.VOID_DAMASCO_RE_ISSUE_REQUESTED_I_VDR);
			} else if (PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF == this
					.getPaymentCode()) {
				this.setPaymentCode(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR);
			} else if (PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN == this
					.getPaymentCode()) {
				this.setPaymentCode(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_REQUESTED_I_VNR);
			} else if (PaymentCode.VOID_HOLD_VOIDED_VH_VH == this
					.getPaymentCode()) {
				this.setPaymentCode(PaymentCode.VOID_HOLD_RE_ISSUE_REQUESTED_I_VHR);
			} else if (PaymentCode.VOID_VOIDED_V_V == this.getPaymentCode()) {
				this.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
			}
		} else {
			throw new IllegalStateException(
					String.format(
							"Unsupported PaymentCode %s for a ressue request on Check %s",
							this.getPaymentCode(), this.getIdentificatonNo()));
		}
		final StopReplaceCheckRipObject ripObject = createStopReplaceCheckRipObject();
		if (ripObject != null) {
			ripEventListener.stopReplaceCheckRequested(ripObject);
		}
		return true;
	}

	public Payment reissueCheckAsWire(BigDecimal wireTransferAmount) {
		if(log.isInfoEnabled())
			log.info(String.format("Requesting Check as wire for check Id : %s", this.getId()));		
		if (this.isCreated()) {
			this.setPaymentType(PaymentType.WIRE);
			this.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
			// Set the wireTransferAmount as the paymentAmount.
			this.setPaymentAmount(wireTransferAmount);
			return this;
		}

		if (this.isStoppable() || this.isStop()) {
			this.setPaymentCode(PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW);
		} else if (this.isReissueable()) {
			this.setPaymentCode(PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW);
		} else {
			throw new IllegalStateException("Cannot wire check: "
					+ this.getIdentificatonNo() + " with PaymentCode : "
					+ this.getPaymentCode());
		}
		final Payment wire = Payment.newPayment(PaymentCode.WIRE_REQUESTED_W_W);
		wire.setPaymentType(PaymentType.WIRE);
		wire.setPayTo(this.getPayTo());
		wire.setReissueOf(this);
		wire.setPaymentCalc(this.getPaymentCalc().clone());
		// Set the wireTransferAmount as the paymentAmount of the Wire.
		wire.setPaymentAmount(wireTransferAmount);
		wire.setWireInfo(this.getWireInfo());
		this.setWireInfo(new WireInfo());
		return wire;
	}

	private void replaceCheck() {
		if(log.isInfoEnabled())
			log.info(String.format("Requesting replace for check Id : %s", this.getId()));		
		if (this.getPaymentCode() == PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR) {
			this.setPaymentCode(PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR);
		} else if (this.getPaymentCode() == PaymentCode.STOP_STOP_REQUESTED_S_SR) {
			this.setPaymentCode(PaymentCode.STOP_REPLACE_REQUESTED_R_SRR);
		} else {
			throw new IllegalStateException(
					String.format(
							"Unsupported PaymentCode %s for a stop replace request on Check %s",
							this.getPaymentCode(), this.getIdentificatonNo()));
		}
	}

	public void stopReplaceCheckWithoutRip() { 
		stopReplaceCheck(false);
	}
	
	/**
	 * Used only from WSS and IVR to approve a stop directly
	 */
	public void stopReplaceCheckWithDirectApproval() {
		if(log.isInfoEnabled())
			log.info(String.format("Processing Stop with direct approval for check Id : %s", this.getId()));		
		if (!this.isStoppable()) {
			throw new IllegalStateException(String.format(
					"Cannot stop Check : %s, with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		this.setPaymentCode(PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
	}
	
	public void stopReplaceCheck() { 
		stopReplaceCheck(true);
	}
	
	private void stopReplaceCheck(boolean rip) {
		if(log.isInfoEnabled())
			log.info(String.format("Requesting stop replace for check Id : %s", this.getId()));		
		if (!this.isStoppable()) {
			throw new IllegalStateException(String.format(
					"Cannot replace Check : %s, with PaymentCode: %s",
					this.getIdentificatonNo(), this.getPaymentCode()));
		}
		stopCheck();
		replaceCheck();
		if(rip) {
			final StopReplaceCheckRipObject ripObject = createStopReplaceCheckRipObject();
			if (ripObject != null) {
				if(log.isInfoEnabled())
					log.info(String.format("Sending RIP on Stop Replace for check Id : %s", this.getId()));				
				ripEventListener.stopReplaceCheckRequested(ripObject);
			}
		}
	}

	/**
	 * @return A new StopReplaceCheckRipObject for this {@link Payment}.
	 */
	protected StopReplaceCheckRipObject createStopReplaceCheckRipObject() {
		final String mailedDate=ConverterUtils.getFormattedString(this.paymentDate, "MM/dd/yyyy");
		PhoneCall activeCall = this.getPayTo().findCallInProgess();
		if (activeCall != null) {
			final StopReplaceCheckRipObject ripObject = new StopReplaceCheckRipObject();
			// ripObject.setCorrelationId(this.get)
			ripObject.setReferenceNo(this.getPayTo().getReferenceNo());
			ripObject.setCorrelationId(activeCall.getId());
			ripObject.setPaymentIdentificationNo(this.getIdentificatonNo());
			ripObject.setRegistration1(this.payToRegistration.getRegistration1());
			ripObject.setRegistration2(this.payToRegistration.getRegistration2());
			ripObject.setRegistration3(this.payToRegistration.getRegistration3());
			ripObject.setRegistration4(this.payToRegistration.getRegistration4());
			ripObject.setRegistration5(this.payToRegistration.getRegistration5());
			ripObject.setRegistration6(this.payToRegistration.getRegistration6());
			ripObject.setGrossAmount(this.paymentCalc.getGrossAmount());
			ripObject.setMailedDate(mailedDate);
			ripObject.setCreatedByUser(AccountContext.getCurrentUsername());			
			return ripObject;
		}
		return null;

	}

	/**
	 * In case of StopVoidReverse process updates the PaymentCode as the
	 * Payment's previous paymentCode
	 * 
	 * @param paymentCodePriorToStopOrVoid
	 * @return A Payment object with updated PaymentCode.
	 */
	public Payment doStopVoidReverse(PaymentCode paymentCodePriorToStopOrVoid) {
		if(!canPerformStopVoidReverse()) {
			throw new IllegalStateException(String.format("Check # %s not eligible for stop/void reverse.", this.getIdentificatonNo()) );
		}
		if(log.isInfoEnabled())
			log.info(String.format("Processign Stop-Void reverse for check Id : %s", this.getId()));		
		if (this.isStop()) {
			this.setStopReversed(true);
			this.setPaymentCode(paymentCodePriorToStopOrVoid);
		} else if (isVoid()) {
			this.setVoidReversed(true);
			this.setPaymentCode(PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL);
		}
		return this;
	}
	
	/**
	 * Can a stop or void reverse be performed on this payment. 
	 * A stop reverse can be performed if the Payment is in a valid stop state and has not yet been sent on the stop file to the bank.   
	 * A void reverse can be performed if the Payment is in a valid void state.
	 * @return true if the a stop or void reverse be performed on this payment.
	 */
	private boolean canPerformStopVoidReverse() {
		if (this.isStop() && !Boolean.TRUE.equals(this.sentOnBankStopFile) ) {
			return true;
		} else if (isVoid()) {
			return true;
		}
		return false;
	}

	public void markStaleDated() {
		if(log.isInfoEnabled())
			log.info(String.format("Stale dating check Id : %s", this.getId()));		
		this.setStaleDated(Boolean.TRUE);
		this.setPaymentCode(PaymentCode.STALE_DATE_OUTSTANDING_X_X);
	}

	public void updateActivity(final CheckActivity activity) {
		activity.setToPaymentType(this.getPaymentType());
		activity.setIdentificationNo(this.getIdentificatonNo());
		activity.setToPaymentCode(this.getPaymentCode());
		if (this.isStopLifted()) {
			activity.setComments("Stop lifted");
		} else if (this.isStopReversed()) {
			activity.setComments("Stop reversed");
		} else if (this.isVoidReversed()) {
			activity.setComments("Void reversed");
		} else if (activity.getFromPaymentCode() != activity.getToPaymentCode()
				&& activity.getToPaymentCode() == PaymentCode.STALE_DATE_OUTSTANDING_X_X) {
			activity.setComments("System scan");
		}
	}

	/**
	 * @deprecated - Use getStatusDescription()
	 */
	@Deprecated
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getPaymentStatus() == null ? "" : this
				.getPaymentStatus());
		return sb.toString();

	}

	public String getStatusDescription() {
		return PaymentStatusCodesUtil.getDescription(this.getPaymentCode());

	}

	public static List<Payment> findReissueApprovedChecksOlderThan(
			final int days) {
		final Date date = SaecDateUtils.getDaysFromCurrent(-days);
		if(log.isInfoEnabled())
			log.info(String.format("Finding reissue approved checks older than %s days. Date : %s ", days, date));		
		TypedQuery<Payment> query = entityManager().createNamedQuery(
				"findReissueApprovedChecksOlderThan", Payment.class);
		query.setParameter("vda",
				PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA);
		query.setParameter("vfa",
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA);
		query.setParameter("vna",
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
		query.setParameter("vha", PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA);
		query.setParameter("va", PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		query.setParameter("sra", PaymentCode.STOP_REPLACE_APPROVED_R_SRA);
		query.setParameter("prc", PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA);

		query.setParameter("paymentType", PaymentType.CHECK);
		query.setParameter("toDate", date);
		return query.getResultList();
	}

	public static List<Payment> findAllCreatedStatusChecks() {
		if(log.isInfoEnabled()) {
			log.info(String.format("Finding all created status checks."));
		}
		TypedQuery<Payment> query = entityManager().createNamedQuery(
				"findAllCreatedStatusChecks", Payment.class);
		query.setParameter("fi", PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		query.setParameter("is", PaymentCode.ISSUANCE_CREATED_IS_IS);
		query.setParameter("ni", PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		query.setParameter("ti", PaymentCode.TRANCHE_ITEM_CREATED_TI_TI);
		query.setParameter("paymentType", PaymentType.CHECK);
		return query.getResultList();
	}

	public static List<Payment> findAllPaymentsAssignedToTranch(
			final String tranchCode) {
		TypedQuery<Payment> query = entityManager().createNamedQuery(
				"findAllPaymentsAssignedToTranch", Payment.class);
		query.setParameter("tranchCode", tranchCode);
		return query.getResultList();
	}

	/**
	 * @param claimantId
	 *            - The PK of the {@link Claimant}
	 * @return - The total number of {@link Payment}s belonging to the
	 *         {@link Claimant} that have returned RPO_NON_FORWARDABLE and
	 *         require an address research.
	 */
	public static Long countPaymentsForAddressResaerchOfClaimant(
			final Long claimantId) {
		if(log.isInfoEnabled())
			log.info(String.format("Counting Payments for Address research of Claimant %s.", claimantId));		
		TypedQuery<Long> query = entityManager().createNamedQuery(
				"countPaymentsForAddressResaerchOfClaimant", Long.class);
		query.setParameter("claimantId", claimantId);
		query.setParameter("vn",
				PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);

		return query.getSingleResult();
	}

	/**
	 * @param claimantId
	 *            - The PK of the {@link Claimant}
	 * @return - The Date of the Last RPO Non Forwardable {@link Payment} of the
	 *         claimant
	 */
	public static Date getLastRpoPaymentDateOfClaimant(final Long claimantId) {
		if(log.isInfoEnabled())
			log.info(String.format("Finding last RPO date of Payments of Claimant %s.", claimantId));		
		TypedQuery<Date> query = entityManager().createNamedQuery(
				"getLastRpoPaymentDateOfClaimant", Date.class);
		query.setParameter("claimantId", claimantId);
		query.setParameter("vn",
				PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);

		return query.getSingleResult();
	}

	/**
	 * @param identificatonNo
	 *            - The {@link Payment}s identificationNo -
	 *            {@link Payment#getIdentificatonNo()}
	 * @param amount
	 *            - The {@link Payment}s payment amount -
	 *            {@link Payment#getPaymentAmount()}
	 * @return - A {@link Payment} with the given identificatonNo and amount.
	 */
	public static Payment findCheckByNumberAndAmount(
			final String identificatonNo, double amount) {
		if(log.isInfoEnabled())
			log.info(String.format("Finding Check by #:%s and amount :%s", identificatonNo, amount));		
		TypedQuery<Payment> query = entityManager().createNamedQuery(
				"findCheckByNumberAndAmount", Payment.class);
		query.setParameter("identificatonNo", identificatonNo);
		query.setParameter("nettAmount", BigDecimal.valueOf(amount));
		query.setParameter("paymentType", PaymentType.CHECK);
		try {
			return query.getSingleResult();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * @param identificatonNo
	 *            - The {@link Payment}s identificationNo -
	 *            {@link Payment#getIdentificatonNo()}
	 * @return - A {@link Payment} with the given identificatonNo.
	 */
	public static Payment findPaymentIdentificationNo(
			final String identificatonNo) {
		if(log.isInfoEnabled())
			log.info(String.format("Finding Check by #:%s", identificatonNo));		
		TypedQuery<Payment> query = entityManager().createNamedQuery(
				"findPaymentIdentificationNo", Payment.class);
		query.setParameter("identificatonNo", identificatonNo);
		try {
			return query.getSingleResult();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * @param fundNo
	 *            - {@link Claimant#getFundAccountNo()}
	 * @param accountNo
	 *            - {@link Claimant#getReferenceNo()}
	 * @param amount
	 *            - The payment amount of the {@link Payment}
	 * @return - A {@link Payment} with the given fundNo, accountNo and amount.
	 *         Note: There can be many {@link Payment}s for the given values.
	 
	public static List<Payment> findChecks(String fundNo, String accountNo,
			double amount) {
		if(log.isInfoEnabled())
			log.info(String.format("Finding Check by fund#:%s, accountNo:%s, and amount:%s", fundNo, accountNo, amount));		
		TypedQuery<Payment> query = entityManager().createNamedQuery(
				"findCheckByFundNoAccountNoAmount", Payment.class);
		query.setParameter("accountNo", accountNo);
		query.setParameter("fundNo", fundNo);
		query.setParameter("nettAmount", new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP));

		return query.getResultList();
	}
	*/
	
	/**
	 * @param accountNo
	 *            - {@link Claimant#getReferenceNo()}
	 * @param amount
	 *            - The payment amount of the {@link Payment}
	 * @return - A {@link Payment} with the given accountNo and amount.
	 *         Note: There can be many {@link Payment}s for the given values.
	 *  This method is used by SS BottomLine Inbound Batch Job
	 */
	 public static List<Payment> findChecksByAccountNoAndNettAmount(String accountNo,double amount) {
	    if(log.isInfoEnabled())
	      log.info(String.format("Finding Check by accountNo:%s, and amount:%s", accountNo, amount));    
	    TypedQuery<Payment> query = entityManager().createNamedQuery(
	        "findCheckByAccountNoAmount", Payment.class);
	    query.setParameter("accountNo", accountNo);
	    query.setParameter("nettAmount", new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP));
	
	    return query.getResultList();
	}

	public boolean canPaymentRpoed() {
		return this.isOutstanding();
	}

	private void markPaymentRpo(final RPOType rpoType) {
		Preconditions.checkArgument(rpoType != null, "rpoType is null");
		this.setPaymentRpoFlag(true);
		this.setRpoType(rpoType);
		if (this.getRpoDate() == null) {
			this.setRpoDate(new Date());
		}

	}

	public void markPaymentRpoForwardable(final Boolean reissue) {
		markPaymentRpo(RPOType.FORWARDABLE);

		if (this.isOutstanding()) {
			if (reissue) {
				this.setPaymentCode(PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_REQUESTED_I_VFR);
			} else {
				this.setPaymentCode(PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF);
			}
		}
	}

	public void markPaymentRpoNonForwardable() {
		markPaymentRpo(RPOType.NONFORWARDABLE);

		if (this.isOutstanding()) {
			this.setPaymentCode(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
		}
	}

	/**
	 * @return - A unique check identification#.
	 */
	@Transactional
	@Deprecated
	public static String generateNewCheckNo() {
		String ret = null;
		Event e = Event.getCurrentEvent();
		ret = String.valueOf(e.getNextCheckSequenceNumber());
		e.persist();
		return ret;
	}

	/**
	 * @param mailingControlNo
	 *            - The check#
	 * @return
	 * 
	 * @deprecated - use {@link Payment#findPaymentIdentificationNo(String)}
	 *             instead. The mailingControl# fro a {@link Payment} is its
	 *             {@link #identificatonNo}
	 */
	@Deprecated
	public static Payment findByMailingControlNo(final String mailingControlNo) {
		return findPaymentIdentificationNo(mailingControlNo);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("IdentificatonNo: ").append(getIdentificatonNo())
				.append(", ");
		sb.append("PaymentAmount: ").append(getPaymentAmount()).append(", ");
		sb.append("PaymentCode: ").append(getPaymentCode()).append(", ");
		sb.append("PaymentDate: ").append(getPaymentDate()).append(", ");
		sb.append("PaymentStatus: ").append(getPaymentStatus()).append(", ");
		sb.append("PaymentType: ").append(getPaymentType()).append(", ");		
		sb.append("RpoType: ").append(getRpoType()).append(", ");
		sb.append("StatusChangeDate: ").append(getStatusChangeDate())
				.append(", ");
		return sb.toString();
	}

	public void setPaymentTypeStr(String paymentType) {
		if (StringUtils.hasLength(paymentType)) {
			setPaymentType(PaymentType.valueOf(paymentType));
		}
	}

	public void setRpoTypeStr(String rpoType) {
		if (StringUtils.hasLength(rpoType)) {
			setRpoType(RPOType.valueOf(rpoType));
		}
	}

	public void setPaymentCodeStr(String paymentCode) {
		if (StringUtils.hasLength(paymentCode)) {
			setPaymentCode(PaymentCode.valueOf(paymentCode));
		}
	}

	public RipEventListener getRipEventListener() {
		return ripEventListener;
	}

	public void setRipEventListener(RipEventListener ripEventListener) {
		this.ripEventListener = ripEventListener;
	}

	@Deprecated
	/*
	 * @deprecated - use rpoType
	 */
	private boolean paymentRpoFlag;

	@Deprecated
	/*
	 * @deprecated - no alternative.
	 */
	private double priorRofAmount;


}