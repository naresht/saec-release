package com.bfds.saec.web.ui.components.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.primefaces.component.calendar.Calendar;

/**
 * A renderer that mergers the time with the submitted string date. The String date is assumed to be in MM/DD/YYYY format. 
 * 
 * Example: - 
 * 
 * A backing bean with a date vale 10/10/2011 2:30 is rendered on screen as 10/10/2011. The time is not displayed on screen. 
 * When the value on screen is submitted back the backing bean's date becomes 10/10/2011 00:00. To fix this, the following happens
 * 
 * a. Retrieve the time from the backing bean.
 * b. Merge the time with the submitted value. Here the submitted value becomes 10/10/2011 2:30 from just 10/10/2011. 
 * 
 * However when the date on the screen is changed from 10/10/2011 to 15/10/2011 do we still need the time ? Its does not make much sense to retain the time in this case. 
 * So the time is not merged and the submitted value remains 15/10/2011 and does not become 15/10/2011 2:30.
 */
public class CalendarRenderer extends
		org.primefaces.component.calendar.CalendarRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		Calendar calendar = (Calendar) component;

		if (calendar.isDisabled() || calendar.isReadonly()) {
			return;
		}

		decodeBehaviors(context, calendar);

		String param = calendar.getClientId(context) + "_input";
		String submittedValue = context.getExternalContext()
				.getRequestParameterMap().get(param);

		if (submittedValue != null) {
			calendar.setSubmittedValue(getSubmittedValue(calendar, submittedValue));
		}
	}

	private Object getSubmittedValue(Calendar calendar, Object submittedValue) {
		if (isNotEmpty(submittedValue)) {
			Object value = calendar.getValue();
			if (isDate(value)) {
				submittedValue = mergeTime(calendar, (CharSequence) submittedValue,
						(Date) value);
			}
		}
		return submittedValue;
	}

	@Override
	public Object getConvertedValue(FacesContext context,
			UIComponent component, Object value) throws ConverterException {
		Calendar calendar = (Calendar) component;
		String submittedValue = (String) value;
		Converter converter = calendar.getConverter();

		if (isValueBlank(submittedValue)) {
			return null;
		}

		// Delegate to user supplied converter if defined
		if (converter != null) {
			return converter.getAsObject(context, calendar, submittedValue);
		}

		// Use built-in converter
		try {
			Date convertedValue;
			Locale locale = calendar.calculateLocale(context);
			SimpleDateFormat format = null;
			if (submittedValue.contains(":")) {
				format = new SimpleDateFormat("MM/dd/yyyy HH:mm", locale);
			}
			else {
				format = new SimpleDateFormat(calendar.getPattern(), locale);
			}
			format.setTimeZone(calendar.calculateTimeZone());
			convertedValue = format.parse(submittedValue);

			return convertedValue;

		}
		catch (ParseException e) {
			throw new ConverterException(e);
		}
	}

	private CharSequence mergeTime(Calendar calendar, CharSequence submittedValue, Date date) {
		if (!sameDate(calendar, submittedValue, date)) {
			return submittedValue;
		}
		final int hour = date.getHours();
		final int min = date.getMinutes();
		if (hour > 0 || min > 0) {
			submittedValue = submittedValue + " " + hour + ":" + min;
		}
		return submittedValue;
	}

	private boolean sameDate(Calendar calendar, CharSequence submittedValue, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(calendar.getPattern());//,
				calendar.calculateLocale(FacesContext.getCurrentInstance());
		dateFormat.setTimeZone(calendar.calculateTimeZone());
		String dateAsString = dateFormat.format(date);
		return submittedValue.equals(dateAsString);
	}

	private boolean isDate(Object value) {
		return value != null && value instanceof Date;
	}

	private boolean isNotEmpty(Object submittedValue) {
		return submittedValue != null
				&& submittedValue instanceof CharSequence
				&& ((CharSequence) submittedValue).toString().trim().length() > 0;
	}

}
