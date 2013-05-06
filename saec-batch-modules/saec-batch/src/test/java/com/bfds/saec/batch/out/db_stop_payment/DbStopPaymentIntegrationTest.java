package com.bfds.saec.batch.out.db_stop_payment;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.file.domain.out.db_stop_payment.DbStopPaymentRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({SaecTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})

public class DbStopPaymentIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired	
	private Job dbStopPaymentJob;

    @Autowired
    DbStopPaymentEventTestData eventTestData;

    
    public void beforeAllTests() {
        eventTestData.create();
    }
    
	public void afterAllTests() {
        testDataUtil.deleteData();
	}

    @Override
    protected Job geJOb() {
        return dbStopPaymentJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder().addString("dda", "22222222").toJobParameters();
    }


    /**
     * All {@link Payment}s sent to bank must have {@link Payment#sentOnBankStopFile} = true
     */
    @Test
    @Transactional
    public void verifySentToBankFlag() {
        List<Payment> list = Lists.newArrayList(
                Payment.findPaymentIdentificationNo("12345"),
                Payment.findPaymentIdentificationNo("123456"),
                Payment.findPaymentIdentificationNo("1234567"),
                Payment.findPaymentIdentificationNo("1234568"),
                Payment.findPaymentIdentificationNo("1234569"));
        

        assertThat(list).onProperty("sentOnBankStopFile").containsOnly(Boolean.TRUE);
    }



    /**
     * Verify data in stage table.
     */
    @Test
    public void verifyCreatedFileRecords() {

        final List<DbStopPaymentRec> actualRecords = testDataUtil.findAllFileRecords(DbStopPaymentRec.class);
        assertThat(actualRecords).hasSize(5);
        
       
    }

}