package com.bfds.saec.web.ui.components;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.bfds.saec.domain.reference.CallType;


public class CallTypeEnumConverter implements Converter {

	private static final String INBOUND = "IN";
	private static final String OUTBOUND = "OUT";
    /**
     * Returns empty String value as null and if component is an instance of EditableValueHolder,
     * then also set its submitted value to null. This affects under each UIInput.
     * @see javax.faces.convert.Converter#getAsObject(
     *      javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.trim().length() == 0) {
            if (component instanceof EditableValueHolder) {
                ((EditableValueHolder) component).setSubmittedValue(null);
            }
            return null;
        }
        if(INBOUND.equals(value)) {
        	return CallType.INBOUND;
        } else if(OUTBOUND.equals(value)) {
        	return CallType.OUTBOUND;
        }
        throw new IllegalArgumentException("unknown CallType " + value);
    }

    /**
     * Does nothing special.
     * @see javax.faces.convert.Converter#getAsString(
     *      javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     */
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if(value == null) {
        	return null;
        } 
        CallType callType = (CallType)value;
        if(callType == CallType.INBOUND) {
        	return INBOUND;
        } else if(callType == CallType.OUTBOUND) {
        	return OUTBOUND;
        }
        throw new IllegalArgumentException("unknown CallType " + callType);
    }
}