package com.bfds.saec.batch.out.dsto_print_file;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bfds.saec.batch.util.TestDataUtil;
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
import com.bfds.saec.domain.Letter;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
public class DstoPrintFileIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    TestDataUtil testDataUtil;

	@Autowired
	private Job dstoPrintFileJob;

	@Autowired
	DstoPrintFileEventTestData eventTestData;

	public void afterAllTests() {
        testDataUtil.deleteData();
        testDataUtil.deleteAllFileRecords(DstoRec.class);
	}

	public void beforeAllTests() {
		eventTestData.createDSTOPrintFileEvent();
		eventTestData.create();
	}

	/**
	 * Test to validate, that only Letters having {MailType:OUTGOING} and  {LetterType: CLAIM_FORM or OPTOUT_FORM or GENERAL_CORRESPONDENCE} and
	 * {LetterCode>= 0 and LetterCode <=100} will be picked for the dstoPrintFileJob Job, Once those gets picked their "DstoCheckFileSentFlag" must be set to True,
	 * So that the same check can't be picked for the next time when Job runs again.
	 */
	
	@Test
	@Transactional
	public void verifySentToBankFlag() {
		List<Letter> list = Lists.newArrayList( Letter.findByMailingControlNo("1011"),
												Letter.findByMailingControlNo("1012"));
		assertThat(list).onProperty("dstoPrintFileSentFlag").containsOnly(Boolean.TRUE);
	}
	
	/**
	 * Letters with invalid DSTO MailType Or LetterCode Range doesn't picked by job.
	 */
	@Test
	@Transactional
	public void verifyNotSentToBankFlag() {
		List<Letter> list = Lists.newArrayList( Letter.findByMailingControlNo("1010"),
												Letter.findByMailingControlNo("1013"));
		assertThat(list).onProperty("dstoPrintFileSentFlag").containsOnly(Boolean.FALSE);
	}

	/**
	 * Verify data in stage table.
	 */
	@Test
	public void verifyCreatedFileRecords() {
		Object[][] expectedRecords = new Object[][] {
				{ "recordType", "100" },
				{ "fromLibID", "Library1" },
				{ "chkPrintRunNum", "0" },
				{ "jobRunNum", "0" },
				{ "bankRouting", "2234" },
				{ "dda", "34512" },
				{ "chkNum", "1011", "1012" },
				{ "fromAccount", "100007", "100007"},
				{ "extAccountID", "66666" },
				{ "taxID", "112233445" },
				{ "reg1OrAdd1", "RegistrationName1" },
				{ "reg2OrAdd2", "RegistrationName2" },
				{ "reg3OrAdd3", "RegistrationName3" },
				{ "reg4OrAdd4", "RegistrationName4" },
				{ "reg5OrAdd5", "RegistrationName5" },
				{ "reg6OrAdd6", "letteraddress 1" },
				{ "reg7OrAdd7", "letteraddress 2" },
				{ "city", "NEWYORK" },
				{ "state", "NY" },
				{ "postalCode", "23456" },
				{ "postalCodeExt", "4455" },
				{ "country", "USA" },
				{ "printCode", "sampleEven" },
				{ "msgOrLetterNum", "011" },
				{ "auditCheckFlag", "A" },
				{ "checkForm", "N" },
				{ "brokerNum", "5578" },
				{ "payeeRegistration1", "RegistrationName1" },
				{ "payeeRegistration2", "RegistrationName2" },
				{ "payeeRegistration3", "RegistrationName3" },
				{ "signature1", "C" },
				{ "clientOrProductIndicator", "sampleEven" },
				{ "sentUser", "BFDS" },{"dstoFileType","PRINTFILE","PRINTFILE"},
				{ "barCode", "BFO3,MCON1011,MISC100007","BFO3,MCON1012,MISC100007" },
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
		return dstoPrintFileJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("dda", "all")
				.toJobParameters();
	}

}
