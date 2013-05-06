package com.bfds.saec.claim;

import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimUserResponseGroup;
import com.bfds.wss.domain.ClaimantUserResponses;
import com.bfds.wss.domain.UserResponse;
import com.bfds.wss.domain.UserResponseGroup;
import com.bfds.wss.domain.reference.QuestionGroup;
import com.google.common.base.Preconditions;

@RooJavaBean
public class ClaimantQuestionnaireBuilder extends ClaimQuestionnaireBuilder {
	@Override
	protected void postConstruct(ClaimAnswerGroup newClaimAnswerGroup,
			UserResponseGroup userResponseGroup) {
		super.postConstruct(newClaimAnswerGroup, userResponseGroup);
		Preconditions.checkArgument(
				userResponseGroup instanceof ClaimUserResponseGroup,
				"questionGroup must be of type %s",
				ClaimUserResponseGroup.class);

		final ClaimUserResponseGroup claimantUserResponseGroup = (ClaimUserResponseGroup) userResponseGroup;

		if (claimantUserResponseGroup.getProof() != null) {
			ClaimProof claimProof = claimantUserResponseGroup.getProof();
			claimProof.getStatus();
			newClaimAnswerGroup.setClaimProof(claimProof);
		} else {
			newClaimAnswerGroup.setClaimProof(ClaimProof.newClaimProof());
		}
	}

	@Override
	protected void postConstruct(ClaimAnswer newClaimAnswer,
			UserResponse userResponse) {
		super.postConstruct(newClaimAnswer, userResponse);
		Preconditions.checkArgument(
				userResponse instanceof ClaimantUserResponses,
				"userResponse must be of type %s", ClaimantUserResponses.class);
		ClaimantUserResponses claimantUserResponses = (ClaimantUserResponses) userResponse;
		if (claimantUserResponses.getProof() != null) {
			ClaimProof claimProof = claimantUserResponses.getProof();
			claimProof.getStatus();
			newClaimAnswer.setClaimProof(claimProof);
		} else {
			newClaimAnswer.setClaimProof(ClaimProof.newClaimProof());
		}
	}

	@Override
	protected void postConstruct(ClaimAnswerGroup newClaimAnswerGroup,
			QuestionGroup questionGroup) {
		super.postConstruct(newClaimAnswerGroup, questionGroup);
		newClaimAnswerGroup.setClaimProof(ClaimProof.newClaimProof());
	}

	@Override
	protected void postConstruct(ClaimAnswer newClaimAnswer,
			QuestionGroup questionGroup) {
		super.postConstruct(newClaimAnswer, questionGroup);
		newClaimAnswer.setClaimProof(ClaimProof.newClaimProof());
	}
}
