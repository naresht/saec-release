package com.bfds.saec.domain;


import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantPosition;
import com.bfds.wss.domain.ClaimantPosition.PositionType;
import com.bfds.wss.domain.reference.ClaimStatus;
import com.bfds.wss.domain.reference.SecurityFund;
import com.bfds.wss.domain.reference.SecurityRef;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/applicationContext-domain-test.xml"})
@Transactional
public class ClaimStatusRulesTest {

	ClaimantStatusRules claimantStatusRules; 
	
	/**
	 * Comparing the {@link ClaimStatus} as IGO with the following conditions
	 * {@link ClaimProof}s flagged as {@link ProofStatus} "IGO"
	 * AND
	 * {@link ClaimantPosition} is in "In Balance"
	 * AND
	 * There is no OptOut correspondence flagged as "IGO"
	 * 
	 */
	@Test
	public void shouldBeIGOStatusWithManyIGO() {
		Claimant claimant = newClaimant();
		claimant.persist();
		
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.persist();
		claimantClaim.clear();
		
		claimant.setClaimantClaims(Sets.newHashSet(claimantClaim));
		claimant.merge();
		claimant.clear();
		//IGO ClaimProof
		ClaimProof claimProof = getClaimProof(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		
		//IGO ClaimProof
		ClaimProof claimProof1 = getClaimProof(ProofStatus.IGO);
		claimProof1.setClaimantClaim(claimantClaim);
		claimProof1.persist();
		
		claimProof.clear();
		
		ClaimantPosition claimantPosition = claimantPosition("In Balance");
		claimantPosition.setClaimantClaim(claimantClaim);
		claimantPosition.persist();
		claimantPosition.clear();
		
		Letter letter = getOptOutCorrespondence("Opt Out", LetterStatus.NIGO);
		letter.setLetterCode(LetterCode.findByCode("800"));
		letter.setClaimant(claimant);
		letter.persist();
		letter.clear();
		
		claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		assertThat(claimantStatusRules.getStatus()).isEqualTo(ClaimStatus.IGO);
		
		
	}
	
	/**
	 * Comparing the {@link ClaimStatus} as IGO with the following conditions
	 * {@link ClaimProof}s flagged as {@link ProofStatus} "IGO OVERRIDE"
	 * AND
	 * {@link ClaimantPosition} is in "In Balance"
	 * AND
	 * There is no OptOut Correspondence flagged as "IGO"
	 * 
	 */
	@Test
	public void shouldBeIGOStatusWithManyIGOOVERRIDE() {
		Claimant claimant = newClaimant();
		claimant.persist();
		
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.persist();
		claimantClaim.clear();
		
		claimant.setClaimantClaims(Sets.newHashSet(claimantClaim));
		claimant.merge();
		claimant.clear();
		//IGO_OVERRIDE
		ClaimProof claimProof = getClaimProof(ProofStatus.IGO_OVERRIDE);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		claimProof.clear();
		
		//IGO_OVERRIDE
		ClaimProof claimProof1 = getClaimProof(ProofStatus.IGO_OVERRIDE);
		claimProof1.setClaimantClaim(claimantClaim);
		claimProof1.persist();
		claimProof1.clear();
		
		ClaimantPosition claimantPosition = claimantPosition("In Balance");
		claimantPosition.setClaimantClaim(claimantClaim);
		claimantPosition.persist();
		claimantPosition.clear();
		
		Letter letter = getOptOutCorrespondence("Opt Out", LetterStatus.NIGO);
		letter.setLetterCode(LetterCode.findByCode("800"));
		letter.setClaimant(claimant);
		letter.persist();
		letter.clear();
		
		claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		assertThat(claimantStatusRules.getStatus()).isEqualTo(ClaimStatus.IGO);
		
	}
	
	/**
	 * Comparing the {@link ClaimStatus} as IGO with the following conditions
	 * {@link ClaimProof}s flagged as {@link ProofStatus} "IGO" and "IGO OVERRIDE"
	 * AND
	 * {@link ClaimantPosition} is in "In Balance"
	 * AND
	 * There is no OptOut Correspondence flagged as "IGO"
	 * 
	 */
	@Test
	public void shouldBeIGOStatusWithOneIGOOneIGOOverride() {
		Claimant claimant = newClaimant();
		claimant.persist();
		
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.persist();
		claimantClaim.clear();
		
		claimant.setClaimantClaims(Sets.newHashSet(claimantClaim));
		claimant.merge();
		claimant.clear();
		//IGO ClaimProof
		ClaimProof claimProof = getClaimProof(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		claimProof.clear();
		//IGO_OVERRIDE
		ClaimProof claimProof1 = getClaimProof(ProofStatus.IGO_OVERRIDE);
		claimProof1.setClaimantClaim(claimantClaim);
		claimProof1.persist();
		claimProof1.clear();
		
		ClaimantPosition claimantPosition = claimantPosition("In Balance");
		claimantPosition.setClaimantClaim(claimantClaim);
		claimantPosition.persist();
		claimantPosition.clear();
		
		Letter letter = getOptOutCorrespondence("Opt Out", LetterStatus.NIGO);
		letter.setLetterCode(LetterCode.findByCode("800"));
		letter.setClaimant(claimant);
		letter.persist();
		letter.clear();
		
		claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		assertThat(claimantStatusRules.getStatus()).isEqualTo(ClaimStatus.IGO);
		
	}
	
	
	/**
	 * Comparing the {@link ClaimStatus} as PENDING with the following conditions
	 * None of the {@link ClaimProof}s are flagged as {@link ProofStatus} "NIGO" and at least one {@link ClaimProof} is flagged as "PENDING"
	 * OR
	 * All {@link ClaimProof}s are {@link ProofStatus} as "IGO" or "IGO OVERRIDE" AND at least one instance of "Proof Provided" is unchecked.
	 * AND
	 * There is no OptOut Correspondence flagged as "IGO"
	 * 
	 */
	@Test
	public void shouldBePendingStatus() {
		Claimant claimant = newClaimant();
		claimant.persist();
		
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.persist();
		claimantClaim.flush();
		claimantClaim.clear();
		
		claimant.setClaimantClaims(Sets.newHashSet(claimantClaim));
		claimant.merge();
		claimant.flush();
		claimant.clear();
		//IGO ClaimProof
		ClaimProof claimProof = getClaimProof(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		claimProof.flush();
		claimProof.clear();
		//PENDING
		ClaimProof claimProof1 = getClaimProof(ProofStatus.PENDING);
		claimProof1.setClaimantClaim(claimantClaim);
		claimProof1.persist();
		claimProof1.flush();
		claimProof1.clear();
		//NIGO
		ClaimProof claimProof2 = getClaimProof(ProofStatus.IGO_OVERRIDE);
		claimProof2.setClaimantClaim(claimantClaim);
		claimProof2.persist();
		claimProof2.flush();
		claimProof2.clear();
		
		Letter letter = getOptOutCorrespondence("Opt Out", LetterStatus.NIGO);
		letter.setLetterCode(LetterCode.findByCode("800"));
		letter.setClaimant(claimant);
		letter.persist();
		letter.flush();
		letter.clear();
		
		claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		assertThat(claimantStatusRules.getStatus()).isEqualTo(ClaimStatus.PENDING);
		
	}
	
	
	/**
	 * Comparing the {@link ClaimStatus} as OptOut with the following condition
	 * There is OptOut Correspondence flagged as "IGO"
	 * 
	 */
	@Test
	public void shouldBeOptOutStatus() {
		Claimant claimant = newClaimant();
		claimant.persist();
		
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.persist();
		claimantClaim.clear();
		
		claimant.setClaimantClaims(Sets.newHashSet(claimantClaim));
		claimant.merge();
		claimant.clear();
		//IGO ClaimProof
		ClaimProof claimProof = getClaimProof(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		claimProof.clear();
		//PENDING
		ClaimProof claimProof1 = getClaimProof(ProofStatus.PENDING);
		claimProof1.setClaimantClaim(claimantClaim);
		claimProof1.persist();
		claimProof1.clear();
		//IGO OVERRIDE
		ClaimProof claimProof2 = getClaimProof(ProofStatus.IGO_OVERRIDE);
		claimProof2.setClaimantClaim(claimantClaim);
		claimProof2.persist();
		claimProof2.clear();
		//NIGO
		ClaimProof claimProof3 = getClaimProof(ProofStatus.NIGO);
		claimProof3.setClaimantClaim(claimantClaim);
		claimProof3.persist();
		claimProof3.clear();
		
		Letter letter = getOptOutCorrespondence("Opt Out", LetterStatus.IGO);
		letter.setLetterCode(LetterCode.findByCode("800"));
		letter.setClaimant(claimant);
		letter.persist();
		letter.clear();
		
		claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		assertThat(claimantStatusRules.getStatus()).isEqualTo(ClaimStatus.OPT_OUT);
		
	}
	
	/**
	 * Comparing the {@link ClaimStatus} as NIGO with the following condition
	 * (At least one {@link ClaimProof} is flagged as "NIGO" 
	 * OR 
	 * {@link ClaimantPosition} is In Balance)
	 * AND 
	 * There is no OptOut correspondence flagged as "IGO"
	 */
	@Test
	public void shouldBeNIGOStatus() {
		Claimant claimant = newClaimant();
		claimant.persist();
		
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.persist();
		claimantClaim.clear();
		
		claimant.setClaimantClaims(Sets.newHashSet(claimantClaim));
		claimant.merge();
		claimant.clear();
		//IGO ClaimProof
		ClaimProof claimProof = getClaimProof(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		claimProof.clear();
		//IGO OVERRIDE ClaimProof
		ClaimProof claimProof1 = getClaimProof(ProofStatus.IGO_OVERRIDE);
		claimProof1.setClaimantClaim(claimantClaim);
		claimProof1.persist();
		claimProof1.clear();
		//PENDING
		ClaimProof claimProof2 = getClaimProof(ProofStatus.PENDING);
		claimProof2.setClaimantClaim(claimantClaim);
		claimProof2.persist();
		claimProof2.clear();
		//NIGO
		ClaimProof claimProof3 = getClaimProof(ProofStatus.NIGO);
		claimProof3.setClaimantClaim(claimantClaim);
		claimProof3.persist();
		claimProof3.clear();
		
		ClaimantPosition claimantPosition = claimantPosition("In Balance");
		claimantPosition.setClaimantClaim(claimantClaim);
		claimantPosition.persist();
		claimantPosition.clear();
		
		Letter letter = getOptOutCorrespondence("Opt Out", LetterStatus.NIGO);
		letter.setLetterCode(LetterCode.findByCode("800"));
		letter.setClaimant(claimant);
		letter.persist();
		letter.clear();
		
		claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		assertThat(claimantStatusRules.getStatus()).isEqualTo(ClaimStatus.NIGO);
		
	}
	
	/**
	 * Comparing the {@link ClaimStatus} as NIGO with the following condition
	 * (At least one {@link ClaimProof} is flagged as "NIGO" 
	 * OR 
	 * {@link ClaimantPosition} is Out Of Balance)
	 * AND 
	 * There is no OptOut correspondence flagged as "IGO"
	 */
	@Test
	public void shouldBeNIGOStatusWithOutOfBalance() {
		Claimant claimant = newClaimant();
		claimant.persist();
		
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.persist();
		claimantClaim.clear();
		
		claimant.setClaimantClaims(Sets.newHashSet(claimantClaim));
		claimant.merge();
		claimant.clear();
		//IGO ClaimProof
		ClaimProof claimProof1 = getClaimProof(ProofStatus.IGO);
		claimProof1.setClaimantClaim(claimantClaim);
		claimProof1.persist();
		claimProof1.clear();
		//IGO OVERRIDE ClaimProof
		ClaimProof claimProof2 = getClaimProof(ProofStatus.IGO_OVERRIDE);
		claimProof2.setClaimantClaim(claimantClaim);
		claimProof2.persist();
		claimProof2.clear();
		
		ClaimantPosition claimantPosition = claimantPosition("Out Of Balance");
		claimantPosition.setClaimantClaim(claimantClaim);
		claimantPosition.persist();
		claimantPosition.clear();
		
		Letter letter = getOptOutCorrespondence("Opt Out", LetterStatus.NIGO);
		letter.setLetterCode(LetterCode.findByCode("800"));
		letter.setClaimant(claimant);
		letter.persist();
		letter.clear();
		
		claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		assertThat(claimantStatusRules.getStatus()).isEqualTo(ClaimStatus.NIGO);
		
	}
	
	private static Claimant newClaimant() {
		Claimant claimant = new Claimant();
		claimant.setBrokerId("5578");
		claimant.setFundAccountNo("66666");
		claimant.setReferenceNo("100007");
		claimant.setClaimantRegistration(new ClaimantRegistration());
		claimant.getClaimantRegistration()
				.setRegistration1("RegistrationName1");
		claimant.getClaimantRegistration()
				.setRegistration2("RegistrationName2");
		claimant.getClaimantRegistration()
				.setRegistration3("RegistrationName3");
		claimant.getClaimantRegistration()
				.setRegistration4("RegistrationName4");
		setNewPrimaryAddress(claimant);
		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("112-23-3445");
		claimant.setTaxInfo(cl);
		return claimant;
	}
	
	private static void setNewPrimaryAddress(Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setAddress1("address 1");
		a.setCity("NEWYORK");
		a.setStateCode("NY");
		a.setCountryCode("USA");
		a.setZipCode(new ZipCode("23456", "4455"));
		a.setClaimant(p);
		p.addAddress(a);
	}

	private static ClaimantClaim newClaimantClaim() {
		ClaimantClaim claim = new ClaimantClaim();
		claim.setClaimIdentifier("600000090");
		claim.setControlNumber(1);
		claim.setStatus(ClaimStatus.PENDING);
		return claim;
	}

	private static ClaimProof getClaimProof(ProofStatus proofStatus) {
		ClaimProof proof = new ClaimProof();
		proof.setComments("Claim Proof 1");
		proof.setDateReceived(new Date());
		proof.setId(1L);
		proof.setProofRequiredInd(Boolean.TRUE);
		List<String> proofTypes = new ArrayList<String>();
		proofTypes.add("Birth Certificate1");
		proofTypes.add("Driver License");
		proof.setProofTypes(proofTypes);
		proof.setStatus(proofStatus);
		return proof;
	}
	
	
	private static ClaimantPosition claimantPosition(String status){
		ClaimantPosition claimantPosition = new ClaimantPosition();
		ClaimantClaim claimantClaim = newClaimantClaim();
		claimantPosition.setClaimantClaim(claimantClaim);
		claimantPosition.setId(1L);
		claimantPosition.setSecurityFund(getSecurityFund());
		claimantPosition.setPositionType(PositionType.EOD);
		claimantPosition.setPositionDate(new Date());
		claimantPosition.setShareBalance(new BigDecimal(100));
		claimantPosition.setStatus(status);
		return claimantPosition;
	}
	
	private static SecurityFund getSecurityFund(){
		SecurityFund securityFund = new SecurityFund();
		SecurityRef securityRef = new SecurityRef();
		securityFund.setFund("1000");
		securityRef.setSecurityId(1);
		securityRef.setName("XYZ Corp");
		securityRef.setTicker("XYZ");
		securityRef.setCusip("1234");
		securityRef.persist();
		securityFund.setSecurityRef(securityRef);
		securityFund.persist();
		return securityFund;
	}
	
	private static Letter getOptOutCorrespondence(String optOut, LetterStatus letterStatus){
		Letter letter = new Letter();
		LetterCode lc = new LetterCode("800", "Opt Out - Not Signed", LetterType.OPTOUT_CURE_LETTER);
		lc.persist();
		letter.setDescription(optOut);
		letter.setLetterStatus(letterStatus);
		return letter;
	}
	
}
