package com.bfds.saec.batch.stale_date;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
public class StaleDateQueryProviderTest {

	@Autowired
	StaleDateQueryProvider staleDateQueryProvider;

	@PersistenceContext(unitName="entityManagerFactory")
	EntityManager entityManager;

	@Test
	public void runStaleDateQueryProviderTest() throws Exception {
		DataGenerator.createStaleDateProcessingData();
		staleDateQueryProvider.setEntityManager(entityManager);
		List<Long> staleDateCheckList = staleDateQueryProvider.createQuery()
				.getResultList();
		assertThat(staleDateCheckList.size()).isEqualTo(2);
	}
}
