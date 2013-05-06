package com.bfds.saec.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.reference.PaymentComponentType;
import com.google.common.base.Preconditions;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"event_fk", "payment_component_type_fk"}),
        @UniqueConstraint(columnNames = {"event_fk", "defaultDescription"}),
        @UniqueConstraint(columnNames = {"event_fk", "activeDescription"})})
public class EventPaymentConfig implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "event_fk", nullable = false)
    private Event event;
	
	@ManyToOne
    @JoinColumn(name = "payment_component_type_fk", nullable = false)
	private PaymentComponentTypeLov paymentComponentType;
	
	private Boolean active = true ;
	
	@Column(nullable = false)
	private String defaultDescription ;

    @Column(nullable = false)
	private String activeDescription ;
	
	private char objectCode ;

	public String getDescription() {
		return active && StringUtils.hasText(activeDescription) ? activeDescription : defaultDescription;  
	}
	
    @PrePersist
    @PreUpdate
    public void updateActiveDescription() {
       if(!StringUtils.hasText(activeDescription)) {
           activeDescription = defaultDescription;
       }
    }
    
    /**
     * Find an EventPaymentConfig that has one of it's descriptions equal to description.
     * @param description
     * @return
     */
    public static EventPaymentConfig findByDescription(String description) {
    	Preconditions.checkNotNull(description, "description is null");
    	try {
    	return entityManager().createQuery("SELECT o FROM EventPaymentConfig o where o.defaultDescription = :defaultDescription or o.activeDescription = :activeDescription", EventPaymentConfig.class)
    			.setParameter("activeDescription", description)
    			.setParameter("defaultDescription", description)
    			.getSingleResult();
    	}catch(EmptyResultDataAccessException e) {
    		return null;
    	}
    }
    
    /**
     * Find an EventPaymentConfig for the given PaymentComponentType. Only one will exist per event. 
     * @param PaymentComponentType
     * @return
     */
    public static EventPaymentConfig findByPaymentComponentType(PaymentComponentType paymentComponentType) {
    	Preconditions.checkNotNull(paymentComponentType, "PaymentComponentType is null");
    	try {
    	return entityManager().createQuery("SELECT o FROM EventPaymentConfig o where o.paymentComponentType.code = :paymentComponentType", EventPaymentConfig.class)
    			.setParameter("paymentComponentType", paymentComponentType.name())
    			.getSingleResult();
    	}catch(EmptyResultDataAccessException e) {
    		return null;
    	}
    }

}
