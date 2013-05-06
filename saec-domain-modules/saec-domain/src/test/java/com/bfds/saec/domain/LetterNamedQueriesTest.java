package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-domain-test.xml" })
@Transactional
public class LetterNamedQueriesTest { 

	@Test
	public void findLetterOfClaimant() {
		List<Letter> letters = Letter.findLetterOfClaimant(10L);		
		assertThat(letters).isNotNull();
	}
	
	@Test
	public void findClaimFormsOfClaimant() {
		List<Letter> letters = Letter.findClaimFormsOfClaimant(10L);		
		assertThat(letters).isNotNull();
	}
	
	@Test
	public void findOptoutFormsOfClaimant() {
		List<Letter> letters = Letter.findOptoutFormsOfClaimant(10L);		
		assertThat(letters).isNotNull();
	}
	
	@Test
	public void findCorrespondenceOfClaimant() {
		List<Letter> letters = Letter.findCorrespondenceOfClaimant(10L);		
		assertThat(letters).isNotNull();
		letters = Letter.findCorrespondenceOfClaimant(10L, (String[]) null);		
		assertThat(letters).isNotNull();
	}
}
