package com.bfds.saec.batch.out.infoage;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Named;

import com.bfds.saec.batch.out.infoage.AddressResearchEventTestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.domain.reference.RpoEligibleOption;

@Configurable
@Named(value = "addressResearchSendServiceTestDataGenerator")
public class InfoAgeOutboundServiceTestData {

    @Autowired
    AddressResearchEventTestData eventTestData;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	public void newEvent(int paymentSendCountLimit, int  mailSendCountLimit) {
		newEvent(paymentSendCountLimit, mailSendCountLimit, RpoEligibleOption.MAIL_AND_PAYMENT);
	}
	
	public void newEvent(int paymentSendCountLimit, int  mailSendCountLimit, RpoEligibleOption rpoEligibleItems) {
		Event e = Event.newEvent();
		e.setMailSendCountLimit(mailSendCountLimit);
		e.setPaymentSendCountLimit(paymentSendCountLimit);
		e.setRpoEligible(rpoEligibleItems);
		e.persist();
	}

	public Claimant newClaimant(final int paymentSentCount, final int letterSentCount, 
			 final int paymentAndLetterSentCount, final boolean createPayment, 
			 final boolean createLetter) { 
		return newClaimant(true, paymentSentCount, letterSentCount, paymentAndLetterSentCount, createPayment, createLetter);
	}
	
	public Claimant newClaimant(final boolean isCorporate, final int paymentSentCount, final int letterSentCount, 
							 final int paymentAndLetterSentCount, final boolean createPayment, 
							 final boolean createLetter) {
		
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		return tt.execute(new TransactionCallback<Claimant>() {
			@Override
			public Claimant doInTransaction(TransactionStatus status) {
								
				Claimant claimant = eventTestData.newClaimant(isCorporate);
                eventTestData.newPrimaryAddress(claimant, "--1");
				claimant.setReferenceNo("10000001");
				claimant.setCertificationDate(new Date());
				claimant.setAddressResearchSentCountForChecks(paymentSentCount);
				claimant.setAddressResearchSentCountForLetters(letterSentCount);
				claimant.setAddressResearchSentCountForLettersAndChecks(paymentAndLetterSentCount);
				claimant.persist();
				claimant = Claimant.findClaimantsByReferenceNo("10000001").getSingleResult();
				if(createPayment) {
					Payment c = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
					c.setPaymentType(PaymentType.CHECK);
					c.setStatusChangeDate(new Date(120, 9, 1));
					c.setIdentificatonNo("100010");
					c.setPaymentAmount(new BigDecimal(200));
					c.setPayTo(claimant);
					claimant.addCheck(c);
				}
				if(createLetter) {
					LetterCode lc = new LetterCode("700", "Claim Form", LetterType.CLAIM_FORM_CURE_LETTER);
					lc.persist();
					
					Letter letter = new Letter();
					letter.setLetterCode(LetterCode.findByCode("700"));
					letter.setClaimant(claimant);
					letter.setRpoType(RPOType.NONFORWARDABLE);
					letter.setMailDate(new Date(120, 1, 1));
					letter.persist();
				}
				claimant.merge();
				claimant.flush();
				claimant.clear();
				return claimant;
			}
			
		});
	}	

	public Claimant update(final Claimant claimant) {
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		return tt.execute(new TransactionCallback<Claimant>() {
			@Override
			public Claimant doInTransaction(TransactionStatus status) {
								
				final Claimant ret = claimant.merge();
				ret.flush();
				return ret;
			}
			
		});		
	}
	public void cleanup_() {
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		tt.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
                eventTestData.deleteData();
				return null;
			}
		});
	}
}
