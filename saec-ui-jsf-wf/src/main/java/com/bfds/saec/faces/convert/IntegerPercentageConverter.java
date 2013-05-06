package com.bfds.saec.faces.convert;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.IntegerConverter;

@FacesConverter("com.bfds.saec.faces.convert.IntegerPercentageConverter")
public class IntegerPercentageConverter extends IntegerConverter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (context == null || component == null) {
			throw new NullPointerException();
		}

		// If the specified value is null or zero-length, return null
		if (value == null) {
			return (null);
		}
		value = value.trim();
		if (value.length() < 1) {
			return (null);
		}

		try {
			int ret = Integer.valueOf(value);
			if(ret < 0 || ret > 100) {throw new NumberFormatException();}
			return ret;
		}
		catch (NumberFormatException nfe) {
			FacesMessage msg = 	new FacesMessage("Invalid Percentage value.", " Percentage must be an integer betwee 0 and 100.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}
		catch (Exception e) {
			throw new ConverterException(e);
		}
	}

}
