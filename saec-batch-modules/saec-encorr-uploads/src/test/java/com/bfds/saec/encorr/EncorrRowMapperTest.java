package com.bfds.saec.encorr;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.bfds.saec.domain.Event;
import com.bfds.saec.encorr.dao.EncorrItem;
import com.bfds.saec.encorr.dto.EncorrReaderPreparedStatementSetter;
import com.bfds.saec.encorr.dto.EncorrRowMapper;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TestExecutionListeners({ StepScopeTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class EncorrRowMapperTest {

	@Autowired
	DataSource awdDataSource;

	@Autowired
	JdbcCursorItemReader<EncorrItem> encorrItemReader;

	@Test
	public void testEncorrRowMapper() throws SQLException {

		Event e = new Event();
		e.setCode("NYL");
		e.persist();
		e.flush();
		e.clear();

		Connection con = awdDataSource.getConnection();

		PreparedStatement st = con.prepareStatement(encorrItemReader.getSql());
		EncorrReaderPreparedStatementSetter preparedStatementSetter = new EncorrReaderPreparedStatementSetter();
		preparedStatementSetter.setValues(st);

		ResultSet resultSet = st.executeQuery();

		final List<EncorrItem> list = Lists.newArrayList();

		EncorrRowMapper mapper = new EncorrRowMapper();
		while (resultSet.next()) {
			list.add(mapper.mapRow(resultSet, 0));
		}

		assertThat(list).hasSize(5);
		assertThat(list).onProperty("businessArea").containsOnly("NYL");
		assertThat(list).onProperty("workType").containsOnly("TLETTER",
				"LETTER");
		assertThat(list).onProperty("mailControlNo").containsOnly("123449",
				"234568", "123450","234567","123321");

	}

}
