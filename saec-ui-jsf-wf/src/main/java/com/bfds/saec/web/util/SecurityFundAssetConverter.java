package com.bfds.saec.web.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.util.StringUtils;

import com.bfds.wss.domain.reference.SecurityFund;

@FacesConverter(forClass=SecurityFund.class)
public class SecurityFundAssetConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		SecurityFund ret = null;
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		if (!StringUtils.hasText(submittedValue)) {
			ret = null;
		} else {
			try {
				
				ret = SecurityFund.findSecurityFund(Long.valueOf(submittedValue));
			} catch (Exception exception) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid Asset"));
			}		
		} 
		
		return ret;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null ) {
			return ((SecurityFund) value).getId().toString();
		}
		return null;
	}

}
