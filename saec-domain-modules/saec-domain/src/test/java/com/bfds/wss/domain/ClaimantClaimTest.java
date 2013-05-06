package com.bfds.wss.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.wss.domain.reference.ClaimStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class ClaimantClaimTest {

	/**
	 * If the Claim Form was submitted by mail without the Class Member
	 * registering in Web Self Service. Claim Details will entered through SAEC
	 * in this case system has to creates a new {@link ClaimantClaim} details
	 * with Unique Claim Identifier and control numbers.
	 */
	@Test
	public void claimIdentifierAndControllNumberShouldBeGenerated() {

		Claimant claimant = newClaimant("190000011");
		assertThat(claimant).isNotNull();
		ClaimantClaim claim = new ClaimantClaim();
		claim.setDateFiled(new Date());
		claim.setClaimant(claimant);
		claim.setStatus(ClaimStatus.PENDING);
		claim.persist();
		claim.flush();
		claim.clear();

		claimant = Claimant.findClaimant("190000011");
		claim = claimant.getSingleClaimantClaim();
		assertThat(claim).isNotNull();
		assertThat(claim.getClaimIdentifier()).isNotNull();
		assertThat(claim.getControlNumber()).isEqualTo(1);

	}

	/**
	 * If the Class Member opted to Print a personalized Claim Form in WSS and
	 * mail it in, Then it will look the most recent(largest controlNumber)
	 * claimIdentifier and controlNumber in {@link ClaimantClaimId}.
	 */
	@Test
	public void claimIdentifierAndControllNumberShouldBePopulatedFromClaimantClaimId() {

		Claimant claimant = newClaimant("190000012");
		assertThat(claimant).isNotNull();
		ClaimantClaimId claimId = new ClaimantClaimId();
		claimId.setClaimant(claimant);
		claimId.persist();
		claimId.flush();
		claimId.clear();

		claimant = Claimant.findClaimant("190000012");
		ClaimantClaim claim = claimant.getSingleClaimantClaim();
		assertThat(claim).isNull();
		assertThat(claimant.getClaimantClaimIds().size() > 0).isTrue();
		claimId = claimant.getLatestClaimantClaimId();

		assertThat(claimId.getClaimIdentifier()).isNotNull();
		assertThat(claimId.getControlNumber()).isEqualTo(1);
	}

	private static Claimant newClaimant(String reff) {
		Claimant claimant = new Claimant();
		claimant.setBrokerId("5578");
		claimant.setFundAccountNo("66666");
		claimant.setReferenceNo(reff);
		claimant.setClaimantRegistration(new ClaimantRegistration());
		claimant.getClaimantRegistration()
				.setRegistration1("RegistrationName1");
		claimant.getClaimantRegistration()
				.setRegistration2("RegistrationName2");
		claimant.getClaimantRegistration()
				.setRegistration3("RegistrationName3");
		claimant.getClaimantRegistration()
				.setRegistration4("RegistrationName4");
		ClaimantTaxInfo cl = new ClaimantTaxInfo();
		cl.setSsn("112-23-3445");
		claimant.setTaxInfo(cl);
		claimant.persist();
		claimant.flush();
		return claimant;
	}

}
