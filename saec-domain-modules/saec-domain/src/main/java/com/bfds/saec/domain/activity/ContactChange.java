package com.bfds.saec.domain.activity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

@Configurable
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @RooJavaBean
public class ContactChange extends Activity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public static final String ACTIVITY_TYPE_DESCRIPTION = "Contact Info Change";
	public static final String ACTIVITY_TYPE_DESCRIPTION_PRIMARY_CONTACT = "Primary Contact Info Change";
	public static final String ACTIVITY_TYPE_DESCRIPTION_CONTACT_ADD = "Additional Contact Created";
	

	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name.firstName", column = @Column(name = "from_firstName")),
        @AttributeOverride(name = "name.middleName", column = @Column(name = "from_middleName")),
        @AttributeOverride(name = "name.lastName", column = @Column(name = "from_lastName")),
        @AttributeOverride(name = "phoneNo", column = @Column(name = "from_phoneNo")),
        @AttributeOverride(name = "workPhoneNo", column = @Column(name = "from_workPhoneNo")),
        @AttributeOverride(name = "cellPhoneNo", column = @Column(name = "from_cellPhoneNo")),
        @AttributeOverride(name = "email", column = @Column(name = "from_email")),
        @AttributeOverride(name = "comments", column = @Column(name = "from_comments"))})
	private Contact from;

	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name.firstName", column = @Column(name = "to_firstName")),
        @AttributeOverride(name = "name.middleName", column = @Column(name = "to_middleName")),
        @AttributeOverride(name = "name.lastName", column = @Column(name = "to_lastName")),
        @AttributeOverride(name = "phoneNo", column = @Column(name = "to_phoneNo")),
        @AttributeOverride(name = "workPhoneNo", column = @Column(name = "to_workPhoneNo")),
        @AttributeOverride(name = "cellPhoneNo", column = @Column(name = "to_cellPhoneNo")),
        @AttributeOverride(name = "email", column = @Column(name = "to_email")),
        @AttributeOverride(name = "comments", column = @Column(name = "to_comments"))})	
	private Contact to;
	
	private Boolean isPrimaryContact;
	
	public void setFrom(com.bfds.saec.domain.Contact from) {
		this.from = new Contact(from);
	}	

	public void setTo(com.bfds.saec.domain.Contact to) {
		this.to = new Contact(to);
	}	
	
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
			return contactChangeDescription(sb);
		}	

	private String contactChangeDescription(StringBuilder sb) {
		if (from != null) {
            sb.append(from.getAsString());
            sb.append(" To ");
        }
        if (to != null) {            
            sb.append(to.getAsString());
        }
		return sb.toString();
	}

	public String getActivityTypeDescription() {
		if(from == null && ! Boolean.TRUE.equals(this.isPrimaryContact)) {
			return ACTIVITY_TYPE_DESCRIPTION_CONTACT_ADD;
		}
		return Boolean.TRUE.equals(this.isPrimaryContact) ? ACTIVITY_TYPE_DESCRIPTION_PRIMARY_CONTACT : ACTIVITY_TYPE_DESCRIPTION;
	}	
}
