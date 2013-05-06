package com.bfds.saec.web.ui.components;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.bfds.saec.domain.reference.LetterStatus;


public class LetterStatusEnumConverter implements Converter {

	private static final String IGO = "IGO";
	private static final String NIGO = "NIGO";
	private static final String RPO = "RPO";
    
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.trim().length() == 0) {
            if (component instanceof EditableValueHolder) {
                ((EditableValueHolder) component).setSubmittedValue(null);
            }
            return null;
        }
        if(IGO.equals(value)) {
        	return LetterStatus.IGO;
        } else if(NIGO.equals(value)) {
        	return LetterStatus.NIGO;
        } else if(RPO.equals(value)) {
        	return LetterStatus.RPO;
        }
        throw new IllegalArgumentException("unknown LetterStatus " + value);
    }

    
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if(value == null) {
        	return null;
        } 
        LetterStatus val = (LetterStatus) value;
        if(val == LetterStatus.IGO) {
        	return IGO;
        } else if(val == LetterStatus.NIGO) {
        	return NIGO;
        } else if(val == LetterStatus.RPO) {
        	return RPO;
        }
        throw new IllegalArgumentException("unknown LetterStatus " + val);
    }
}