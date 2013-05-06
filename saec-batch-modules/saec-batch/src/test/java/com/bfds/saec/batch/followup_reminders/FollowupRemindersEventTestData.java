package com.bfds.saec.batch.followup_reminders;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Bank;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.Contact;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.wss.domain.ClaimProof;
import com.bfds.wss.domain.ClaimProof.ProofStatus;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantReminder;
import com.bfds.wss.domain.ClaimantReminder.ReminderStatus;
import com.bfds.wss.domain.ClaimantReminder.ContactMethod;
import com.bfds.wss.domain.reference.ReminderType;


@Component
public class FollowupRemindersEventTestData extends com.bfds.saec.batch.util.DataGenerator {

	
	@Transactional
	public static void create() {
		createEvent1();
		Claimant claimant = newClaimant();
		Contact contact=new Contact();
		contact.setEmail("busandeep@dstworldwideservices.com");
		claimant.setPrimaryContact(contact);
		claimant.setReferenceNo("100000123");
		Payment c;

		c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setIdentificatonNo("1033");
		c.setPaymentType(PaymentType.CHECK);
		c.setPaymentCode(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentAmount(new BigDecimal(50));
		c.setPayTo(claimant);
		claimant.addCheck(c);

		claimant.persist();
		
		// --------------- claimantReminder 1 -----------------------------------
		
		loadReminderTypes();
		
		ClaimantClaim claimantClaim=new ClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.setControlNumber(223344);
		claimantClaim.setClaimIdentifier("10001");
		claimantClaim.persist();
		
		ClaimProof claimProof=new ClaimProof();
		claimProof.setProofRequiredInd(Boolean.TRUE);
		claimProof.setComments("test comments for claim proof1");
		claimProof.setStatus(ProofStatus.PENDING);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		
		ReminderType reminderType=ReminderType.findReminderType(1L);
		
		ClaimantReminder claimantReminder = new ClaimantReminder();
		claimantReminder.setClaimant(claimant);
		claimantReminder.setClaimantClaim(claimantClaim);
		claimantReminder.setReminderType(reminderType);
		claimantReminder.setReminderDescription("desc1");
		Long ms = reminderType.getDueDateReference().getTime();
		ms = ms+reminderType.getDueDateOffset()*24*60*60*1000;
		claimantReminder.setReminderDueDate(new Date(ms));
		claimantReminder.setReminderStatus(ReminderStatus.PENDING);
		claimantReminder.setActionDate(new Date());
		claimantReminder.setContactMethod(ContactMethod.DIALER);
		claimantReminder.persist();
		
		// --------------- claimantReminder 2 -----------------------------------
		claimantClaim=new ClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.setControlNumber(223355);
		claimantClaim.setClaimIdentifier("10002");
		claimantClaim.persist();
		
		claimProof=new ClaimProof();
		claimProof.setProofRequiredInd(false);
		claimProof.setComments("test comments for claim proof2");
		claimProof.setStatus(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		
		reminderType=ReminderType.findReminderType(2L);
		
		claimantReminder = new ClaimantReminder();
		claimantReminder.setClaimant(claimant);
		claimantReminder.setClaimantClaim(claimantClaim);
		claimantReminder.setReminderType(ReminderType.findReminderType(2L));
		claimantReminder.setReminderDescription("desc2");
		ms = reminderType.getDueDateReference().getTime();
		ms = ms+reminderType.getDueDateOffset()*24*60*60*1000;
		claimantReminder.setReminderDueDate(new Date(ms));
		claimantReminder.setReminderStatus(ReminderStatus.PENDING);
		claimantReminder.setActionDate(new Date());
		claimantReminder.setContactMethod(ContactMethod.EMAIL);
		claimantReminder.persist();
		

		// --------------- claimantReminder 3 -----------------------------------
		claimantClaim=new ClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.setControlNumber(223366);
		claimantClaim.setClaimIdentifier("10003");
		claimantClaim.persist();
		
		claimProof=new ClaimProof();
		claimProof.setProofRequiredInd(false);
		claimProof.setComments("test comments for claim proof3");
		claimProof.setStatus(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		
		claimProof=new ClaimProof();
		claimProof.setProofRequiredInd(false);
		claimProof.setComments("test comments for claim proof3 NIGO");
		claimProof.setStatus(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		
		reminderType=ReminderType.findReminderType(3L);
		 
		claimantReminder = new ClaimantReminder();
		claimantReminder.setClaimant(claimant);
		claimantReminder.setClaimantClaim(claimantClaim);
		claimantReminder.setReminderType(ReminderType.findReminderType(3L));
		claimantReminder.setReminderDescription("desc3");
		ms = reminderType.getDueDateReference().getTime();
		ms = ms+reminderType.getDueDateOffset()*24*60*60*1000;
		claimantReminder.setReminderDueDate(new Date(ms));
		claimantReminder.setReminderStatus(ReminderStatus.PENDING);
		claimantReminder.setActionDate(new Date());
		claimantReminder.setContactMethod(ContactMethod.EMAIL);
		claimantReminder.persist();
		
		// --------------- claimantReminder 4 -----------------------------------
		claimantClaim=new ClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.setControlNumber(223377);
		claimantClaim.setClaimIdentifier("10004");
		claimantClaim.persist();

		claimProof=new ClaimProof();
		claimProof.setProofRequiredInd(true);
		claimProof.setComments("test comments for claim proof4");
		claimProof.setStatus(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();

		reminderType=ReminderType.findReminderType(4L);
		 
		claimantReminder = new ClaimantReminder();
		claimantReminder.setClaimant(claimant);
		claimantReminder.setClaimantClaim(claimantClaim);
		claimantReminder.setReminderType(ReminderType.findReminderType(4L));
		claimantReminder.setReminderDescription("desc4");
		ms = reminderType.getDueDateReference().getTime();
		ms = ms+reminderType.getDueDateOffset()*24*60*60*1000;
		claimantReminder.setReminderDueDate(new Date(ms));
		claimantReminder.setReminderStatus(ReminderStatus.PENDING);
		claimantReminder.setActionDate(new Date());
		claimantReminder.setContactMethod(ContactMethod.EMAIL);
		claimantReminder.persist();
		
		// --------------- claimantReminder 5 -----------------------------------
		claimantClaim=new ClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.setControlNumber(223388);
		claimantClaim.setClaimIdentifier("10005");
		claimantClaim.persist();

		claimProof=new ClaimProof();
		claimProof.setProofRequiredInd(true);
		claimProof.setComments("test comments for claim proof5");
		claimProof.setStatus(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
		claimProof.persist();
		
		reminderType=ReminderType.findReminderType(5L);

		claimantReminder = new ClaimantReminder();
		claimantReminder.setClaimant(claimant);
		claimantReminder.setClaimantClaim(claimantClaim);
		claimantReminder.setReminderType(ReminderType.findReminderType(5L));
		claimantReminder.setReminderDescription("desc5");
		ms = reminderType.getDueDateReference().getTime();
		ms = ms+reminderType.getDueDateOffset()*24*60*60*1000;
		claimantReminder.setReminderDueDate(new Date(ms));
		claimantReminder.setReminderStatus(ReminderStatus.NO_ADDRESS_AVAILABLE);
		claimantReminder.setActionDate(new Date());
		claimantReminder.setContactMethod(ContactMethod.EMAIL);
		claimantReminder.persist();
		
		
		// --------------- claimantReminder 6-----------------------------------
		claimantClaim=new ClaimantClaim();
		claimantClaim.setClaimant(claimant);
		claimantClaim.setControlNumber(223356);
		claimantClaim.setClaimIdentifier("10066");
	//	claimantClaim.persist();

		claimProof=new ClaimProof();
		claimProof.setProofRequiredInd(false);
		claimProof.setComments("test comments for claim proof6");
		claimProof.setStatus(ProofStatus.IGO);
		claimProof.setClaimantClaim(claimantClaim);
	//	claimProof.persist();

		reminderType=ReminderType.findReminderType(2L);

		claimantReminder = new ClaimantReminder();
		claimantReminder.setClaimant(claimant);
	//	claimantReminder.setClaimantClaim(claimantClaim);
		claimantReminder.setReminderType(ReminderType.findReminderType(2L));
		claimantReminder.setReminderDescription("desc2");
		ms = reminderType.getDueDateReference().getTime();
		ms = ms+reminderType.getDueDateOffset()*24*60*60*1000;
		claimantReminder.setReminderDueDate(new Date(ms));
		claimantReminder.setReminderStatus(ReminderStatus.PENDING);
		claimantReminder.setActionDate(new Date());
		claimantReminder.setContactMethod(ContactMethod.EMAIL);
		claimantReminder.persist();
	}
	
	private static void loadReminderTypes() {
		ReminderType remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit Claim Form");
		remindertype.setDueDateOffset(5);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit Saved Data");
		remindertype.setDueDateOffset(-5);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit for NIGO");
		remindertype.setDueDateOffset(12);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit Documents");
		remindertype.setDueDateOffset(-13);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Cash Outstanding Check");
		remindertype.setDueDateOffset(7);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
	}
	
	public static void createEvent1() {
		Event e = new Event();
		e.setDda("DDA-6");
		e.setCode("hydroxy cut");
		e.persist();
		e.flush();
	}
}
