package com.bfds.saec.batch.util;

import java.util.List;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;

public class TestClaimantDto {
	private Claimant claimant;
	
	private List<Letter> claimantLetters;

	public Claimant getClaimant() {
		return claimant;
	}

	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}

	public List<Letter> getClaimantLetters() {
		return claimantLetters;
	}

	public void setClaimantLetters(List<Letter> claimantLetters) {
		this.claimantLetters = claimantLetters;
	}
		
}
