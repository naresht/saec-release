package com.bfds.saec.external.service.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.Event;

@RooJavaBean
@RooToString
public class EventDto implements java.io.Serializable {
	
	private String code;
	private Date targetEndDate;
	private Boolean closed;
	

	public static EventDto newEventDto(Event event) {
		EventDto ret = new EventDto();
		ret.setCode(event.getCode());
		ret.setTargetEndDate(event.getTargetEndDate());
		ret.setClosed(event.getClosedEvent());
		return ret;
	}
	
}
