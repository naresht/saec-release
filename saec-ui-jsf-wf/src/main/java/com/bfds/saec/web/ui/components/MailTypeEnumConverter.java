package com.bfds.saec.web.ui.components;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.bfds.saec.domain.reference.MailType;


public class MailTypeEnumConverter implements Converter {

	private static final String INCOMING = "Incoming";
	private static final String OUTGOING = "Outgoing";
    
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.trim().length() == 0) {
            if (component instanceof EditableValueHolder) {
                ((EditableValueHolder) component).setSubmittedValue(null);
            }
            return null;
        }
        if(INCOMING.equals(value)) {
        	return MailType.INCOMING;
        } else if(OUTGOING.equals(value)) {
        	return MailType.OUTGOING;
        }
        throw new IllegalArgumentException("unknown MailType " + value);
    }

    
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if(value == null) {
        	return null;
        } 
        MailType mailType = (MailType) value;
        if(mailType == MailType.INCOMING) {
        	return INCOMING;
        } else if(mailType == MailType.OUTGOING) {
        	return OUTGOING;
        }
        throw new IllegalArgumentException("unknown MailType " + mailType);
    }
}