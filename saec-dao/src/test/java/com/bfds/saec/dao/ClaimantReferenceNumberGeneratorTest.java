package com.bfds.saec.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.ClaimantReferenceNumber;
import org.junit.Ignore;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-dao-test.xml" })
@Transactional
public class ClaimantReferenceNumberGeneratorTest {
    
	@Test
	public void generate() {		
		Long start = getNextValue();
		for(int i =0; i< 100; i++) {
			getNextValue();
		}
		assertEquals(new Long(101 + start) , getNextValue());
	}
	
	public Long getNextValue() {
		return (new ClaimantReferenceNumber()).getNextValue();
	}	
}
