package com.bfds.saec.batch.out.bank_issue_void;


import com.bfds.saec.domain.Payment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@Transactional
public class IssueVoidQueryProviderTest {


    IssueVoidQueryProvider issueVoidQueryProvider;

    @PersistenceContext(unitName="entityManagerFactory")
    EntityManager entityManager;

    @Autowired
    DbIssueVoidEventTestData eventTestData;

    @Before
    public void before() {
        eventTestData.create();
        issueVoidQueryProvider = new  IssueVoidQueryProvider();
         issueVoidQueryProvider.setEntityManager(entityManager);
    }

    /**
     * Tranching applies to ony {@link com.bfds.saec.domain.reference.PaymentCode#FIRST_ISSUE_OUTSTANDING_FI_FIO} only.
     *
     */
    @Test
    public void filterByExistingTranchCode() throws Exception {
        issueVoidQueryProvider.setTranchCodes(new String[] {"t1", "t2 "});
        List<Payment> payments = issueVoidQueryProvider.createQuery().getResultList();
        assertThat(payments.size()).isEqualTo(19);
        assertThat(payments).onProperty("tranch.code").containsOnly("t1");
    }

    @Test
    public void filterByNonExistingTranchCode() throws Exception {
        issueVoidQueryProvider.setTranchCodes(new String[] {"t11", "t2 "});
        List<Payment> payments = issueVoidQueryProvider.createQuery().getResultList();
        // We still get 17 as tranching applies to {@link com.bfds.saec.domain.reference.PaymentCode#FIRST_ISSUE_OUTSTANDING_FI_FIO} only.
        assertThat(payments.size()).isEqualTo(17);
    }

}
