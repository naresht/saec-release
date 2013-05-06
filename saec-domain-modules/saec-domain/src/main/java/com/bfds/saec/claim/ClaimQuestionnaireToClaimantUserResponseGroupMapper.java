package com.bfds.saec.claim;

import com.bfds.wss.domain.*;

public class ClaimQuestionnaireToClaimantUserResponseGroupMapper extends
		ClaimQuestionnaireToUserResponseGroupMapper {

	@Override
	protected UserResponse newUserResponse() {
		return new ClaimantUserResponses();
	}

	@Override
	protected UserResponseGroup newUserResponseGroup() {
		return new ClaimUserResponseGroup();
	}

	@Override
	public void postUpdateUserResponse(ClaimAnswer answer,
			UserResponse userResponse) {
		if (answer.getClaimProof().getId() != null) {
			((ClaimantUserResponses) userResponse).setProof(ClaimProof
					.findClaimProof(answer.getClaimProof().getId()));
		}
	}

	@Override
	protected void postUpdateUserResponseGroup(ClaimAnswerGroup answerGroup,
			UserResponseGroup userResponseGroup) {
		super.postUpdateUserResponseGroup(answerGroup, userResponseGroup);
		if (answerGroup.getClaimProof().getId() != null) {
			((ClaimUserResponseGroup) userResponseGroup).setProof(ClaimProof
					.findClaimProof(answerGroup.getClaimProof().getId()));
		}
	}

}