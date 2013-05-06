package com.bfds.saec.rip.integration;

import org.springframework.integration.Message;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.stereotype.Component;

@Component("awdFileNameGenerator")
public class AwdRipFileNameGenerator extends DefaultFileNameGenerator {
	
	public String generateFileName(Message<?> message) {
		return message.getHeaders().getId() + ".dat";
	}

}
