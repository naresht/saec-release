package com.bfds.wss.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
public class ClaimFileLob implements java.io.Serializable {

    @Lob
    private byte[] data;
	
}
