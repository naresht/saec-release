package com.bfds.saec.web.ui.components;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.bfds.saec.domain.reference.AddressType;

public class AddressTypeEnumConverter implements Converter {

	private static final String PRIMARY_ADDRESS = "Primary Address";
	private static final String SEASONAL_ADDRESS = "Seasonal Address";

	/**
	 * Returns empty String value as null and if component is an instance of
	 * EditableValueHolder, then also set its submitted value to null. This
	 * affects under each UIInput.
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		if (value == null || value.trim().length() == 0) {
			if (component instanceof EditableValueHolder) {
				((EditableValueHolder) component).setSubmittedValue(null);
			}
			return null;
		}
		if (SEASONAL_ADDRESS.equals(value)) {
			return AddressType.SEASONAL_ADDRESS;
		} else if (PRIMARY_ADDRESS.equals(value)) {
			return AddressType.ADDRESS_OF_RECORD;
		}
		throw new IllegalArgumentException("unknown AddressType " + value);
	}

	/**
	 * Does nothing special.
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		if (value == null) {
			return null;
		}
		AddressType addressType = (AddressType) value;
		if (addressType == AddressType.ADDRESS_OF_RECORD) {
			return PRIMARY_ADDRESS;
		} else if (addressType == AddressType.SEASONAL_ADDRESS) {
			return SEASONAL_ADDRESS;
		}
		throw new IllegalArgumentException("unknown AddressType " + addressType);
	}
}