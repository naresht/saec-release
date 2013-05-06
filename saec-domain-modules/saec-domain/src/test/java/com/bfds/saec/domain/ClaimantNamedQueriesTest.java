package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class ClaimantNamedQueriesTest { 

	@Test
	public void getSplitAccounts() {
		Claimant claimant = DataGenerator.newClaimant("Margarita",
				"Barrington",
				"100001",
				"200001",
				"30001",
				"400001",
				"50001",
				"60001",
				false,
				null,
				null, null, null);
		claimant.persist();
		claimant.flush();
		List<Claimant> claimants = claimant.getAlternatePayees();		
		assertThat(claimants).isNotNull();
	}
}
