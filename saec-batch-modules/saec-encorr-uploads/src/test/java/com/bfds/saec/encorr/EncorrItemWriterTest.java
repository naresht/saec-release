package com.bfds.saec.encorr;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.domain.InRecordStatus;
import com.bfds.saec.batch.encorr.util.DataGenerator;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.encorr.dao.EncorrItem;
import com.bfds.saec.encorr.dao.EncorrItemWriter;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@Transactional
public class EncorrItemWriterTest {

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Test
	public void testEncorrItemWriter() {
		DataGenerator.createEncorrItemWriterData();

		final EncorrItemWriter encorrItemWriter = new EncorrItemWriter();
		
		encorrItemWriter.setEntityManagerFactory(entityManagerFactory);

		List<EncorrItem> items = getItems();
		encorrItemWriter.write(items);

		Letter l1 = Letter.findByMailingControlNo("123450");
		assertThat(l1.getLetterStatus()).isEqualTo(LetterStatus.LETTER_SENT);

		Letter l2 = Letter.findByMailingControlNo("123450");
		assertThat(l2.getLetterStatus()).isEqualTo(LetterStatus.LETTER_SENT);

		Letter l3 = Letter.findByMailingControlNo("123450");
		assertThat(l3.getLetterStatus()).isEqualTo(LetterStatus.LETTER_SENT);

		DataGenerator.deleteData();
	}
	

	@Test
	public void testNoMatchRecords() {
		DataGenerator.createEncorrItemWriterData();

		final EncorrItemWriter encorrItemWriter = new EncorrItemWriter() {
			@Override
			public Long getJobExecutionId() {
				return 100L;
			}
			
		};
		
		encorrItemWriter.setEntityManagerFactory(entityManagerFactory);

		List<EncorrItem> items = getItems();		
		
		EncorrItem item = new EncorrItem();
		item.setMailControlNo("123456");
		item.setWorkType("TLETTER");
		items.add(item);

		item = new EncorrItem();
		item.setMailControlNo("123457");
		item.setWorkType("LETTER");
		items.add(item);
		
		encorrItemWriter.write(items);
		
		List<EncorrItem> noMatchitems = EncorrItem.findAllEncorrItems();
		
		assertThat(noMatchitems).onProperty("mailControlNo").containsOnly("123456", "123457");
		assertThat(noMatchitems).onProperty("status").containsOnly(InRecordStatus.NOT_FOUND_IN_SAEC);
		DataGenerator.deleteData();
	}
	

	private List<EncorrItem> getItems() {
		List<EncorrItem> items = Lists.newArrayList();

		EncorrItem item;

		item = new EncorrItem();
		item.setMailControlNo("123450");
		item.setWorkType("TLETTER");
		items.add(item);

		item = new EncorrItem();
		item.setMailControlNo("123451");
		item.setWorkType("LETTER");
		items.add(item);

		item = new EncorrItem();
		item.setMailControlNo("123321");
		item.setWorkType("TLETTER");
		items.add(item);



		return items;
	}
	
	@Test
	public void test(){
		
	}

}
