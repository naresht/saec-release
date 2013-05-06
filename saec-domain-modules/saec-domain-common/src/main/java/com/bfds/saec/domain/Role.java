package com.bfds.saec.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import java.io.Serializable;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findRolesByRoleName"})
@RooConfigurable
@RooJavaBean
@Table(name = "role_master")
public class Role implements Serializable, BaseIdentityEntity {
	static final private long serialVersionUID = 1L;

	static final private Logger logger = LoggerFactory.getLogger(Role.class);

	private String roleName;

	public Role() {
	}

	public Role(String roleName) {
		super();
		this.roleName = roleName;
	}

	@Length(max = 255)
	@Column(name = "role_name")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getRoleName()).append(",");
        return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId())) {
			return false;
		} else if (getId().longValue() != (other.getId().longValue()))
			return false;		
		System.err.println("FOUND SAME ROLE: " + obj.toString()) ;
		return true;
	}

}