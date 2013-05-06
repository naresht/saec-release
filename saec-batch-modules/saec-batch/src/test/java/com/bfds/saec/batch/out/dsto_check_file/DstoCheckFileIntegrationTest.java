package com.bfds.saec.batch.out.dsto_check_file;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
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

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class DstoCheckFileIntegrationTest extends
		AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job dstoCheckFileJob;

	@Autowired
	DstoCheckFileEventTestData eventTestData;
	
	public void afterAllTests() {
		testDataUtil.deleteData();
        testDataUtil.deleteAllFileRecords(DstoRec.class);
	}

	public void beforeAllTests() {
		eventTestData.createBankLov();
		eventTestData.create();
	}


	/**
	 * Test to validate, that only Checks with status
	 * {NEW_ISSUE_OUTSTANDING_NI_NIO & ISSUANCE_OUTSTANDING_IS_ISO} has to be
	 * picked for the dstoCheckFileJob Job, Once those gets picked their
	 * "DstoCheckFileSentFlag" must be set to True, So that the same check can't
	 * be picked for the next time when Job runs again.
	 */

	@Test
	@Transactional
	public void verifySentToBankFlag() {
		List<Payment> list = Lists.newArrayList( Payment.findPaymentIdentificationNo("1234561444"),
												 Payment.findPaymentIdentificationNo("1234561555"));
		assertThat(list).onProperty("dstoCheckFileSentFlag").containsOnly(Boolean.TRUE);
	}

	/**
	 * Payment with invalid DSTO payment code:VOID_RE_ISSUE_APPROVED_I_VA, Or
	 * PaymentType != CHECK doesn't picked by the job.
	 */
	@Test
	@Transactional
	public void verifyNotSentToBankFlag() {
		List<Payment> list = Lists.newArrayList(Payment.findPaymentIdentificationNo("1234561666"),
												Payment.findPaymentIdentificationNo("1234561777"));
		assertThat(list).onProperty("dstoCheckFileSentFlag").containsOnly(Boolean.FALSE);
	}
	/**
	 * Payment with identificationNo == null,doesn't picked by the job.
	 */
	@Test
	@Transactional
	public void verifyNotSentToBankFlagForNullIdentification() {
		List<Payment> list = Payment.findChecksByAccountNoAndNettAmount("100007", 101.75);
		assertThat(list).onProperty("dstoCheckFileSentFlag").containsOnly(Boolean.FALSE);
	}
	
	/**
	 * Verify computeGrossamount().
	 */
	@Test
	@Transactional
	public void varifyComputeGrossamount()
	{
		
	}
	/**
	 * Verify data in stage table.
	 */
	@Test
	public void verifyCreatedFileRecords() {
		Object[][] expectedRecords = new Object[][] {
				{ "recordType", "100" },
				{ "chkType", "MIX01" },
				{ "fromLibID", "Library1" },
				{ "chkPrintRunNum", "0" },
				{ "jobRunNum", "0" },
				{ "bankRouting", "2234" },
				{ "dda", "34512" },
				{ "chkNum", "1234561444", "1234561555" },
				{ "fromAccount", "100007","100007"},
				{ "extAccountID", "66666" },
				{ "taxID", "112233445" },
				{ "reg1OrAdd1", "RegistrationName1" },
				{ "reg2OrAdd2", "RegistrationName2" },
				{ "reg3OrAdd3", "RegistrationName3" },
				{ "reg4OrAdd4", "RegistrationName4" },
				{ "reg5OrAdd5", "address 1" },
				{ "reg6OrAdd6", "address 2" },
				{ "reg7OrAdd7", "address 3" },
				{ "city", "NEWYORK" },
				{ "state", "NY" },
				{ "postalCode", "23456" },
				{ "postalCodeExt", "4455" },
				{ "country", "USA" },
				{ "printCode", "101", "101" },
				{ "msgOrLetterNum", "c1", "c2" },
				{ "auditCheckFlag", "A" },
				{ "checkForm", "Y" },
				{ "brokerNum", "5578", "5578" },
				{ "printTaxForm1", "9" },
				{ "grossAmount1", new BigDecimal(100.75),new BigDecimal(345.75) },
                { "totGross", new BigDecimal(100.75),new BigDecimal(345.75) },
				{ "netAmount1", new BigDecimal(100.75), new BigDecimal(345.75) },
                { "totProceeds", new BigDecimal(100.75), new BigDecimal(345.75) },
                { "totTaxes", new BigDecimal(10.75)},
				{ "printTaxForm2", "9" }, { "printTaxForm3", "9" },
				{ "printTaxForm4", "9" }, { "printTaxForm5", "9" },
				{ "printTaxForm6", "9" },
				{ "payeeRegistration1", "RegistrationName1" },
				{ "payeeRegistration2", "RegistrationName2" },
				{ "payeeRegistration3", "RegistrationName3" },
				{ "signature1", "C" },
				{ "clientOrProductIndicator", "101", "101" },
				{ "sentUser", "BFDS" },{"dstoFileType","CHECKFILE","CHECKFILE"},
				{"eventNameAddress1","Eventadd1"},
				{"eventNameAddress2","Eventadd2"},
				{"eventNameAddress3","Eventadd3"},
				{"eventNameAddress4","Eventadd4"},
				{"eventNameAddress5","Eventadd5"},
				{"eventNameAddress6","Eventadd6"},
				{"variableVerbiage","variableverbiage"},
				{"bankInfo1","BankInfo1"},
				{"bankInfo2","BankInfo2"},
				{"bankInfo3","BankInfo3"},
				{"distributionText","distribution"},
				{"abaTop","abatop"},
				{"runType","T"},
				{"abaBottom","ababottom"},
		// TODO: verify other properties.
		};

		final List<DstoRec> actualRecords = testDataUtil.findAllFileRecords(DstoRec.class);
		assertThat(actualRecords).hasSize(2);
		Collections.sort(actualRecords, new Comparator<DstoRec>() {
			@Override
			public int compare(DstoRec o1, DstoRec o2) {
				return o1.getChkNum().compareTo(o2.getChkNum());
			}
		});
		testDataUtil.verifyData(actualRecords, expectedRecords);
	}

	
	@Override
	protected Job geJOb() {
		return dstoCheckFileJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.addString("runType", "T")
                .addString("pickFirstIssuances", "false")
				.toJobParameters();
	}

}
