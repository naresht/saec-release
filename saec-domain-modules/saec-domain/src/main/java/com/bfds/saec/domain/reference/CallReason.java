package com.bfds.saec.domain.reference;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString (excludeFields = { "id", "version"})
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 public class CallReason implements java.io.Serializable{


	private static final long serialVersionUID = 1L;

	@NotNull
    @Size(max = 3)
    private String reasonCode;

    @NotNull
    @Size(max = 1024)
    private String description;

    private Integer priority;
}
