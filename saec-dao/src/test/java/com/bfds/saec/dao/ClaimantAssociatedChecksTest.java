package com.bfds.saec.dao;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.util.DataGenerator;
import com.bfds.saec.util.SaecDateUtils;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-dao-test.xml" })
@Transactional
public class ClaimantAssociatedChecksTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory
			.getLogger(ClaimantAssociatedChecksTest.class);

	@Autowired
	private ClaimantDaoImpl claimantDaoImpl;

	@Before
	public void onSetUp() throws Exception {
		DataGenerator.deleteData();
	}
	
	/**
	 * This method adds the first Check# and amount of the claimant, it is based on the payment creation date.
	 */
	@Test
	public void testClaimantassociatedPayments() {
		Claimant claimant = DataGenerator.newClaimant("Margarita",
				"Barrington", "100001", "200001", "30001", "400001", "50001",
				"60001", false, DataGenerator.newAddress(
						AddressType.ADDRESS_OF_RECORD, "2397 Bee Street",
						"Muskegon", "MI", "49470"), DataGenerator.newAddress(
						AddressType.SEASONAL_ADDRESS, "1234 Bee Street",
						"New York", "NY", "4470"), DataGenerator.newCheck(
						"1000010", 100, SaecDateUtils.getDaysFromCurrent(3)),
				DataGenerator.newCheck("1000020", 200,
						SaecDateUtils.getDaysFromCurrent(-3)));

		claimant.setMailingAddressByType(AddressType.SEASONAL_ADDRESS);
		claimant.persist();
		claimant.flush();
		ClaimantSearchCriteriaDto c = new ClaimantSearchCriteriaDto();
		c.setCheckNo("1000020");

		final Object[] record = { claimant.getId(), null, null, null,null };
		final List<Object[]> records = Lists.newArrayList();
		records.add(record);
		Collection<Object[]> updatedrecords = claimantDaoImpl
				.addFirstCheckInfo(records, c);
		final Object[] updatedRecord = updatedrecords.iterator().next();
		assertThat(updatedRecord).hasSize(7);
		assertThat(updatedRecord[0]).isEqualTo(claimant.getId());
		assertThat(updatedRecord[5]).isEqualTo("1000020");
		assertThat(((BigDecimal) updatedRecord[6]).doubleValue()).isEqualTo(
				200.0);
	}
	
	 
	/**
	 * This method ensures that if there are no records to pass to verify the
	 * payment object ,then it returns zero records.
	 */
	@Test
	public void testNullRecordCheckForaClaimant() {
		final List<Object[]> records = Lists.newArrayList();
		ClaimantSearchCriteriaDto claimantdto = new ClaimantSearchCriteriaDto();
		claimantdto.setCheckNo("");
		Collection<Object[]> updatedrecords = claimantDaoImpl
				.addFirstCheckInfo(records, claimantdto);
		assertThat(updatedrecords).hasSize(0);
	}
}
