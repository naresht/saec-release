package com.bfds.saec.web.ui.components;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Request parameters have the unwanted behaviour that they are still been sent even if they does
 * not contain any value. In the Java side they will be translated as an empty String. Besides, JSF
 * has the unwanted behaviour that it sets a non-required String property with the empty String
 * value anyway instead of setting it with null.
 * <p>
 * This converter will convert any empty String value to null so that the String property will be
 * set to null in case of an empty String. Define the converter in faces-config.xml as follows:
 * <pre>
 * &lt;converter&gt;
 *     &lt;converter-for-class&gt;java.lang.String&lt;/converter-for-class&gt;
 *     &lt;converter-class&gt;mymodel.EmptyToNullConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 * NOTE: this converter doesn't work in JSF 1.1, but in JSF 1.2 or newer only, because JSF 1.1
 * by design ignores any converter which is associated with java.lang.String.
 *
 * @author BalusC
 *
 */
public class EmptyToNullConverter implements Converter {

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
        return value;
    }

    /**
     * Does nothing special.
     * @see javax.faces.convert.Converter#getAsString(
     *      javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     */
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        return value == null ? null : value.toString();
    }

}