package com.bfds.saec.domain;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/META-INF/spring/applicationContext.xml"})
public class ApplicationContextTests {

	@Autowired
	private DataSource dataSource;
	
	
	@Test
	public void dataSourceBeanShouldNotBeNull() {
		assertNotNull("dataSource bean should be injected", dataSource);

	}
	
}
