package com.bfds.saec.domain.activity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.RegistrationLines;
import com.bfds.saec.domain.reference.ChangeSource;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders = {"findNameChange"})
public class NameChange extends Activity  implements Serializable  {

    private static final long serialVersionUID = 1L;

    private static final String ACTIVITY_TYPE_DESCRIPTION = "Name Change";

	@Embedded
	private RegistrationLines from;
	
	@Embedded
    @AttributeOverrides( {
        @AttributeOverride(name="registration1", column = @Column(name="to_registration1") ),
        @AttributeOverride(name="registration2", column = @Column(name="to_registration2") ),
        @AttributeOverride(name="registration3", column = @Column(name="to_registration3") ),
        @AttributeOverride(name="registration4", column = @Column(name="to_registration4") ),
        @AttributeOverride(name="registration5", column = @Column(name="to_registration5") ),
        @AttributeOverride(name="registration6", column = @Column(name="to_registration6") )
    } )	
	private RegistrationLines to;
    
    @Enumerated(EnumType.STRING)
    private ChangeSource source;
    
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		if(from != null) {
			sb.append(" From : ");
			sb.append(from.getRegistrationLinesAsString());
		}
		if(to != null) {
			sb.append(" To : ");
			sb.append(to.getRegistrationLinesAsString());
		}		
		return sb.toString();
    }
	
	public String getActivityTypeDescription() {
		return ACTIVITY_TYPE_DESCRIPTION;
	}	
}
