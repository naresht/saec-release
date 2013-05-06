package com.bfds.saec.rip.service;

import com.bfds.saec.rip.domain.*;
import com.bfds.saec.rip.dto.PhoneCallDto;

/**
 * An Listener to consume application events that might trigger sending a Rip file to AWD. 
 * 
 * We will have two kinds of events
 * 	1. An event that contains data that can be sent on a RIP file. 
 *  2. An event that will trigger the sending of the data sent by an earlier event( type 1 event). 
 */
public interface RipEventListener {
	
	
	void followupreminderCreated(FollowupRemindersRipObject ripObject) ;
	/**
	 * @param ripObject - A {@link AddressChangeRipObject} that contains the details of the address change.
	 */
	void addressChanged(AddressChangeRipObject ripObject);
	
	/**
	 * @param ripObject - A {@link CureLetterRipObject} that contains the details of the new cure letter.
	 */
	void cureLetterCreated(CureLetterRipObject ripObject);
	
	/**
	 * @param ripObject - A {@link StopReplaceCheckRipObject} that contains the details of the Check that is request for a stop-replace.
	 */
	void stopReplaceCheckRequested(StopReplaceCheckRipObject ripObject);
	
	/**
	 * @param ripObject - A {@link SpecialhandlingRipObject} that contains special handling instructions for the Claimant.
	 */
	void sendSpecialHandlingInstructions(SpecialhandlingRipObject ripObject);	
	
	/**
	 * @param callDto - A {@link PhoneCallDto} that contains PK of the call.
	 */
	void phoneCallReceived(PhoneCallDto callDto);

    /**
     *
     * @param ripObject  - A {@link com.bfds.saec.rip.domain.ClaimFormSupportingDocRipObject} that represents a scanned image submitted for a claim.
     */
    void sendClaimFormSupportingDocument(ClaimFormSupportingDocRipObject ripObject);

}
