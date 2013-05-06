package com.bfds.saec.rpo.awd;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.rpo.util.DataGenerator;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.rpo.dao.RpoItemWriter;
import com.bfds.saec.rpo.dto.RpoItem;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@Transactional
public class RpoItemWriterTest {

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Test
	public void testRpoItemWriter() {
		DataGenerator.createRpoItemWriterData();

		final RpoItemWriter rpoItemWriter = new RpoItemWriter();
		rpoItemWriter.setEntityManagerFactory(entityManagerFactory);

		List<RpoItem> items = getItems();
		rpoItemWriter.write(items);

		Payment c1 = Payment.findCheckByNumberAndAmount("123450", 900);
		assertThat(c1.getPaymentCode()).isEqualTo(PaymentCode.VOID_RPO_FORWARDABLE_VOIDED_VF_VF);

		Payment c2 = Payment.findCheckByNumberAndAmount("123451", 800);
		assertThat(c2.getPaymentCode()).isEqualTo(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);

		Payment c3 = Payment.findCheckByNumberAndAmount("123321", 1000);
		assertThat(c3.getPaymentCode()).isEqualTo(PaymentCode.VOID_RPO_NON_FORWARDABLE_VOIDED_VN_VN);

		Letter l1 = Letter.findByMailingControlNo("234566");
		assertThat(l1.getRpoType()).isEqualTo(RPOType.FORWARDABLE);

		Letter l2 = Letter.findByMailingControlNo("234567");
		assertThat(l2.getRpoType()).isEqualTo(RPOType.NONFORWARDABLE);

		DataGenerator.deleteData();
	}

	private List<RpoItem> getItems() {
		List<RpoItem> items = Lists.newArrayList();
		RpoItem item;

		item = new RpoItem();
		item.setCheckNo("123450");
		item.setCheckAmount(new BigDecimal(900));
		item.setWorkType("FORWARDCHK");
		items.add(item);

		item = new RpoItem();
		item.setCheckNo("123451");
		item.setCheckAmount(new BigDecimal(800));
		item.setWorkType("NOFORWDCHK");
		items.add(item);

		item = new RpoItem();
		item.setCheckNo("123321");
		item.setCheckAmount(new BigDecimal(1000));
		item.setWorkType("NOFORWDCHK");
		items.add(item);

		item = new RpoItem();
		item.setMailControlNo("234566");
		item.setWorkType("FRWRDNOCHK");
		items.add(item);

		item = new RpoItem();
		item.setMailControlNo("234567");
		item.setWorkType("NONFORWARD");
		items.add(item);

		return items;
	}

	@Test
	public void test() {

	}

}
