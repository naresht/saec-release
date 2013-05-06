package com.bfds.saec.web.util;
import java.util.TimeZone;
import javax.faces.convert.DateTimeConverter;
public class CustomDateConverter extends DateTimeConverter {
	public CustomDateConverter() {
	super();
	setTimeZone(TimeZone.getDefault());
	// here you can set your custom date pattern for your project
	setPattern("MM/dd/yyyy");
	}
}

