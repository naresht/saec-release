package com.bfds.saec.batch.util;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;

public class TestClaimantItemWriter implements ItemWriter<TestClaimantDto> {

	@Override
	public void write(List<? extends TestClaimantDto> items) throws Exception {
		for (TestClaimantDto claimantDto : items) {
			final List<Letter> letters = claimantDto.getClaimantLetters();	
			final Claimant c = claimantDto.getClaimant();
			c.persist();
			c.merge().getId();
			c.getId();
			for(Letter l : letters) {
				l.setClaimant(c);
				l.persist();
			}
		}

	}

}
