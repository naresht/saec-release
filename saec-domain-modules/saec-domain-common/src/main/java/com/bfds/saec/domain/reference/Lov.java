package com.bfds.saec.domain.reference;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.TaxTypeLov;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.io.Serializable;
import java.util.List;


/**
 * List of Values. Holds Static data for several "Types" in SAEC.
 */
@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", inheritanceType = "SINGLE_TABLE")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@RooToString (excludeFields = {"id", "version"})
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public abstract class Lov implements Serializable {

    private static final long serialVersionUID = 8219090088538612688L;

    // Indicates if this code is active
    @NotNull
    @Column(nullable = false)
    private Boolean active = true;

    @NotNull
    @Column(nullable = false, length = 100, unique = true)
    protected String code;

    @NotNull
    @Column(nullable = false, length = 255)
    protected String description;
    
    @Null 
    @Column(nullable = true, length = 255)
    protected String category;
    

    public Lov() {
        super();
    }

    public Lov(String code, String description) {
        super();
        this.active = true;
        this.code = code;
        this.description = description;
    }

}
