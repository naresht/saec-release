package com.bfds.saec.encorr;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bfds.saec.domain.Event;
import com.bfds.saec.encorr.dao.EncorrItem;
import com.bfds.saec.encorr.dto.EncorrReaderPreparedStatementSetter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
public class EncorrReaderPreparedStatementSetterTest {

	@Autowired
	DataSource awdDataSource;

	@Autowired
	JdbcCursorItemReader<EncorrItem> encorrItemReader;

	@Test
	public void testPreparedStmt() throws SQLException {
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

		List<EncorrItem> encorrItemList = new ArrayList<EncorrItem>();

		while (resultSet.next()) {
			EncorrItem item = new EncorrItem();
			item.setBusinessArea(resultSet.getString(1));
			item.setWorkType(resultSet.getString(2));
			item.setMailControlNo(resultSet.getString(7));
			encorrItemList.add(item);
		}
		assertThat(encorrItemList).hasSize(5);
		assertThat(encorrItemList).onProperty("businessArea").containsOnly(
				"NYL");
		assertThat(encorrItemList).onProperty("workType").containsOnly(
				"TLETTER", "LETTER");
		assertThat(encorrItemList).onProperty("mailControlNo").containsOnly(
				"123449", "234568", "123450","123321","234567");

	}

}
