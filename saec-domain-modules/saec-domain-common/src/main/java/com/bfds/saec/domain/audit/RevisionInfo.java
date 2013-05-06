package com.bfds.saec.domain.audit;

import javax.persistence.Entity;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@RevisionEntity(RevisionListener.class)
public class RevisionInfo extends DefaultRevisionEntity  implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
