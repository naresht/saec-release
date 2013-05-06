package com.bfds.saec.domain.activity;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.bfds.saec.domain.util.AccountContext;

public class ActivityTest {

	
	@Test
	public void verifyActivityDescription() {
		Activity activity = new Activity();
		Activity.setActivityDefaults(activity);
		activity.setDescription("1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890");
		
		assertThat(activity.getActivityDate()).isNotNull();
		assertThat(activity.getUserId()).isEqualTo("system!");
		assertThat(activity.getShortDescription()).hasSize(Activity.DEFAULT_SHORT_DESCRIPTION_LENGTH);	
		
		AccountContext.setAccountContext(new AccountContext());
		AccountContext.getAccountContext().setUsername("csr");		
		Activity.setActivityDefaults(activity);
		assertThat(activity.getUserId()).isEqualTo("csr");	
		AccountContext.getAccountContext().setUsername(null);

	}
	
	@Test
	public void verifyActivityTypeDescription() { 
		Activity activity = new Activity();
		assertThat(activity.getActivityTypeDescription()).isEqualTo(Activity.DEFAULT_ACTIVITY_TYPE_DESCRIPTION);
		activity.setActivityTypeDescription("asdf");
		assertThat(activity.getActivityTypeDescription()).isEqualTo("asdf");
	}
}
