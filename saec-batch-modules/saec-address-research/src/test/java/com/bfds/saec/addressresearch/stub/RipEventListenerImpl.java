package com.bfds.saec.addressresearch.stub;

import com.bfds.saec.rip.domain.*;
import com.bfds.saec.rip.dto.PhoneCallDto;
import com.bfds.saec.rip.service.RipEventListener;
import org.springframework.stereotype.Service;
@Service("ripEventListener")
public class RipEventListenerImpl implements RipEventListener {

	@Override
	public void addressChanged(AddressChangeRipObject ripObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cureLetterCreated(CureLetterRipObject ripObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopReplaceCheckRequested(StopReplaceCheckRipObject ripObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSpecialHandlingInstructions(
			SpecialhandlingRipObject ripObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void phoneCallReceived(PhoneCallDto callDto) {
		// TODO Auto-generated method stub

	}

    @Override
    public void sendClaimFormSupportingDocument(ClaimFormSupportingDocRipObject ripObject) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public void followupreminderCreated(FollowupRemindersRipObject ripObject) {
		// TODO Auto-generated method stub
		
	}

}
