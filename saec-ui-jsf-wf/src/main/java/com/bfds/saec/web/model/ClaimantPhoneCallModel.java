package com.bfds.saec.web.model;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.CallCode;
import com.bfds.saec.domain.reference.CallType;
import com.bfds.saec.rip.dto.PhoneCallDto;
import com.bfds.saec.rip.service.RipEventListener;
import com.bfds.saec.web.ui.components.model.CallCodeDataModel;
import com.bfds.saec.web.util.JsfUtils;

public class ClaimantPhoneCallModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static final int MAX_ALLOWED_CALL_CODES = 6;
	private PhoneCall call;
	private CallCode[] selectedCallCodes;
	
	private transient CallCodeDataModel callcodesModel;
	
	public ClaimantPhoneCallModel(PhoneCall call) {
		this.call = call;
	}
		
	public CallCode[] getSelectedCallCodes() {
		return selectedCallCodes;
	}

	public void setSelectedCallCodes(CallCode[] selectedCallCodes) {
		this.selectedCallCodes = selectedCallCodes;
	}

	public PhoneCall getCall() {
		return call;
	}
	public void setCall(PhoneCall call) {
		this.call = call;
	}
	
	public CallCodeDataModel getAllCallCodes() {
		if(this.callcodesModel == null) {
			this.callcodesModel = new CallCodeDataModel(CallCode.findAllCallCodes());
		}
		return this.callcodesModel;
	}

	public CallType getInboundCallType() {
		return CallType.INBOUND;
	}
	
	public CallType getOutboundCallType() {
		return CallType.OUTBOUND;
	}	
	
	public boolean loadPhoneCall(final MessageContext messageContext) {
		this.call = PhoneCall.findPhoneCall(call.getId());
		return true;
	}
	
	public boolean save(final MessageContext messageContext, final RipEventListener ripEventListener) {
		if(isValidCallLog(messageContext)) {
			call.setShapshot(false);
			call.getCallCode().clear();
			call.getCallCode().addAll(java.util.Arrays.asList(selectedCallCodes));
			call.setPhoneExtension(getPhoneExtn());
			call.merge().persist();
			PhoneCallDto callDto = new PhoneCallDto();
			callDto.setPhoneCallId(call.getId());
			ripEventListener.phoneCallReceived(callDto);
			return true;
		}		
		return false;
	}

	private boolean isValidCallLog(final MessageContext messageContext) {
		if(this.getSelectedCallCodes() == null || this.getSelectedCallCodes().length == 0) {
			messageContext.addMessage(new MessageBuilder().error()
					.source(JsfUtils.getClientId("callCodesList"))
					.defaultText("A call code must be selected.")
					.build());
			return false;
		} else if(this.getSelectedCallCodes().length > MAX_ALLOWED_CALL_CODES){
			messageContext.addMessage(new MessageBuilder().error()
					.source(JsfUtils.getClientId("callCodesList"))
					.defaultText("You have selected "+this.getSelectedCallCodes().length+" call code(s). Max allowed is "+MAX_ALLOWED_CALL_CODES+".")
					.build());	
			return false;
		}
		return true;

	}


	/**Phone Extension getting from the session to show in UI 
	 * @return
	 */
	public String getPhoneExtn() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return (String) session.getAttribute("phoneExtension");
	}

	/**
	 * @param phoneExtension -- setting the value from UI to session
	 */
	public void setPhoneExtn(String phoneExtension) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		session.setAttribute("phoneExtension", phoneExtension);
	}

	
}