package com.bfds.saec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.configurable.RooConfigurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@Table(name = "user_master")
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @RooConfigurable
@RooJavaBean
@RooToString(excludeFields = {"id", "version"})
public class User implements Serializable, BaseIdentityEntity {
	static final private long serialVersionUID = 1L;

	static final private Logger logger = LoggerFactory.getLogger(User.class);

	private String password;

	private String userName;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<Role>();

	public User() {
	}

	public User(String password, String userName) {
		super();
		this.password = password;
		this.userName = userName;
	}

	
	/**
	 * Returns the granted authorities for this user. You may override this
	 * method to provide your own custom authorities.
	 */
	@Transient
	public List<String> getRoleNames() {
		List<String> roleNames = new ArrayList<String>();

		for (Role role : getRoles()) {
			roleNames.add(role.getRoleName());
		}

		return roleNames;
	}

	/**
	 * Helper method to add the passed role to the roles List.
	 */
	public boolean addRole(Role role) {
		return getRoles().add(role);
	}

	/**
	 * Helper method to remove the passed role from the roles List.
	 */
	public boolean removeRole(Role role) {
		return getRoles().remove(role);
	}

	/**
	 * Helper method to determine if the passed role is present in the roles
	 * List.
	 */
	public boolean containsRole(Role role) {
		return getRoles() != null && getRoles().contains(role);
	}
	
    public static User findUserByName(final String userName) {
        try {
            return entityManager().createQuery("SELECT o FROM User o where o.userName = :userName", User.class).setParameter("userName", userName).getSingleResult();
        }catch(final NoResultException e) {
            //Noop
        }catch(EmptyResultDataAccessException e) {
            //Noop
        }
        return null;
    }	
}