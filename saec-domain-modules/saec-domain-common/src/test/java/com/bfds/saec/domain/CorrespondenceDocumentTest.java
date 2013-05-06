package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-test.xml" })
@Transactional
public class CorrespondenceDocumentTest {

	@Test
	public void findCorrespondenceDocuments() {
		CorrespondenceDocument c1 = new CorrespondenceDocument();
		c1.setName("c1");
		c1.persist();
		
		c1 = new CorrespondenceDocument("c2");
		c1.persist();
		c1.flush();
		
		assertThat(CorrespondenceDocument.findCorrespondenceDocument(c1.getId())).isNotNull();
	}
	
	@Test
	public void testEquals() {
		CorrespondenceDocument c1 = new CorrespondenceDocument();
		c1.setName("c1");
		c1.setWithinThreshold(Boolean.TRUE);
		
		CorrespondenceDocument c2 = new CorrespondenceDocument();
		c2.setName("c2");
		c2.setWithinThreshold(Boolean.FALSE);
		
		CorrespondenceDocument c3 = new CorrespondenceDocument();
		c3.setName("c1");
		c3.setWithinThreshold(Boolean.TRUE);
		
		assertThat(c1).isEqualTo(c1);
		assertThat(c1).isNotEqualTo(c2);
		assertThat(c3).isEqualTo(c1);
			
	}	
}
