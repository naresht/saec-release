package com.bfds.saec.batch.in.infoage;

import com.bfds.saec.batch.in.infoage.rule.RulesManager;
import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;
import com.bfds.saec.domain.*;
import com.bfds.saec.domain.activity.AddressResearchChangeActivity;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.util.IMailSender;
import com.bfds.saec.util.SaecDateUtils;
import com.bfds.scheduling.domain.MailingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Parent Address Research Service for InfoAge vendor. Handles common business process activities, except
 * Address Update
 */
public abstract class InfoAgeInboundService implements AddressResearchInboundService {

    public static final String UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED = "Updated address was not found - NO FURTHER ACTION needed";

	final Logger log = LoggerFactory.getLogger(InfoAgeInboundService.class);

    @Inject
    @Named("simpleRulesManager")
    private RulesManager rulesManager;

    @Autowired
    private IMailSender mailSender;

    // Shared notification Info
    protected AddressResearchNotification notification = new AddressResearchNotification();

    /**
     * Abstract address Change that Individual and Corproate Accounts need to handle
     *
     * @param claimant            referenced Claimant
     * @param researchStatus      updated research status (after rules processing)
     */
    protected abstract void updateAddressChange(Claimant claimant, AddressResearchStatus researchStatus);

    
    @Override
    public void processResearchedAddresses(List<AddressResearchUpdate> addressResearchUpdates) {
    	if (log.isInfoEnabled()) 
    		log.info("Processing Researched Addresses....");
    	
        List<AddressResearchStatus> addressResearchStatuses = new ArrayList<AddressResearchStatus>();

        for(AddressResearchUpdate addressResearchUpdate : addressResearchUpdates) {
            addressResearchStatuses.add(rulesManager.fireAllRules(addressResearchUpdate));
        }

        for (AddressResearchStatus researchStatus : addressResearchStatuses) {
        	if (log.isDebugEnabled()) 
        		log.debug(String.format("Found [%s] AddressResearchStatus", researchStatus));
            updateNotificationInfo(researchStatus.getAddressResearchUpdate());


            try {
            	Claimant claimant= Claimant.findClaimant(researchStatus.getAddressResearchUpdate().getUserRef());
            	if (log.isInfoEnabled()) 
            		log.info(String.format("Retrieved Claimant[%s], Updating status ...", claimant.getReferenceNo()));
                claimant.setAddressResearchSent(false);
                updateAddressChange(claimant, researchStatus);
                // In addition to Prescrub process, Address Research updates Payment Objects
                updatePaymentHistory(claimant, researchStatus);
                updateMailingHistory(claimant, researchStatus); 
            }
            catch (EmptyResultDataAccessException e) {
            	if (log.isErrorEnabled())
            		log.error("Claimant not found for userRef: " + researchStatus.getAddressResearchUpdate().getUserRef());
                continue;  
            }               
        }
    
    }

    protected void updateAddress(Claimant claimant, AddressResearchStatus researchStatus, AddressResearchUpdate research, String researchReturnString) {
        // Update Phone irrespective of status
        updatePhoneNumber(research, claimant);

        // Validate
        // Added One Extra Validation,For GitHub Issue #715 {issue_1,Log is not getting created when the address is not updated i.e Hit indicator=N}
        if (!researchStatus.isAddressUpdated()) {
            if(!research.isHit()) {
                log.info("ResearchStatus marked Address Updated as false,But HitIndicator is N,So Updating Acctivity");
                AddressResearchChangeActivity activity = new AddressResearchChangeActivity().markReturnedAsNoHit(researchReturnString);
                Date addressResearchDate = AddressResearchUtils.convertToDate(research.getAddressDateReported());
                activity.setAddressResearchDate(addressResearchDate == null ? new Date() : addressResearchDate);
                claimant.getMailingAddress().getAddress().setResearchChangeActivity(activity);
            } else {
                log.info("Oops! ResearchStatus marked Address Updated as false, ignoring the request.");
            }
            return;
        }

        // Initialize Address from Research Data
        // NOTE: This is a hack for now. Need an elegant solution
        Address newAddress = initAddressWithResearchActivity(researchStatus.getAddressResearchUpdate(), AddressResearchUtils.convertToDate(research.getAddressDateReported()), researchReturnString);
        // Update Address with research data
        updateAddress(research, newAddress);
        // Get the old address
        ClaimantAddress claimantAddress = claimant.getMailingAddress();
        if (claimantAddress == null) {  // TODO: Is this correct?
            log.warn("Old address doesn't exist for Claimant: " + claimant.getReferenceNo());
            claimantAddress = new ClaimantAddress();
            claimantAddress.setAddress(newAddress);
            claimantAddress.setClaimant(claimant);
            newAddress.setAddressType(AddressType.ADDRESS_OF_RECORD);
            newAddress.setAddressResearchCount(1);
            claimantAddress.setMailingAddress(true);
            claimantAddress.persist();
        } else {
            claimantAddress.setAddress(newAddress);
            claimantAddress.setMailingAddress(true);
            newAddress.setAddressType(claimantAddress.getAddressType() == null ? AddressType.ADDRESS_OF_RECORD : claimantAddress.getAddressType());
            int count = claimantAddress.getAddress().getAddressResearchCount();
            newAddress.setAddressResearchCount(count + 1);
        }
        log.debug("Updated Address Change History...");
    }


    @Override
    public void notifyStatus() {
        try {
            mailSender.send(MailingList.findByCode("batch.jobs.infoage"), "InfoAge Address Research Notification", notification.toString());
        }
        catch (Exception e) {
            log.error("Unable to send email notification: " + e.getMessage());
        }
    }

    protected void updatePaymentHistory(final Claimant claimant, final AddressResearchStatus researchStatus) {
        log.info(String.format("Updating Payment History for Claimant[%s] with a Research [%s] ...", claimant.getReferenceNo(), researchStatus));

        // Retrieve all NONFORWARDABLE Checks
        List<Payment> checks = Payment.findPaymentsByPaymentCodeAndPayTo(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN, claimant).getResultList();
        log.info(String.format("Retrieved %s checks for Claimant[%s]", checks.size(), claimant.getReferenceNo()));

        if (researchStatus.isRpoObjectUpdated()) {
            log.info("Found rpo object as updated");

            for (Payment check : checks) {
            	if(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN == check.getPaymentCode()) {
            		check.setPaymentCode(PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA);
            		log.debug("Updated Payment History [Payment.PaymentStatus = REISSUE_APPROVED]: " + check.getIdentificatonNo());
            	}
            }
        } else {
            log.info("Found rpo object as NOT updated");

            log.info(String.format("Retrieved %s checks with RPOType.NONFORWARDABLE, Updating status ...", checks.size()));
            // Update RPO Non-forwardable object status to ‘No further Action’
            for (Payment check : checks) {
                check.setComments(getResearchDate(researchStatus.getAddressResearchUpdate()) + " " + UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
                log.debug("Updated Payment History [Payment.PaymentStatus = NO_FURTHER_ACTION]: " + check.getIdentificatonNo());
            }
        }
    }

    protected void updateMailingHistory(final Claimant claimant, final AddressResearchStatus researchStatus) {
        log.info(String.format("Updating Mailing History for Claimant[%s] with a Research [%s] ...", claimant.getReferenceNo(), researchStatus));

         List<Letter> letters = Letter.findLettersByRpoTypeAndClaimant(RPOType.NONFORWARDABLE, claimant).getResultList();

        for (Letter letter : letters) {
           // log.debug("Updating Mailing Object [Letter]: " + letter);
            if (researchStatus.isRpoObjectUpdated()) {
                log.info(String.format("Found %s letters, Updating status ...", letters.size()));
                letter.setLetterStatus(LetterStatus.SUBMITTED);
            }
            else {  // Mail object will update to ‘no further action’
                log.info(String.format("Found %s letters, Updating status ...", letters.size()));
                // letter.setLetterStatus(LetterStatus.NO_FURTHER_ACTION);
                letter.setComments(getResearchDate(researchStatus.getAddressResearchUpdate()) + " " + UPDATED_ADDRESS_WAS_NOT_FOUND_NO_FURTHER_ACTION_NEEDED);
            }
        }
    }

    protected void updateNotificationInfo(final AddressResearchUpdate addressResearchUpdate) {
        log.debug("Updating Record count");
        notification.setRecordCount(notification.getRecordCount() + 1);

        if (addressResearchUpdate.isHit()) {
            log.debug("Found a Hit! Updating Notification Hit count");
            notification.setNoOfHits(notification.getNoOfHits() + 1);
        }
        else {
            log.debug("Found a NO Hit. Updating Notification NO Hit count");
            notification.setNoOfNoHits(notification.getNoOfNoHits() + 1);
        }
        log.debug("Latest Notification Info: " + notification);
    }


    /**
     * Returns a new Address based on the research status
     *
     * @param addressResearchUpdate
     * @param addressDateReported
     * @param researchReturnString
     * @return
     */
    protected Address initAddressWithResearchActivity(final AddressResearchUpdate addressResearchUpdate, final Date addressDateReported, final String researchReturnString) {
        // Create the new address
        Address newAddress = new Address();

        // Mark that address has changed due to research so the listener can pick up.
        // NOTE: This is a hack for now. Need an elegant solution
        AddressResearchChangeActivity activity = null;
        if (addressResearchUpdate.isHit()) {
            activity = new AddressResearchChangeActivity().markReturnedAsHit(researchReturnString);
        } else {
            activity = new AddressResearchChangeActivity().markReturnedAsNoHit(researchReturnString);
        }

        activity.setAddressResearchDate(addressDateReported == null ? new Date() : addressDateReported);
        newAddress.setResearchChangeActivity(activity);
        if (log.isInfoEnabled()) 
    		log.info("Address research change activity saved........");
        return newAddress;
    }

    protected void updatePhoneNumber(final AddressResearchUpdate research, final Claimant claimant) {
        StringBuilder phone = new StringBuilder();
        if(StringUtils.hasText(research.getPhone())) {
            if(StringUtils.hasText(research.getPhoneAreaCode())) {
                phone.append(research.getPhoneAreaCode()).append("-");
            }
            phone.append(research.getPhone());           
        }
        if(StringUtils.hasText(phone)) {
            if (claimant.getPrimaryContact() == null) {
                claimant.setPrimaryContact(new Contact());
            }
            claimant.getPrimaryContact().setPhoneNo(phone.toString());            
        }
        if (log.isInfoEnabled()) 
    		log.info("Phone Number Updated from Address Research Update........");
    }

    /**
     * Update newAddress {@link Address} with data from research {@link AddressResearchUpdate}.
     * @param research - The address research result record.
     * @param newAddress - The new Address that needs to be updated.
     */
    protected void updateAddress(final AddressResearchUpdate research, final Address newAddress) {
        final String address = research.getAddress();
        if (address.length() > 48) {
            newAddress.setAddress1(address.substring(0, 47));
            newAddress.setAddress2(address.toString().substring(48));
        } else {
            newAddress.setAddress1(address);
        }
        newAddress.setCity(research.getCity());
        newAddress.setStateCode(research.getState());
        if (StringUtils.hasText(research.getZipCode())) {
                newAddress.setZipCode(ZipCode.parse((research.getZipCode())));
        }
        newAddress.setPhone(research.getPhone());
        newAddress.setPhoneExt(research.getPhoneExt());
        if (log.isInfoEnabled()) 
    		log.info("Address Updated from Address Research Update....");
    }
    
    private String getResearchDate(final AddressResearchUpdate addressResearchUpdate) {
       String date = "";                
       if (addressResearchUpdate.getAddressDateReported() != null) {
           date = SaecDateUtils.getDateFormatted(AddressResearchUtils.convertToDate(addressResearchUpdate.getAddressDateReported()));
           date += "-";
       }
       
       return date;
    }
}
