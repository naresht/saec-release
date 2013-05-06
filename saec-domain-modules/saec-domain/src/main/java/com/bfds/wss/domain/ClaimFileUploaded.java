package com.bfds.wss.domain;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Audited
public class ClaimFileUploaded implements java.io.Serializable {
	
	public static final String CLAIM_FORM_NAME = "claim-form.pdf";
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Claimant_Claim_fk", nullable = true)
    private ClaimantClaim claimantClaim;

    /**
     * The name of the file when uploaded by the user. This is the name of the file on the user's filesystem.
     */
    @NotNull
    @Column(nullable = false)
	private String uploadedFileName;

    /**
     * This is the absolute name of the file once it is stored on the server.
     */
    @NotNull
    @Column(nullable = false)
	private String absoluteFileName;
    
    @NotNull
    @Column(nullable = false)
    private Date dateUploaded ; 
    
    @Null
    @Column(nullable = true)
    private Date dateRipped;

    /**
     * Name of the RIP file.
     */
    @NotNull
    @Column(nullable = true)
    private String ripFileName;

    /**
     * This is the absolute name of the file sent on the RIP. This may or may not be the same as {@link #absoluteFileName}.
     * This will not be the same as {@link #absoluteFileName} if the contents of {@link #fileData} is saved to a new file and sent
     * on the RIP.
     *
     * This most likely will be the same as {@link #absoluteFileName} if {@link #absoluteFileName} is sent on the RIP.
     */
    @NotNull
    @Column(nullable = true)
    private String absoluteFileNameInRip;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "claim_file_lob", nullable = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ClaimFileLob fileData;
	
}
