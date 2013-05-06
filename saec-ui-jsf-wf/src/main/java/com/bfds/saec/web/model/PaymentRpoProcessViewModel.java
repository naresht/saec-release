package com.bfds.saec.web.model;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.dao.EntityAuditDao;
import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.dao.PaymentDaoImpl;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.audit.RevisionInfo;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.domain.util.PaymentCodeUtil;

public class PaymentRpoProcessViewModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	final Logger log = LoggerFactory.getLogger(PaymentRpoProcessViewModel.class);

	private String checkNumber;

	private Payment payment;

	private List<Payment> payments;

	private ClaimantAddress address;

	private RevisionInfo addressRevisionInfo;

	private Claimant claimant;

	private boolean rpoTypeFlag;

	private boolean showAddressDetails;

	private Payment selectedPayment;

	private String rpoType;

	private boolean showDate;

	@Autowired
	private transient EntityAuditDao entityAuditDaoDao;

	// Flag to indicate if an RPO forwardable required a re-issue.
	private boolean rpoForwardableReissueRequired;

	@Autowired
	private transient PaymentDao paymentDao = new PaymentDaoImpl();

	private String paymentIdentificationNo;

	private Date paymentStatusChangeDate;

	public void reset() {
		payment = new Payment();
		checkNumber = null;
		showAddressDetails = false;
	}

	public boolean loadPaymentByControlNumber(
			final MessageContext messageContext) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading payments for RPO Process...."));
		setRpoType(null);
		setShowDate(false);
		payment = findPaymentByCheckNumber(checkNumber);

		if (payment == null) {
			messageContext
					.addMessage(new MessageBuilder()
							.error()
							.defaultText(
									"Invalid check number, please enter the correct check number")
							.build());
			return false;
		}

		claimant = Claimant.findClaimant(payment.getPayTo().getId(),
				Claimant.ASSOCIATION_ADDRESSES);
		paymentStatusChangeDate = payment.getStatusChangeDate();
		loadClaimantAddress();
		if (rpoType != null) {
			showAddressDetails = RPOType.FORWARDABLE == RPOType
					.valueOf(rpoType);
		}
		return true;

	}

	private boolean loadClaimantAddress() {
		address = claimant.getMailingAddress();
		addressRevisionInfo = entityAuditDaoDao.getLastRevisionInfo(
				ClaimantAddress.class, address.getId());
		return true;
	}

	private Payment findPaymentByCheckNumber(final String checkNumber) {
		return Payment.findByMailingControlNo(checkNumber);
	}

	public ClaimantAddress getClaimantAddress() {
		return address;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Transactional
	public boolean save(final MessageContext messageContext) {
		if (!valid(messageContext)) {
			return false;
		}
		Payment c=Payment.findPayment(payment.getId());
		c.setComments(payment.getComments());
		c.setPaymentRpoFlag(true);
		
		paymentStatusChangeDate = payment.getStatusChangeDate();
		c.setRpoDate(paymentStatusChangeDate);
		if (rpoType!=null&&RPOType.valueOf(rpoType) == RPOType.FORWARDABLE) {
			c.markPaymentRpoForwardable(rpoForwardableReissueRequired);
			if(log.isInfoEnabled())
				log.info(String.format("Marking RPO Forwardable for check : %s in save method.",c.getIdentificatonNo()));
		} else {
			c.markPaymentRpoNonForwardable();
			if(log.isInfoEnabled())
				log.info(String.format("Marking RPO Non-Forwardable for check : %s in save method.",c.getIdentificatonNo()));
		}

		// Update status change data. We need to do this after the
		// payment.markPaymentRpo()
		c.setStatusChangeDate(paymentStatusChangeDate);

		if (c.getRpoType() == RPOType.FORWARDABLE) {
			address = address.merge();
			address.persist();
			c.setCheckAddress(new MailObjectAddress());
			address.copyTo(c.getCheckAddress());
		}
		c = c.merge();
		c.persist();
		if(log.isInfoEnabled())
			log.info(String.format("Check %s is saved.",c.getIdentificatonNo()));
		payment=c;
		return true;
	}

	@Transactional
	public boolean saveSelectedPayment(final MessageContext messageContext) {
		if (!valid(messageContext)) {
			return false;
		}
		Payment selected_payment=Payment.findPayment(payment.getId());
		selected_payment.setComments(payment.getComments());
		selected_payment.setPaymentRpoFlag(true);

		paymentStatusChangeDate = payment.getStatusChangeDate();
		selected_payment.setRpoDate(paymentStatusChangeDate);
		if (payment.getRpoType() == RPOType.FORWARDABLE) {
			selected_payment.markPaymentRpoForwardable(rpoForwardableReissueRequired);
			if(log.isInfoEnabled())
				log.info(String.format("Marking RPO Forwardable for check : %s in saveSelectedPayment method.",selected_payment.getIdentificatonNo()));
		} else {
			selected_payment.markPaymentRpoNonForwardable();
			if(log.isInfoEnabled())
				log.info(String.format("Marking RPO Non-Forwardable for check : %s in saveSelectedPayment method.",selected_payment.getIdentificatonNo()));
		}

		// Update status change data. We need to do this after the
		// payment.markPaymentRpo()
		selected_payment.setStatusChangeDate(paymentStatusChangeDate);
		if (selected_payment.getRpoType() == RPOType.FORWARDABLE) {
			address = address.merge();
			address.persist();
			selected_payment.setCheckAddress(new MailObjectAddress());
			address.copyTo(selected_payment.getCheckAddress());
		}
		selected_payment = selected_payment.merge();
		selected_payment.persist();
		if(log.isInfoEnabled())
			log.info(String.format("Check %s is saved.",selected_payment.getIdentificatonNo()));
		payment=selected_payment;
		return true;
	}

	private boolean valid(final MessageContext messageContext) {
		if (!PaymentCodeUtil.getOutstandingCodes().contains(payment.getPaymentCode())
				&& rpoForwardableReissueRequired) {
			messageContext.addMessage(new MessageBuilder()
					.error()
					.defaultText(
							"Can re-issue only if the Check is an Outstanding status."
									+ this.getClaimant().getReferenceNo())
					.build());
			return false;
		}
		return true;
	}

	public List<Payment> loadClaimantPaymentsForRPO(final Claimant claimant,
			final MessageContext messages) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading Claimant Payments for RPO Process..."));
		this.payments = paymentDao.loadPaymentsForRPO(
				claimant.getReferenceNo(), paymentIdentificationNo);
		return payments;
	}

	public boolean loadSelectedPayment(final Long id) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading Selected Payment for RPO Process..."));
		selectedPayment = Payment.findPayment(id);
		payment = findPaymentByCheckNumber(selectedPayment.getIdentificatonNo());
		payment.setRpoType(null);
		setShowDate(false);
		claimant = Claimant.findClaimant(payment.getPayTo().getId(),
				Claimant.ASSOCIATION_ADDRESSES);
		loadClaimantAddress();
		if (payment.getRpoType() != null) {
			showAddressDetails = RPOType.FORWARDABLE == payment.getRpoType();
		}
		return true;
	}

	public void onChangeRpoType() {
		RPOType rpoTypeEnum = rpoType != null ? RPOType.valueOf(rpoType) : null;

		if (rpoTypeEnum != null) {
			showAddressDetails = (rpoTypeEnum == RPOType.FORWARDABLE);
			setShowDate(true);
			payment.setStatusChangeDate(new Date());
		} else {
			setShowDate(false);
			showAddressDetails = false;
			payment.setStatusChangeDate(paymentStatusChangeDate);
		}
	}

	public void onChangeRpoTypeOfSelectedPayment() {
		RPOType rpoTypeEnum = payment.getRpoType();

		if (rpoTypeEnum != null) {
			showAddressDetails = (rpoTypeEnum == RPOType.FORWARDABLE);
			setShowDate(true);
			payment.setStatusChangeDate(new Date());
		} else {
			setShowDate(false);
			showAddressDetails = false;
			payment.setStatusChangeDate(paymentStatusChangeDate);
		}
	}

	public boolean isOutstandingOrVoidRPO() {
		return payment.isOutstanding() /*|| payment.isVoidRPORequested()*/;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public ClaimantAddress getAddress() {
		return address;
	}

	public void setAddress(ClaimantAddress address) {
		this.address = address;
	}

	public Claimant getClaimant() {
		return claimant;
	}

	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	public boolean isRpoTypeFlag() {
		return rpoTypeFlag;
	}

	public void setRpoTypeFlag(boolean rpoTypeFlag) {
		this.rpoTypeFlag = rpoTypeFlag;
	}

	public boolean isShowAddressDetails() {
		return showAddressDetails;
	}

	public void setShowAddressDetails(boolean showAddressDetails) {
		this.showAddressDetails = showAddressDetails;
	}

	public RevisionInfo getAddressRevisionInfo() {
		return addressRevisionInfo;
	}

	public void setAddressRevisionInfo(RevisionInfo addressRevisionInfo) {
		this.addressRevisionInfo = addressRevisionInfo;
	}

	public boolean isRpoForwardableReissueRequired() {
		return rpoForwardableReissueRequired;
	}

	public void setRpoForwardableReissueRequired(
			boolean rpoForwardableReissueRequired) {
		this.rpoForwardableReissueRequired = rpoForwardableReissueRequired;
	}

	public Payment getSelectedPayment() {
		return selectedPayment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public String getPaymentIdentificationNo() {
		return paymentIdentificationNo;
	}

	public void setPaymentIdentificationNo(String paymentIdentificationNo) {
		this.paymentIdentificationNo = paymentIdentificationNo;
	}

	public String getRpoType() {
		return rpoType;
	}

	public void setRpoType(String rpoType) {
		this.rpoType = rpoType;
	}

	public boolean isShowDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public Date getPaymentStatusChangeDate() {
		return paymentStatusChangeDate;
	}

	public void setPaymentStatusChangeDate(Date paymentStatusChangeDate) {
		this.paymentStatusChangeDate = paymentStatusChangeDate;
	}

}
