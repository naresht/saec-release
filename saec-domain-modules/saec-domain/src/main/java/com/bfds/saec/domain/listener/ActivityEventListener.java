package com.bfds.saec.domain.listener;

import java.util.Date;

import org.hibernate.cfg.Configuration;
//import org.hibernate.event.Initializable;
//import org.hibernate.event.PostCollectionRecreateEvent;
//import org.hibernate.event.PostCollectionRecreateEventListener;
//import org.hibernate.event.PostDeleteEvent;
//import org.hibernate.event.PostDeleteEventListener;
//import org.hibernate.event.PostInsertEvent;
//import org.hibernate.event.PostInsertEventListener;
//import org.hibernate.event.PostUpdateEvent;
//import org.hibernate.event.PostUpdateEventListener;
//import org.hibernate.event.PreCollectionRemoveEvent;
//import org.hibernate.event.PreCollectionRemoveEventListener;
//import org.hibernate.event.PreCollectionUpdateEvent;
//import org.hibernate.event.PreCollectionUpdateEventListener;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.ClaimantRegistration;
import com.bfds.saec.domain.ClaimantTaxInfo;
import com.bfds.saec.domain.Contact;
import com.bfds.saec.domain.IAddress;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Name;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.domain.activity.ActivityCode;
import com.bfds.saec.domain.activity.AddressChange;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.bfds.saec.domain.activity.AlternatePayeeActivity;
import com.bfds.saec.domain.activity.CheckActivity;
import com.bfds.saec.domain.activity.ContactChange;
import com.bfds.saec.domain.activity.GeneralAccountMaintenance;
import com.bfds.saec.domain.activity.GeneralAccountMaintenanceChange;
import com.bfds.saec.domain.activity.NameChange;
import com.bfds.saec.domain.activity.TaxInfoChange;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.util.SaecStringUtils;
import com.google.common.base.Preconditions;

public class ActivityEventListener implements PostInsertEventListener,
        PostUpdateEventListener, PostDeleteEventListener,
        PreCollectionUpdateEventListener, PreCollectionRemoveEventListener,
		PostCollectionRecreateEventListener {

	private static final long serialVersionUID = 1L;

	final Logger log = LoggerFactory.getLogger(ActivityEventListener.class);

//	@Override
//	public void initialize(Configuration cfg) {
//		// TODO Auto-generated method stub
//	}

	@Override
	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
		if (event.getAffectedOwnerEntityName().equals(Claimant.class.getName())) {
			// throw new
			// UnsupportedOperationException("onPostRecreateCollection() not supported.");
		}

	}

	@Override
	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
		// throw new
		// UnsupportedOperationException("onPreRemoveCollection() not supported.");

	}

	@Override
	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
		// throw new
		// UnsupportedOperationException("onPreUpdateCollection() not supported.");

	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		// throw new
		// UnsupportedOperationException("onPostDelete() not supported.");

	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		if (event.getEntity() instanceof Claimant) {
			createClaimantChangeActivity(event);
		}
		if (event.getEntity() instanceof ClaimantRegistration) {
			createNameChangeActivity(event);
		} else if (event.getEntity() instanceof ClaimantAddress) {
			createAddressChangeActivity(event);
		} else if (event.getEntity() instanceof Payment) {
			createCheckChangeActivity(event);
		} else if (event.getEntity() instanceof Contact) {
			createContactChangeActivity(event);
		}

		else if (event.getEntity() instanceof Letter) {
			createLetterChangeActivity(event);
		}

	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		if (event.getEntity() instanceof Claimant) {
			if (((Claimant) event.getEntity()).getParentClaimant() != null) {
				createAlternatePayeeCreatedActivity(event);
			}
		} else if (event.getEntity() instanceof Payment) {
			createCheckCreatedActivity(event);
		} else if (event.getEntity() instanceof Letter) {
			createLetterCreatedActivity(event);
		} else if (event.getEntity() instanceof Contact) {
			createContactCreatedActivity(event);
		}else if (event.getEntity() instanceof ClaimantAddress) {
			createAddressCreatedActivity(event);
		}
	}

	/**
	 * Method for displaying Additional contacts info in Account History Table.
	 * @param event
	 */
	private void createContactCreatedActivity(PostInsertEvent event) {
		Claimant claimant = ((Contact) event.getEntity()).getClaimant();
		boolean isPrimary = false;
		if (claimant == null) {
			claimant = ((Contact) event.getEntity()).getPrimaryContactOf();
			isPrimary = false;
		}
		if (claimant == null) {
			// This should never be possible.
			return;
		}

		final ContactChange contactChange = (ContactChange) setActivityDefaults(new ContactChange());
		contactChange.setTo(getContact(event.getPersister(), event.getState()));
		contactChange.setIsPrimaryContact(isPrimary);
		contactChange.setClaimant(claimant);
		contactChange.persist();
		claimant.addActivity(contactChange);
	}

	private void createLetterCreatedActivity(PostInsertEvent event) {
		Letter letter = (Letter) event.getEntity();

		if (letter.getLetterCode() != null
				&& (letter.getLetterCode().getLetterType() == LetterType.CLAIM_FORM || letter.getLetterCode().getLetterType() == LetterType.CLAIM_FORM_CURE_LETTER)) {
			// New claim for created/received.
			Claimant c = Claimant.findClaimant(letter.getClaimant().getId());
			Activity activity = (Activity) setActivityDefaults(new Activity());
			activity.setActivityTypeDescription("Claim Form");
			activity.setDescription(letter.getDescription()== null ? "" :letter.getDescription() +" "
					+ (letter.getLetterStatus() != null ? letter
							.getLetterStatus().name() : ""));
			c.addActivity(activity);
			c.persist();
		} else if (letter.getLetterCode() != null
				&& letter.getLetterCode().getLetterType() == LetterType.OPTOUT_FORM || letter.getLetterCode().getLetterType() == LetterType.OPTOUT_CURE_LETTER) {
			// New claim for created/received.
			Claimant c = Claimant.findClaimant(letter.getClaimant().getId());
			Activity activity = (Activity) setActivityDefaults(new Activity());
			activity.setActivityTypeDescription("OptOut");
			activity.setDescription(letter.getDescription() == null ? "" :letter.getDescription() +" "
					+ (letter.getLetterStatus() != null ? letter
							.getLetterStatus().name() : ""));
			c.addActivity(activity);
			c.persist();
		}
		else if (letter.getLetterCode() != null && letter.getAddress()!=null && letter.getAddress().getAddressType() == AddressType.ONE_TIME_MAILING) {
			MailObjectAddress m = letter.getAddress();
			RegistrationLines registrationLines = letter.getPayToRegistration();
			Claimant c = Claimant.findClaimant(letter.getClaimant().getId());
			Activity activity = (Activity) setActivityDefaults(new Activity());
			activity.setActivityTypeDescription("Correspondence Process");
			StringBuffer sb = new StringBuffer();
			sb.append("One Time Mailing Address:");
			sb.append(registrationLines.getRegistrationLinesAsString("<br/>"));
			sb.append(m.getAddressAsString());
			activity.setDescription(sb.toString());
			c.addActivity(activity);
			c.persist();
			sb = null;
		}
		else if (letter.getLetterCode() != null) {
			Claimant c = Claimant.findClaimant(letter.getClaimant().getId());
			Activity activity = (Activity) setActivityDefaults(new Activity());
			activity.setActivityTypeDescription("Correspondence Process");
			StringBuffer sb = new StringBuffer();
			sb.append("Correspondence Process ");
			sb.append(letter.getLetterStatus() != null ? letter
					.getLetterStatus().name().toString() : "");
			if (StringUtils.hasText(SaecStringUtils.getAsString(letter
					.getCorrespondenceDocumentNames(letter
							.getIgoCorrespondenceDocuments()), "<br/>"))) {
				sb.append(" ,Received :"
						+ SaecStringUtils.getAsString(letter
								.getCorrespondenceDocumentNames(letter
										.getIgoCorrespondenceDocuments()),
								"<br/>"));
			}
			if (StringUtils.hasText(SaecStringUtils.getAsString(letter
					.getCorrespondenceDocumentNames(letter
							.getNigoCorrespondenceDocuments()), "<br/>"))) {
				sb.append(" ,Missing : "
						+ SaecStringUtils.getAsString(letter
								.getCorrespondenceDocumentNames(letter
										.getNigoCorrespondenceDocuments()),
								"<br/>"));
			}
			sb.append(" MailType: "+letter.getMailType()+" Mail Date: "+letter.getMailDate() == null ? "" :letter.getMailDate()+" Request Type: "+letter.getRequestType() == null ? "" :letter.getRequestType()+" Comments: "+ letter.getComments()== null ? "" :letter.getComments());
			if(letter.getRpoType() != null){
				sb.append(" RPO Status: "+letter.getRpoType()== null ? "" :letter.getRpoType()+" RPO Date: "+letter.getRpoDate() == null ? "" : letter.getRpoDate() );
			}
			activity.setDescription(sb.toString());
			c.addActivity(activity);
			c.persist();
			sb = null;
		}
		letter.sendCureLetterCreatedRip();

	}
	/**
	 * Method for displaying Primary Contact Change info in Account History Table.
	 * @param event
	 */
	private void createContactChangeActivity(PostUpdateEvent event) {
		Contact from = getContact(event.getPersister(), event.getOldState());
		Contact to = getContact(event.getPersister(), event.getState());
		if ((from.isEmpty() && to.isEmpty()) || from.equals(to)) {
			return;
		}
		Claimant claimant = ((Contact) event.getEntity()).getClaimant();
		boolean isPrimary = false;
		if (claimant == null) {
			claimant = ((Contact) event.getEntity()).getPrimaryContactOf();
			isPrimary = true;
		}
		if (claimant == null) {
			// This should never be possible.
			return;
		}

		final ContactChange contactChange = (ContactChange) setActivityDefaults(new ContactChange());
		contactChange.setFrom(from);
		contactChange.setTo(to);
		contactChange.setIsPrimaryContact(isPrimary);
		contactChange.setClaimant(claimant);
		contactChange.persist();
		claimant.addActivity(contactChange);
	}

	private void createLetterChangeActivity(PostUpdateEvent event) {

		final Letter letter = (Letter) event.getEntity();
		Claimant climantClaimForm = Claimant.findClaimant(letter.getClaimant()
				.getId());
		if (isClaimFormLetterChanged(event.getPersister(), event.getOldState(),
				event.getState())) {
			Activity claimFormProcessActivity = setActivityDefaults(new Activity());
			claimFormProcessActivity.setActivityTypeDescription("ClaimForm");
			claimFormProcessActivity.setDescription("Claim Form Changed To -"+letter.getLetterStatus());
			claimFormProcessActivity.setClaimant(climantClaimForm);
			climantClaimForm.addActivity(claimFormProcessActivity);
			climantClaimForm.persist();
			return;

		} else if (isRpoLetterChanged(event.getPersister(),
				event.getOldState(), event.getState())) {

			final Letter letterRpo = (Letter) event.getEntity();
			Claimant claimantRpo = Claimant.findClaimant(letterRpo.getClaimant()
					.getId());
			Activity rpoActivity = setActivityDefaults(new Activity());
			rpoActivity.setActivityTypeDescription("RPO Process");
			String mailingControlNo = (letter.getMailingControlNo() != null) ? letter
					.getMailingControlNo() : "";
			RPOType rpoType = (letterRpo.getRpoType() != null) ? letterRpo
					.getRpoType() : null;
			rpoActivity.setDescription("MailingControlNo# " + mailingControlNo
					+ ",RPO :" + rpoType);

			rpoActivity.setClaimant(claimantRpo);
			claimantRpo.addActivity(rpoActivity);
			claimantRpo.persist();
			return;
		}

		else if (isOptOutLetterChanged(event.getPersister(),
				event.getOldState(), event.getState())) {

			final Letter letterOptOut = (Letter) event.getEntity();
			Claimant claimantOptOut = Claimant.findClaimant(letterOptOut
					.getClaimant().getId());
			Activity optOutActivity = setActivityDefaults(new Activity());
			optOutActivity.setActivityTypeDescription("Opt Out");

			optOutActivity.setDescription("OptOut Changed To -"
					+ letterOptOut.getLetterStatus());

			optOutActivity.setClaimant(claimantOptOut);
			claimantOptOut.addActivity(optOutActivity);
			claimantOptOut.persist();
			return;

		}
		else {
			return;
		}

	}
	
	
	private boolean isOptOutLetterChanged(EntityPersister persister,
			Object[] oldStatus, Object[] updatedStatus) {

		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		Object[] oldStates = oldStatus;
		Object[] updatedStates = updatedStatus;
		return optOutLetterChange(entityMetamodel, oldStates, updatedStates);

	}

	private boolean optOutLetterChange(EntityMetamodel entityMetamodel,
			Object[] oldStates, Object[] updatedStates) {
		LetterStatus oldLetterStatus = (LetterStatus) oldStates[entityMetamodel.getPropertyIndex("letterStatus")];
		LetterStatus updatedLetterStatus =  (LetterStatus) updatedStates[entityMetamodel.getPropertyIndex("letterStatus")];
		String oldDesc = (String) oldStates[entityMetamodel.getPropertyIndex("description")] == null ? "" : (String) oldStates[entityMetamodel.getPropertyIndex("description")];
		String updatedDesc = (String) updatedStates[entityMetamodel.getPropertyIndex("description")] == null ? "" : (String) oldStates[entityMetamodel.getPropertyIndex("description")];
		
		if (oldLetterStatus != updatedLetterStatus && ((oldDesc).contains("Opt") || (updatedDesc).contains("Opt"))) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isClaimFormLetterChanged(EntityPersister persister,
			Object[] oldStatus, Object[] updatedStatus) {

		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		Object[] oldStates = oldStatus;
		Object[] updatedStates = updatedStatus;
		return claimFormLetterChange(entityMetamodel, oldStates, updatedStates);

	}

	private boolean claimFormLetterChange(EntityMetamodel entityMetamodel,
			Object[] oldStates, Object[] updatedStates) {
		LetterStatus oldLetterStatus = (LetterStatus) oldStates[entityMetamodel.getPropertyIndex("letterStatus")];
		LetterStatus updatedLetterStatus =  (LetterStatus) updatedStates[entityMetamodel.getPropertyIndex("letterStatus")];
		String oldDesc = (String) oldStates[entityMetamodel.getPropertyIndex("description")] == null ? "" : (String) oldStates[entityMetamodel.getPropertyIndex("description")];
		String updatedDesc = (String) updatedStates[entityMetamodel.getPropertyIndex("description")] == null ? "" : (String) oldStates[entityMetamodel.getPropertyIndex("description")];
		
		if (oldLetterStatus != updatedLetterStatus && ((oldDesc).contains("Claim") || (updatedDesc).contains("Claim"))) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isRpoLetterChanged(EntityPersister persister,
			Object[] oldState, Object[] updatedState) {

		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		Object[] oldStates = oldState;
		Object[] updatedStates = updatedState;
		if ((RPOType) oldStates[entityMetamodel.getPropertyIndex("rpoType")] != (RPOType) updatedStates[entityMetamodel
				.getPropertyIndex("rpoType")]) {
			return true;
		}

		return false;
	}

	private Contact getContact(EntityPersister persister, Object[] state) {
		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		Contact newContact = new Contact();
		newContact.setCellPhoneNo((String) state[entityMetamodel
				.getPropertyIndex("cellPhoneNo")]);
		newContact.setComments((String) state[entityMetamodel
				.getPropertyIndex("comments")]);
		newContact.setEmail((String) state[entityMetamodel
				.getPropertyIndex("email")]);
		newContact.setName((Name) state[entityMetamodel
				.getPropertyIndex("name")]);
		newContact.setPhoneNo((String) state[entityMetamodel
				.getPropertyIndex("phoneNo")]);
		newContact.setWorkPhoneNo((String) state[entityMetamodel
				.getPropertyIndex("workPhoneNo")]);
		return newContact;
	}
	

	private void createClaimantChangeActivity(PostUpdateEvent event) {

		final Claimant claimant = (Claimant) event.getEntity();
		final GeneralAccountMaintenanceChange generalAccountMaintenance = getGeneralMaintenanceChangeActivity(event);
		final TaxInfoChange taxInfoChange = getTaxinfoChangeActivity(event);

		if (generalAccountMaintenance != null) {
			claimant.addActivity(generalAccountMaintenance);
			claimant.persist();
		} else if (taxInfoChange != null) {
			claimant.addActivity(taxInfoChange);
			claimant.persist();
		}

	}

	private TaxInfoChange getTaxinfoChangeActivity(PostUpdateEvent event) {
		ClaimantTaxInfo from = getClaimantTaxInfo(event.getPersister(),
				event.getOldState());
		ClaimantTaxInfo to = getClaimantTaxInfo(event.getPersister(),
				event.getState());

		if (from == null || to == null || from.equals(to)) {
			return null;
		}

		TaxInfoChange a = (TaxInfoChange) setActivityDefaults(new TaxInfoChange());
		a.setFrom(from);
		a.setTo(to);
		return a;
	}

	private ClaimantTaxInfo getClaimantTaxInfo(EntityPersister persister,
			Object[] state) {
		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		return (ClaimantTaxInfo) state[entityMetamodel
				.getPropertyIndex("taxInfo")];
	}

	private GeneralAccountMaintenanceChange getGeneralMaintenanceChangeActivity(
			PostUpdateEvent event) {
		GeneralAccountMaintenance from = getGeneralMaintenance(
				event.getPersister(), event.getOldState());
		GeneralAccountMaintenance to = getGeneralMaintenance(
				event.getPersister(), event.getState());
		if (from == null || to == null || from.equals(to)) {
			return null;
		}
		GeneralAccountMaintenanceChange generalAccountMaintenanceChange = (GeneralAccountMaintenanceChange) setActivityDefaults(new GeneralAccountMaintenanceChange());
		generalAccountMaintenanceChange.setFrom(from);
		generalAccountMaintenanceChange.setTo(to);
		return generalAccountMaintenanceChange;
	}

	private GeneralAccountMaintenance getGeneralMaintenance(
			EntityPersister persister, Object[] state) {
		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		GeneralAccountMaintenance generalAccountMaintenance = new GeneralAccountMaintenance();
		generalAccountMaintenance
				.setFundAccountNo((String) state[entityMetamodel
						.getPropertyIndex("fundAccountNo")]);
		generalAccountMaintenance
				.setAccountStatus((String) state[entityMetamodel
						.getPropertyIndex("accountStatus")]);
		generalAccountMaintenance.setAccountType((String) state[entityMetamodel
				.getPropertyIndex("accountType")]);
		generalAccountMaintenance.setBrokerId((String) state[entityMetamodel
				.getPropertyIndex("brokerId")]);
		generalAccountMaintenance.setBin((String) state[entityMetamodel
				.getPropertyIndex("bin")]);
		return generalAccountMaintenance;
	}

	private void createCheckCreatedActivity(PostInsertEvent event) {
		Payment check = (Payment) event.getEntity();
		if (check.getPaymentType() == PaymentType.ROF) {
			return;
		}
		Claimant claimant = Claimant.findClaimant(check.getPayTo().getId());
		if (claimant == null) {
			return;
		}
		
		CheckActivity activity = (CheckActivity) setActivityDefaults(new CheckActivity());
		activity.setActivityCode(ActivityCode.CREATED);
		activity.setIdentificationNo(check.getIdentificatonNo());
		activity.setToPaymentCode(check.getPaymentCode());
		activity.setToPaymentType(check.getPaymentType());
		activity.setPayment(check);
		claimant.addActivity(activity);
		// claimant.merge();
		claimant.persist();
		

	}

	private void createCheckChangeActivity(PostUpdateEvent event) {
		Payment check = (Payment) event.getEntity();
		if (check.isSkipActivityGeneration()) {
			return;
		}
		
		// reload detached instance
		Claimant claimant = Claimant.findClaimant(check.getPayTo().getId());
		
		if(check.isPaymentRpoFlag() && check.getPaymentType() == PaymentType.CHECK) {
			Activity activity = createRpoActivity(check);
			claimant.addActivity(activity);
			claimant.persist();
			return;
		} 
		
		CheckActivity activity = (CheckActivity) setActivityDefaults(new CheckActivity());
		activity.setActivityCode(ActivityCode.UPDATED);
		check.updateActivity(activity);
		claimant.addActivity(activity);
		claimant.persist();
		
		if(check.getPaymentType() == PaymentType.CHECK) {
			createActivityIfCheckHasNewOneTimeMailingAddress(event, check, claimant);
		}
	}
	
	/**
	 * A new Activity must be created if the Payment has a new one time mailing address. 
	 * This activity is in addition to any other update activities generated for the Payment.
	 * 
	 * @param event - The PostUpdateEvent for the updated Payment
	 * @param check - The Payment that was updated
	 * @param claimant - The Claimant, the Payment belongs to.
	 */
	private void createActivityIfCheckHasNewOneTimeMailingAddress(PostUpdateEvent event, Payment check, Claimant claimant) {
		Preconditions.checkState(check.getPaymentType() == PaymentType.CHECK, "One time mailing can ony be done for payments of type Check. Buy payment type is %s for id %s.", check.getPaymentType(), check.getId());

		// Retrieve before/after addresses
		MailObjectAddress fromAddress = getMailObjectAddressOfPayment(event.getPersister(), event.getOldState());
		MailObjectAddress toAddress = getMailObjectAddressOfPayment(event.getPersister(), event.getState());
		//Added one another activity, if OneTimeMailingAddress has been updated for the Payment object.
		if(!fromAddress.equals(toAddress) && AddressType.ONE_TIME_MAILING.equals(toAddress.getAddressType())){
			CheckActivity activity_ = (CheckActivity) setActivityDefaults(new CheckActivity());
			activity_.setDescription("Payment mailed to a one time mailing address: \n" + toAddress.getAddressAsString());
			activity_.setActivityCode(ActivityCode.UPDATED);
			check.updateActivity(activity_);
			claimant.addActivity(activity_);
			claimant.persist();
		}
	}
	
	/**
	 * 
	 * @param check - The Payment that has been RPOed.
	 * @return - Activity to record the RPO.
	 */
	private Activity createRpoActivity(Payment check) {
		Preconditions.checkState(check.getPaymentType() == PaymentType.CHECK, "Only payments of type Check can be RPO. Buy payment type is %s for id %s.", check.getPaymentType(), check.getId());
		StringBuilder sb = new StringBuilder();
		Activity activity = (Activity) setActivityDefaults(new Activity());
		activity.setActivityTypeDescription("RPO Process");
		sb.append("<b>Check#</b>").append(check.getIdentificatonNo()).append(" ").append(check.getStatusDescription());
		activity.setDescription(sb.toString());
		activity.setActivityCode(ActivityCode.UPDATED);
		return activity;
	}
	
	/**
	 * Extracts the {@link MailObjectAddress} from the {@link Payment}s state. 
	 * 
	 * @param persister - The EntityPersister of the Payment.
	 * @param state The object state from which the address must be extracted.
	 * @return The {@link MailObjectAddress} of the {@link Payment} or null if the {@link Payment} has no address. 	 
	 */
	private MailObjectAddress getMailObjectAddressOfPayment(EntityPersister persister, Object[] state) {
		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();	
		return (MailObjectAddress) state[entityMetamodel.getPropertyIndex("checkAddress")];
	}

	private void createAlternatePayeeCreatedActivity(PostInsertEvent event) {
		Claimant alternatePayee = (Claimant) event.getEntity();
		Claimant parent = Claimant.findClaimant(alternatePayee
				.getParentClaimant().getId());

		AlternatePayeeActivity activity = (AlternatePayeeActivity) setActivityDefaults(new AlternatePayeeActivity());
		setActivityDefaults(activity);
		Address addr = new Address();
		alternatePayee.getAddressOfRecord().copyTo(addr);
		activity.setAddress(addr);

		RegistrationLines reg = new RegistrationLines();
		alternatePayee.getClaimantRegistration().getLines().copyTo(reg);
		activity.setRegistration(reg);
		activity.setReferenceNo(alternatePayee.getReferenceNo());
		activity.setAlternatePayeeId(alternatePayee.getId());
		parent.addActivity(activity);
		parent.persist();

		Activity childActivity = setActivityDefaults(new Activity());
		childActivity.setActivityTypeDescription(AlternatePayeeActivity.ACTIVITY_TYPE_DESCRIPTION);
		StringBuilder sb = new StringBuilder("Created from Reference#");
		//TODO: This is a very poor hack. Can cause memory leaks in the UI. Second the context root is hard coded.
		sb.append("<a href='/saec/app/claimant/edit?id=").append(alternatePayee.getParentClaimant().getId()).append("'>").append(parent.getReferenceNo()).append("</a>");
		childActivity.setDescription(sb.toString());
		alternatePayee.addActivity(childActivity);

	}
	

	/**
	 * Handles Address Change / Address Research Change activity
	 * 
	 * @param event
	 *            update event
	 */
	private void createAddressChangeActivity(PostUpdateEvent event) {
		// Retrieve before/after addresses
		Address from = getAddress(event.getPersister(), event.getOldState());
		Address to = getAddress(event.getPersister(), event.getState());
		Claimant claimant = ((ClaimantAddress) event.getEntity()).getClaimant();
		
		//Added an Activity for Address-Type Update
		if(claimant != null && to.getAddressType() != null ){
			if (!requiresActivityGeneration(from, to) && from.getAddressType() == claimant.getMailingAddress().getAddressType()) {
				return;
			}
			else if (!requiresActivityGeneration(from, to) || from.getAddressType() != claimant.getMailingAddress().getAddressType()) {
				Activity addressTypeChangeActivity = new Activity();
				addressTypeChangeActivity.setActivityDate(new Date());
				if (claimant.getMailingAddress().getAddressType().equals(AddressType.ADDRESS_OF_RECORD)) {
					addressTypeChangeActivity.setDescription("Primary Address Selected");
				} 
				else if (claimant.getMailingAddress().getAddressType().equals(AddressType.SEASONAL_ADDRESS)){
					addressTypeChangeActivity.setDescription("Seasonal Address Selected");
				}
				claimant.addActivity(addressTypeChangeActivity);
				claimant.persist();
				return;
			}
		
		// Create an Address Change event
		AddressChange addressChange = (AddressChange) setActivityDefaults(new AddressChange());
		addressChange.setFrom(from);
		addressChange.setTo(to);

		// Evaluate if Address Change is an Address Research Return activity
		AddressResearchChangeActivity researchChangeActivity = to
				.getResearchChangeActivity();
		if (researchChangeActivity.isResearchReturned()) {
			log.info("Address Research Return identified ... ");
		}

		// Update Claimant with activity
		//Claimant claimant = ((ClaimantAddress) event.getEntity()).getClaimant();
		addressChange.setClaimant(claimant);
		claimant.addAddressChangeActivity(addressChange);

		claimant.persist();
		}
	}
	
	
	private void createAddressCreatedActivity(PostInsertEvent event) {
		Claimant claimant = ((ClaimantAddress) event.getEntity()).getClaimant();
			claimant.triggerAddressCreatedEvent(claimant.getMailingAddress());		
	}
	
	/**
	 * @param from
	 *            - The {@link Address} with old state.
	 * @param to
	 *            - The {@link Address} with updated state.
	 * @return true if the from and to {@link Address} are equal
	 *         {@link Object#equals(Object)} or if their corresponding
	 *         {@link AddressResearchChangeActivity} are equal
	 *         {@link Object#equals(Object)} .
	 */
	private boolean requiresActivityGeneration(Address from, Address to) {
		if (from.equals(to)) { // The address has not changed.
			AddressResearchChangeActivity fromResearchChangeActivity = from
					.getResearchChangeActivity();
			AddressResearchChangeActivity toResearchChangeActivity = to
					.getResearchChangeActivity();
			if (fromResearchChangeActivity != null) {
				if (fromResearchChangeActivity.equals(toResearchChangeActivity)) { // The
																					// Research
																					// info
																					// has
																					// not
																					// changed.
					return false;
				} else if (toResearchChangeActivity == null) { // No Research
																// info on both
																// from and to
																// objects.
					return false;
				}
			}
		}
		return true;
	}

	private Address getAddress(final EntityPersister persister,
			final Object[] state) {
		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		Address addr = new Address();
		((IAddress) state[entityMetamodel.getPropertyIndex("address")])
				.copyTo(addr);
		return addr;
	}

	private void createNameChangeActivity(PostUpdateEvent event) {
		NameChange a = (NameChange) setActivityDefaults(new NameChange());
		String[] propertyNames = event.getPersister().getPropertyNames();
		a.setFrom(getRegistrationLines(event.getPersister(),
				event.getOldState()));
		a.setTo(getRegistrationLines(event.getPersister(), event.getState()));

		Claimant c = Claimant.findClaimant(((ClaimantRegistration) event
				.getEntity()).getId());
		a.setClaimant(c);
		// a.map();
		c.addActivity(a);
		c.persist();
		// c.flush();
	}

	private Activity setActivityDefaults(final Activity activity) {
		return Activity.setActivityDefaults(activity);
	}

	private RegistrationLines getRegistrationLines(
			final EntityPersister persister, final Object[] state) {
		EntityMetamodel entityMetamodel = persister.getEntityMetamodel();
		RegistrationLines ret = new RegistrationLines();
		((RegistrationLines) state[entityMetamodel.getPropertyIndex("lines")])
				.copyTo(ret);
		return ret;
	}

}
