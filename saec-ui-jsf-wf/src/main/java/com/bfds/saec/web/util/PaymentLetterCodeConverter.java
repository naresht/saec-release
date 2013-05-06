package com.bfds.saec.web.util;

import com.bfds.saec.domain.PaymentLetterCode;
import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass=PaymentLetterCode.class)
public class PaymentLetterCodeConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        PaymentLetterCode ret = null;
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		if (!StringUtils.hasText(submittedValue)) {
			ret = null;
		} else {
			try {
				
				ret = PaymentLetterCode.findByCode(submittedValue);
			} catch (Exception exception) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid PaymentLetterCode"));
			}		
		} 
		
		return ret;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null ) {
			return ((PaymentLetterCode) value).getCode();
		}
		return null;
	}

}
