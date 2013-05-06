package com.bfds.saec.domain.activity;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.bfds.saec.domain.ClaimantTaxInfo;

public class TaxInfoChangeTest {

	
	@Test
	public void verifyActivityDescription() {
		TaxInfoChange activity = new TaxInfoChange();
		
		ClaimantTaxInfo from = new ClaimantTaxInfo();
		from.setCertificationDate(new Date(1111, 11, 20));
		from.setCertificationStatus("cs1");
		from.setCertificationType("ct1");
		from.setEin("1111");
		from.setSsn("2222");
		from.setTin("3333");
		from.setTaxCountryCode("US");
		from.setUsCitizen(true);
		activity.setFrom(from);
		
		ClaimantTaxInfo to = new ClaimantTaxInfo();
		to.setCertificationDate(new Date(1111, 11, 20));
		to.setCertificationStatus("cs1");
		to.setCertificationType("ct1");
		to.setEin("1111");
		to.setSsn("2222");
		to.setTin("3333");
		to.setTaxCountryCode("US");
		to.setUsCitizen(true);
		activity.setTo(to);
		
		assertThat(activity.getDescription()).isEqualTo("Tax ID/SSN:2222, Certification Type:ct1, Certification Status:cs1, US Citizen:Yes, Tax Country:US, Certification Date:12/20/3011 To Tax ID/SSN:2222, Certification Type:ct1, Certification Status:cs1, US Citizen:Yes, Tax Country:US, Certification Date:12/20/3011");
		
		from.setSsn(null);
		assertThat(activity.getDescription()).isEqualTo("Tax ID/SSN:1111, Certification Type:ct1, Certification Status:cs1, US Citizen:Yes, Tax Country:US, Certification Date:12/20/3011 To Tax ID/SSN:2222, Certification Type:ct1, Certification Status:cs1, US Citizen:Yes, Tax Country:US, Certification Date:12/20/3011");
		
		from.setEin(null);
		assertThat(activity.getDescription()).isEqualTo("Tax ID/SSN:3333, Certification Type:ct1, Certification Status:cs1, US Citizen:Yes, Tax Country:US, Certification Date:12/20/3011 To Tax ID/SSN:2222, Certification Type:ct1, Certification Status:cs1, US Citizen:Yes, Tax Country:US, Certification Date:12/20/3011");
	}
	
	@Test
	public void verifyActivityTypeDescription() { 
		Activity activity = new TaxInfoChange();
		assertThat(activity.getActivityTypeDescription()).isEqualTo("Tax Info Change");
	}
}
