package com.bfds.saec.batch.out.dsto_print_file;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.Theme;

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
public class DSTOPrintfileProcessorTest {

	@Autowired
	DSTOPrintFileItemProcessor dstoPrintFileItemProcessor;
	
	private Claimant claimant;

	@Before
	public void before() {
		DataGenerator.createDSTOEvent_();
		this.claimant=DataGenerator.DSTOPrintfileProcessorTestData();
		dstoPrintFileItemProcessor.beforeStep(
				MetaDataInstanceFactory.createStepExecution(MetaDataInstanceFactory.createJobExecution(), "step-1", 10L));
	}
	
	/**
	 * When the mailing address of the {@link Claimant} is changes {@link Theme} {@link MailObjectAddress} of all {@link Payment}s
	 * eligible for mailing must also be updated so that when they are mailed they go to the new mailing address.
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void mailingAddressChangeTest()throws Exception
	{
		claimant.getMailingAddress().setCity("NEWJERSEY");
		claimant.getMailingAddress().setStateCode("AY");
		claimant.getMailingAddress().setCountryCode("US");
		claimant.merge();
		claimant.flush();
		
		Letter letter=claimant.getLetters().iterator().next();
		DstoRec dstoPrintFileDto=dstoPrintFileItemProcessor.process(letter);
		assertThat(dstoPrintFileDto.getCity()).containsIgnoringCase("NEWYORK");
	}
}
