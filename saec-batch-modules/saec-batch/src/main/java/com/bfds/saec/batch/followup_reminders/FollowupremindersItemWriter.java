package com.bfds.saec.batch.followup_reminders;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.ClaimantReminder.ReminderStatus;

public class FollowupremindersItemWriter implements ItemWriter<ClaimantReminder> {

	
	@Autowired
	private IMailSender mailSender;
	
	boolean  isValidCheck;
	
	boolean  isClaimantReminder=true;
	
	@Override 
	public void write(List<? extends ClaimantReminder> items) throws Exception {
		 for(ClaimantReminder claimantReminders:items)
		 {
			 ClaimantReminder claimantReminder = ClaimantReminder.findClaimantReminder(claimantReminders.getId());
			 boolean verifyClaimantEmail = emailCheckForaClaimant(claimantReminder);
			 if(verifyClaimantEmail){
			 causeConditionforSubmitClaimFormType(claimantReminder);
			 causeConditionforSubmitSavedDataType(claimantReminder);
			 causeConditionforSubmitDocumentsandNIGOType(claimantReminder);
			 isValidCheck=causeConditionforcashOutstandingCheckType(claimantReminder);
			 validateRemindertoSendEmail(claimantReminder);
		 }
			 claimantReminder.merge();
		 }
	}
	
	private boolean emailCheckForaClaimant(ClaimantReminder claimantReminder)
	{
		 if(claimantReminder.getReminderStatus().equals(ReminderStatus.NO_ADDRESS_AVAILABLE))
		 {
			claimantReminder.setReminderStatus(ClaimantReminder.ReminderStatus.REJECT);
			
			createAwdRipObjectforRejectedReminder(claimantReminder);
			 return false;
		 }
		return true;
	}
	
	private void createAwdRipObjectforRejectedReminder(ClaimantReminder claimantReminder)
	{
		claimantReminder.sendFollowupReminderRip();
	}
	
	private void causeConditionforSubmitClaimFormType(ClaimantReminder claimantReminder)
	{
		if(claimantReminder.getClaimantClaim()!=null && claimantReminder.getReminderType().getDescription().equals("Submit Claim Form")
				&& (claimantReminder.getReminderStatus().toString().equals("Pending") || claimantReminder.getReminderStatus().toString().equals("NO_ADDRESS_AVAILABLE")))
		{
			claimantReminder.setReminderStatus(ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
		}
	}
	
	
	private void causeConditionforSubmitSavedDataType(ClaimantReminder claimantReminder)
	{
		if(claimantReminder.getClaimantClaim()!=null && claimantReminder.getReminderType().getDescription().equals("Submit Saved Data")
				&& (claimantReminder.getReminderStatus().toString().equals("Pending") || claimantReminder.getReminderStatus().toString().equals("NO_ADDRESS_AVAILABLE")))
		{
			claimantReminder.setReminderStatus(ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
		}
	}
	

	private void causeConditionforSubmitDocumentsandNIGOType(ClaimantReminder claimantReminder)
	{
		if(claimantReminder.getClaimantClaim()!=null && claimantReminder.getClaimantClaim().getClaimProofs()!=null && (claimantReminder.getReminderType().getDescription().equals("Submit for NIGO")
				|| claimantReminder.getReminderType().getDescription().equals("Submit Documents"))
				&& (claimantReminder.getReminderStatus().toString().equals("Pending") || claimantReminder.getReminderStatus().toString().equals("NO_ADDRESS_AVAILABLE")))
		{
			List proofstatus=new ArrayList<String>();
			Integer claimproofssize=claimantReminder.getClaimantClaim().getClaimProofs().size();
			Iterator i=claimantReminder.getClaimantClaim().getClaimProofs().iterator();
			while(i.hasNext())
			{
				ClaimProof claimproof=(ClaimProof)i.next();
				if(claimproof.getStatus().toString().equals("IGO"))
				{
					proofstatus.add(claimproof.getStatus().toString());
				}
			}
			if(proofstatus.size()==claimproofssize)
				claimantReminder.setReminderStatus(ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
		}
	}
	
	
	private boolean causeConditionforcashOutstandingCheckType(ClaimantReminder claimantReminder)
	{
		//	isClaimantReminder=false;
		// TO DO : Need clarity on which check to consider
		return false;
	}
	
	public void validateRemindertoSendEmail(ClaimantReminder claimantReminder)
	{
		if(claimantReminder.getReminderStatus().toString().equals("Pending") && claimantReminder.getReminderType().getSendReminder()
				&& (claimantReminder.getReminderDueDate().before(new Date()) || !claimantReminder.getReminderDueDate().after(new Date()) ) && (isValidCheck || isClaimantReminder))
		{
			String event=Event.getCurrentEventCode()!=null ? Event.getCurrentEventCode() : "";
			String mailSubject=event + " - Reminder to "+ claimantReminder.getReminderType().getDescription();
			sendReminder(claimantReminder.getClaimant().getPrimaryContact().getEmail(),getRemindermailcontent(isValidCheck,isClaimantReminder,claimantReminder.getReminderType().getDescription()),mailSubject);
			claimantReminder.setReminderStatus(ClaimantReminder.ReminderStatus.COMPLETE);
			generateReminderSentActivity(claimantReminder);
		}
	}
 
	public String getRemindermailcontent(boolean isValidCheck,boolean isClaimantReminder,String description)
	{
		String event=Event.getCurrentEventCode()!=null ? Event.getCurrentEventCode() : "";
		String remindertype= isValidCheck ? "payment": "claim"; 
		return "Dear Class Member:" + "<br><br>" + "This is to alert you that the following is still required in order for your "+remindertype+" in the " +event+ " Class Action to be processed:" + "<br><br>"
					+ description + "<br><br>"+ "Thank you for your prompt disposition of this item." + "<br><br>" + "Event Center Team" + "<br><br>" + "    *** This is a system generated email.  Please do not respond. ***";
	}
	
	public void sendReminder(String mailTo,String mailcontent,String mailSubject)
	{
		mailSender.sendMail("admin@bfds.com",mailTo, mailSubject, mailcontent);
	}
	
private void generateReminderSentActivity(ClaimantReminder claimantReminder) {
		Activity activity =  Activity.setActivityDefaults(new Activity());
		activity.setActivityCode(ActivityCode.CREATED);	
		activity.setDescription("Claimant Reminder sent");
		activity.setActivityTypeDescription("Claimant Reminder");
		activity.setClaimant(claimantReminder.getClaimant());
		activity.persist();
		
	}
	
}
