/**
 * 
 */
package com.bfds.saec.web.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfds.saec.claim.ClaimAnswer;
import com.bfds.saec.claim.ClaimAnswerGroup;
import com.bfds.wss.domain.ClaimProof;

/**
 * @author dt83019
 * 
 */
public class QuestionsClaimProofViewModel extends ClaimProofViewModel {

	private static final long serialVersionUID = 1L;
	
	final static Logger log = LoggerFactory
			.getLogger(QuestionsClaimProofViewModel.class);

	ClaimAnswer claimAnswer;

	ClaimAnswerGroup answerGroup;

	String proofFor = "";

	@Override
	public boolean saveClaimSupportingDocuments() {
		ClaimProof claimProof = getClaimProof();
		claimProof.setClaimantClaim(getClaimantClaim());
		List<String> proofs = new ArrayList<String>();
		for (String str : getProofTypes()) {
			proofs.add(str);
		}
		claimProof.setProofTypes(proofs);
		if (claimProof.getId() != null) {
			claimProof.merge();
			if(log.isInfoEnabled()){
				log.info(String.format("ClaimProof id#%s for claimIdentifier#%s is updated", claimProof.getId(), claimProof.getClaimantClaim().getClaimIdentifier()));
			}
		} else {
			claimProof.persist();
			if(log.isInfoEnabled()){
				log.info(String.format("New ClaimProof id#%s for claimIdentifier#%s is created", claimProof.getId(), claimProof.getClaimantClaim().getClaimIdentifier()));
			}
		}
		if ("claimAnswer".equals(proofFor)) {
			this.claimAnswer.setClaimProof(ClaimProof.findClaimProof(this
					.getClaimProof().getId()));
		} else if ("claimAnswerGroup".equals(proofFor)) {
			this.answerGroup.setClaimProof(ClaimProof.findClaimProof(this
					.getClaimProof().getId()));
		}
		return true;
	}

	public ClaimAnswer getClaimAnswer() {
		return claimAnswer;
	}

	public void setClaimAnswer(ClaimAnswer claimAnswer) {
		this.claimAnswer = claimAnswer;
	}

	public ClaimAnswerGroup getAnswerGroup() {
		return answerGroup;
	}

	public void setAnswerGroup(ClaimAnswerGroup answerGroup) {
		this.answerGroup = answerGroup;
	}

	public String getProofFor() {
		return proofFor;
	}

	public void setProofFor(String proofFor) {
		this.proofFor = proofFor;
	}

}
