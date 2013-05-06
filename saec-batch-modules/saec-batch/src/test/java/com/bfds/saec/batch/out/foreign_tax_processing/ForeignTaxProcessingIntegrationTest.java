package com.bfds.saec.batch.out.foreign_tax_processing;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.Comparator;
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

import com.bfds.saec.batch.file.domain.out.damasco_foreign.*;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class ForeignTaxProcessingIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;
	
	@Autowired
	private Job foreignTaxProcessOutJob;

	@Autowired
	ForeignTaxProcessingOutEventTestData eventTestData;

	public void afterAllTests() {
        testDataUtil.deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
	}

	@Override
	protected Job geJOb() {
		return foreignTaxProcessOutJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}
	
	
	@Test
	@Transactional
	public void verifyTaxProcessedFlag() {
		List<Payment> list = Lists.newArrayList( Payment.findPaymentIdentificationNo("00001011"),
												 Payment.findPaymentIdentificationNo("20047"));
		assertThat(list).onProperty("sentOnTaxFile").containsOnly(Boolean.TRUE);
	}
	 
	
	@Test
	public void verifyCreatedFileRecords() {
		Object[][] expectedRecords = new Object[][] {
				{ "fundAccount", "66666" },
				{ "bin", "883398" },
				{ "brokerId", "5578" },
				{ "parentReferenceNo", "100007" },
				{ "tin", "647483" },
				{ "parentReg1OrAdd1", "RegistrationName1", "RegistrationName1" },
				{ "parentReg2OrAdd2", "RegistrationName2", "RegistrationName2" },
				{ "parentReg3OrAdd3", "RegistrationName3", "RegistrationName3" },
				{ "parentReg4OrAdd4", "RegistrationName4", "RegistrationName4" },
				{ "parentReg5OrAdd5", "address 1", "address 1" },
				{ "parentReg6OrAdd6", "address 2", "address 2" },
				{ "parentCity", "NEWYORK", "NEWYORK" },
				{ "parentState", "NY", "NY" },
				{ "parentCountry", "USA", "USA" },
				{ "parentZip", "23456", "23456" },
				{ "parentZipCodeExt", "4455", "4455" },
				{ "checkNumber", "00001011", "20047" },
				{ "altPayeeReg1OrAdd1", "RegistrationName11", "RegistrationName11" },
				{ "altPayeeReg2OrAdd2", "RegistrationName22", "RegistrationName22" },
				{ "altPayeeReg3OrAdd3", "RegistrationName33", "RegistrationName33"  },
				{ "altPayeeReg4OrAdd4", "RegistrationName44", "RegistrationName44"  },
				{ "altPayeeReg5OrAdd5", "address 1", "address 1"},
				{ "altPayeeReg6OrAdd6", "address 2", "address 2"  },
				{ "altPayeeCity", "NEWYORK", "NEWYORK" },
				{ "altPayeeState", "NY", "NY" },
				{ "altPayeeCountry", "USA", "USA" },
				{ "altPayeeZip", "23456", "23456" },
				{ "altPayeeZipCodeExt", "4455", "4455" },
				
				 
				
		// TODO: verify other properties.
		};

		final List<ForeignTaxOutRec> actualRecords = testDataUtil.findAllFileRecords(ForeignTaxOutRec.class);
		assertThat(actualRecords).hasSize(2);
		Collections.sort(actualRecords, new Comparator<ForeignTaxOutRec>() {
			@Override
			public int compare(ForeignTaxOutRec o1, ForeignTaxOutRec o2) {
				return o1.getCheckNumber().compareTo(o2.getCheckNumber());
			}
		});
		testDataUtil.verifyData(actualRecords, expectedRecords);
	}

	
	
}
