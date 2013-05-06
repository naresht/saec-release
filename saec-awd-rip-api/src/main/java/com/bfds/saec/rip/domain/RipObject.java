package com.bfds.saec.rip.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", inheritanceType="TABLE_PER_CLASS", 
finders = { "findRipObjectsByCorrelationId"}) 
@RooJavaBean
@RooConfigurable
@RooToString
public abstract class RipObject implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	//@Column(nullable=false)
	private String workType;
	
	/**
	 * Is this {@link RipObject} sent to AWD ?
	 */
	@Column(nullable=false)
	private RipObjectStatus status;
	
	/**
	 * An id by which this object will be referenced in a later event.  
	 */
	private Long  correlationId;
	
	/**
	 * The user that created this Object. 
	 */
	@Column(nullable=false)
	private String createdByUser;	
	
	@Value("${awdRipBusinessUnit}")
	private String businessUnit;
	
	@Value("${awdRipHostName}")
	private String hostName;
	
	/**
	 * The role of the user at the time of creating the object. 
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	private List<String> createdByRoles;		
	
//	public void setCreatedByRoles(List<String> roles) {
//		this.setCreatedByRoles("");
//		SaecStringUtils.getAsString(roles, ",");
//	}
	
	 public static List<RipObject> findByCorrelationId(Long correlationId) {
		 List<RipObject> list = findRipObjectsByCorrelationId(correlationId)
					.getResultList();
		 return list;
		 
	  }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((correlationId == null) ? 0 : correlationId.hashCode());
		result = prime * result
				+ ((createdByUser == null) ? 0 : createdByUser.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((workType == null) ? 0 : workType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RipObject other = (RipObject) obj;
		if (correlationId == null) {
			if (other.correlationId != null) {
				return false;
			}
		} else if (!correlationId.equals(other.correlationId)) {
			return false;
		}
		if (createdByUser == null) {
			if (other.createdByUser != null) {
				return false;
			}
		} else if (!createdByUser.equals(other.createdByUser)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (workType == null) {
			if (other.workType != null) {
				return false;
			}
		} else if (!workType.equals(other.workType)) {
			return false;
		}
		return true;
	}
	
}
