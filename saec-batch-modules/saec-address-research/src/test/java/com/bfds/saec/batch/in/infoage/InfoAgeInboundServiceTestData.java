package com.bfds.saec.batch.in.infoage;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Named;

import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.HitIndicatorCorpType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.HitIndicatorType;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_individual.MatchIndicatorType;
import com.bfds.saec.batch.out.infoage.AddressResearchEventTestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.PlatformTransactionManager;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RPOType;

@Configurable
@Named(value = "addressResearchReceiveServiceTestDataGenerator")
public class InfoAgeInboundServiceTestData {

    @Autowired
    AddressResearchEventTestData eventTestData;

	@Autowired
	private PlatformTransactionManager transactionManager;

	public void newEvent() {
		Event e = Event.newEvent();
		e.persist();
	}

	public void newClaimant(final boolean createPayment, final boolean createLetter) {
		Claimant claimant = eventTestData.newClaimant(true);
        eventTestData.newPrimaryAddress(claimant, "--1");
		claimant.setReferenceNo("10000001");
		claimant.setCertificationDate(new Date());
		claimant.persist();
		claimant = Claimant.findClaimantsByReferenceNo("10000001") .getSingleResult();
		if (createPayment) {
			Payment c = Payment.newPayment(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);
			c.setPaymentType(PaymentType.CHECK);
			c.setStatusChangeDate(new Date(120, 9, 1));
			c.setIdentificatonNo("100010");
			c.setPaymentAmount(new BigDecimal(200));
			c.setPayTo(claimant);
			claimant.addCheck(c);
		}
		if (createLetter) {
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
		// Delete all activity that will created during the creation of Claimant and it's associations.
		for(Activity a : Activity.findAllActivitys()) {
			a.remove();
		}
		claimant.flush();
		claimant.clear();
	}
	

    public IndividualAddressResearch newIndividualAddressResearch(HitIndicatorType hitIndicator, MatchIndicatorType matchIndicator,
    															   String invalidOrDeseasedSsn, String matchAnalysisTag, 
    															   String nameChangeTag, String ssnMatchTag,
    															   String ofacIndicator) {
    	
       final IndividualAddressResearch research = new IndividualAddressResearch();
        research.setUserRef("10000001");
        research.setHitIndicator(hitIndicator);
        research.setMatchIndicator(matchIndicator);
        
        research.setFirstName("John");
        research.setMiddleName("Joseph");
        research.setLastName("Travolta");
        research.setPrefix("Mr.");
        research.setSuffix("Sr.");
        //research.setMaternalName(value);
        //research.setHouseNumber(value);
        //research.setPreDirection(value);
        //research.setThoroughfareName(value);
        //research.setApartmentNumber(value);
        research.setCity("Quincy");
        research.setState("MA");
        research.setZipCode("02139");
        research.setAddressDateReported("201101--");
        //research.setSsn(value);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970, 1, 1);
        research.setDateOfBirth(calendar);       
        research.setPhoneAreaCode("718");
        research.setPhone("1231234");
        research.setPhoneExt("1232");
        
        research.setInvalidOrDeceasedSSNTag(invalidOrDeseasedSsn);
        research.setMatchAnalysisTag(matchAnalysisTag);
        research.setNameChangeTag(nameChangeTag);
        research.setSsnMatchTag(ssnMatchTag);
        research.setOfacIndicator(ofacIndicator);
        return research;

    }
    
    public CorporateAddressResearch newCorporateAddressResearch(HitIndicatorCorpType hitIndicator, String matchAnalysisTag) {
        CorporateAddressResearch research = new CorporateAddressResearch();
        research.setUserRef("10000001");
        research.setHitIndicator(hitIndicator);
        
        research.setCompanyName("BFDS");
        research.setAddress("2000 crown colony dr");
        research.setCity("Quincy");
        research.setState("MA");
        research.setZipCode("02139");
        research.setAddressDateReported("201102--");                
        research.setFein("FEIN");
        research.setPhoneAreaCode("718");
        research.setPhone("1231234");
        
        research.setPhoneExt("1232");// This field is not there in the spec.
        
        research.setMatchAnalysisTag(matchAnalysisTag);
        //research.setPartialAddressIndicator(value);
        
        
        return research;

    }

}
