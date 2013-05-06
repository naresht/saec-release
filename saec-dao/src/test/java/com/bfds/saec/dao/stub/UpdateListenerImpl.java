package com.bfds.saec.dao.stub;

import com.bfds.saec.integration.api.UpdateEvent;
import com.bfds.saec.integration.api.UpdateListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateListenerImpl implements UpdateListener {

	@Override
	public void notifyUpdate(UpdateEvent event) {
		System.out.println(event);		
	}

}
