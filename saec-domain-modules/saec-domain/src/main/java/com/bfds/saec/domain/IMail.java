package com.bfds.saec.domain;

import java.util.Date;

import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.reference.RPOType;

public interface IMail extends java.io.Serializable {
	Date getMailDate();
	void setMailDate(Date date);
	MailType getMailType();
	void setMailType(MailType mailType);
	String getMailingControlNo();
	void setMailingControlNo(String controlNo);	
	GroupMailCode getGroupMailCode();
	void setGroupMailCode(GroupMailCode code);	
	boolean isSpecialPull();
	void setSpecialPull(boolean val);
	boolean isAuditable();
	void setAuditable(boolean auditable);	
	RPOType getRpoType();
	void setRpoType(RPOType type);
	Date getRpoDate();
	void setRpoDate(Date date);
}
