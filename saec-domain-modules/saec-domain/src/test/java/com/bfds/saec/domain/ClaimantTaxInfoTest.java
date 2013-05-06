package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

public class ClaimantTaxInfoTest {

	@Test
	public void getAsFormattedText() {
		ClaimantTaxInfo tax = new ClaimantTaxInfo();
		tax.setCertificationDate(new Date(111, 1, 1));
		tax.setCertificationStatus("cs");
		tax.setCertificationType("ct");
		tax.setEin("111");
		tax.setSsn("222");
		tax.setTaxCountryCode("us");
		tax.setTin("333");
		tax.setUsCitizen(Boolean.TRUE);
		assertThat(tax.getAsFormattedText())
				.isEqualTo(
						"Tax ID/SSN:222, Certification Type:ct, Certification Status:cs, US Citizen:Yes, Tax Country:us, Certification Date:02/01/2011");

		tax = new ClaimantTaxInfo();
		tax.setCertificationDate(new Date(111, 1, 1));
		tax.setCertificationStatus("cs");
		tax.setCertificationType("ct");
		tax.setEin("111");
		// tax.setSsn("222");
		tax.setTaxCountryCode("us");
		tax.setTin("333");
		tax.setUsCitizen(Boolean.TRUE);
		assertThat(tax.getAsFormattedText())
				.isEqualTo(
						"Tax ID/SSN:111, Certification Type:ct, Certification Status:cs, US Citizen:Yes, Tax Country:us, Certification Date:02/01/2011");

		tax = new ClaimantTaxInfo();
		tax.setCertificationDate(new Date(111, 1, 1));
		tax.setCertificationStatus("cs");
		tax.setCertificationType("ct");
		// tax.setEin("111");
		// tax.setSsn("222");
		tax.setTaxCountryCode("us");
		tax.setTin("333");
		tax.setUsCitizen(Boolean.TRUE);
		assertThat(tax.getAsFormattedText())
				.isEqualTo(
						"Tax ID/SSN:333, Certification Type:ct, Certification Status:cs, US Citizen:Yes, Tax Country:us, Certification Date:02/01/2011");

	}

	@Test
	public void testEquals() {
		ClaimantTaxInfo tax = new ClaimantTaxInfo();
		tax.setCertificationDate(new Date(111, 1, 1));
		tax.setCertificationStatus("cs");
		tax.setCertificationType("ct1");
		tax.setEin("111");
		tax.setSsn("222");
		tax.setTaxCountryCode("us");
		tax.setTin("333");
		tax.setUsCitizen(Boolean.TRUE);

		ClaimantTaxInfo tax1 = new ClaimantTaxInfo();
		tax1.setCertificationDate(new Date(111, 1, 1));
		tax1.setCertificationStatus("cs1");
		tax1.setCertificationType("ct1");
		tax1.setEin("111");
		tax1.setSsn("222");
		tax1.setTaxCountryCode("canada");
		tax1.setTin("333");
		tax1.setUsCitizen(Boolean.FALSE);
		assertThat(tax).isEqualTo(tax);
		assertThat(tax).isEqualTo(tax1);
	}

	@Test
	public void getTaxIdentifier() {
		ClaimantTaxInfo tax = new ClaimantTaxInfo();
		tax.setEin("111");
		tax.setSsn("222");
		tax.setTin("333");
		assertThat(tax.getTaxIdentifier()).isEqualTo("222");

		tax = new ClaimantTaxInfo();
		tax.setEin("111");
		// tax.setSsn("222");
		tax.setTin("333");
		assertThat(tax.getTaxIdentifier()).isEqualTo("111");

		tax = new ClaimantTaxInfo();
		// tax.setEin("111");
		// tax.setSsn("222");
		tax.setTin("333");
		assertThat(tax.getTaxIdentifier()).isEqualTo("333");
	}

	@Test
	public void verifyHashcode() {
		ClaimantTaxInfo taxInfo1 = newClaimantTaxInfo("111", "222", "333",
				"cs", "ct", "us", true, new Date(111, 1, 1));
		ClaimantTaxInfo taxInfo2 = newClaimantTaxInfo("111", "222", "333",
				"cs1", "ct2", "CANADA", true, new Date(111, 1, 1));

		assertThat(taxInfo1.hashCode()).isEqualTo(taxInfo2.hashCode());
		assertThat(taxInfo1.hashCode()).isNotEqualTo(
				new ClaimantTaxInfo().hashCode());
		assertThat((new ClaimantTaxInfo()).hashCode()).isEqualTo(
				(new ClaimantTaxInfo()).hashCode());
	}

	@Test
	public void verifyClone() {
		ClaimantTaxInfo taxInfo1 = newClaimantTaxInfo("111", "222", "333",
				"cs", "ct", "us", true, new Date(111, 1, 1));
		ClaimantTaxInfo taxInfo2 = taxInfo1.clone();
		assertThat(taxInfo1).isEqualTo(taxInfo2);
		assertThat(taxInfo2).isEqualTo(taxInfo1);
		assertThat(taxInfo1 == taxInfo2).isFalse();
	}

	@Test
	public void createClaimantTaxInfoAndVerifyAttributes() {
		ClaimantTaxInfo taxInfo1 = newClaimantTaxInfo("111", "222", "333",
				"cs", "ct", "us", true, new Date(111, 1, 1));
		assertThat(taxInfo1.getSsn()).isEqualTo("111");
		assertThat(taxInfo1.getEin()).isEqualTo("222");
		assertThat(taxInfo1.getTin()).isEqualTo("333");
		assertThat(taxInfo1.getCertificationStatus()).isEqualTo("cs");
		assertThat(taxInfo1.getCertificationType()).isEqualTo("ct");
		assertThat(taxInfo1.getTaxCountryCode()).isEqualTo("us");
		assertThat(taxInfo1.getUsCitizen()).isEqualTo(true);
		assertThat(taxInfo1.getCertificationDate()).isEqualTo(
				new Date(111, 1, 1));

	}

	private ClaimantTaxInfo newClaimantTaxInfo(String ssn, String ein,
			String tin, String certificationStatus, String certificationType,
			String taxCountryCode, boolean isUSCitizen, Date certificationDate) {
		ClaimantTaxInfo taxInfo1 = new ClaimantTaxInfo();
		taxInfo1.setSsn(ssn);
		taxInfo1.setEin(ein);
		taxInfo1.setTin(tin);
		taxInfo1.setCertificationStatus(certificationStatus);
		taxInfo1.setCertificationType(certificationType);
		taxInfo1.setTaxCountryCode(taxCountryCode);
		taxInfo1.setUsCitizen(true);
		taxInfo1.setCertificationDate(certificationDate);

		return taxInfo1;
	}
}
