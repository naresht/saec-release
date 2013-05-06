package com.bfds.saec.web.util;

import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsfUtils {
	
	public static UIViewRoot getUIViewRoot() {
		return FacesContext.getCurrentInstance().getViewRoot();
	}
	
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	public static String getClientId(UIComponent component) {
		return component.getClientId(JsfUtils.getFacesContext());
	}
	
	public static String getClientId(final String componentId) {
		return JsfUtils.findComponent(JsfUtils.getUIViewRoot(), componentId).getClientId(JsfUtils.getFacesContext());
	}
	
	public static UIComponent findComponent(UIComponent root, String id) {
	    if (id.equals(root.getId())) {
	      return root;
	    }
	    Iterator<UIComponent> kids = root.getFacetsAndChildren();
	    while (kids.hasNext()) {
	      UIComponent found = findComponent(kids.next(), id);
	      if (found != null) {
	        return found;
	      }
	    }
	    return null;
	  }
	
	/**
	 * See http://ishabalov.blogspot.com/2007/08/sad-story-about-uiinput.html
	 * @param root
	 * @param childId
	 */
	public static void clearValues(UIComponent root, String... childId) {
		for(String id : childId) {
			UIInput input =  (UIInput)JsfUtils.findComponent(JsfUtils.getUIViewRoot(), id);			
			input.resetValue();
		}
	}
	
    public static UIComponent findComponent(UIComponent root, Class clazz) {
        if (clazz.isAssignableFrom(root.getClass())) {
            return root;
        }
        Iterator<UIComponent> kids = root.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), clazz);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
	
	public static Object getRequestAttribute(String name) {
		return getHttpServletRequest().getAttribute(name);
	}
	
	public static void setRequestAttribute(String name, Object value) {
		getHttpServletRequest().setAttribute(name, value);
	}
	public static String getRequestParameter(final String paramaterName) {		
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(paramaterName);
	}	
	
	public static Map<String, String> getRequestParameterMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}	
	
	public static HttpServletRequest getHttpServletRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
	}
	
	public static HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}	
}
