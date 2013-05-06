package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.reference.RPOType;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
//@Transactional
 @Ignore
public class LetterTest{
	private Claimant claimant;
	@Autowired
	ClaimantDataOnDemand claimantDod;
	
	 
	@Before
	public void insertData1() {
		LetterCode lc = new LetterCode("700", "Claim Form - Not Signed", LetterType.CLAIM_FORM_CURE_LETTER);
		lc.persist();
		
		lc = new LetterCode("600", "Claim Form", LetterType.CLAIM_FORM);
		lc.persist();

		lc = new LetterCode("800", "Opt Out - Not Signed", LetterType.OPTOUT_CURE_LETTER);
		lc.persist();
		
		lc = new LetterCode("601", "Optout Form", LetterType.OPTOUT_FORM);

		lc.persist();
		
		lc = new LetterCode("500", "Correspondence", LetterType.GENERAL_CORRESPONDENCE);

		lc.persist();
				
		this.claimant = claimantDod.getNewTransientClaimant(1);
		claimant.persist();
		Letter claimForm = new Letter();
		claimForm.setLetterCode(LetterCode.findByCode("600"));
		claimForm.setClaimant(claimant);
		claimForm.persist();
		
		Letter optOutForm = new Letter();
		optOutForm.setLetterCode(LetterCode.findByCode("601"));
		optOutForm.setClaimant(claimant);
		optOutForm.persist();
		
		Letter correspondence = new Letter();
		correspondence.setClaimant(claimant);
		correspondence.setLetterCode(LetterCode.findByCode("500"));
		correspondence.persist();		
		
	}

	@Test	
	public void optOutFormsAndClaimFormsShouldNotBeListedAlongWithOtherCorrespondence() {
		List<Letter> correspondenceList = Letter.findCorrespondenceOfClaimant(this.claimant.getId());
		assertThat(correspondenceList).onProperty("letterCode.letterType")
				.isNotIn(LetterType.CLAIM_FORM, LetterType.OPTOUT_FORM, LetterType.CLAIM_FORM_CURE_LETTER, LetterType.OPTOUT_CURE_LETTER);
	}
	
	@Test
	@Ignore
	public void testCorrespondenceDocuments() {
		Letter letter = newCorrespondenceLetter();

		letter.persist();
		letter.flush();
		Letter.entityManager().clear();
		letter = Letter.findLetter(letter.getId());
		Letter.entityManager().clear();
		letter = Letter.findLetter(letter.getId(), (String[]) null);
		assertThat(letter.getIgoCorrespondenceDocuments()).hasSize(1);
		assertThat(letter.getNigoCorrespondenceDocuments()).hasSize(1);
	}
	
	@Test
	public void getLetterCountOfClaimant() {		
		assertThat(Letter.getLetterCountOfClaimant(claimant.getId())).isEqualTo(3);
	}
	
	@Test
	public void getStatusDescription() {
		Letter l = new Letter();
		l.setLetterStatus(LetterStatus.IGO);
		assertThat(l.getStatusDescription()).isEqualTo(LetterStatus.IGO.toString());
	}
	
	@Test
	@Ignore
	public void findByMailingControlNo() {
		Letter let1 = new Letter();
		let1.setMailingControlNo("1");
		let1.persist();
		let1 = new Letter();
		let1.setMailingControlNo("1");
		let1.persist();
		let1 = new Letter();
		let1.setMailingControlNo("2");
		let1.persist();
		
		assertThat(Letter.findByMailingControlNo("1").getMailingControlNo()).isEqualTo("1");
		assertThat(Letter.findByMailingControlNo("2").getMailingControlNo()).isEqualTo("2");
		assertThat(Letter.findByMailingControlNo("3")).isNull();		
	}
	
	@Test
	@Ignore
	public void getDerivedComments() {
		Letter let1 = new Letter();
		let1.setMailingControlNo("1");
		let1.setComments("12345");
		let1.persist();
		
		let1 = new Letter();
		let1.setMailingControlNo("2");
		let1.setComments("abcd");
		let1.setMailType(MailType.OUTGOING);
		let1.setLetterCode(LetterCode.findByCode("700"));
		let1.persist();
				
		//assertThat(Letter.findByMailingControlNo("1").getDerivedComments()).isEqualTo("");
		//assertThat(Letter.findByMailingControlNo("2").getDerivedComments()).isEqualTo(LetterCode.findByCode("700").getDescription());
		assertThat(Letter.findByMailingControlNo("3")).isNull();		
	}
	
	@Test
	public void createCorrespondence() {
		Letter form = Letter.newCorrespondence();
		if(form.getLetterCode() != null) {
			assertThat(form.getLetterCode().getLetterType()).isNotIn(
						LetterType.CLAIM_FORM, LetterType.OPTOUT_FORM, 
						LetterType.CLAIM_FORM_CURE_LETTER, LetterType.OPTOUT_CURE_LETTER);
		}

		assertThat(form.getMailType()).isNull();
		
		assertThat(form.getMailDate()).isNotNull();
		
		assertThat(form.getUserId()).isEqualTo("system!");	
	}
	
	@Test
	public void newOutgoingCorrespondenceForIncomingNigo() {
		Letter form = Letter.newCorrespondence();
		form.setLetterStatus(LetterStatus.NIGO);
		form.setMailType(MailType.INCOMING);
		
		Letter outGoingNigo = form.newOutgoingCorrespondenceForIncomingNigo();
		
		assertThat(outGoingNigo.getMailType()).isEqualTo(MailType.OUTGOING);
		assertThat(outGoingNigo.getInResponseTo()).isNotNull();
		assertThat(outGoingNigo.getInResponseTo()).isEqualTo(form);
		assertThat(outGoingNigo.getMailDate()).isNull();
		assertThat(outGoingNigo.getUserId()).isEqualTo("system!");
		
		assertThat(outGoingNigo.getIgoCorrespondenceDocuments()).isEmpty();
		assertThat(outGoingNigo.getNigoCorrespondenceDocuments()).isEmpty();
		
		form = newCorrespondenceLetter();
		form.setLetterStatus(LetterStatus.NIGO);
		form.setMailType(MailType.INCOMING);
		outGoingNigo = form.newOutgoingCorrespondenceForIncomingNigo();
		
		assertThat(outGoingNigo.getIgoCorrespondenceDocuments()).onProperty("name").containsOnly("Medallion Guarantee");
		assertThat(outGoingNigo.getNigoCorrespondenceDocuments()).onProperty("name").containsOnly("Original Death Certificate");
	}
	
	@Test
	public void newOutgoingCorrespondenceForIncomingNigoOfNIGO() {
		Letter form = Letter.newCorrespondence();
		form.setMailType(MailType.OUTGOING);
		
		Letter outGoingMail = form.newOutgoingMailObjectForNIGO();
		
		assertThat(outGoingMail.getMailType()).isEqualTo(MailType.OUTGOING);
		assertThat(outGoingMail.getInResponseTo()).isNotNull();
		assertThat(outGoingMail.getInResponseTo()).isEqualTo(form);
		assertThat(outGoingMail.getMailDate()).isNull();
		assertThat(outGoingMail.getUserId()).isEqualTo("system!");
		
		assertThat(outGoingMail.getIgoCorrespondenceDocuments()).isEmpty();
		assertThat(outGoingMail.getNigoCorrespondenceDocuments()).isEmpty();
		
		form = newCorrespondenceLetter();
		form.setLetterStatus(LetterStatus.NIGO);
		form.setMailType(MailType.INCOMING);
		outGoingMail = form.newOutgoingMailObjectForNIGO();
		
		assertThat(outGoingMail.getIgoCorrespondenceDocuments()).onProperty("name").contains("Medallion Guarantee");
		assertThat(outGoingMail.getNigoCorrespondenceDocuments()).onProperty("name").contains("Original Death Certificate");
	}
	
	@Test(expected=IllegalStateException.class)
	public void cannotCreateNewOutgoingCorrespondenceForIgo() {
		Letter form = newCorrespondenceLetter();
		form.setMailType(MailType.INCOMING);
		form.newOutgoingCorrespondenceForIncomingNigo();
	}
	
	@Test(expected=IllegalStateException.class)
	public void cannotCreateNewOutgoingCorrespondenceForOutgoing() {
		Letter form = newCorrespondenceLetter();
		form.setLetterStatus(LetterStatus.NIGO);
		form.setMailType(MailType.OUTGOING);
		form.newOutgoingCorrespondenceForIncomingNigo();
	}
	
	
	@Test
	public  void getSentCureLetterCountOfClaimant() {
		LetterCode lc = new LetterCode("L1", "L1 desc", LetterType.CLAIM_FORM_CURE_LETTER);
		lc.persist();
		lc.flush();
		lc.clear();
		claimant = claimant.merge();
		Letter letter =Letter.newCorrespondence();
		letter.setLetterCode(LetterCode.findByCode("L1"));
		letter.setMailType(MailType.OUTGOING);
		claimant.addLetter(letter);
		letter.persist();
		
		Letter letter1 =Letter.newCorrespondence();
		letter1.setLetterCode(LetterCode.findByCode("L1"));
		letter1.setMailType(MailType.OUTGOING);
		claimant.addLetter(letter1);
		letter1.persist();
		
		
		
		claimant.persist();
		assertThat(Letter.getSentCureLetterCountOfClaimant(claimant.getId())).isEqualTo(2);
		claimant.flush();
		claimant.clear();
	}
	
	/**
	 * Test to count the number of {@link Letter} belonging to a {@link Claimant} that have been returned RPO_NON_FORWARDABLE and are eligible for address research.
	 */
	@Test
	@Ignore("failing in mvn test")
	public void countLettersForAddressResaerchOfClaimant() {

		LetterCode lc = new LetterCode("100", "L1 desc", LetterType.CLAIM_FORM_CURE_LETTER);
		lc.persist();
		lc = new LetterCode("200", "L2 desc", LetterType.CLAIM_FORM_CURE_LETTER);
		lc.persist();
		lc.flush();
		lc.clear();
		claimant = claimant.merge();
		Letter letter =Letter.newCorrespondence();
		letter.setLetterCode(LetterCode.findByCode("100"));
		letter.setMailType(MailType.OUTGOING);
		letter.setRpoType(RPOType.NONFORWARDABLE);
		claimant.addLetter(letter);
		letter.persist();
		
		letter =Letter.newCorrespondence();
		letter.setLetterCode(LetterCode.findByCode("200"));
		letter.setMailType(MailType.OUTGOING);
		letter.setRpoType(RPOType.NONFORWARDABLE);
		claimant.addLetter(letter);
		letter.persist();
		
		
		claimant.persist();
		claimant.flush();
		claimant.clear();
		
		Event e = Event.newEvent();
		e.setCureLetterRangeStart(101);
		e.setCureLetterRangeEnd(201);
		e.persist();
		e.flush();
		e.clear();
		
		Long count = Letter.countLettersForAddressResaerchOfClaimant(claimant.getId());

		assertThat(count).isEqualTo(1);

	}
	
	/**
	 * To test that method should return only those {@link Letter} objects which
	 * have not been yet mailed .
	 * 
	 * TODO: Need to make changes once the Letter :dstoPrintFileSentFlag type
	 * will be changed to Boolean.
	 */
	@Test
	@Ignore("Need to Fix")
	public void testLettersEligibleForMailing() {
		assertThat(this.claimant.getLettersEligibleForMailing()).isNotEmpty();
		assertThat(this.claimant.getLettersEligibleForMailing().size())
				.isEqualTo(3);

	}
	
	private Letter newCorrespondenceLetter() {
		Letter letter =Letter.newCorrespondence();
		CorrespondenceDocument cd = new CorrespondenceDocument("Original Death Certificate");
		letter.setLetterCode(LetterCode.findByCode("500"));
		cd.setWithinThreshold(Boolean.TRUE);
		cd.persist();
		letter.getNigoCorrespondenceDocuments().add(cd);
		
		cd = new CorrespondenceDocument("Medallion Guarantee");
		cd.setWithinThreshold(Boolean.TRUE);
		cd.persist();
		letter.getIgoCorrespondenceDocuments().add(cd);
		return letter;
	}
	
	private Letter newCorrespondenceLetterForIncomingNigo() {
		Letter letter =Letter.newCorrespondence();
		CorrespondenceDocument cd = new CorrespondenceDocument("Original Death Certificate");
		cd.setWithinThreshold(Boolean.TRUE);
		cd.persist();
		letter.getIgoCorrespondenceDocuments().add(cd);
		
		cd = new CorrespondenceDocument("Medallion Guarantee");
		cd.setWithinThreshold(Boolean.TRUE);
		cd.persist();
		letter.getIgoCorrespondenceDocuments().add(cd);
		return letter;
	}
	
	@Test
	public void varifyGetNextMailingControlNumber() {
		Letter letter = new Letter();
		Long no = Long.parseLong(letter.getNextMailingControlNumber());		
		assertNotNull(no);
		assertEquals(no + 1, Long.parseLong(letter.getNextMailingControlNumber()));
	}
}
