package com.bfds.saec.web.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.dialog.Dialog;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.GroupMailCode;

@FacesConverter(forClass=GroupMailCode.class)
public class GroupMailCodeConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		GroupMailCode ret = null;
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		if (!StringUtils.hasText(submittedValue)) {
			ret = null;
		} else {
			try {
				ret = GroupMailCode.findGroupMailCodesByCode(submittedValue).getSingleResult();
			} catch (Exception exception) {
				Dialog cureLetterDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "cureLetterDialog");
				if(cureLetterDialog != null){
					cureLetterDialog.setVisible(false);
				}
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Not a valid GroupMailCode",
						"Not a valid GroupMailCode"));
			}		
		} 
		
		return ret;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null ) {
			return ((GroupMailCode) value).getCode();
		}
		return null;
	}

}
