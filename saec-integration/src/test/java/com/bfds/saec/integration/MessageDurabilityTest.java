package com.bfds.saec.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.bfds.saec.integration.api.UpdateListener;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.jdbc.JdbcMessageStore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test to ensure that we have correct channel and message store configuration to 
 * persist data to a JDBC connection<p>
 * NOTE: We execute code in transactions that are committed, and clear down in 
 * the {@link #clearStore()} method
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/spring/saec-*.xml",
		"classpath:/test-applicationContext.xml"
})
@TransactionConfiguration(defaultRollback=false) // we deliberately commit exiting each @Transactional
public class MessageDurabilityTest {

	@Autowired
	private UpdateListener updateListener;
	
	@Autowired
	private JdbcMessageStore messageStore;
	
	@Autowired
	private PollableChannel updatesFromApp;
		
	
	@After
	public void clearStore() {
		pollMessages();
	}
	
	@Test
	@Ignore("Fix ME ASAP.")
	public void oneMessageShouldBePersisted() {
	
		assertThat(messageStore.getMessageCountForAllMessageGroups(), equalTo(0)); 
		doUpdate();
		assertThat(messageStore.getMessageCountForAllMessageGroups(), equalTo(1)); 
		int numRead = pollMessages();
		assertThat(numRead, equalTo(1));
		assertThat(messageStore.getMessageCountForAllMessageGroups(), equalTo(0)); 
	}

	/**
	 * Read all messages within a transaction
	 */
	@Test
	@Ignore("Fix ME ASAP.")
	public void multipleMessagesShouldBePersisted() {
	
		assertThat(messageStore.getMessageCountForAllMessageGroups(), equalTo(0)); 
		doUpdate();
		doUpdate();
		assertThat(messageStore.getMessageCountForAllMessageGroups(), equalTo(2)); 
		int numRead = pollMessages();
		assertThat(numRead, equalTo(2));
		assertThat(messageStore.getMessageCountForAllMessageGroups(), equalTo(0)); 
	}
	

	@Transactional
	private int pollMessages() {

		@SuppressWarnings("unused")
		Message<?> m = null;

		int i = 0;
		while((m = updatesFromApp.receive(10)) != null){
			i++;
		}
		return i;
	}

	@Transactional
	private void doUpdate(){
		updateListener.notifyUpdate(new TestUpdateActivity());
	}
	
	
}
