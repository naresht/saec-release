package com.bfds.saec.domain;

import com.bfds.saec.domain.reference.LetterType;
import com.google.common.base.Preconditions;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A Payment object(typically Checks) will be mailed along with a letter attached to it.
 * A PaymentLetter code identifies the type of letter that will be attached.
 * This is not in any way related to {@link LetterCode}
 */
@RooJavaBean
@RooToString (excludeFields = { "id", "version"})
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findPaymentLetterCodesByCode"})
public class PaymentLetterCode implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(nullable=false, unique=true)
    private String code;

    @NotNull
    @Column(nullable=false, unique=true)
    private String description;

    /**
     * @deprecated - Required by JPA. Use {@link #PaymentLetterCode(String, String)} always.
     */
    @Deprecated
    public PaymentLetterCode() {}


    public PaymentLetterCode(final String code, final String description) {
    	this.code = code;
    	this.description = description;
    }

    public static PaymentLetterCode findByCode(String code) {
    	Preconditions.checkNotNull(code, "code cannot be null");
    	try {
        return findPaymentLetterCodesByCode(code).getSingleResult();
    	}catch(EmptyResultDataAccessException e) {
    		return null;
    	}
    }
}
