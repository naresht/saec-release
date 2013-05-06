package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ClaimantRegistrationTest {
	
	@Test
	public void verifyEquals() {
		ClaimantRegistration reg1 = newClaimantRegistration("r1", "r2", "r3", "r4", "r5", "r6");				
		ClaimantRegistration reg2 = newClaimantRegistration("r1", "r2", "r3", "r4", "r5", "r6");
		
		assertThat(reg1).isEqualTo(reg2);
		assertThat(reg1).isEqualTo(reg1);
		assertThat(reg1).isNotEqualTo(null);
		assertThat(reg1).isNotEqualTo("");
		assertThat(reg2).isEqualTo(reg1);
		assertThat(reg1).isNotEqualTo(new ClaimantRegistration());
		assertThat(new ClaimantRegistration()).isNotEqualTo(reg1);
		assertThat(new ClaimantRegistration()).isEqualTo(new ClaimantRegistration());
		
		assertThat(reg1).isEqualTo(reg2);
	}

	@Test
	public void verifyHashcode() {
		ClaimantRegistration reg1 = newClaimantRegistration("r1", "r2", "r3", "r4", "r5", "r6");				
		ClaimantRegistration reg2 = newClaimantRegistration("r1", "r2", "r3", "r4", "r5", "r6");		
		
		assertThat(reg1.hashCode()).isEqualTo(reg2.hashCode());
		assertThat(reg1.hashCode()).isNotEqualTo(new ClaimantRegistration().hashCode());
		assertThat((new ClaimantRegistration()).hashCode()).isEqualTo((new ClaimantRegistration()).hashCode());		
	}
	
	@Test
	public void verifyProperties() {
		ClaimantRegistration reg= newClaimantRegistration("r1", "r2", "r3", "r4", "r5", "r6");						
		
		assertThat(reg.getRegistration1()).isEqualTo("r1");
		assertThat(reg.getRegistration2()).isEqualTo("r2");
		assertThat(reg.getRegistration3()).isEqualTo("r3");
		assertThat(reg.getRegistration4()).isEqualTo("r4");
		assertThat(reg.getRegistration5()).isEqualTo("r5");
		assertThat(reg.getRegistration6()).isEqualTo("r6");
	}
	
	private ClaimantRegistration newClaimantRegistration(String r1,
			String r2, String r3, String r4, String r5,
			String r6) {
		ClaimantRegistration reg1 = new ClaimantRegistration();
		reg1.setRegistration1(r1);
		reg1.setRegistration2(r2);
		reg1.setRegistration3(r3);
		reg1.setRegistration4(r4);
		reg1.setRegistration5(r5);
		reg1.setRegistration6(r6);
		return reg1;
	}
}
