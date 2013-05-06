package com.bfds.saec.batch.out.dsto_print_file;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bfds.saec.batch.util.DataGenerator;
import com.bfds.saec.domain.Letter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
public class DSTOPrintFileQueryProviderTest {

	@Autowired
	DSTOPrintFileQueryProvider dstoPrintFileQueryProvider;

	@PersistenceContext(unitName="entityManagerFactory")
	EntityManager entityManager;

	@Test
	@Ignore
	public void runDSTOCheckFileQueryProviderTest() throws Exception {
		DataGenerator.createDSTOPrintFileJobData();

		dstoPrintFileQueryProvider.setEntityManager(entityManager);
		List<Letter> dstoPrintFileList = dstoPrintFileQueryProvider
				.createQuery().getResultList();

		assertThat(dstoPrintFileList.size()).isEqualTo(2);

		assertThat(dstoPrintFileList).onProperty("letterCode.code")
				.containsOnly("011", "010");
		assertThat(dstoPrintFileList).onProperty("letterCode.code").excludes(
				"201");

	}
}
