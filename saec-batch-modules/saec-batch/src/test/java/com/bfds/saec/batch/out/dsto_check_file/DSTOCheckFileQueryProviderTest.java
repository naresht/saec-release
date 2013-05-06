package com.bfds.saec.batch.out.dsto_check_file;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bfds.saec.batch.util.TestDataUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bfds.saec.batch.util.DataGenerator;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@Transactional
public class DSTOCheckFileQueryProviderTest {

	DSTOCheckFileQueryProvider dstoCheckFileQueryProvider;

	@PersistenceContext(unitName="entityManagerFactory")
	EntityManager entityManager;

    @Before
    public void before() {
        DataGenerator.createDSTOCheckFileJobData();
        dstoCheckFileQueryProvider = new DSTOCheckFileQueryProvider();
        dstoCheckFileQueryProvider.setEntityManager(entityManager);
    }

	@Test
	public void runExcludingFirstIssuances() throws Exception {
		List<Payment> dstoCheckFileList = dstoCheckFileQueryProvider
				.createQuery().getResultList();

		assertThat(dstoCheckFileList.size()).isEqualTo(2);
		assertThat(dstoCheckFileList).onProperty("paymentType").containsOnly(
				PaymentType.CHECK);
		assertThat(dstoCheckFileList).onProperty("paymentCode").containsOnly(
				PaymentCode.ISSUANCE_OUTSTANDING_IS_ISO,
				PaymentCode.NEW_ISSUE_OUTSTANDING_NI_NIO);
	}

    @Test
    public void runForFirstIssuances() throws Exception {
        dstoCheckFileQueryProvider.setPickFirstIssuances(true);
        List<Payment> dstoCheckFileList = dstoCheckFileQueryProvider
                .createQuery().getResultList();

        assertThat(dstoCheckFileList.size()).isEqualTo(1);
        assertThat(dstoCheckFileList).onProperty("paymentType").containsOnly(
                PaymentType.CHECK);
        assertThat(dstoCheckFileList).onProperty("paymentCode").containsOnly(
                PaymentCode.FIRST_ISSUE_OUTSTANDING_FI_FIO);
    }
}
