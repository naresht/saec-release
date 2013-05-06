package com.bfds.saec.batch.out.dsto_check_file;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
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
import com.bfds.saec.domain.MailObjectAddress;
import com.bfds.saec.domain.Payment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@Transactional
public class DSTOCheckfileProcessorTest {

	@Autowired
	DSTOCheckFileItemProcessor dstoCheckFileJobProcessor;
	  
	private Claimant claimant;

	@Before
	public void before() {
		DataGenerator.createBankLov();
		this.claimant=DataGenerator.getDSTOClaimant();
		
		dstoCheckFileJobProcessor.beforeStep(
				MetaDataInstanceFactory.createStepExecution(MetaDataInstanceFactory.createJobExecution(), "step-1", 10L));
	}
	
	 
	/**
	 * When the mailing address of the {@link Claimant} is changes {@link Theme} {@link MailObjectAddress} of all {@link Payment}s
	 * eligible for mailing must also be updated so that when they are mailed they go to the new mailing address.
	 * @throws Exception
	 */
	@Test
	 @Ignore("The implementation is casuing hibernate flush problems. We need to redesign the implementations. ")
	public void mailingAddressChangeTest()throws Exception
	{
		claimant.getMailingAddress().setCity("NEWJERSEY");
		claimant.getMailingAddress().setStateCode("NYC");
		claimant.getMailingAddress().setCountryCode("US");
		claimant.merge();
		claimant.flush();
		
		Payment payment=claimant.getPayments().iterator().next();
		DstoRec dstoCheckFileDto=dstoCheckFileJobProcessor.process(payment);
		assertThat(dstoCheckFileDto.getCity()).containsIgnoringCase("NEWJERSEY");
	}
}
