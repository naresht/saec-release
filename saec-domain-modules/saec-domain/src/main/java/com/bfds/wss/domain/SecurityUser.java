package com.bfds.wss.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.bfds.saec.domain.Role;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

/**
 * 
 *
 */
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class SecurityUser implements java.io.Serializable {

    @Column(length=50)
    private String email;

    @Column(length=50)
    private String password;

    @Column(nullable=false)
    private String securityAnswer;

    @Column(nullable = false, length = 50)
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, length = 10)	
    private Date updateDate;

    @Column(nullable = false)	
    private boolean isActive;

    @Column(nullable = false)   
    private int failedLoginCount;

    @Column
    private boolean securityQuestionAnswered;
    
    @Column
    private boolean passwordResetRequired;

    //@OneToMany(fetch = FetchType.EAGER)
    //private ClaimEntry claimEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Security_Question_fk", nullable = false)
    private SecurityQuestion securityQuestion;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<Role>();

    public SecurityUser(String email, String password, String securityAnswer, SecurityQuestion securityQuestion) {
        this.email = email;
        this.password = password;
        this.securityAnswer = securityAnswer;
        this.securityQuestion = securityQuestion;
        isActive = true;
        failedLoginCount = 0;
        securityQuestionAnswered = false;
        passwordResetRequired = false;
        
        updateDate = new Date();
        updatedBy = "system";
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

    /**
     * Search for a SecurityUser whose email address matches the parameter.
     * If the user is not found, method returns null.
     * 
     * @param email Username of user.
     * @return SecurityUser if found, null otherwise
     */
    public static SecurityUser findUserByEmail(final String email) {
        try {
            return entityManager().createQuery("SELECT o FROM SecurityUser o where o.email = :email", SecurityUser.class).setParameter("email", email).getSingleResult();
        }
        catch (EmptyResultDataAccessException erdae) {
            // The username entered does not exist. 
            // So let the processing go back to the 
            // login url.
        }
        return null;
     }   

}
