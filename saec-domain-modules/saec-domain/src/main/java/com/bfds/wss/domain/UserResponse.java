package com.bfds.wss.domain;


import com.bfds.wss.domain.reference.AdditionalQuestions;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", inheritanceType="TABLE_PER_CLASS")
@RooJavaBean
@Audited
public abstract class UserResponse implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Additional_Questions_fk", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private AdditionalQuestions additionalQuestions;

    /**
     * Text field if the question requires a free text response.
     */
    @Column
    private String responseText;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(length = 10)
    @DateTimeFormat(style = "S-")
    private Date responseDate;

    /**
     * If a multi-occuring question, we include a row id
     */
    @Column
    private short rowId;


    public abstract UserResponseGroup getUserResponseGroup();

    public void convertDateToString() {
        if (this.responseDate != null) {
            SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("MM/dd/yyyy");
            StringBuilder nowYYYYMMDD = new StringBuilder( dateformatYYYYMMDD.format( responseDate ) );
            responseText = nowYYYYMMDD.toString() ;
        }
    }

    public void responseDateValueChange(ValueChangeEvent vce) {
        if (responseDate != null) {
            this.responseText = responseDate.toString() ;
        }
    }


}
