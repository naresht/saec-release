package com.bfds.saec.domain.activity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.reference.CallCode;
import com.bfds.saec.domain.reference.CallType;
import com.bfds.saec.util.SaecStringUtils;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @NamedQueries({
	@NamedQuery(name="findAllCallsofUser", query="from PhoneCall o where o.userId = :userId")
} )
public class PhoneCall extends Activity  implements Serializable  {
	private static final long serialVersionUID = 1L;
	private static final String ACTIVITY_TYPE_DESCRIPTION_IN_CALL = "Incoming Call";
	private static final String ACTIVITY_TYPE_DESCRIPTION_OUT_CALL = "Outgoing Call";
	private static final String ACTIVITY_TYPE_DESCRIPTION_CALL = "Call";
	private static final String DEFAULT_LINE_SEPERATOR = "<br/>";
	
	@PersistenceContext(unitName="entityManagerFactory")
    transient EntityManager entityManager;
	
	@NotNull
    @ManyToMany(cascade = CascadeType.DETACH, fetch=FetchType.EAGER)
    private Set<CallCode> callCode = new HashSet<CallCode>();

    @Size(max = 1024)
    private String comments;

    @Enumerated(EnumType.STRING)
    private CallType callType;
    
    private boolean shapshot;
    
    private String phoneExtension;
    
    @ManyToOne
    @JoinColumn(name="claimant_fk")    
    private Claimant claimant;
	
	public String getDescription() {
		String[] lines = new String[callCode.size()];
		int index = 0;
		for(CallCode code : callCode) {
			lines[index ++ ] = code.getDescription();
		}
    	return SaecStringUtils.getArrayString(lines, DEFAULT_LINE_SEPERATOR);
	}

	public String getActivityTypeDescription() {
		if(CallType.INBOUND == callType) {
			return ACTIVITY_TYPE_DESCRIPTION_IN_CALL;
		} else if(CallType.OUTBOUND == callType) {
			return ACTIVITY_TYPE_DESCRIPTION_OUT_CALL;
		}
		return ACTIVITY_TYPE_DESCRIPTION_CALL;
	}

    public static List<PhoneCall> findAllCallsofUser(final String userId) {
        return entityManager().createNamedQuery("findAllCallsofUser", PhoneCall.class)
        .setParameter("userId", userId)
        .getResultList();
    }
    
    public static PhoneCall startCallLog(final String userName) {
        PhoneCall call = new PhoneCall();
        call.setShapshot(true);
        call.setUserId(userName);
        call.setActivityDate(new Date());
        return call;
    }
    
    public void setCallTypeStr(String callType) {
        if (StringUtils.hasLength(callType)) {
            setCallType(CallType.valueOf(callType));
        }
    }         
}
