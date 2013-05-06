package com.bfds.saec.rpo.awd;

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
import com.bfds.saec.rpo.dao.RpoPreparedStatementSetter;
import com.bfds.saec.rpo.dto.AwdRpoRowMapper;
import com.bfds.saec.rpo.dto.RpoItem;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TestExecutionListeners({ StepScopeTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class AwdRpoRowMapperTest {

	@Autowired
	DataSource awdDataSource;

	@Autowired
	JdbcCursorItemReader<RpoItem> rpoItemReader;

	@Test
	public void testAwdRpoRowMapper() throws SQLException {

		Event e = new Event();
		e.setCode("NYL");
		e.persist();
		e.flush();
		e.clear();

		Connection con = awdDataSource.getConnection();

		PreparedStatement st = con.prepareStatement(rpoItemReader.getSql());
		RpoPreparedStatementSetter preparedStatementSetter = new RpoPreparedStatementSetter();
		preparedStatementSetter.setValues(st);

		ResultSet resultSet = st.executeQuery();

		final List<RpoItem> rpoList = Lists.newArrayList();

		AwdRpoRowMapper mapper = new AwdRpoRowMapper();
		while (resultSet.next()) {
			rpoList.add(mapper.mapRow(resultSet, 0));
		}

		assertThat(rpoList).hasSize(4);
		assertThat(rpoList).onProperty("businessArea").containsOnly("NYL");
		assertThat(rpoList).onProperty("workType").containsOnly("FORWARDCHK","FRWRDNOCHK","NONFORWARD","NOFORWDCHK");		

	}

}
