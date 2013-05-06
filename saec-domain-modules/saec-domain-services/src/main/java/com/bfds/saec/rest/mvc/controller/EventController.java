package com.bfds.saec.rest.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bfds.saec.domain.Event;
import com.bfds.saec.external.service.dto.EventDto;

@Controller
@RequestMapping("/event")
public class EventController {
	private static final Logger log = LoggerFactory.getLogger(EventController.class);
	
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public EventDto getEvent() {
		if(log.isInfoEnabled()) {
			log.info("Fetching Event");
		}		
		Event event = Event.getCurrentEvent();
		return EventDto.newEventDto(event);		
	}	
}
