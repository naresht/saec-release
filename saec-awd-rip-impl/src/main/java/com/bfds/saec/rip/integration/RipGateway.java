package com.bfds.saec.rip.integration;

import org.springframework.integration.annotation.Gateway;

import com.bfds.saec.rip.domain.RipObject;

public interface RipGateway {
	
	@Gateway
	public void sendRipObject(RipObject ripObject);

}
