package com.bfds.saec.encorr;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
public class EncorrDataAccessTest {

	@Autowired
	DataSource awdDataSource;

	final Logger log = LoggerFactory.getLogger(EncorrDataAccessTest.class);

	@Test
	public void testDataSource() throws SQLException {
		assertThat(awdDataSource).isNotNull();
		Connection con = awdDataSource.getConnection();
		String sqlQuery = "select ec.unitcd, ec.wrktype, '1234567' as SAECReference, ps27.SSNO as SSN, ps27.AMTV as CheckAmount, ps27.CKNM as CheckNumber, ps27.MLID as MailControlNumber "
				+ " from "
				+ " ( "
				+ "  select min(wc1.enddattim) as minenddattim, wa5.key_date, wa5.key_time, wa5.key_milsec, wa5.recordcd, wa5.crnode, wa5.wrktype, wa5.unitcd "
				+ "   from  wc1u999s wc1 join wa5u999s wa5 "
				+ "   on wc1.key_date = wa5.key_date and wc1.key_time = wa5.key_time and wc1.key_milsec = wa5.key_milsec and wc1.recordcd = wa5.recordcd and wc1.crnode = wa5.crnode and wc1.event_dattim = wa5.event_dattim "
				+ "   where wa5.wrktype in ('TLETTER','LETTER') and wa5.statcd = 'PROCESSED' "
				+ "  group by wa5.key_date, wa5.key_time, wa5.key_milsec, wa5.recordcd, wa5.crnode, wa5.wrktype, wa5.unitcd "
				+ "  ) ec "
				+ " join ps027pf ps27 "
				+ "  on ec.key_date = ps27.key_date and ec.key_time = ps27.key_time and ec.key_milsec = ps27.key_milsec and ec.recordcd = ps27.recordcd and ec.crnode = ps27.crnode;";
		Statement st = con.createStatement();

		ResultSet resultSet = st.executeQuery(sqlQuery);

		log.info("ResultSet Executed --> " + resultSet.toString());
	}

}