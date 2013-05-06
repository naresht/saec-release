package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.MailType;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-test.xml" })
@Transactional
public class RequestTypeTest {

	@Test
	public void findCorrespondenceRequestType() {
		RequestType r1 = new RequestType();
		r1.setName("r1");
		r1.setMailType(MailType.INCOMING);
		r1.persist();
		
		r1 = new RequestType("r2");
		r1.setMailType(MailType.OUTGOING);
		r1.persist();
		r1.flush();
		
		assertThat(RequestType.findCorrespondenceRequestType(MailType.INCOMING)).onProperty("mailType").containsOnly(MailType.INCOMING);
		assertThat(RequestType.findCorrespondenceRequestType(MailType.OUTGOING)).onProperty("mailType").containsOnly(MailType.OUTGOING);
	}
	
	@Test
	public void testEquals() {
		RequestType r1 = new RequestType();
		r1.setName("r1");
		r1.setMailType(MailType.INCOMING);
		
		RequestType r2 = new RequestType();
		r2.setName("r2");
		r2.setMailType(MailType.OUTGOING);
				
		assertThat(r1).isEqualTo(r1);
		assertThat(r1).isNotEqualTo(r2);
		
		r2.setName("r1");
		
		assertThat(r1).isEqualTo(r2);
		assertThat(r1).isNotEqualTo(null);
		assertThat(r1).isNotEqualTo("");
		r1.setName(null);
		assertThat(r1).isNotEqualTo(r2);
	}	
	
	@Test
	public void verifyHashcode() {
		RequestType r1 = new RequestType();
		r1.setName("r1");
		r1.setMailType(MailType.INCOMING);
	
		RequestType r2 = new RequestType();
		r2.setName("r1");
		r2.setMailType(MailType.INCOMING);			
		
		assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
		assertThat(r1.hashCode()).isNotEqualTo(new RequestType().hashCode());
		assertThat((new RequestType()).hashCode()).isEqualTo((new RequestType()).hashCode());		
	}
}
