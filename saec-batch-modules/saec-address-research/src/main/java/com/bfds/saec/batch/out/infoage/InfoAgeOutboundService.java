package com.bfds.saec.batch.out.infoage;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.bfds.saec.batch.file.domain.out.infoage.AddressResearchOut;
import com.bfds.saec.batch.file.domain.out.infoage_corporate.OutboundCorporateAddressResearch;
import com.bfds.saec.batch.file.domain.out.infoage_individual.OutboundIndividualAddressResearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;

import com.bfds.saec.dao.EntityAuditDao;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantAddress;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.InfoForNewAddressResearch;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.bfds.saec.domain.audit.EntityWithAuditVo;
import com.bfds.saec.domain.audit.RevisionInfo;
import com.bfds.saec.util.SaecDateUtils;
import org.springframework.util.StringUtils;

/**
 * Parent Address Research Creation Service for InfoAge vendor. Handles common
 * business process activities
 */
public abstract class InfoAgeOutboundService<T extends AddressResearchOut> implements AddressResearchOutboundService<T> {

	final Logger log = LoggerFactory.getLogger(InfoAgeOutboundService.class);

	public static String RESEARCH_TYPE_PRESCRUB = "prescrub";
	public static String RESEARCH_TYPE_ADDRESS_RESEARCH = "research";

	// Shared notification Info
	protected AddressResearchOutboundNotification notification = new AddressResearchOutboundNotification();

	@Autowired
	private IMailSender mailSender;

	@Inject
	protected EntityAuditDao entityAuditDao;

    @Override
    public T createPreScrub(Claimant account) {
        account= Claimant.findClaimant(account.getReferenceNo());
        account.setAddressResearchSent(Boolean.TRUE);
        account.merge();

        // Update account history
        final ClaimantAddress claimantAddress = account.getMailingAddress();
        if(claimantAddress != null) {
            final AddressResearchChangeActivity activity = newAddressResearchChangeActivity(RESEARCH_TYPE_PRESCRUB, null);
            claimantAddress.getAddress().setResearchChangeActivity(activity);
            claimantAddress.merge().persist();
        }

        updateNotificationInfo(true);
        return buildResearchOutObject(account);
    }

    @Override
    public T createAddressResearch(Claimant account) {
        log.info(String.format("Found Account:", account));
        account = validateAndUpdateAccount(account);
        if(account == null) { return null;}
        updateNotificationInfo(true);

        return buildResearchOutObject(account);
    }

    private T buildResearchOutObject(Claimant account) {
        final T research =  newOutputObject();
        setStandardProperties(account, research);
        setAdditionalProperties(account, research);
        return research;
    }

    /**
     * A factory  method to create the output object for address research.
     * @return An instance of {@link AddressResearchOut}
     */
    protected abstract T newOutputObject() ;

    protected void setStandardProperties(final Claimant account, final T research) {

        research.setInputRecord("");
        research.setUserRef(account.getReferenceNo());
        ClaimantAddress claimantAddress = account.getMailingAddress();
        if(claimantAddress != null) {
            research.setStreetAddress(claimantAddress.getAddress1());
            research.setCity(claimantAddress.getCity());
            research.setState(claimantAddress.getStateCode());
            if (claimantAddress.getZipCode() != null && StringUtils.hasText(claimantAddress.getZipCode().getZip())) {
                research.setZipCode(claimantAddress.getZipCode().toString());
            }
            else{
                research.setZipCode("");
            }
        }
    }

    protected abstract void setAdditionalProperties(final Claimant account, T research) ;

	 

	protected void updateNotificationInfo(boolean success) {
		log.debug("Updating Record count");
		notification.setSuccess(success);
		notification.setRecordCount(notification.getRecordCount() + 1);
		log.debug("Latest Notification Info: " + notification);
	}

	/**
	 * Mark that address has changed due to research so the listener can pick
	 * up.
	 * 
	 * @param researchType
	 * @param rpoType
	 */
	protected AddressResearchChangeActivity newAddressResearchChangeActivity(
			String researchType, String rpoType) {
		AddressResearchChangeActivity activity = null;
		if (RESEARCH_TYPE_PRESCRUB.equals(researchType)) {
			activity = new AddressResearchChangeActivity()
					.markSentAsPrescrubResearch();
		} else if (RESEARCH_TYPE_ADDRESS_RESEARCH.equals(researchType)) {
			activity = new AddressResearchChangeActivity()
					.markSentAsAddressResearch(rpoType);
		}

		activity.setAddressResearchDate(new Date());

		return activity;
	}

	/**
	 * Retrieve the last maintenance date on Claimant Address
	 * 
	 * @param account
	 *            account
	 * @return
	 */
	protected Date getLastMaintenanceDate(final ClaimantAddress account) {
        try{
		RevisionInfo revisionInfo = entityAuditDao.getLastRevisionInfo(ClaimantAddress.class, account.getId());
		if (revisionInfo != null) {
			return revisionInfo.getRevisionDate();
		}
        }catch (Exception e) {
            //There is a bug in EntityAuditDaoImpl that results in an exception when the entity has no revisions.
            // no op.
        }
		return null;
	}

	
	/**
	 * @param account - The {@link Claimant}
	 * @param info - The InfoForNewAddressResearch for the given {@link Claimant}
	 * @return - True if the {@link Claimant} is eligible for address research.
	 */
	protected boolean isClaimantEligibleForResearch(final Claimant account, final InfoForNewAddressResearch info) {
		if(!info.isResearchEligible()) {
			log.info(String.format("Not eligible for address research. Skipping address research for %s: ", account.getReferenceNo()));

			// Skip this record
			return false;
		}
		ClaimantAddress address = account.getAddressOfRecord();
		final Date rpoitemDate = info.getLastRpoItemDate();

		// If last maint date > the mail date ==> EXCLUDE.
		// TODO: Check logic, spec unclear
		if (getLastMaintenanceDate(address) != null
				&& rpoitemDate != null
				&& SaecDateUtils.isAfterDay(getLastMaintenanceDate(address),
						rpoitemDate)) {
			// Skip this record
			return false;
		}
		return true;
	}

	protected String retrieveCompanyName(final Claimant account) {
		String name = account.getRegistrationLinesAsString("");
		if (name.length() > 54) {
			return name.substring(0, 54);
		}
		return name;
	}
	
	/**
	 * @param account - The {@link Claimant} for which address research is hapenning.
	 * @param info - The {@link InfoForNewAddressResearch} for the given {@link Claimant}
	 * @return A new {@link AddressResearchChangeActivity} 
	 */
	protected AddressResearchChangeActivity newAddressResearchChangeActivity( Claimant account, final InfoForNewAddressResearch info) {
		AddressResearchChangeActivity activity = null;
      
		if(info.getCurrentRpoPaymentCount() > 0 && info.getCurrentRpoLetterCount() > 0) {
			activity = newAddressResearchChangeActivity(RESEARCH_TYPE_ADDRESS_RESEARCH, 
					                                     AddressResearchChangeActivity.ADDRESS_RESEARCH_SENT_TYPE_LETTER_AND_CHECK);
		} else if(info.getCurrentRpoPaymentCount() > 0) {
			activity = newAddressResearchChangeActivity(RESEARCH_TYPE_ADDRESS_RESEARCH, 
                		                                 AddressResearchChangeActivity.ADDRESS_RESEARCH_SENT_TYPE_CHECK);
		} else if(info.getCurrentRpoLetterCount() > 0) {
			activity = newAddressResearchChangeActivity(RESEARCH_TYPE_ADDRESS_RESEARCH, 
					                                    AddressResearchChangeActivity.ADDRESS_RESEARCH_SENT_TYPE_LETTER);
		} else {
			throw new IllegalStateException("No RPO NON Forwardable items for Claimant: " + account.getReferenceNo());
		}
		return activity;
	}
	
	/**
	 * @param account - The {@link Claimant} that is going out for address research
	 * @return - An update {@link Claimant} whith the addresearch information.
	 */
	protected Claimant validateAndUpdateAccount(Claimant account) {
		account = Claimant.findClaimant(account.getId());
        log.info(String.format("Found Account: %s", account.getReferenceNo()));
        
        final InfoForNewAddressResearch info = InfoForNewAddressResearch.getInfoForNewAddressResearch(account, Event.getCurrentEvent());
        if (!isClaimantEligibleForResearch(account, info)) { 
        	 log.info(String.format("Skipping record since validation failed %s", account.getReferenceNo()));
             return null;
        }

        account.getMailingAddress().getAddress().setResearchChangeActivity(newAddressResearchChangeActivity(account, info));

        if(info.getCurrentRpoLetterCount() > 0 && info.getCurrentRpoPaymentCount() > 0) {
        	account.setAddressResearchSentCountForLettersAndChecks(
        			account.getAddressResearchSentCountForLettersAndChecks() + 1);
        } else if(info.getCurrentRpoLetterCount() > 0 ) {
        	account.setAddressResearchSentCountForLetters(
        			account.getAddressResearchSentCountForLetters() + 1);
        }if(info.getCurrentRpoPaymentCount() > 0) {
        	account.setAddressResearchSentCountForChecks(account.getAddressResearchSentCountForChecks() + 1);
        }
        account.setAddressResearchSent(Boolean.TRUE);
        account = account.merge();
		return account;
	}
	
}
