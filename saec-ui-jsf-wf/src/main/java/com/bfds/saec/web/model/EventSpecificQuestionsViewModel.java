package com.bfds.saec.web.model;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.claim.ClaimAnswer;
import com.bfds.saec.claim.ClaimAnswerGroup;
import com.bfds.saec.claim.ClaimQuestionnaire;
import com.bfds.saec.claim.ClaimQuestionnaireToClaimantUserResponseGroupMapper;
import com.bfds.saec.claim.ClaimantQuestionnaireBuilder;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantStatusRules;
import com.bfds.saec.domain.IClaimantStatusRules;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.web.util.JsfUtils;
import com.bfds.wss.domain.ClaimActivity;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimUserResponseGroup;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.ClaimantReminder.ContactMethod;
import com.bfds.wss.domain.ClaimantReminder.ReminderStatus;
import com.bfds.wss.domain.UserResponseGroup;
import com.bfds.wss.domain.reference.AdditionalQuestions;
import com.bfds.wss.domain.reference.AdditionalQuestionsResponses;
import com.bfds.wss.domain.reference.ClaimActivityType;
import com.bfds.wss.domain.reference.QuestionGroup;
import com.bfds.wss.domain.reference.ReminderType;
import com.bfds.wss.domain.reference.ResponseDisplayCode;
import com.google.common.collect.Lists;

public class EventSpecificQuestionsViewModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger log = LoggerFactory
			.getLogger(EventSpecificQuestionsViewModel.class);

	private Long claimantId;

	private ClaimantClaim claim;

	private ClaimQuestionnaire questionnaire;

	private IClaimantStatusRules claimantStatusRules;

	public ClaimantClaimHeader getClaimantClaimHeader() {
		return new ClaimantClaimHeader(this.claimantId);
	}

	public void buildClaimQuestionnaire() {
		if (this.questionnaire == null) {
			final ClaimantQuestionnaireBuilder builder = new ClaimantQuestionnaireBuilder();
			List<QuestionGroup> questionGroups = QuestionGroup
					.findAllQuestionGroups();
			builder.setQuestionGroups(questionGroups);
			List<UserResponseGroup> userResponseGroups = Lists.newArrayList();
			List<ClaimUserResponseGroup> claimUserResponseGroups = this
					.getClaim().getClaimUserResponseGroups();
			if (claimUserResponseGroups == null) {
				claimUserResponseGroups = Lists.newArrayList();
			}
			userResponseGroups.addAll(claimUserResponseGroups);
			builder.setResponseGroups(userResponseGroups);
			this.questionnaire = builder.build();
		}
		this.questionnaire.prepareForRendering();
		
		if(log.isDebugEnabled()){
			log.debug(String.format("Total AnswerGroups prepared :%s",questionnaire.getAnswerGroups().size()));
		}
	}

	public boolean isProofRequired(final ClaimAnswer answer) {
		final AdditionalQuestions question = answer.getQuestion();
		final ResponseDisplayCode displayCode = question
				.getResponseDisplayCode();
		if (ResponseDisplayCode.TEXT.equals(displayCode)
				|| ResponseDisplayCode.DATE.equals(displayCode)) {
			return question.isProofRequiredInd();
		} else if (ResponseDisplayCode.SELECTION.equals(displayCode)) {
			List<AdditionalQuestionsResponses> responses = answer
					.getAnswerChoices();
			if (!responses.isEmpty() && answer.getTextAnswer() == null) {
				return responses.get(0).isProofRequiredInd();
			}
			for (final AdditionalQuestionsResponses response : responses) {
				if (null != answer.getTextAnswer()
						&& answer.getTextAnswer().equals(
								response.getResponseDescription())) {
					return response.isProofRequiredInd();
				}
			}
		}

		return false;
	}

	/**
	 * Persist the claimantclaim into database
	 */
	@Transactional
	public void saveClaim() {
		this.updateClaimUserResposeGroups();
		this.claim.setStatus(this.claimantStatusRules.getStatus());
		this.claim = this.getClaim().merge();
		this.claim.flush();
		this.claim.clear();
		this.addClaimActivity();
		this.addNewActivity();
		this.updateReminder();
		showMessage("Successful", "Data is saved.");
		if(log.isInfoEnabled()){
			log.info(String.format("Successfully Saved Answers for Questions and claim Status updated to %s",claim.getStatus()));
		}
	}

	private void addClaimActivity() {
		final ClaimActivity claimActivity = ClaimActivity
				.setActivityDefaults(new ClaimActivity());
		if (this.questionnaire.getAnswerGroups().get(0).getId() != null) {
			claimActivity.setActivityType(ClaimActivityType.UPDATED);
		} else {
			claimActivity.setActivityType(ClaimActivityType.CREATED);
		}
		claimActivity.setComments("Event Specific Questions "
				+ claimActivity.getActivityType() + " for ClaimIdentifier"
				+ claim.getClaimIdentifier());
		claimActivity.setSystem("SASEC");
		claimActivity.setStatus(this.claim.getStatus() == null ? ""
				: this.claim.getStatus().toString());
		claimActivity.setClaimantClaim(ClaimantClaim
				.findClaimantClaim(this.claim.getId()));
		claimActivity.persist();
		if(log.isInfoEnabled()){
			log.info(String.format("New Claim Activity is generated for Event Specific Questions with activityType:%s", claimActivity.getActivityType()));
		}
	}

	private void addNewActivity() {
		Activity activity = Activity.setActivityDefaults(new Activity());
		if (questionnaire.getAnswerGroups().get(0).getId() != null) {
			activity.setActivityCode(ActivityCode.UPDATED);
		} else {
			activity.setActivityCode(ActivityCode.CREATED);
		}
		activity.setDescription("Event Specific Questions "
				+ activity.getActivityCode());
		activity.setActivityTypeDescription("Event Specific Questions");
		activity.setClaimant(Claimant.findClaimant(this.claimantId));
		activity.persist();
		if(log.isInfoEnabled()){
			log.info(String.format("New Activity is generated for %s with activityCode:%s", activity.getActivityTypeDescription(), activity.getActivityCode()));
		}
	}
	
	private void updateReminder() {

		if (hasProof(ProofStatus.PENDING)) {
			final String submitDocumentsStr = "Submit Documents";
			final List<ClaimantReminder> submitDocumentsReminders = ClaimantReminder
					.findClaimantReminders(Claimant.findClaimant(claimantId),
							submitDocumentsStr);
			if (submitDocumentsReminders.size() == 0) {
				addNewReminder(submitDocumentsStr);
			}
		}
		if (hasProof(ProofStatus.NIGO)) {
			final String submitForNIGOStr = "Submit for NIGO";
			final List<ClaimantReminder> submitForNIGOReminders = ClaimantReminder
					.findClaimantReminders(Claimant.findClaimant(claimantId),
							submitForNIGOStr);
			if (submitForNIGOReminders.size() == 0) {
				addNewReminder(submitForNIGOStr);
			}
		}

	}
	
	private boolean hasProof(ProofStatus proofStatus) {
		for (ClaimAnswerGroup answerGroup : this.questionnaire
				.getAnswerGroups()) {
			for (ClaimAnswer answer : answerGroup.getAnswers()) {
				if (isProofRequired(answer)
						&& answer.getClaimProof().getId() != null
						&& proofStatus.equals(answer.getClaimProof()
								.getStatus())) {
					if(log.isDebugEnabled()){
						log.debug(String.format("There is a proof associated with answer whose question description is %s and proofStatus is %s", answer.getQuestion().getQuestionDescription(),proofStatus));
					}
					return true;
				}
			}
		}
		if(log.isDebugEnabled()){
			log.debug(String.format("There is no proof associated with any answer having proofStatus:%s", proofStatus));
		}
		return false;
	}
	
	private void addNewReminder(String reminderDesc) {
		ClaimantReminder newClaimantReminder = new ClaimantReminder();
		newClaimantReminder.setClaimant(Claimant.findClaimant(claimantId));
		ReminderType reminderType = ReminderType.findReminderTypeByDescription(reminderDesc);
		newClaimantReminder.setReminderDescription(reminderDesc);
		newClaimantReminder.setReminderType(reminderType);
		newClaimantReminder.setReminderStatus(ReminderStatus.PENDING);
		newClaimantReminder.setReminderDueDate(FollowupRemindersViewModel.getDueDate(reminderType));
		//DEFAULT
		newClaimantReminder.setContactMethod(ContactMethod.EMAIL);
		newClaimantReminder.persist();
		if(log.isInfoEnabled()){
			log.info(String.format("New ClaimantReminder is generated for Claimant#%s with description:%s", claimantId, reminderDesc));
		}
	}

	/**
	 * Loads Responses from buffer into instance variables to persist
	 */
	private void updateClaimUserResposeGroups() {
		for (ClaimAnswerGroup answerGroup : questionnaire.getAnswerGroups()) {
			if (answerGroup.isMultiOccuring()) {
				answerGroup.removeRow(answerGroup.getMaxRowId());
			}
		}
		this.claim.getClaimUserResponseGroups().clear();
		ClaimQuestionnaireToClaimantUserResponseGroupMapper mapper = new ClaimQuestionnaireToClaimantUserResponseGroupMapper();
		for (UserResponseGroup userResponseGroup : mapper.map(questionnaire)) {
			ClaimUserResponseGroup claimUserResponseGroup = (ClaimUserResponseGroup) userResponseGroup;
			claimUserResponseGroup.setClaimantClaim(this.claim);
			this.claim.getClaimUserResponseGroups().add(claimUserResponseGroup);
		}
		if(log.isInfoEnabled()){
			log.info("Updated Claim UserResponseGroups");
		}
	}

	/**
	 * Shows the message on browser
	 * 
	 * @param summary
	 * @param detail
	 */
	public void showMessage(String summary, String detail) {
		FacesMessage msg = new FacesMessage(summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void addRowRowToGroup(ClaimAnswerGroup answerGroup) {
		answerGroup.addRow();
		questionnaire.prepareForRendering();
	}

	public void deleteRowFromGroup(ClaimAnswerGroup answerGroup, Long index) {
		answerGroup.removeRow(index.intValue());
		AccordionPanel panel = (AccordionPanel) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), AccordionPanel.class);
		if (panel != null) {
			List<UIComponent> components = panel.getChildren();
			for (Iterator<UIComponent> itr = components.iterator(); itr
					.hasNext();) {
				UIComponent component = itr.next();
				if (component instanceof Tab) {
					itr.remove();
				}
			}
		}
	}

	public String getCSSStyleClass(ClaimProof claimProof) {
		if (claimProof != null) {
			if (ProofStatus.PENDING.equals(claimProof.getStatus())) {
				return "yellowpending";
			}
			if (ProofStatus.NIGO.equals(claimProof.getStatus())) {
				return "rednigo";
			}
		}
		return "";
	}

	public void setClaimantId(final Long claimantId) {
		this.claimantId = claimantId;
		this.claim = Claimant.findClaimant(claimantId).getSingleClaimantClaim();
		this.claimantStatusRules = new ClaimantStatusRules(claimantId);
	}

	public ClaimantClaim getClaim() {
		return this.claim;
	}

	public void setClaim(final ClaimantClaim claim) {
		this.claim = claim;
	}

	public ClaimQuestionnaire getQuestionnaire() {
		return this.questionnaire;
	}

}
