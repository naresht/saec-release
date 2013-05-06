package com.bfds.saec.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.primefaces.component.dialog.Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.web.util.JsfUtils;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.ClaimantReminder.ContactMethod;
import com.bfds.wss.domain.ClaimantReminder.ReminderStatus;
import com.bfds.wss.domain.reference.ReminderType;

public class FollowupRemindersViewModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	final Logger log = LoggerFactory.getLogger(FollowupRemindersViewModel.class);
	
	private Claimant claimant;
	
	private ClaimantClaim claimantClaim;
	
	private List<ClaimantReminder> followupRemindersList=new ArrayList<ClaimantReminder>();
	
	private ClaimantReminder claimantReminder=new ClaimantReminder();
	
	private List<ReminderType> reminderTypesList=new ArrayList<ReminderType>();
	
	private boolean showDialog;


	/**
	 * To Show {@link ClaimantClaimHeader} of a {@link Claimant} at the top of the Page
	 * @return
	 */
	public ClaimantClaimHeader getClaimantClaimHeader() {
		return new ClaimantClaimHeader(this.claimant.getId());
	} 
	
	public List<ClaimantReminder> getFollowupRemindersList() {
	return claimantReminder.findClaimantReminderByClaimant(claimant); 
	} 

	public void setFollowupRemindersList(List<ClaimantReminder> followupRemindersList) {
		this.followupRemindersList = followupRemindersList;
	}

	public void prepareViewModel(Long claimantId) {
		claimant = Claimant.findClaimant(claimantId, Claimant.ASSOCIATION_ALL);
		if (claimant.getSingleClaimantClaim() != null) {
			this.claimantClaim = this.claimant.getSingleClaimantClaim();

		}  

	}
	
	public void editFollowUpReminder(final MessageContext messageContext,Long id){
		this.claimantReminder=ClaimantReminder.findClaimantReminder(id);
		showHideEditDialog(true);
		setShowDialog(true);
	}

	public void saveFollowUpReminder(final MessageContext messageContext){
		if(!validateMandatoryValues(claimantReminder)){
			messageContext.addMessage(new MessageBuilder().error().source("form:editDialog").defaultText("Please fill all Required fields").build());
			return;
		}
		generateActivity(claimantReminder);
		if(claimantReminder.getId() == null){
			emailVerification(claimantReminder);
			claimantReminder.setClaimant(this.getClaimant());
			if(claimant.getSingleClaimantClaim() != null){
			claimantReminder.setClaimantClaim(this.getClaimantClaim());
			}
			claimantReminder.persist();
			if(log.isInfoEnabled())
				log.info(String.format("Follow up reminder saved."));			
		}else{
			emailVerification(claimantReminder);
			claimantReminder.merge();
			if(log.isInfoEnabled())
				log.info(String.format("Follow up reminder updated."));
		}
		setShowDialog(false);
		showHideEditDialog(false);		
	}
	
	private boolean validateMandatoryValues(ClaimantReminder claimantReminder)
	{
		
		if(claimantReminder.getReminderType()!=null && StringUtils.hasText(claimantReminder.getReminderType().getDescription()) && 
				 claimantReminder.getContactMethod()!=null && claimantReminder.getReminderDueDate()!=null && claimantReminder.getActionDate()!=null){
			if(log.isInfoEnabled())
				log.info(String.format("Mandatory Fields validated for saving Follow up reminder."));			
			return true ;
		}	
		if(log.isInfoEnabled())
			log.info(String.format(" Mandatory Fields to be validated for saving Follow up reminder are missing."));		
		return false;
	}

	private void emailVerification(ClaimantReminder claimantReminder)
	{
		
		if(ContactMethod.EMAIL == claimantReminder.getContactMethod() 
				&& !StringUtils.hasText(this.getClaimant().getPrimaryContact().getEmail()))
		{
			claimantReminder.setReminderStatus(ClaimantReminder.ReminderStatus.NO_ADDRESS_AVAILABLE);
		}
	}
	
	public void onChangeReminderType(ReminderType remindertype)
	{
		claimantReminder.setReminderDueDate(getDueDate(remindertype));
	}

	private void showHideEditDialog(boolean val) {
		Dialog editDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "editDialog");
		editDialog.setVisible(val);
	}
	
	public void newFollowUpReminder(){
		this.claimantReminder = new ClaimantReminder();
		setShowDialog(true);
		showHideEditDialog(true);
	}
	
	public String getAddressText(String param) {
		ClaimantAddress address = this.getClaimant().getMailingAddress();
		if (address != null) {
			return address.getAddressAsString(param);
		}
		return null;
	}
	
	
	public String getRegistrationText() {
		return this.claimant.getClaimantRegistration()
				.getRegistrationLinesAsString("<br/>");
	}

	public Claimant getClaimant() {
		return claimant;
	}


	public void setClaimant(Claimant claimant) {
		this.claimant = claimant;
	}


	public ClaimantClaim getClaimantClaim() {
		return claimantClaim;
	}


	public void setClaimantClaim(ClaimantClaim claimantClaim) {
		this.claimantClaim = claimantClaim;
	}

	public ClaimantReminder getClaimantReminder() {
		return claimantReminder;
	}

	public void setClaimantReminder(ClaimantReminder claimantReminder) {
		this.claimantReminder = claimantReminder;
	}
	
	public ContactMethod getDailer() {
		return ContactMethod.DIALER;
	}

	public ContactMethod getEmail() {
		return ContactMethod.EMAIL;
	}
	
	
	public ReminderStatus getCompletestatus() {
		return ReminderStatus.COMPLETE;
	}
	public ReminderStatus getRejectstatus() {
		return ReminderStatus.REJECT;
	}
	public ReminderStatus getPendingstatus() {
		return ReminderStatus.PENDING;
	}
	public ReminderStatus getAutoresolvedstatus() {
		return ReminderStatus.AUTO_RESOLVED;
	}
	
	public ReminderStatus getManresolvedstatus() {
		return ReminderStatus.MAN_RESOLVED;
	}
	
	public ReminderStatus getNoaddressstatus() {
		return ReminderStatus.NO_ADDRESS_AVAILABLE;
	}
	public ReminderStatus getInsufficientstatus() {
		return ReminderStatus.INSUFFICIENT_AMOUNT;
	}
	public List<ReminderType> getReminderTypesList() {
		return reminderTypesList;
	}

	public void setReminderTypesList(List<ReminderType> reminderTypesList) {
		this.reminderTypesList = reminderTypesList;
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

	private void generateActivity(ClaimantReminder claimantReminder) {
		
		Activity activity =  Activity.setActivityDefaults(new Activity());
		
		if(claimantReminder.getId()!=null){
			activity.setActivityCode(ActivityCode.UPDATED);
			activity.setDescription("Claimant Reminder updated");
			}
			else{
				activity.setActivityCode(ActivityCode.CREATED);	
				activity.setDescription("Claimant Reminder Created");
			}
		activity.setActivityCode(null);
		activity.setActivityTypeDescription("Claimant Reminder");
		activity.setClaimant(this.claimant);
		
		activity.persist();
		if(log.isInfoEnabled()) {
			log.info(String.format("Activity is generated for Follow up Reminders"));
		}
		
	}
	
	
	public static Date getDueDate(ReminderType remindertype){
		Long ms = remindertype.getDueDateReference().getTime();
		ms = ms+remindertype.getDueDateOffset()*24*60*60*1000;
		return new Date(ms);
	}
   
}
