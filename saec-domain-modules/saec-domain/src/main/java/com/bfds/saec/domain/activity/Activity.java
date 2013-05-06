package com.bfds.saec.domain.activity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.bfds.saec.domain.reference.CallType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.util.AccountContext;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", inheritanceType = "JOINED")
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_SHORT_DESCRIPTION_LENGTH = 75;

	public static final String DEFAULT_ACTIVITY_TYPE_DESCRIPTION = "Generic Activity";

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	protected Date activityDate;

	@Column(length=1024)
	protected String description;

	protected String userId;

	protected String activityTypeDescription;

	protected ActivityCode activityCode;

    @ManyToOne
    @JoinColumn(name = "claimant_fk")
    private Claimant claimant;

	public String getShortDescription() {
		String desc = this.getDescription();
		if (desc != null && desc.length() > getShortDescriptionLength()) {
			desc = desc.substring(0, getShortDescriptionLength());
		}
		return desc;
	}

	public int getShortDescriptionLength() {
		return DEFAULT_SHORT_DESCRIPTION_LENGTH;
	}

	public String getActivityTypeDescription() {
		return StringUtils.hasText(this.activityTypeDescription) ? this.activityTypeDescription
				: DEFAULT_ACTIVITY_TYPE_DESCRIPTION;
	}

	public void setActivityTypeDescription(String activityTypeDescription) {
		this.activityTypeDescription = activityTypeDescription;
	}

	public static Activity setActivityDefaults(final Activity activity) {
		activity.setActivityDate(new Date());
		activity.setUserId(AccountContext.getCurrentUsername());
		return activity;
	}
    
    public void setActivityCodeStr(String activityCode) {
        if (StringUtils.hasLength(activityCode)) {
            setActivityCode(ActivityCode.valueOf(activityCode));
        }
    }   
}
