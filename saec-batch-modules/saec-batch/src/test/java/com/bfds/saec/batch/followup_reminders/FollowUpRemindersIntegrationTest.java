package com.bfds.saec.batch.followup_reminders;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.activity.Activity;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.bfds.wss.domain.ClaimantReminder;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,DependencyInjectionTestExecutionListener.class })
public class FollowUpRemindersIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {
	private static final Logger log = LoggerFactory.getLogger(FollowUpRemindersIntegrationTest.class);
	
	@Autowired
	private Job followupremindersJob;
	

	@Override
	protected Job geJOb() {
		return followupremindersJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	};
	
	@Autowired
	private FollowupRemindersEventTestData eventTestData;

	public void afterAllTests() {
		(new TestDataUtil()).deleteRemindersData();
	}

	public void beforeAllTests() {
		eventTestData.create();
	}
	
	
	@Test
	@Transactional
	public void verifyStatusforPendingReminder() {
		ClaimantReminder climantReminder6=ClaimantReminder.findClaimantReminder(6L);
		assertThat(climantReminder6.getReminderStatus()).isEqualTo(ClaimantReminder.ReminderStatus.COMPLETE);
	}


	@Test
	@Transactional
	public void verifyStatusforNoAddressAvailableReminder() {
		ClaimantReminder climantReminder5=ClaimantReminder.findClaimantReminder(5L);
		assertThat(climantReminder5.getReminderStatus()).isEqualTo(ClaimantReminder.ReminderStatus.REJECT);
	}

	@Test
	@Transactional
	public void verifycauseConditionforSubmitClaimFormTypeisResolved() {
		ClaimantReminder climantReminder1=ClaimantReminder.findClaimantReminder(1L);
		assertThat(climantReminder1.getReminderStatus()).isEqualTo(ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
	}


	@Test
	@Transactional
	public void verifycauseConditionforSubmitSavedDataisResolved() {
		ClaimantReminder climantReminder2=ClaimantReminder.findClaimantReminder(2L);
		assertThat(climantReminder2.getReminderStatus()).isEqualTo(ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
	}


	@Test
	@Transactional
	public void verifythataClaiamnthasNoclaim() {
		ClaimantReminder climantReminder6=ClaimantReminder.findClaimantReminder(6L);
		assertThat(climantReminder6.getClaimantClaim()).isNull();
	}

	@Test
	@Transactional
	public void verifycauseConditionforSubmitForNIGOTypeisResolved() {
		ClaimantReminder climantReminder3=ClaimantReminder.findClaimantReminder(3L);
		assertThat(climantReminder3.getReminderStatus()).isEqualTo(ClaimantReminder.ReminderStatus.AUTO_RESOLVED);
	}

	@Test
	@Transactional
	public void verifythataClaimanthasNoEmailAddress() {
		ClaimantReminder climantReminder6=ClaimantReminder.findClaimantReminder(6L);
		List<ClaimantReminder> list = Lists.newArrayList(climantReminder6);
		assertThat(list).onProperty("claimant").onProperty("primaryContact").onProperty("email").isNotNull();
	}


	@Test
	@Transactional
	public void verifytReminderActivity() {
		List<Activity> activities=Activity.findAllActivitys();
		assertThat(activities).onProperty("description").contains("Claimant Reminder sent");
	}

}
