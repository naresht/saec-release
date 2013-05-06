package com.bfds.saec.claim.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.wss.domain.ClaimActivity;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantClaimId;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.reference.ClaimActivityType;
import com.bfds.wss.domain.reference.ClaimEntryMethod;
import com.bfds.wss.domain.reference.ClaimStatus;
import com.bfds.wss.domain.reference.ReminderType;

@Service
public class ClaimServiceImpl implements ClaimService {
	
	final static Logger log = LoggerFactory.getLogger(ClaimServiceImpl.class);	
	
	@PersistenceContext(unitName="entityManagerFactory")
	private EntityManager entityManager;	

	@Override
	public ClaimantClaim getClaimantClaim(final Long claimantId) {
		final Claimant claimant  = Claimant.findClaimant(claimantId);
		ClaimantClaim claimantClaim = claimant.getSingleClaimantClaim();		
		//	case 1:
		if (claimantClaim != null) {
			claimantClaim.setComments(claimantClaim.getLatestClaimActivityComments());						
		} else {
		//	case 2:
			claimantClaim = new ClaimantClaim();
			claimantClaim.setEntryMethod(ClaimEntryMethod.SASEC);
		//	case 3:
			if(claimant.getClaimantClaimIds().size() > 0 ) {
				ClaimantClaimId claimantClaimId = claimant.getLatestClaimantClaimId();				
				claimantClaim.setClaimIdentifier(claimantClaimId.getClaimIdentifier());
				claimantClaim.setControlNumber(claimantClaimId.getControlNumber());
			}
		}
		return claimantClaim;
	}

	@Override
	@Transactional
	public void saveClaimantClaim(final Long claimantId, final ClaimantClaim claimantClaim) {
		
		if(!isDirty(claimantClaim)) {
			return;
		}
		
		final Claimant claimant  = Claimant.findClaimant(claimantId);
		if(claimantClaim.getClaimant() == null) {			
			if (claimant.getSingleClaimantClaim() == null) {
				claimant.getClaimantClaims().add(claimantClaim);
				claimantClaim.setClaimant(claimant);
			} 	
		}
					
		generateClaimActivity(claimantClaim);
		generateActivity(claimantClaim, claimant);
			
		if (claimantClaim.getId() != null) {
			claimantClaim.merge();
			if(log.isInfoEnabled())
				log.info(String.format("Claim claim is updated. "));			
		} else {
			claimantClaim.setStatus(ClaimStatus.PENDING);
			claimantClaim.persist();
			if(log.isInfoEnabled())
				log.info(String.format("Claim claim is saved. "));			
			updateFollowUpReminder(claimant);
		}

	}
	
	private boolean isDirty(ClaimantClaim claimantClaim) {
		if(claimantClaim.getId() == null) {
			return true;
		}
		entityManager.clear();
		ClaimantClaim oldClaimantClaim = entityManager.find(ClaimantClaim.class, claimantClaim.getId());
		
		if (claimantClaim.getLatestClaimActivity().getComments() == null
				&& oldClaimantClaim.getComments() != null) {
			return true;
		} else if (claimantClaim.getDateFiled() == null
				&& oldClaimantClaim.getDateFiled() != null) {
			return true;
		} else if (claimantClaim.getComments() != null
				&& !claimantClaim.getComments().equals(oldClaimantClaim.getLatestClaimActivityComments())) {
			return true;
		} else if (claimantClaim.getDateFiled() !=null && claimantClaim.getDateFiled().compareTo(
				oldClaimantClaim.getDateFiled()) != 0) {
			return true;
		}
		return false;
	}

	private void generateClaimActivity(ClaimantClaim claimantClaim) {
		//
		ClaimActivity activity =  ClaimActivity.setActivityDefaults(new ClaimActivity());
		if(claimantClaim.getId() != null) {
			activity.setActivityType(ClaimActivityType.UPDATED);	
			activity.setStatus(claimantClaim.getStatus() == null ? "UPDATED" : claimantClaim.getStatus().toString()); 
		}
		else{
			activity.setActivityType(ClaimActivityType.CREATED);
			activity.setStatus("SUBMITTED"); 			
		}
		activity.setComments(claimantClaim.getComments());
		activity.setSystem("SASEC");	     	
		claimantClaim.addClaimActivity(activity);		
	}
	
	private void generateActivity(ClaimantClaim claim, Claimant claimant) {
		
		Activity activity =  Activity.setActivityDefaults(new Activity());
		
		if(claim.getId()!=null){
			activity.setActivityCode(ActivityCode.UPDATED);
			activity.setDescription("Claim Details Updated");						
		}else{
			activity.setActivityCode(ActivityCode.CREATED);	
			activity.setDescription("Claim Details Created");
		}
		activity.setActivityCode(null);
		activity.setActivityTypeDescription("Claim Form");
		activity.setClaimant(claimant);
		
		activity.persist();		
		
	}
	
	/**
	 * Whenever the {@link ClaimantClaim}} is 'Saved', the system will resolve any {@link ReminderType}'Submit Claim Form' Reminder for the claimant. 
	 */
	private void updateFollowUpReminder(Claimant claimant) {
		List<ClaimantReminder> claimantReminders = ClaimantReminder.findClaimantRemindersWithReminderStatus(claimant, "Submit Claim Form");
		if(claimantReminders != null){
			for (ClaimantReminder reminder : claimantReminders) {
				reminder.setReminderStatus(ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
				reminder.merge();
			}
			if(log.isDebugEnabled())
				log.debug(String.format("Claimant Reminders updated : %s . ",claimantReminders.size()));			
		}
	}

}
