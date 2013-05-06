package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.reference.LetterType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-test.xml" })
@Transactional
public class LetterCodeRangeTest{

    @Before
    public void before() {
        generateLetterCodesData();
    }

    /**
     * This is actually not required. But without this we have unique key constraint error !!! on 'mvn clean install'
     */
    @After
    public void after() {
        for(LetterCode lc : LetterCode.findAllLetterCodes()) {
            lc.remove();
        }
    }

	@Test
	public void testLetterCodeRange() {

		final Query query1 = LetterCode
				.entityManager()
				.createQuery(
						"select l.code from com.bfds.saec.domain.LetterCode l where l.code >= :letterCode1  and l.code <= :letterCode2");
		query1.setParameter("letterCode1", "001");
		query1.setParameter("letterCode2", "100");
		List<LetterCode> list1 = query1.getResultList();

		assertThat(list1).containsOnly("001", "011", "048", "097", "100");

		final Query query2 = LetterCode
				.entityManager()
				.createQuery(
						"select l.code from com.bfds.saec.domain.LetterCode l where l.code > :letterCode");
		query2.setParameter("letterCode", "100");
		List<LetterCode> list2 = query2.getResultList();

		assertThat(list2).contains("143", "379", "211", "400", "456");

		assertThat(list1).isNotEqualTo(list2);

	}

	public static void generateLetterCodesData() {
		LetterCode lc;

		lc = new LetterCode("001", "ClaimForm - 001", LetterType.CLAIM_FORM);
		lc.persist();

		lc = new LetterCode("011", "ClaimForm - 011", LetterType.CLAIM_FORM);
		lc.persist();

		lc = new LetterCode("048", "Optout - 048", LetterType.OPTOUT_FORM);
		lc.persist();

		lc = new LetterCode("097", "Optout - 097", LetterType.OPTOUT_FORM);
		lc.persist();

		lc = new LetterCode("100", "Correspondence - 100",
				LetterType.GENERAL_CORRESPONDENCE);
		lc.persist();

		lc = new LetterCode("143", "Optout - 143", LetterType.OPTOUT_FORM);
		lc.persist();

		lc = new LetterCode("1189", "GENERALCORRESPONDENCE1189",
				LetterType.GENERAL_CORRESPONDENCE);
		lc.persist();

		lc = new LetterCode("210", "Claim Form - 210)", LetterType.CLAIM_FORM);
		lc.persist();

		lc = new LetterCode("211", "Optout - 1211", LetterType.OPTOUT_FORM);
		lc.persist();

		lc = new LetterCode("278", "Claim - 278", LetterType.CLAIM_FORM);
		lc.persist();

		lc = new LetterCode("300", "Correspondence - 1300",
				LetterType.GENERAL_CORRESPONDENCE);
		lc.persist();

		lc = new LetterCode("355", "Claim - 1355", LetterType.CLAIM_FORM);
		lc.persist();

		lc = new LetterCode("379", "Correspondence - 379",
				LetterType.GENERAL_CORRESPONDENCE);
		lc.persist();

		lc = new LetterCode("400", "Claim - 400", LetterType.CLAIM_FORM);
		lc.persist();

		lc = new LetterCode("401", "Optout - 401", LetterType.OPTOUT_FORM);
		lc.persist();

		lc = new LetterCode("456", "Correspondence - 456",
				LetterType.GENERAL_CORRESPONDENCE);
		lc.persist();

		lc = new LetterCode("499", "Claim - 499", LetterType.CLAIM_FORM);
		lc.persist();

	}

}