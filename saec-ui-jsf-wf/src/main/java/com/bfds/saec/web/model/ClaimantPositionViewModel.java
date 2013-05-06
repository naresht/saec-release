/**
 * 
 */
package com.bfds.saec.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.primefaces.component.dialog.Dialog;
import org.primefaces.model.SortOrder;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantCalculationAttributes;
import com.bfds.saec.domain.ClaimantStatusRules;
import com.bfds.saec.domain.IClaimantStatusRules;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.domain.dto.ClaimantPositionCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.web.ui.components.BaseLazyDataModel;
import com.bfds.saec.web.util.JsfUtils;
import com.bfds.wss.domain.ClaimActivity;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantPosition;
import com.bfds.wss.domain.ClaimantPosition.PositionType;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.ClaimantReminder.ContactMethod;
import com.bfds.wss.domain.ClaimantReminder.ReminderStatus;
import com.bfds.wss.domain.ClaimantTransactions;
import com.bfds.wss.domain.reference.ClaimActivityType;
import com.bfds.wss.domain.reference.ReminderType;
import com.bfds.wss.domain.reference.SecurityFund;
import com.bfds.wss.domain.reference.SecurityRef;
import com.google.common.collect.Lists;

/**
 * @author dt83019
 *
 */
public class ClaimantPositionViewModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ClaimantPosition claimantPosition = newClaimantPosition();
	
	private List<ClaimantPosition> claimantPositionList = new ArrayList<ClaimantPosition>();
	
	private Claimant claimant;
	
	private ClaimantClaim claimantClaim;
	
	private SecurityFund selectedFund;
	
	private boolean showDialog;
	
	private ClaimantPositionCriteriaDto param = new ClaimantPositionCriteriaDto();
	
	private IClaimantStatusRules claimantStatusRules;
	
	private Long positionId;
	
	private boolean showDelete;
	
	public void prepareViewModel(Long claimantId) {
		claimant = Claimant.findClaimant(claimantId, Claimant.ASSOCIATION_ALL);
		if (claimant.getSingleClaimantClaim() != null) {
			this.claimantClaim = this.claimant.getSingleClaimantClaim();

		} else {
			this.claimantClaim = new ClaimantClaim();
		}
		this.claimantStatusRules = new ClaimantStatusRules(this.claimant.getId());
	}
	
	private ClaimantPosition newClaimantPosition() {
		final ClaimantPosition claimantPosition = new ClaimantPosition();
		final SecurityFund securityFund = new SecurityFund();
		securityFund.setSecurityRef(new SecurityRef());
		claimantPosition.setSecurityFund(securityFund);
		return claimantPosition;
	}

	public ClaimantClaimHeader getClaimantClaimHeader() {
		return new ClaimantClaimHeader(this.claimant.getId());
	}
	
	/**
	 * @return
	 */
	public BaseLazyDataModel<ClaimantPosition> getClaimantPositions() {

		param.setClaimantClaim(Claimant.findClaimant(this.claimant.getId()).getSingleClaimantClaim());

		final ClaimantPosition claimantPosition = new ClaimantPosition();

		BaseLazyDataModel<ClaimantPosition> model = new BaseLazyDataModel<ClaimantPosition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ClaimantPosition> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {

				param.setFirstResult(first);
				param.setMaxResults(pageSize);
				param.setSortField(sortField);
				param.setSortOrder(SortOrder.ASCENDING == sortOrder ? ClaimantSearchCriteriaDto.SORT_ORDER_ASC
						: ClaimantSearchCriteriaDto.SORT_ORDER_DESC);
				param.setFilters(filters);
				claimantPositionList = claimantPosition
						.findClaimantPositionByClaimant(param);
				Collections.sort(claimantPositionList, new PositionsComparator());
				return claimantPositionList;
			}

		};

		Long l = claimantPosition.getClaimantPositionCountForClaimant(param);
		if (l != null) {
			int i = (int) l.longValue();
			model.setRowCount(i);
		}

		return model;
	}
	
	public static class PositionsComparator implements java.util.Comparator<ClaimantPosition> {
		@Override
		public int compare(ClaimantPosition o1, ClaimantPosition o2) {
			if (o1.getDeleted() == null) {
				if (o2.getDeleted() == null) {
					return 0;
				} else {
					return -1;
				}
			} else if (o2.getDeleted() == null) {
				return 1;
			} else {
				return 0 - o1.getDeleted().compareTo(o2.getDeleted());
			}

		}
	}
	
	/**
	 * Loading the {@link SecurityFund}
	 * @return
	 */
	public List<SecurityFund> getAllAssets(){
		return SecurityFund.findAllSecurityFunds();
	}
	
	/**
	 * Adding a new {@link ClaimantPosition}
	 * @return
	 */
	public boolean addNewClaimantPosition(){
		claimantPosition = newClaimantPosition();
		showHideClaimantPositionDialog(true);
		setShowDialog(true);
		setShowDelete(false);
		return true;
	}
	
	/**
	 * The values for the Fund, Cusip and Ticker will be system populated based on the {@link SecurityFund} Security ID / Fund selected from a drop-down list of 
	 * Security ID/Fund combos configured for the event (via the Security and Security_Fund tables).
	 * @param secFund
	 */
	public void onChangeAsset(SecurityFund secFund){
		selectedFund = SecurityFund.findSecurityFund(secFund.getId());
		claimantPosition.getSecurityFund().setFund(selectedFund.getFund());
		claimantPosition.getSecurityFund().getSecurityRef().setCusip(selectedFund.getSecurityRef().getCusip());
		claimantPosition.getSecurityFund().getSecurityRef().setTicker(selectedFund.getSecurityRef().getTicker());
	}


	/**
	 * To Show/Hide the edit dialog
	 * @param val
	 */
	private void showHideClaimantPositionDialog(boolean val) {
		Dialog editDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "editDialog");
		editDialog.setVisible(val);
	}
	
	/**
	 * Data is saved to the database (i.e. each row is saved individually).  
	 * The page should be refreshed so the changes are reflected.
	 * @return
	 */
	public boolean saveClaimantPosition(final MessageContext messageContext){
		if(!validateMandatoryFields(this.claimantPosition)){
			messageContext.addMessage(new MessageBuilder().error().source("form:editDialog").defaultText("Please fill all Required fields").build());
			return false;
		}
			this.claimantPosition.setClaimantClaim(this.claimantClaim);
			this.claimantPosition.setSecurityFund(this.getSelectedFund());
			addNewActivity();
			addClaimActivity();
			if (claimantPosition.getId() != null) {
				claimantPosition.merge();
			} else {
				claimantPosition.persist();
			}
			updateRecalcRequiredFlag();
			setShowDialog(false);
			showHideClaimantPositionDialog(false);
		return true;
	}
	
	/**
	 * Written this method to validate the required fields.
	 * There are some problems we faced if do these validations in xhtml
	 * <p>1. AddNew click on save then required field validations are fired.</p>
	 * <p>2. Now click on id to edit the record then the validation messages are coming instead of pre population of position values.</p>
	 * <p>3. If we keep immediate="true" on id link then we are unable to invoke the server side action method.</p> 
	 * @param claimantPosition
	 * @return
	 */
	private boolean validateMandatoryFields(ClaimantPosition claimantPosition)
	{
		if(claimantPosition.getShareBalance() !=null && claimantPosition.getPositionType() != null 
				&& claimantPosition.getPositionDate() != null && this.selectedFund != null){
			return true ;
		}	
		return false;
	}
	
	/**
	 * Saving the edited {@link ClaimProof} of a {@link ClaimantPosition}.
	 * Applying the claim status rules and updating the {@link ClaimantReminder} 
	 * @param claimProofViewModel
	 * @return
	 */
	public boolean saveClaimSupportingDocuments(ClaimProofViewModel claimProofViewModel) {
		ClaimProof claimProof = claimProofViewModel.getClaimProof();
		claimProof.setClaimantClaim(this.claimantClaim);
		List<String> proofs = new ArrayList<String>();
		for (String str : claimProofViewModel.getProofTypes()) {
			proofs.add(str);
		}
		claimProof.setProofTypes(proofs);
		if (claimProof.getId() != null) {
			claimProof.merge();
		}
		updateReminder();
		updateClaimStatusRules();
		resolveFollowUpReminder();
		return true;
	}
	
	/**
	 * Since the entry/update of a {@link ClaimantTransactions} record would likely impact a Class Member's calculated Daily Position, 
	 * the claimant's 'Recalc Required' flag (and 'process_status'?) should be set when an entry/update is submitted, 
	 * so that the claimant’s Daily Positions will be calculated again.
	 */
	private void updateRecalcRequiredFlag() {
		List<ClaimantCalculationAttributes> claimantCalculationAttributes = ClaimantCalculationAttributes.findClaimantCalculationAttributesByClaimant(this.claimant);
		for(ClaimantCalculationAttributes attrbts : claimantCalculationAttributes){
			attrbts.setReCalculatePositions(Boolean.TRUE);
		}
	}
	
	/**
	 * Applying the Claim Status algorithm to determine the {@link ClaimantClaim} status.
	 */
	private void updateClaimStatusRules() {
		this.claimantClaim.setStatus(this.claimantStatusRules.getStatus());
		this.claimantClaim.merge();
		this.claimantClaim.flush();
		this.claimantClaim.clear();
	}
	
	/**
	 * The existing {@link ClaimantPosition} will populate the form and the user can make the appropriate changes
	 * @param id
	 * @return
	 */
	public boolean loadPositionById(Long id){
		claimantPositionById(id);
		showHideClaimantPositionDialog(true);
		setShowDialog(true);
		setShowDelete(true);
		return true;
	}

	/**
	 * @param id
	 */
	private void claimantPositionById(Long id) {
		setPositionId(id);
		this.claimantPosition = ClaimantPosition.findClaimantPosition(id);
		this.setSelectedFund(this.claimantPosition.getSecurityFund());
	}
	
	public boolean loadClaimProofByClaimantPosition(Long id,ClaimProofViewModel claimProofViewModel){
		claimantPositionById(id);
		ClaimProof claimProof = ClaimantPosition.getProofOfClaimantPositionById(id);
		claimProofViewModel.setClaimProof(claimProof);
		claimProofViewModel.setClaimantClaim(this.claimantClaim);
		claimProofViewModel.setItemId(id);
		return true;
	}
	
	/**
	 * If there are any required {@link ClaimProof}s in a "Pending" status, a single Follow-up Reminder record of type "Submit Supporting Documents" 
	 * should be created if a {@link ClaimantReminder} of the same type does not already exist for the {@link Claimant}.
	 * 
	 * If there are any required {@link ClaimProof}s in a "NIGO" status, a single Follow-up Reminder record of type "Submit for NIGO" 
	 * should be created if a {@link ClaimantReminder} of the same type does not already exist for the {@link Claimant}.
	 * 
	 */
	private void updateReminder() {

		if (hasProof(ProofStatus.PENDING)) {
			final String submitDocumentsStr = "Submit Documents";
			final List<ClaimantReminder> submitDocumentsReminders = ClaimantReminder
					.findClaimantReminders(Claimant.findClaimant(this.claimant.getId()),
							submitDocumentsStr);
			if (submitDocumentsReminders.size() == 0) {
				addNewReminder(submitDocumentsStr);
			}
		}
		if (hasProof(ProofStatus.NIGO)) {
			final String submitForNIGOStr = "Submit for NIGO";
			final List<ClaimantReminder> submitForNIGOReminders = ClaimantReminder
					.findClaimantReminders(Claimant.findClaimant(this.claimant.getId()),
							submitForNIGOStr);
			if (submitForNIGOReminders.size() == 0) {
				addNewReminder(submitForNIGOStr);
			}
		}

	}
	
	private boolean hasProof(ProofStatus proofStatus) {
		for(ClaimantPosition position : this.claimantPositionList){
			if(position.getProof() != null){
				if(position.getProof().getId() != null && proofStatus.equals(position.getProof().getStatus())){
					return true;
			}
			}
				
		}
		return false;
	}
	
	private void addNewReminder(String reminderDesc) {
		ClaimantReminder newClaimantReminder = new ClaimantReminder();
		newClaimantReminder.setClaimant(Claimant.findClaimant(this.claimant.getId()));
		newClaimantReminder.setClaimantClaim(this.claimantClaim);
		ReminderType reminderType = ReminderType.findReminderTypeByDescription(reminderDesc);
		newClaimantReminder.setReminderDescription(reminderDesc);
		newClaimantReminder.setReminderType(reminderType);
		newClaimantReminder.setReminderStatus(ReminderStatus.PENDING);
		newClaimantReminder.setReminderDueDate(FollowupRemindersViewModel.getDueDate(reminderType));
		//DEFAULT
		newClaimantReminder.setContactMethod(ContactMethod.EMAIL);
		newClaimantReminder.persist();
	}
	
	private void addClaimActivity() {
		final ClaimActivity claimActivity = ClaimActivity
				.setActivityDefaults(new ClaimActivity());
		if (this.claimantPosition.getId() != null) {
			claimActivity.setActivityType(ClaimActivityType.UPDATED);
		} else {
			claimActivity.setActivityType(ClaimActivityType.CREATED);
		}
		claimActivity.setComments(this.claimantPosition.getComments());
		claimActivity.setSystem("SASEC");
		claimActivity.setStatus(this.claimantClaim.getStatus() == null ? ""
				: this.claimantClaim.getStatus().toString());
		claimActivity.setClaimantClaim(ClaimantClaim
				.findClaimantClaim(this.claimantClaim.getId()));
		claimActivity.persist();
		
	}
	
	private void addNewActivity() {
		Activity activity = Activity.setActivityDefaults(new Activity());
		if (this.claimantPosition.getId() != null) {
			activity.setActivityCode(ActivityCode.UPDATED);
			activity.setDescription("Claimant Position Updated");
		} else{
			activity.setActivityCode(ActivityCode.CREATED);
			activity.setDescription("Claimant Position Created");
		}
		activity.setActivityCode(null);
		activity.setActivityTypeDescription("Claimant Position");
		activity.setClaimant(this.claimant);
		activity.persist();
	}
	
	/**
	 * When saving a {@link ClaimProof}, if all {@link ClaimProof}s of {@link ClaimantTransactions} are in IGO status, then any Follow-up Reminder record with the description 
	 * "Submit Supporting Documentation" should be set to "Complete".
	 */
	private void resolveFollowUpReminder() {
		if(areAllProofsIGO()){
			List<ClaimantReminder> claimantReminders = ClaimantReminder.findClaimantReminderByClaimantClaim(this.claimantClaim);
			for(ClaimantReminder reminder : claimantReminders){
				reminder.setReminderStatus(ClaimantReminder.ReminderStatus.COMPLETE);
				reminder.merge();
			}
			
		}
	}
	
	public boolean areAllProofsIGO() {
		return ClaimantPosition.getClaimProofs(this.claimantClaim,
				Lists.newArrayList(ProofStatus.IGO)).size() == ClaimantPosition
				.getProofsOfClaimantPosition(this.claimantClaim).size();
	}
	
	public boolean deleteClaimantPosition(){
		this.claimantPosition = ClaimantPosition.findClaimantPosition(getPositionId());
		if(this.claimantPosition.getId() != null){
			this.claimantPosition.setDeleted(Boolean.TRUE);
			this.claimantPosition.merge();
		}
		setShowDialog(false);
		showHideClaimantPositionDialog(false);
		return true;
	}
	
	public boolean restoreClaimantPosition(){
		this.claimantPosition = ClaimantPosition.findClaimantPosition(getPositionId());
		if(this.claimantPosition.getId() != null){
			this.claimantPosition.setDeleted(Boolean.FALSE);
			this.claimantPosition.merge();
		}
		setShowDialog(false);
		showHideClaimantPositionDialog(false);
		return true;
	}
	
	/**
	 * A {@link ClaimProof} status of 'Pending' will be highlighted in Yellow, 
	 * while a {@link ClaimProof} status of 'NIGO' will be highlighted in Red.
	 * @param claimProof
	 * @return
	 */
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
	
	public PositionType getEod() {
		return PositionType.EOD;
	}
	
	public PositionType getMe() {
		return PositionType.ME;
	}
	
	public PositionType getQe() {
		return PositionType.QE;
	}
	
	public PositionType getYe() {
		return PositionType.YE;
	}
	
	/**
	 * @return the claimantPositionList
	 */
	public List<ClaimantPosition> getClaimantPositionList() {
		return claimantPositionList;
	}

	/**
	 * @param claimantPositionList the claimantPositionList to set
	 */
	public void setClaimantPositionList(List<ClaimantPosition> claimantPositionList) {
		this.claimantPositionList = claimantPositionList;
	}
	
	/**
	 * @return the claimantPosition
	 */
	public ClaimantPosition getClaimantPosition() {
		return claimantPosition;
	}

	/**
	 * @param claimantPosition the claimantPosition to set
	 */
	public void setClaimantPosition(ClaimantPosition claimantPosition) {
		this.claimantPosition = claimantPosition;
	}

	/**
	 * @return the claimant
	 */
	public Claimant getClaimant() {
		return claimant;
	}


	/**
	 * @param claimant the claimant to set
	 */
	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}


	/**
	 * @return the claimantClaim
	 */
	public ClaimantClaim getClaimantClaim() {
		return claimantClaim;
	}


	/**
	 * @param claimantClaim the claimantClaim to set
	 */
	public void setClaimantClaim(ClaimantClaim claimantClaim) {
		this.claimantClaim = claimantClaim;
	}

	/**
	 * @return the selectedFund
	 */
	public SecurityFund getSelectedFund() {
		return selectedFund;
	}

	/**
	 * @param selectedFund the selectedFund to set
	 */
	public void setSelectedFund(SecurityFund selectedFund) {
		this.selectedFund = selectedFund;
	}

	public boolean isShowDialog() {
		return showDialog;
	}

	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}

	/**
	 * @return the positionId
	 */
	public Long getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId the positionId to set
	 */
	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	/**
	 * @return the showDelete
	 */
	public boolean isShowDelete() {
		return showDelete;
	}

	/**
	 * @param showDelete the showDelete to set
	 */
	public void setShowDelete(boolean showDelete) {
		this.showDelete = showDelete;
	}
	
	
	
	

}
