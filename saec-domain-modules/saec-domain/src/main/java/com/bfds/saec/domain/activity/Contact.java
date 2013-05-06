package com.bfds.saec.domain.activity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.Name;
import com.bfds.saec.util.SaecStringUtils;

@Embeddable
@RooToString
@RooJavaBean
public class Contact  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Embedded
	private Name name = new Name();

	private String phoneNo;

	private String workPhoneNo;

	private String cellPhoneNo;

	private String email;

	private String comments;
	
	public Contact() {}
	
	public Contact(com.bfds.saec.domain.Contact contact) {
		this.setCellPhoneNo(contact.getCellPhoneNo());
		this.setComments(contact.getComments());
		this.setEmail(contact.getEmail());
		this.setName(contact.getName());
		this.setPhoneNo(contact.getPhoneNo());
		this.setWorkPhoneNo(contact.getWorkPhoneNo());
	}
	
	public Name getName() {
		if (name == null) {
			name = new Name();
		}
		return this.name;
	}
	
	public String getAsString() {
		String[] values = new String[6];
		values[0] = name == null ? null : name.getAsString();
		values[1] = phoneNo;
		values[2] = cellPhoneNo;
		values[3] = workPhoneNo;
		values[4] = comments;
		values[5]=email;
		return SaecStringUtils.getArrayString(values, ", ");
	}
	
}
