/**
 * 
 */
package com.bfds.saec.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.bfds.saec.domain.dto.ClaimantTransactionCriteriaDto;
import com.bfds.saec.web.ui.components.BaseLazyDataModel;
import com.bfds.saec.web.util.JsfUtils;
import com.bfds.wss.domain.ClaimActivity;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.ClaimantReminder.ContactMethod;
import com.bfds.wss.domain.ClaimantReminder.ReminderStatus;
import com.bfds.wss.domain.ClaimantTransactions;
import com.bfds.wss.domain.reference.ClaimActivityType;
import com.bfds.wss.domain.reference.ReminderType;
import com.bfds.wss.domain.reference.SecurityFund;
import com.bfds.wss.domain.reference.SecurityRef;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author dt83019
 *
 */
public class ClaimantTransactionsViewModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ClaimantTransactions claimantTransactions = newClaimantTransactions();
	
	private List<ClaimantTransactions> claimantTransactionsList = new ArrayList<ClaimantTransactions>();
	
	private Claimant claimant;
	
	private ClaimantClaim claimantClaim;
	
	private SecurityFund selectedFund;
	
	private boolean showDialog;
	
	private ClaimantTransactionCriteriaDto param = new ClaimantTransactionCriteriaDto();
	
	private IClaimantStatusRules claimantStatusRules;
	
	private Long transactionId;
	
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
	
	private ClaimantTransactions newClaimantTransactions() {
		final ClaimantTransactions claimantTransactions = new ClaimantTransactions();
		final SecurityFund securityFund = new SecurityFund();
		securityFund.setSecurityRef(new SecurityRef());
		claimantTransactions.setSecurityFund(securityFund);
		return claimantTransactions;
	}

	public ClaimantClaimHeader getClaimantClaimHeader() {
		return new ClaimantClaimHeader(this.claimant.getId());
	}
	
	/**
	 * Data will be sourced from the following tables:
	 * {@link ClaimantTransactions} 
	 * {@link SecurityRef}
	 * {@link SecurityFund} (when adding transactions)
	 * {@link }Trans Type (when adding transactions)
	 * {@link ClaimProof}
	 * @return the claimantTransactionsList
	 */
	public BaseLazyDataModel<ClaimantTransactions> getClaimantTransaction() {

		param.setClaimantClaim(Claimant.findClaimant(this.claimant.getId()).getSingleClaimantClaim());

		final ClaimantTransactions claimantTransactions = new ClaimantTransactions();

		BaseLazyDataModel<ClaimantTransactions> model = new BaseLazyDataModel<ClaimantTransactions>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ClaimantTransactions> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {

				param.setFirstResult(first);
				param.setMaxResults(pageSize);
				param.setSortField(sortField);
				param.setSortOrder(SortOrder.ASCENDING == sortOrder ? ClaimantTransactionCriteriaDto.SORT_ORDER_ASC
						: ClaimantTransactionCriteriaDto.SORT_ORDER_DESC);
				param.setFilters(filters);
				claimantTransactionsList = claimantTransactions.findClaimantTransactionByClaimant(param);
				Collections.sort(claimantTransactionsList, new TransactionsComparator());
				return claimantTransactionsList;
			}

		};

		Long l = claimantTransactions.getClaimantTransactionsCountForClaimant(param);
		if (l != null) {
			int i = (int) l.longValue();
			model.setRowCount(i);
		}

		return model;
	}
	
	
	public static class TransactionsComparator implements java.util.Comparator<ClaimantTransactions> {
		@Override
		public int compare(ClaimantTransactions o1, ClaimantTransactions o2) {
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
	 * Loads all the {@link SecurityFund} and {@link SecurityRef} data for a event
	 * to show in the UI.
	 * @return
	 */
	public List<SecurityFund> getAllAssets(){
		return SecurityFund.findAllSecurityFunds();
	}
	
	/**
	 * Adding a new {@link ClaimantTransactions}
	 * @return
	 */
	public boolean addNewClaimantTransaction(){
		claimantTransactions = newClaimantTransactions();
		claimantTransactions.setSettlementDate(new Date());
		showHideClaimantTransactionDialog(true);
		setShowDialog(true);
		setShowDelete(false);
		return true;
	}
	
	/**
	 * The values for the Fund, Cusip and Ticker will be system populated based on the Security ID / Fund selected from a drop-down list of 
	 * Security ID/Fund combos configured for the event (via the Security and Security_Fund tables).
	 * @param secFund
	 */
	public void onChangeAsset(SecurityFund secFund){
		selectedFund = SecurityFund.findSecurityFund(secFund.getId());
		claimantTransactions.getSecurityFund().setFund(selectedFund.getFund());
		claimantTransactions.getSecurityFund().getSecurityRef().setCusip(selectedFund.getSecurityRef().getCusip());
		claimantTransactions.getSecurityFund().getSecurityRef().setTicker(selectedFund.getSecurityRef().getTicker());
	}


	private void showHideClaimantTransactionDialog(boolean val) {
		Dialog editDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "editDialog");
		editDialog.setVisible(val);
	}
	
	/**
	 * Saves the {@link ClaimantTransactions} data to DB 
	 * @return
	 */
	public boolean saveClaimantTransactions(final MessageContext messageContext){
		if(!validateMandatoryFields(this.claimantTransactions)){
			messageContext.addMessage(new MessageBuilder().error().source("form:editDialog").defaultText("Please fill all Required fields").build());
			return false;
		}
			this.claimantTransactions.setClaimantClaim(this.claimantClaim);
			this.claimantTransactions.setSecurityFund(this.getSelectedFund());
			addNewActivity();
			addClaimActivity();
			if (claimantTransactions.getId() != null) {
				claimantTransactions.merge();
			} else {
				claimantTransactions.persist();
			}
			updateRecalcRequiredFlag();
			setShowDialog(false);
			showHideClaimantTransactionDialog(false);
		return true;
	}
	
	private boolean validateMandatoryFields(ClaimantTransactions claimantTransactions)
	{
		if(claimantTransactions.getTransactionCode() !=null && claimantTransactions.getTransactionType() != null 
				&& claimantTransactions.getTradeDate() != null && claimantTransactions.getQuantity() != null){
			return true ;
		}	
		return false;
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
	 * Saving the edited {@link ClaimProof} of a {@link ClaimantTransactions}.
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
		if(this.claimantTransactions.getDeleted() == null ){
			updateReminder();
			updateClaimStatusRules();
			resolveFollowUpReminder();
		}
		return true;
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
	 * To edit an individual record, the user should click on the ID field for the desired {@link ClaimantTransactions}.
	 * The existing data will populate the form and the user can make the appropriate changes to {@link ClaimantTransactions}.
	 * @param id
	 * @return
	 */
	public boolean loadTransactionById(Long id){
		claimantTransactionsById(id);
		showHideClaimantTransactionDialog(true);
		setShowDialog(true);
		setShowDelete(true);
		return true;
	}

	/**
	 * @param id
	 */
	private void claimantTransactionsById(Long id) {
		setTransactionId(id);
		this.claimantTransactions = ClaimantTransactions.findClaimantTransactions(id);
		this.setSelectedFund(this.claimantTransactions.getSecurityFund());
	}
	
	/**
	 * @param id
	 * @param claimProofViewModel
	 * @return
	 */
	public boolean loadClaimProofByClaimantTransaction(Long id,ClaimProofViewModel claimProofViewModel){
		claimantTransactionsById(id);
		ClaimProof claimProof = ClaimProof.getProofOfClaimantTransactions(id);
		claimProofViewModel.setClaimProof(claimProof);
		claimProofViewModel.setClaimantClaim(this.claimantClaim);
		claimProofViewModel.setProofTypes(Sets.newHashSet(claimProof.getProofTypes()));
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
		for(ClaimantTransactions trans : this.claimantTransactionsList){
			if(trans.getProof() != null){
				if(trans.getProof().getId() != null && proofStatus.equals(trans.getProof().getStatus())){
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
	
	private void addClaimActivity() {
		final ClaimActivity claimActivity = ClaimActivity
				.setActivityDefaults(new ClaimActivity());
		if (this.claimantTransactions.getId() != null) {
			claimActivity.setActivityType(ClaimActivityType.UPDATED);
		} else {
			claimActivity.setActivityType(ClaimActivityType.CREATED);
		}
		claimActivity.setComments(this.claimantTransactions.getComments());
		claimActivity.setSystem("SASEC");
		claimActivity.setStatus(this.claimantClaim.getStatus() == null ? ""
				: this.claimantClaim.getStatus().toString());
		claimActivity.setClaimantClaim(ClaimantClaim
				.findClaimantClaim(this.claimantClaim.getId()));
		claimActivity.persist();
		
	}
	
	private void addNewActivity() {
		Activity activity = Activity.setActivityDefaults(new Activity());
		if (this.claimantTransactions.getId() != null) {
			activity.setActivityCode(ActivityCode.UPDATED);
			activity.setDescription("Claimant Transactions Updated");
		} else{
			activity.setActivityCode(ActivityCode.CREATED);
			activity.setDescription("Claimant Transactions Created");
		}
		activity.setActivityCode(null);
		activity.setActivityTypeDescription("Claimant Transactions");
		activity.setClaimant(this.claimant);
		activity.persist();
	}
	
	public boolean areAllProofsIGO() {
		return ClaimantTransactions.getClaimProofs(this.claimantClaim,
				Lists.newArrayList(ProofStatus.IGO)).size() == ClaimantTransactions
				.getProofsOfClaimantTransactions(this.claimantClaim).size();
	}
	
	public boolean deleteClaimantTransaction(){
		this.claimantTransactions = ClaimantTransactions.findClaimantTransactions(getTransactionId());
		if(this.claimantTransactions.getId() != null){
			this.claimantTransactions.setDeleted(Boolean.TRUE);
			this.claimantTransactions.merge();
		}
		setShowDialog(false);
		showHideClaimantTransactionDialog(false);
		return true;
	}
	
	public boolean restoreClaimantTransaction(){
		this.claimantTransactions = ClaimantTransactions.findClaimantTransactions(getTransactionId());
		if(this.claimantTransactions.getId() != null){
			this.claimantTransactions.setDeleted(Boolean.FALSE);
			this.claimantTransactions.merge();
		}
		setShowDialog(false);
		showHideClaimantTransactionDialog(false);
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
	
	/**
	 * @return the claimantTransactions
	 */
	public ClaimantTransactions getClaimantTransactions() {
		return claimantTransactions;
	}


	/**
	 * @param claimantTransactions the claimantTransactions to set
	 */
	public void setClaimantTransactions(ClaimantTransactions claimantTransactions) {
		this.claimantTransactions = claimantTransactions;
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
	 * @return the transactionId
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
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
