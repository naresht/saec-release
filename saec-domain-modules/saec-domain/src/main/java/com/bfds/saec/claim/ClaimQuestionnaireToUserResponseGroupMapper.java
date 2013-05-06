package com.bfds.saec.claim;

import java.util.List;
import java.util.Map;

import com.bfds.wss.domain.UserResponse;
import com.bfds.wss.domain.UserResponseGroup;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class ClaimQuestionnaireToUserResponseGroupMapper {

	public List<UserResponseGroup> map(ClaimQuestionnaire questionnaire) {
		questionnaire.prepareForPersistence();
		List<UserResponseGroup> userResponseGroups = Lists.newArrayList();
		for (ClaimAnswerGroup answerGroup : questionnaire.getAnswerGroups()) {
			UserResponseGroup userResponseGroup = getUserResponseGroup(answerGroup);
			userResponseGroups.add(userResponseGroup);
		}
		return userResponseGroups;
	}

	private UserResponseGroup getUserResponseGroup(ClaimAnswerGroup answerGroup) {
		answerGroup.prepareForPersistence();
		UserResponseGroup userResponseGroup = null;
		if (answerGroup.getId() == null) {
			userResponseGroup = buildUserResponseGroup(answerGroup);
		} else {
			userResponseGroup = updateUserResponseGroup(answerGroup);
		}
		postUpdateUserResponseGroup(answerGroup, userResponseGroup);
		return userResponseGroup;
	}

	private UserResponseGroup updateUserResponseGroup(
			ClaimAnswerGroup answerGroup) {
		UserResponseGroup userResponseGroup = UserResponseGroup
				.findUserResponseGroup(answerGroup.getId());
		Map<Long, ClaimAnswer> updatedAnswersMap = Maps.newHashMap();
		List<UserResponse> newAnswers = Lists.newArrayList();
		for (ClaimAnswer answer : answerGroup.getAnswers()) {
			if (answer.getId() == null) {
				UserResponse userResponse = newUserResponse();
				updateUserResponse(answer, userResponse);
				newAnswers.add(userResponse);
			} else {
				updatedAnswersMap.put(answer.getId(), answer);
			}
		}

		for (UserResponse userResponse : userResponseGroup.getUserResponses()) {
			ClaimAnswer answer = updatedAnswersMap.get(userResponse.getId());
			if (answer == null) {
				userResponseGroup.removeUserResponse(userResponse);
				userResponse.remove();
				userResponse.flush();
			} else {
				updateUserResponse(answer, userResponse);
			}
		}

		for (UserResponse newUserResponse : newAnswers) {
			userResponseGroup.addUserResponse(newUserResponse);
		}
		return userResponseGroup;
	}

	protected void postUpdateUserResponseGroup(ClaimAnswerGroup answerGroup,
			UserResponseGroup userResponseGroup) {
	}

	private UserResponseGroup buildUserResponseGroup(
			ClaimAnswerGroup answerGroup) {
		UserResponseGroup userResponseGroup;
		userResponseGroup = newUserResponseGroup();
		userResponseGroup.setQuestionGroup(answerGroup.getQuestionGroup());
		for (ClaimAnswer answer : answerGroup.getAnswers()) {
			UserResponse userResponse = newUserResponse();
			updateUserResponse(answer, userResponse);
			userResponseGroup.addUserResponse(userResponse);
		}
		return userResponseGroup;
	}

	private void updateUserResponse(ClaimAnswer answer,
			UserResponse userResponse) {
		userResponse.setResponseDate(answer.getDateAnswer());
		userResponse.setResponseText(answer.getTextAnswer());
		userResponse.setRowId(answer.getRowId());
		userResponse.setAdditionalQuestions(answer.getQuestion());
		postUpdateUserResponse(answer, userResponse);
	}

	protected void postUpdateUserResponse(ClaimAnswer answer,
			UserResponse userResponse) {

	}

	protected abstract UserResponse newUserResponse();

	protected abstract UserResponseGroup newUserResponseGroup();
}
