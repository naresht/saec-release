package com.bfds.saec.domain;


import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.LetterType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-test.xml" })
@Transactional
public class LetterCodeTest {	
	
	@Before
	public void createLetterCodes() {
		LetterCode code = new LetterCode("c1", "code 1", LetterType.CLAIM_FORM);

		code.persist();
		code = new LetterCode("c2", "code 2", LetterType.OPTOUT_FORM);
		
		code.persist();
		
	}

    @After
    /**
     * This is actually not required. But without this we have unique key constraint error !!! on 'mvn clean install'
     */
    public void after() {
        for(LetterCode lc : LetterCode.findAllLetterCodes()) {
            lc.remove();
        }
    }
	
	@Test(expected = NullPointerException.class)
	public void findByLetterType() {
		assertThat(LetterCode.findByLetterType(LetterType.CLAIM_FORM)).onProperty("letterType").containsOnly(LetterType.CLAIM_FORM);
		assertThat(LetterCode.findByLetterType(LetterType.OPTOUT_FORM)).onProperty("letterType").containsOnly(LetterType.OPTOUT_FORM);
		assertThat(LetterCode.findByLetterType(LetterType.GENERAL_CORRESPONDENCE)).isEmpty();	
		//NullPointerException
		LetterCode.findByLetterType(null);	
	}
	
	@Test
	@ExpectedException(NullPointerException.class)
	public void findByCode() {
		assertThat(LetterCode.findByCode("c1").getCode()).isEqualTo("c1");
		assertThat(LetterCode.findByCode("c2").getCode()).isEqualTo("c2");
		assertThat(LetterCode.findByCode("c3")).isNull();
		//NullPointerException
		LetterCode.findByCode(null);	
	}
}
