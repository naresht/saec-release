package com.bfds.saec.web.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.util.StringUtils;

import com.bfds.wss.domain.reference.TransactionType;

@FacesConverter(forClass=TransactionType.class)
public class TransactionTypeConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		TransactionType ret = null;
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		if (!StringUtils.hasText(submittedValue)) {
			ret = null;
		} else {
			try {
				
				ret = TransactionType.findTransactionType(Long.valueOf(submittedValue));
			} catch (Exception exception) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid Transaction Type"));
			}		
		} 
		
		return ret;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null ) {
			return ((TransactionType) value).getId().toString();
		}
		return null;
	}

}
