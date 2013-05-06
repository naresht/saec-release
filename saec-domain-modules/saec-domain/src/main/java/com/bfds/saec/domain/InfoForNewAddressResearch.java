package com.bfds.saec.domain;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.reference.RpoEligibleOption;
import com.bfds.saec.util.SaecDateUtils;

/**
 * Contains all the data required to do the following 
 * 
 * 1. Is the claimant eligible for an address research
 * 2. What type of object(Payment, Letter or both) are returned as RPO.
 * 3. How many times the claimant has be sent for research.
 */
@RooJavaBean(settersByDefault=false)
@RooToString
public class InfoForNewAddressResearch {

	private int  paymentSendCountLimit;
	private int  mailSendCountLimit;
	private Long currentRpoPaymentCount;
	private Long currentRpoLetterCount;
	private int  addressResearchSentCountForChecks;
	private int  addressResearchSentCountForLetters;
	private int  addressResearchSentCountForLettersAndChecks;  
	private Date dateOfLastRpoNonForwardableLetter;
	private Date dateOfLastRpoNonForwardablePayment;
	private boolean paymentsEligibleForAddressResearch;
	private boolean lettersEligibleForAddressResearch;
	
	public static InfoForNewAddressResearch getInfoForNewAddressResearch(final Claimant claimant, final Event event) {
		
		final InfoForNewAddressResearch info = new InfoForNewAddressResearch();
		
		info.paymentSendCountLimit = event.getPaymentSendCountLimit();
		info.mailSendCountLimit = event.getMailSendCountLimit();
		info.currentRpoLetterCount = Letter.countLettersForAddressResaerchOfClaimant(claimant.getId());
		info.currentRpoPaymentCount = Payment.countPaymentsForAddressResaerchOfClaimant(claimant.getId());
		info.addressResearchSentCountForChecks = claimant.getAddressResearchSentCountForChecks();
		info.addressResearchSentCountForLetters = claimant.getAddressResearchSentCountForLetters();
		info.addressResearchSentCountForLettersAndChecks = claimant.getAddressResearchSentCountForLettersAndChecks();
		info.dateOfLastRpoNonForwardableLetter = Letter.getLastRpoLetterDateOfClaimant(claimant.getId());
		info.dateOfLastRpoNonForwardablePayment = Payment.getLastRpoPaymentDateOfClaimant(claimant.getId());
		
		if(RpoEligibleOption.MAIL.equals(event.getRpoEligible())) {
			info.lettersEligibleForAddressResearch = true;
		} else if(RpoEligibleOption.PAYMENT.equals(event.getRpoEligible())) {
			info.paymentsEligibleForAddressResearch = true;
		} else if(RpoEligibleOption.MAIL_AND_PAYMENT.equals(event.getRpoEligible())) {
			info.paymentsEligibleForAddressResearch = true;
			info.lettersEligibleForAddressResearch = true;
		}  
		return info;
    }
	
	/**
	 * @return - True if the {@link Claimant} can be sent for research in the event of a {@link Letter} returning RPO .
	 */
	public boolean canSendMailResearch() {
		return lettersEligibleForAddressResearch && currentRpoLetterCount > 0 && 
				this.addressResearchSentCountForLetters 
        		+ this.addressResearchSentCountForLettersAndChecks < mailSendCountLimit;
	}
	
	/**
	 * @return - True if the {@link Claimant} can be sent for research in the event of a {@link Payment} returning RPO .
	 */	
	public boolean canSendPaymentResearch() {
		return paymentsEligibleForAddressResearch && currentRpoPaymentCount > 0 &&
				this.addressResearchSentCountForChecks 
        + this.addressResearchSentCountForLettersAndChecks < paymentSendCountLimit;
	}
	/**
	 * @return - True if the {@link Claimant} is eligible for address research. 
	 * A {@link Claimant} might need address research but might not be eligible for it.
	 */	
	public boolean isResearchEligible() {
		return canSendMailResearch() || canSendPaymentResearch();
	}
	  	

	/**
	 * @return The {@link Date} of the last {@link Letter} or {@link Payment} that returned RPO if both {@link Payment}s and {@link Letter}s are eligible for research.
	 * If only {@link Letter}s are eligible for research then the {@link Date} of the last {@link Letter} that returned RPO 
	 * If only {@link Payment}s are eligible for research then the {@link Date} of the last {@link Payment} that returned RPO 
	 * 
	 * @throws NullPointerException if both {@link Payment}s and {@link Letter}s are eligible for research and one of both of their last RPO dates are null.
	 */
	public Date getLastRpoItemDate() {
		if(canSendMailResearch()  && canSendPaymentResearch()) {
			return SaecDateUtils.isAfterDay(dateOfLastRpoNonForwardableLetter, dateOfLastRpoNonForwardablePayment)
					? dateOfLastRpoNonForwardableLetter : dateOfLastRpoNonForwardablePayment;
		}
		if(canSendPaymentResearch()) {
			return dateOfLastRpoNonForwardablePayment;
		}
		
		if( canSendMailResearch() ) {
			return dateOfLastRpoNonForwardableLetter;
		} 
		
		return null;
	}	
}
