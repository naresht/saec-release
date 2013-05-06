package com.bfds.saec.rpo.awd;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
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
import com.bfds.saec.rpo.dto.RpoItem;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TestExecutionListeners({ StepScopeTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class RpoItemReaderTest {

	@Autowired
	JdbcCursorItemReader<RpoItem> rpoItemReader;

	@Test
	public void testRpoItemReader() throws UnexpectedInputException,
			ParseException, Exception {

		Event e = new Event();
		e.setCode("NYL");
		e.persist();
		e.flush();
		e.clear();

		final List<RpoItem> list = Lists.newArrayList();

		rpoItemReader.setSaveState(false);
		rpoItemReader.open(null);

		for (RpoItem item = rpoItemReader.read(); item != null; item = rpoItemReader
				.read()) {
			list.add(item);
		}
		assertThat(list).hasSize(4);
		assertThat(list).onProperty("businessArea").containsOnly("NYL");
		assertThat(list).onProperty("workType").containsOnly("FORWARDCHK",
				"FRWRDNOCHK", "NOFORWDCHK", "NONFORWARD");
		assertThat(list).onProperty("checkAmount").containsOnly(
				new BigDecimal(900), new BigDecimal(700), new BigDecimal(1200),
				new BigDecimal(1000));
		assertThat(list).onProperty("checkNo").containsOnly("123450", "123321",null);
		assertThat(list).onProperty("mailControlNo").containsOnly("234566",
				"234567",null);

	}

}
