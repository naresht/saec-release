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
public class ProcessErrorTest {

	@Test
	public void findErrorType() {
		assertThat(ProcessError.ErrorType.values()).isNotEmpty();
		assertThat(ProcessError.ErrorType.values()).contains(
				ProcessError.ErrorType.BUSINESS_PROCESS);
		assertThat(ProcessError.ErrorType.values()).contains(
				ProcessError.ErrorType.SYSTEM);
	}

	@Test
	public void findErrorSeverity() {
		assertThat(ProcessError.ErrorSeverity.values()).isNotEmpty();
		assertThat(ProcessError.ErrorSeverity.values()).contains(
				ProcessError.ErrorSeverity.FATAL);
		assertThat(ProcessError.ErrorSeverity.values()).contains(
				ProcessError.ErrorSeverity.WARN);
	}
}
