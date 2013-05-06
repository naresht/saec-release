package com.bfds.saec.encorr;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.bfds.saec.domain.Event;
import com.bfds.saec.encorr.dao.EncorrItem;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TestExecutionListeners({ StepScopeTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class EncorrItemReaderTest {

	@Autowired
	JdbcCursorItemReader<EncorrItem> encorrItemReader;

	@Test
	public void testEncorrItemReader() throws UnexpectedInputException,
			ParseException, Exception {

		Event e = new Event();
		e.setCode("NYL");
		e.persist();
		e.flush();
		e.clear();

		final List<EncorrItem> list = Lists.newArrayList();

		encorrItemReader.setSaveState(false);
		encorrItemReader.open(null);

		for (EncorrItem item = encorrItemReader.read(); item != null; item = encorrItemReader
				.read()) {
			list.add(item);
		}
		assertThat(list).hasSize(5);
		assertThat(list).onProperty("businessArea").containsOnly("NYL");
		assertThat(list).onProperty("workType").containsOnly("TLETTER",
				"LETTER");
		assertThat(list).onProperty("mailControlNo").containsOnly("123321","234567",
				"123450","123449","234568");		
			
	}

}
