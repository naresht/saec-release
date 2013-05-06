package com.bfds.saec.web.ui.components;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.bfds.saec.domain.reference.RequestType;


public class RequestTypeEnumConverter implements Converter {

	private static final String ADDRESS_CHANGE = "Address Change";
	private static final String DEATH_PAYEE_CHANGE = "Death Payee Change";
	private static final String TRUSTEE_CHANGE = "Trustee Change";
    
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.trim().length() == 0) {
            if (component instanceof EditableValueHolder) {
                ((EditableValueHolder) component).setSubmittedValue(null);
            }
            return null;
        }
        if(ADDRESS_CHANGE.equals(value)) {
        	return RequestType.ADDRESS_CHANGE;
        } else if(DEATH_PAYEE_CHANGE.equals(value)) {
        	return RequestType.DEATH_PAYEE_CHANGE;
        }else if(TRUSTEE_CHANGE.equals(value)) {
        	return RequestType.TRUSTEE_CHANGE;
        }
        throw new IllegalArgumentException("unknown RequestType " + value);
    }

    
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if(value == null) {
        	return null;
        } 
        RequestType requestType = (RequestType) value;
        if(requestType == RequestType.ADDRESS_CHANGE) {
        	return ADDRESS_CHANGE;
        } else if(requestType == RequestType.DEATH_PAYEE_CHANGE) {
        	return DEATH_PAYEE_CHANGE;
        }else if(requestType == RequestType.TRUSTEE_CHANGE) {
        	return TRUSTEE_CHANGE;
        }
        throw new IllegalArgumentException("unknown RequestType " + requestType);
    }
}