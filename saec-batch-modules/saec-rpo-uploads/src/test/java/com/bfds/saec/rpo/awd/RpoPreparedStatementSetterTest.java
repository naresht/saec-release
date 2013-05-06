package com.bfds.saec.rpo.awd;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bfds.saec.rpo.dto.AwdRpoRowMapper;
import com.bfds.saec.rpo.dto.RpoItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bfds.saec.domain.Event;
import com.bfds.saec.rpo.dao.RpoPreparedStatementSetter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
public class RpoPreparedStatementSetterTest {

	@Autowired
	DataSource awdDataSource;

	@Autowired
	JdbcCursorItemReader<RpoItem> rpoItemReader;

	@Test
	public void testPreparedStmt() throws SQLException {
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

		List<RpoItem> list = new ArrayList<RpoItem>();

        AwdRpoRowMapper rowMapper = new AwdRpoRowMapper();
		while (resultSet.next()) {
			RpoItem item = rowMapper.mapRow(resultSet, 1);
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
