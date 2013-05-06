/**
 * 
 */
package com.bfds.saec.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.component.dialog.Dialog;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantStatusRules;
import com.bfds.saec.domain.IClaimantStatusRules;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.util.SaecStringUtils;
import com.bfds.saec.web.util.JsfUtils;
import com.bfds.wss.domain.ClaimActivity;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimProof.Source;
import com.bfds.wss.domain.ClaimUserResponseGroup;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.ClaimantTransactions;
import com.bfds.wss.domain.ClaimantUserResponses;
import com.bfds.wss.domain.reference.ClaimActivityType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author dt83019
 *
 */
public class ClaimProofViewModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Claimant claimant;
	
	private ClaimantClaim claimantClaim;
	
	private ClaimProof claimProof = new ClaimProof();
	
	private List<ClaimProof> allClaimProofs = new ArrayList<ClaimProof>();
	
	private ClaimantTransactions claimantTransactions;
	
	private int claimProofCount = 0;
	
	private List<String> selProof;
	
	private Set<String> proofTypes = new HashSet<String>();
	
	private String selectedCerificate;
	
	private IClaimantStatusRules claimantStatusRules;
	
	private boolean showDialog;
	
	private Long itemId;
	
	/**
	 * To show the header data with {@link Claimant} reference no and {@link ClaimantClaim} claim identifier
	 * @return
	 */
	public ClaimantClaimHeader getClaimantClaimHeader() {
		return new ClaimantClaimHeader(this.claimant.getId());
	}
	
	
	/**
	 * TODO
	 * @return
	 */
	public boolean addNewClaimProof(){
		claimProof = new ClaimProof();
		proofTypes = new HashSet<String>();
		showHideClaimProofDialog(true);
		setShowDialog(true);
		setItemId(null);
		return true;
	}
	
	private void showHideClaimProofDialog(boolean val) {
		Dialog editClaimProofDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "editClaimProofDialog");
		editClaimProofDialog.setVisible(val);
	}
	
	/**
	 * Retrieves The Single {@link ClaimantClaim} of user response group {@link ClaimUserResponseGroup}s <p> 
	 * {@link ClaimProof}s  and {@link ClaimantUserResponses}s {@link ClaimProof}s and <p>
	 * {@link ClaimantTransactions} {@link ClaimProof}s
	 * @return
	 */
	public List<ClaimProof> getAllClaimProofs(Long claimantId) {
		this.claimant = Claimant.findClaimant(claimantId,Claimant.ASSOCIATION_ALL);
		this.claimantClaim = claimant.getSingleClaimantClaim();
		allClaimProofs = ClaimProof.getClaimProofs(claimantId);
		Collections.sort(allClaimProofs, new ClaimProofsComparator());
		claimProofCount = allClaimProofs.size();
		return allClaimProofs;
	}
	
	public static class ClaimProofsComparator implements java.util.Comparator<ClaimProof> {
		@Override
		public int compare(ClaimProof o1, ClaimProof o2) {
			if (o1.getDateReceived() == null) {
				if (o2.getDateReceived() == null) {
					return 0;
				} else {
					return -1;
				}
			} else if (o2.getDateReceived() == null) {
				return 1;
			} else {
				return 0 - o1.getDateReceived().compareTo(o2.getDateReceived());
			}

		}
	}
	
	/**
	 * Creates the {@link ClaimProof} if its a new one<p>
	 * other wise updates the existing {@link ClaimProof}
	 * @return
	 */
	public boolean saveClaimSupportingDocuments(){
		this.claimProof.setClaimantClaim(this.claimantClaim);
		List<String> proofs = new ArrayList<String>();
		for(String str : proofTypes){
			proofs.add(str);
		}
		this.claimProof.setProofTypes(proofs);
		if(!validateRequiredFields(this.claimProof)){
			return false;
		}
		generateActivity(claimProof);
		boolean isSaved = claimProof.getId()==null;
		if (claimProof.getId() != null) {
			claimProof.merge();
		} else {
			claimProof.persist();
		}
		claimProof.flush();
		updateCliamStatus(isSaved);
		updateFollowUpReminder();
		setShowDialog(false);
		showHideClaimProofDialog(false);
		return true;
	}
	
	public boolean validateRequiredFields(ClaimProof proof) {
		FacesContext fc = FacesContext.getCurrentInstance();
		UIInput proofType = (UIInput)JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "suppdoc_proofType");
		UIInput status = (UIInput)JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "suppdoc_status");
		UIInput source = (UIInput)JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "suppdoc_source");
		if(!proof.getProofTypes().isEmpty() && proof.getStatus() != null && proof.getSource() != null){
			return true;
		}
		if( proof.getProofTypes().isEmpty() ){
			FacesMessage msg = new FacesMessage("Please, fill Proof Type");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(proofType.getClientId(),msg);
		}
		if( proof.getStatus() == null ){
			FacesMessage msg = new FacesMessage("Please, fill Status");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(status.getClientId(),msg);
		}
		if( proof.getSource() == null ){
			FacesMessage msg = new FacesMessage("Please, fill Source");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(source.getClientId(),msg);
		}
		return false;
	}

	/**
	 * When saving a {@link ClaimProof}, if all {@link ClaimProof}s are in IGO status, then any Follow-up Reminder record with the description 
	 * "Submit Supporting Documentation" should be set to "Complete".
	 */
	private void updateFollowUpReminder() {
		if(areAllProofsIGO()){
			List<ClaimantReminder> claimantReminders = ClaimantReminder.findClaimantReminderByClaimantClaim(this.claimantClaim);
			for(ClaimantReminder reminder : claimantReminders){
				reminder.setReminderStatus(ClaimantReminder.ReminderStatus.COMPLETE);
				reminder.merge();
			}
			
		}
	}

	/**
	 * Applying the Claim Status algorithm (see Appendix 6.2 Claim Status Rules) to determine the Claim status.
	 */
	private void updateCliamStatus(boolean isSaved) {
		if(this.claimantStatusRules == null){
			this.claimantStatusRules = new ClaimantStatusRules(claimant.getId());
		}
		this.claimantClaim = ClaimantClaim.findClaimantClaim(this.claimantClaim.getId());
		this.claimantClaim.setStatus(this.claimantStatusRules.getStatus());
		this.claimantClaim.merge();
		generateClaimActivity(claimProof,isSaved);
	}
	
	
	/**
	 * Loads the selected {@link ClaimProof} for edit.
	 * @param id
	 * @return
	 */
	public boolean loadClaimProofById(Long id){
		setItemId(id);
		claimProof = ClaimProof.findClaimProof(id);
		this.setProofTypes(Sets.newHashSet(claimProof.getProofTypes()));
		setShowDialog(true);
		showHideClaimProofDialog(true);
		return true;
	}
	
	public void addCertificate(){
		if(selectedCerificate != null){
			proofTypes.add(selectedCerificate);
		}
		selectedCerificate = "";
	}
	
	public void removeCertificate(){
		if(selProof != null){
			for(String str : selProof ){
				proofTypes.remove(str);
			}
		}
	}
	
	private void generateClaimActivity(ClaimProof proof, boolean isSaved) {
		ClaimActivity activity = ClaimActivity.setActivityDefaults(new ClaimActivity());
		if (!isSaved) {
			activity.setActivityType(ClaimActivityType.UPDATED);
		} else {
			activity.setActivityType(ClaimActivityType.CREATED);
		}
		activity.setComments(proof.getComments());
		activity.setSystem("SASEC");
		activity.setStatus(this.claimantClaim.getStatus() == null ? "" : this.claimantClaim.getStatus().toString());
		this.claimantClaim.addClaimActivity(activity);
		
	}
	
	private void generateActivity(ClaimProof proof) {
		Activity activity = Activity.setActivityDefaults(new Activity());
		if (proof.getId() != null) {
			activity.setActivityCode(ActivityCode.UPDATED);
			activity.setDescription("Claim Proof Updated");
		} else{
			activity.setActivityCode(ActivityCode.CREATED);
			activity.setDescription("Claim Proof Created");
		}
		activity.setActivityCode(null);
		activity.setActivityTypeDescription("Claim Proof");
		activity.setClaimant(this.claimant);
		activity.persist();
	}
	
	public String getClaimProofsAsString(final List<String> proofTypes,final String lineSeperator) {
		return SaecStringUtils.getAsString(proofTypes, lineSeperator);
	}
	
	public ProofStatus getIgoProofStatus() {
		return ProofStatus.IGO;
	}
	
	public ProofStatus getNigoProofStatus() {
		return ProofStatus.NIGO;
	}
	
	public ProofStatus getPendingProofStatus() {
		return ProofStatus.PENDING;
	}
	
	public ProofStatus getIgoOverrideProofStatus() {
		return ProofStatus.IGO_OVERRIDE;
	}

	public Source getMail() {
		return Source.MAIL;
	}
	
	public Source getWeb() {
		return Source.WEB;
	}
	
	public Source getDataIntake() {
		return Source.DATA_INTAKE;
	}
	
	/**
	 * @return the claimProofCount
	 */
	public int getClaimProofCount() {
		return claimProofCount;
	}

	/**
	 * @param claimProofCount the claimProofCount to set
	 */
	public void setClaimProofCount(int claimProofCount) {
		this.claimProofCount = claimProofCount;
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
		this.claimantClaim = claimant.getSingleClaimantClaim();
		this.claimantStatusRules = new ClaimantStatusRules(claimant.getId());
	}

	/**
	 * @return the claimProof
	 */
	public ClaimProof getClaimProof() {
		return claimProof;
	}

	/**
	 * @param claimProof the claimProof to set
	 */
	public void setClaimProof(ClaimProof claimProof) {
		this.claimProof = claimProof;
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
	 * @return the proofTypes
	 */
	public Set<String> getProofTypes() {
		return proofTypes;
	}

	/**
	 * @param proofTypes the proofTypes to set
	 */
	public void setProofTypes(Set<String> proofTypes) {
		this.proofTypes = proofTypes;
	}

	/**
	 * @return the selProof
	 */
	public List<String> getSelProof() {
		return selProof;
	}

	/**
	 * @param selProof the selProof to set
	 */
	public void setSelProof(List<String> selProof) {
		this.selProof = selProof;
	}

	/**
	 * @return the selectedCerificate
	 */
	public String getSelectedCerificate() {
		return selectedCerificate;
	}

	/**
	 * @param selectedCerificate the selectedCerificate to set
	 */
	public void setSelectedCerificate(String selectedCerificate) {
		this.selectedCerificate = selectedCerificate;
	}
	
	/**
	 * @return the showDialog
	 */
	public boolean isShowDialog() {
		return showDialog;
	}

	/**
	 * @param showDialog the showDialog to set
	 */
	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}
	
	/**
	 * @return the itemId
	 */
	public Long getItemId() {
		return itemId;
	}


	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}


	public boolean areAllProofsIGO() {
		return ClaimProof.getClaimProofs(claimant.getId(),
				Lists.newArrayList(ProofStatus.IGO)).size() == ClaimProof
				.getClaimProofs(claimant.getId()).size();
	}
	
	
	

}
