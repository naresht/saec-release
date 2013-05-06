package com.bfds.saec.web.model;

import java.util.Date;
import java.util.List;

import org.primefaces.component.dialog.Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContextHolder;

import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.dao.PaymentDaoImpl;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.web.util.JsfUtils;

public class ReturnOfFundingViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger log = LoggerFactory.getLogger(ReturnOfFundingViewModel.class);

	private String referenceNo;

	private String paymentIdentificationNo;

	private List<Payment> payments;

	private Payment selectedPayment;

	private double priorRofAmount;

	private double rofAmount;

	private double rofInterest;

	private boolean residual;

	private Date rofDate;

	private String rofComments;

	private PaymentType paymentType;

	private boolean flag;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Autowired
	private transient PaymentDao paymentDao = new PaymentDaoImpl();

	public void reset() {
		referenceNo = null;
		paymentIdentificationNo = null;
		payments = null;
		paymentType = null;
		if(JsfUtils.getUIViewRoot() != null){
			Dialog refineSearchDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "refineSearchDialog");
			if( refineSearchDialog != null ){
				refineSearchDialog.setVisible(false);
			}
		}
		setFlag(false);
	}

	public List<Payment> loadPaymentsForReturnOfFunding(final Claimant claimant,
			final MessageContext messages) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading Payments for ROF...."));
		
		this.payments = paymentDao.loadPaymentsForReturnOfFunding(claimant.getReferenceNo(),
				paymentIdentificationNo);
		return payments;
	}

	public void loadSelectedPayment(Long id) {

		selectedPayment = Payment.findPayment(id);
		priorRofAmount = selectedPayment.getPriorReturnOfFundingAmount();
		rofDate = new Date();
		setResidual(true);
		resetRofAmounts();

	}

	/**
	 * This method is used to reset the amount and comments fields to blank when we load the ROF page second time.
	 */
	private void resetRofAmounts() {
		rofAmount=0;
		rofInterest=0;
		rofComments=null;
	}

	@Transactional
	public boolean processReturnOfFunding(final MessageContext messages) {
		if(log.isInfoEnabled())
			log.info(String.format("Processing ROF...."));
		List<String> errors = selectedPayment.canDoReturnOfFunding(rofAmount);
		if (!errors.isEmpty()) {
			addErrors(messages, errors);
			return false;
		}		
		selectedPayment = (Payment) selectedPayment.merge();
		List<Payment> rofList = selectedPayment.addReturnOfFunding(rofAmount, rofInterest,
				residual, rofComments, rofDate);
        final Payment rof = rofList.get(0);

		if(log.isInfoEnabled())
			log.info(String.format("ROF saved for payment : %s.",rof.getIdentificatonNo()));
		selectedPayment.setSkipActivityGeneration(true);
		selectedPayment.persist();
		selectedPayment.flush();
		generateRofActivity(rof);
		loadSelectedPayment(selectedPayment.getId());
		//Set the rof Payment id flowScope for use in web folw xml
		RequestContextHolder.getRequestContext().getConversationScope().put("rofPaymentId", rof.getId());
		return true;
	}

	private void generateRofActivity(Payment rof) {
		//
		CheckActivity activity = (CheckActivity) Activity
				.setActivityDefaults(new CheckActivity());
		activity.setActivityCode(ActivityCode.CREATED);
		activity.setIdentificationNo(selectedPayment.getIdentificatonNo());
		activity.setToPaymentCode(selectedPayment.getPaymentCode());
		activity.setToPaymentType(selectedPayment.getPaymentType());

		if (rofAmount == selectedPayment
				.getPaymentAmount().doubleValue()) {
			activity.setDescription("Check # "
					+ selectedPayment.getIdentificatonNo()
					+ " Return of Funding "
					+ (rof.isRofHasResidualMonies() ? " Residual" : "") + " RF " + (rof.isRofHasResidualMonies() ? " R" : ""));
			if(log.isInfoEnabled())
				log.info(String.format("Activity generated for ROF."));
		}
		else {
			activity.setDescription("Check # "
					+ selectedPayment.getIdentificatonNo()
					+ " Partial Return of Funding "
					+ (rof.isRofHasResidualMonies() ? " Residual" : "" )+ " RP " + (rof.isRofHasResidualMonies() ? " R" : ""));
			if(log.isInfoEnabled())
				log.info(String.format("Activity generated for Partial ROF."));
		}

		Claimant c = Claimant.findClaimant(selectedPayment.getPayTo().getId());
		c.addActivity(activity);
		c.persist();
		c.flush();
	}

	private void addErrors(MessageContext messages, List<String> errors) {
		for (String error : errors) {
			messages.addMessage(new MessageBuilder().error().defaultText(error)
					.build());
		}
	}

	public PaymentDao getPaymentDao() {
		return paymentDao;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getPaymentIdentificationNo() {
		return paymentIdentificationNo;
	}

	public void setPaymentIdentificationNo(String paymentIdentificationNo) {
		this.paymentIdentificationNo = paymentIdentificationNo;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public Payment getSelectedPayment() {
		return selectedPayment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public double getPriorRofAmount() {
		return priorRofAmount;
	}

	public void setPriorRofAmount(double priorRofAmount) {
		this.priorRofAmount = priorRofAmount;
	}

	public double getRofAmount() {
		return rofAmount;
	}

	public void setRofAmount(double rofAmount) {
		this.rofAmount = rofAmount;
	}

	public double getRofInterest() {
		return rofInterest;
	}

	public void setRofInterest(double rofInterest) {
		this.rofInterest = rofInterest;
	}

	public boolean isResidual() {
		return residual;
	}

	public void setResidual(boolean residual) {
		this.residual = residual;
	}

	public Date getRofDate() {
		return rofDate;
	}

	public void setRofDate(Date rofDate) {
		this.rofDate = rofDate;
	}

	public String getRofComments() {
		return rofComments;
	}

	public void setRofComments(String rofComments) {
		this.rofComments = rofComments;
	}

	public PaymentType getPaymentType() {
		paymentType = PaymentType.CHECK;
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

}
