package com.bfds.saec.web.model;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.web.service.ClaimantService;
import com.bfds.saec.web.util.JsfUtils;
import com.google.common.base.Preconditions;

/**
 * 
 * A view model for splitting a {@link Payment} of a {@link Claimant} to one of it's alternate payee {@link Claimant}s.
 *
 */
public class PaymentSplitViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	final Logger log = LoggerFactory.getLogger(PaymentSplitViewModel.class);
	
	@Autowired
	private transient ClaimantService claimantService;
		
	/**
	 * The % by which to split the actual payment. This value is bound to the UI. 
	 */
	private int percentageToSplitBy;
	/**
	 * The {@link PaymentType} of the nee split {@link PaymentType}. This value is bound to the UI.  
	 */	
	private String newPaymentType;
	
	private String voidOrStopExistingPayment;
		
	
	public boolean hasAlternatePayees(Long claimantId) {
		return getAlternatePayeeList(claimantId).size() > 0;
	}
	/**
	 * Finds the alternate payee {@link Claimant} for the given alternatePayeeId. If alternatePayeeId is null then a new {@link Claimant} is created.
	 * The found/created {@link Claimant} is wrapped inside a {@link ClaimantViewModel}. 
	 * 
	 * @param alternatePayeeId - The alternate payee {@link Claimant} id. Can be null.
	 * @param parentClaimant - The parent {@link Claimant} to which alternatePayeeId is child.
	 * @param actualPayment - The {@link Payment} being split.
	 * @return {@link ClaimantViewModel}
	 * 
	 * @throws IllegalArgumentException If any of the following is true
	 * 1. AlternatePayeeId is not a child of parentClaimant as payment can only be split to child {@link Claimant}s of parentClaimant. 
	 * 2. The {@link Payment} being split does not belong to the parentClaimant. 	 
	 */
	public ClaimantViewModel newAlternatePayeeViewModel(Long alternatePayeeId, Claimant parentClaimant, Payment actualPayment) {
		Preconditions.checkArgument(parentClaimant != null, "parentClaimant is null");
		Preconditions.checkArgument(actualPayment != null, "actualPayment is null");
		
		if(alternatePayeeId != null) {
			Claimant alternatePayee = findClaimant(alternatePayeeId);
			Preconditions.checkState(alternatePayee.getParentClaimant().getId().equals(parentClaimant.getId()), 
					"The alternat payee with id : %s is not child of the parent claimant with id %s .", alternatePayee.getId(), parentClaimant.getId());
			
			Preconditions.checkState(alternatePayee.getParentClaimant().getId().equals(actualPayment.getPayTo().getId()), 
					"The Alternat payee id : %s and the Payment id : %s being split do not belong to the same Claimant.", alternatePayee.getId(), actualPayment.getId());
			
			//Loading the collection. Without this we get a LazyInitializationException. Find a better fix.
			alternatePayee.getMailingAddress();
			return asClaimantViewModel(alternatePayee);
		}
		return ClaimantViewModel.getNewClaimant();
	}
	
	/**
	 * Fetches all the Alternate payees of a given {@link Claimant}
	 * @param parentClaimantId - 
	 * @return A {@link List} of {@link ClaimantSearchRecordDto}.
	 */
	public List<ClaimantSearchRecordDto> getAlternatePayeeList(Long parentClaimantId) {
		if(log.isInfoEnabled())
			log.info(String.format("Loading Alternate Payee List for Parent Claimant : %s.",parentClaimantId));
		Preconditions.checkArgument(parentClaimantId != null, "parentClaimantId is null");
		ClaimantSearchCriteriaDto param = new ClaimantSearchCriteriaDto();
		// We will never have 100 alternate payees. So we are safe limiting the results to 100. 
		param.setMaxResults(100);
		param.setParentClaimantId(parentClaimantId);
		return claimantService.getClaimantSearchResults(param);
	}
	
	/**
	 * Wraps the {@link Claimant} inside a {@link ClaimantViewModel}
	 * 
	 * @param claimant
	 * @return {@link ClaimantViewModel}
	 */
	public ClaimantViewModel asClaimantViewModel(Claimant claimant) {
		Preconditions.checkArgument(claimant != null, "claimant is null");
		return new ClaimantViewModel(claimant);
	} 
	
	/**
	 * A new {@link Payment} that will receive a % of the original {@link Payment}.
	 * The new {@link Payment} will be given to the alternate payee.
	 * 
	 * @return
	 */
	public Payment createNewSplitPayment(Payment actualPayment) {
		if(log.isInfoEnabled())
			log.info(String.format("Creating New Split Payment for Check : %s.",actualPayment.getId()));
		Payment newPayment = Payment.newPayment(PaymentCode.NEW_ISSUE_CREATED_NI_NI);
		newPayment.setPaymentType(PaymentType.CHECK);
		newPayment.setPaymentCalc(actualPayment.getPaymentCalc().clone());
		return newPayment;
	}
	
	/**
	 * Will do the following
	 * 
	 * 1. Update or create the alternatePayee.
	 * 2. Update actualPayment by reducing it payment values by the amount(s) given to splitPayment.
	 * 3. Add splitPayment to repository.  
	 * 
	 * @param messageContext
	 * @param parent - The {@link Claimant} whose {@link Payment} is being split. 
	 * @param alternatePayee - The alternate payee {@link Claimant} receiving the split.
	 * @return 
	 */
	@Transactional
	public boolean savePaymentSplit(Claimant parent, Claimant alternatePayee,
									Payment actualPayment, Payment splitPayment,
			                        MessageContext messageContext) {
		if(actualPayment.getPaymentType() == PaymentType.ROF) {
			/* There is an bug (OptimisticLockException) when a split is done as part of the ROF flow.
			 * The workaround is to reload actualPayment.
			*/
			actualPayment = Payment.findPayment(actualPayment.getId());
		}
		if(!validateSaveSplitPayment(alternatePayee, actualPayment, splitPayment, messageContext)) {
			return false;
		}
		if(alternatePayee.getId() == null) { // A new Claimant.
			createAlternatePayee(alternatePayee, Claimant.findClaimant(parent.getId()), messageContext);		
		}
		
		//MutableAttributeMap viewScope = RequestContextHolder.getRequestContext().getViewScope();		
		splitPayment.setPaymentType(PaymentType.valueOf(newPaymentType));
		
		if(PaymentType.valueOf(newPaymentType) == PaymentType.WIRE) { // We need to re-create the splitPayment in order to set it's initial status to a wire status.
			PaymentCalc newPaymentCalc = splitPayment.getPaymentCalc();
			splitPayment = Payment.newPayment(PaymentCode.WIRE_REQUESTED_W_W);
			splitPayment.setPaymentType(PaymentType.WIRE);
			splitPayment.setPaymentCalc(newPaymentCalc);
		}
		
		alternatePayee = alternatePayee.merge();
		alternatePayee.addCheck(splitPayment);		
		splitPayment.persist();
		
		actualPayment = actualPayment.merge();
		updateActualPayment(actualPayment);
		actualPayment.addSplit(splitPayment);
		actualPayment.setHasBeenSplit(Boolean.TRUE);
		actualPayment.flush();
		resetTempFileds();
		return true;

	}
	
	/**
	 * Update the {@link PaymentCode} of the actual payment. 
	 * 
	 * @param actualPayment - The {@link Payment} being split.
	 */
	private void updateActualPayment(Payment actualPayment) {		
		
		if(actualPayment.canVoidOrStop()) {
			if(PaymentCode.valueOf(voidOrStopExistingPayment) == PaymentCode.VOID_VOIDED_V_V) {				
				 actualPayment.voidCheck();	            
			} else if (PaymentCode.valueOf(voidOrStopExistingPayment) == PaymentCode.STOP_STOP_REQUESTED_S_SR){				
	            actualPayment.stopCheck();
			}else {
				throw new IllegalStateException("Unsupported PaymentCode " + PaymentCode.valueOf(voidOrStopExistingPayment) +" for split payment.");
			}	
		}		
	}
	
	
	private boolean validateSaveSplitPayment(Claimant alternatePayee, Payment actualPayment, Payment splitPayment, MessageContext messageContext) {
		boolean isValid = true;
		if(!actualPayment.canSplit()) {			
			messageContext.addMessage(new MessageBuilder().error()					
					.defaultText("Payment not eligible for a split").build());
			isValid = false;
			
		}
		if (!validAlternatePayee(alternatePayee, messageContext)) {
			if(log.isDebugEnabled())
				log.debug(String.format("%s is not a valid alternate payee for Split Payment",alternatePayee.getId()));
			isValid = false;
		}
		//We need to recalculate the split in case the user ignores the ajax error message and goes on to submit the form.
		this.applyPercentage(actualPayment, splitPayment, messageContext);
		//If any error messages from % calculations.
		if(messageContext.hasErrorMessages()) {
			isValid = false;
		}		
		return isValid;
	}
	
	private void resetTempFileds() {
		this.percentageToSplitBy = 0;	
		this.newPaymentType = null;
		this.voidOrStopExistingPayment = null;
	}

	/**
	 * Apply {@link #percentageToSplitBy} to the actual Payment and assign the results to the new {@link Payment}.
	 * The new {@link Payment} is retrieved from the flow scope. The actual {@link Payment}s values will remain unchanged after this calculation. 
	 * This is to allow the user to change the % (on-screen) multiple times. Each time the % changes we must apply it to the values of the 
	 * actual payment.   
	 */
	public void applyPercentage(Payment actualPayment, Payment newPayment, MessageContext messageContext) {
		// Need this to avoid a LazyInitializtionException on Payment#splits.
		actualPayment = actualPayment.merge();
		PaymentCalc newPaymentCalc = actualPayment.getPaymentCalc().clone();
		newPaymentCalc.multiply(new BigDecimal(percentageToSplitBy).divide(new BigDecimal(100)));
		if(!actualPayment.canAddNewSplitPayment(newPaymentCalc)) {
			messageContext.addMessage(new MessageBuilder()
			.source(JsfUtils.getClientId("paymentPercentage"))
			.error()
			.defaultText(
					"Cannot perform split payment. The % of all splits exceeds 100%.").build());
		} else {
			newPayment.setPaymentCalc(newPaymentCalc);
		}
	}
	
	/**
	 * Find the {@link Claimant} for the given id.
	 * @param alternatePayeeId - the id of the {@link Claimant}
	 * @return the {@link Claimant} for the given id.
	 * @throws IllegalArgumentException  if a {@link Claimant} with the given alternatePayeeId does not exist.
	 */
	private Claimant findClaimant(Long alternatePayeeId) {
		Claimant claimant = Claimant.findClaimant(alternatePayeeId);
		if(claimant == null) {
			if(log.isErrorEnabled())
				log.error(String.format("Claimant with id : %s does not exist.",alternatePayeeId ));
			throw new IllegalArgumentException("Claimant with id : " + alternatePayeeId +" does not exist.");
		}
		return claimant;
	}

	/**
	 * Find the {@link Claimant} to who the given {@link Payment} belongs.
	 * @param payment - The {@link Payment}
	 * @return - The {@link Claimant}
	 * @throws IllegalStateException if payment is not assigned to any claimant. A {@link Payment} must always be assigned to a {@link Claimant}.
	 */
	public Claimant getPayto(Payment payment) {
		Claimant claimant = payment.getPayTo();
		
		if(claimant == null) {
			if(log.isErrorEnabled())
				log.error(String.format("Claimant for Payment with id : %s does not exist.",payment.getId() ));
			throw new IllegalStateException("Claimant for Payment with id : " + payment.getId() +" does not exist.");
		}
		//Loading the collection. Without this we get a LazyInitializationException. Find a better fix.
		claimant.getMailingAddress();
		return claimant;
	}
	
	/**
	 * Find {@link Payment} form repository.
	 * @param paymentId - The id of the {@link Payment}
	 * @return {@link Payment} for the given paymentId
	 * 
	 * @throws IllegalArgumentException if a {@link Payment} with the given paymentId does not exist.
	 */
	public Payment findPayment(Long paymentId) {
		Payment payment = Payment.findPayment(paymentId);
		
		if(payment == null) {
			if(log.isErrorEnabled())
				log.error(String.format("Payment with id : %s does not exist.",paymentId));
			throw new IllegalArgumentException("Payment with id : " + paymentId +" does not exist.");
		}
		return payment;
	}
	
	/**
	 * Persist the new alternate payee {@link Claimant} to repository.
	 * 
	 * @param alternatePayee - The new Alternate payee {@link Claimant}
	 * @param parent - The {@link Claimant} for who the alternatePayee is being created.
	 * @return - The referenceNo assigned to alternatePayee 
	 */
	@Transactional
	public String createAlternatePayee(final Claimant alternatePayee, final Claimant parent, MessageContext messageContext) {
		parent.addAlternatePayee(alternatePayee);
		alternatePayee.persist();
		messageContext.addMessage(new MessageBuilder()
		.info()
		.defaultText(
				"A New Alternate Payee is created with Reference #"
					+ alternatePayee.getReferenceNo()).build());
		if(log.isInfoEnabled())
			log.info(String.format("A New Alternate Payee is created with Reference No : %s ",alternatePayee.getReferenceNo()));
		return alternatePayee.getReferenceNo();
	}
	
	/**
	 * Semantic validation of the set/updated fields of the alternate payee {@link Claimant}. 
	 * 
	 * @param alternatePayee - The alternate payee {@link Claimant}
	 * @param messageContext
	 * @return - 
	 */
	public boolean validAlternatePayee(final Claimant alternatePayee, MessageContext messageContext) {
		boolean ret = true;
		if (Event.getCurrentEvent().getRequiresTaxInfo()) {
			if (alternatePayee.getUsCitizen() == null) {
				messageContext.addMessage(new MessageBuilder().error()
						.source(JsfUtils.getClientId("claimant_USCitizen"))
						.defaultText("US citizen is mandatory.").build());
				ret = false;
			}

			if (!StringUtils.hasText(alternatePayee.getTaxCountryCode())) {
				messageContext.addMessage(new MessageBuilder().error()
						.source(JsfUtils.getClientId("claimant_TaxCountry"))
						.defaultText("Tax country is mandatory.").build());
				ret = false;
			}
		}
		return ret;

	}

	public int getPercentageToSplitBy() {
		return percentageToSplitBy;
	}

	public void setPercentageToSplitBy(int percentageToSplitBy) {
		this.percentageToSplitBy = percentageToSplitBy;
	}	

	public String getNewPaymentType() {
		return newPaymentType;
	}

	public void setNewPaymentType(String newPaymentType) {
		this.newPaymentType = newPaymentType;
	}

	public String getVoidOrStopExistingPayment() {
		return voidOrStopExistingPayment;
	}

	public void setVoidOrStopExistingPayment(String voidOrStopExistingPayment) {
		this.voidOrStopExistingPayment = voidOrStopExistingPayment;
	}		
	
}
