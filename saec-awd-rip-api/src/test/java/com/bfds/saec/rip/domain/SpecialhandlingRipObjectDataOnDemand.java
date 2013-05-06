package com.bfds.saec.rip.domain;

import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity=SpecialhandlingRipObject.class)
public class SpecialhandlingRipObjectDataOnDemand {
	
    public void setStatus(SpecialhandlingRipObject obj, int index) {
  	  
    	obj.setStatus(RipObjectStatus.NOT_SENT);
    }

}
