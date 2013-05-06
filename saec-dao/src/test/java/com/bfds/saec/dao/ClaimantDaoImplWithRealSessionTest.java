package com.bfds.saec.dao;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.GroupMailCode;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchRecordDto;
import com.bfds.saec.service.ClaimantGenerator;
import com.bfds.saec.util.CsvReader.GarbageLineException;
import com.bfds.saec.util.CsvReader.NoSuchColumnException;
import com.bfds.saec.util.CsvReader.UnsupportedTypeException;
import com.bfds.saec.util.DataGenerator;

/**
 * Integration test on ClaimantDaoImpl
 */
/**
 * @author dt83395
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-dao-test.xml" })
@Transactional
public class ClaimantDaoImplWithRealSessionTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory
			.getLogger(ClaimantDaoImplWithRealSessionTest.class);

	@PersistenceContext(unitName="entityManagerFactory")
	private EntityManager entityManager;

	@Autowired
	private ClaimantDao claimantDao;
	
	@Autowired
	private ClaimantDaoImpl claimantDaoImpl;

	@Autowired
	private ClaimantGenerator claimantGenerator;

	@Autowired
	private EntityAuditDao auditDao;

	@Before
	public void onSetUp() throws Exception {
		DataGenerator.deleteData();
		DataGenerator.generateClaimantDaoImplTestData();
	}
	
	@Test
	@Ignore("Fix me: An empty criteria does not fatch anything!!!!")
	public void getAll() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		List<ClaimantSearchRecordDto> list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 3);
	}
	@Test
	public void getClaimantByReferenceNo() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setReferenceNo(Claimant.findAllClaimants().get(0)
				.getReferenceNo());
		List list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 1);
	}

	@Test
	public void testSaveAndGet() {
		Claimant claimant = claimantGenerator.getClaimant();

		// add it to a Set before saving
		Set<Claimant> set = new HashSet<Claimant>();
		set.add(claimant);

		claimant.persist();
		claimant.flush();
		assertEquals(claimant, Claimant.findClaimant(claimant.getId()));

		// clear cache to force reload from db
		entityManager.clear();

		// but since you do not use a business key, equality is lost.
		assertFalse(claimant.equals(Claimant.findClaimant(claimant.getId())));
	}
	
	private Collection<Object[]> removeDuplicateClaimants(final List<Tuple> records) {
		Map<Long, Object[]> ObjArrayMap = new LinkedHashMap<Long, Object[]>();
		
		for(final Tuple record : records) {
			final Object[] duplicate = ObjArrayMap.get((Long)record.get(0));
			if(duplicate == null) {
				ObjArrayMap.put((Long)record.get(0), record.toArray());
			}
		}
		return ObjArrayMap.values();		
	}


	@Test
	public void getClaimantsByName() throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setName("Barrington");
		List list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void theClaimantsMailingAddressMustBeReturned() throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setName("Barrington");
		List<ClaimantSearchRecordDto> list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 1);
		assertThat(list.get(0).getCity()).isEqualTo("New York");
	}

	@Test
	public void getClaimantsByAddress() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setCity("New York");
		List list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 1);
	}

	@Test
	public void getClaimantsByNameAndAddress() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setName("Dav");
		criteria.setCity("Cobles");
		List list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 1);
	}

	@Test
	public void getClaimantByTaxId() throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setTaxId("60002");
		criteria.setIdentificationType("tin");
		List list = claimantDao.getClaimantSearchResults(criteria);

		assertTrue(list.size() == 1);

		criteria = new ClaimantSearchCriteriaDto();
		criteria.setTaxId("60001");
		criteria.setIdentificationType("ssn");
		list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 0);

	}

	@Test
	public void getClaimantSearchResultsCount() {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setFundAccountNo("100001");
		Long resultCount = claimantDao.getClaimantSearchResultsCount(criteria);
		assertTrue(resultCount == 1);
	}
	
	/**
	 * Test for Issue #541, The below method is to test weather records are
	 * getting sort or Not for the name field.
	 * 
	 */

	@Test
	public void getClaimantsSortedByName() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setSortField("name");
		criteria.setSortOrder("asc");
		List<ClaimantSearchRecordDto> list = claimantDao
				.getClaimantSearchResults(criteria);
		assertThat(list.size()).isEqualTo(4);
		assertThat(list.get(0).getName()).isEqualTo("david, Gonzalez");
		assertThat(list.get(1).getName()).isEqualTo("Edward, Waldrop");
		assertThat(list.get(2).getName()).isEqualTo("Margarita, Barrington");

	}
	
	/**
	 * Test to select Claimant based upon the GroupMailCode as a search
	 * criteria.
	 */
	@Test
	public void getClaimantByGroupMailCode() {
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		String groupMailCode ="101";
		criteria.setGroupMailCode(groupMailCode);
		criteria.setCheckNo("1000010");
		criteria.setName("Margarita");
		List<ClaimantSearchRecordDto> list = claimantDao.getClaimantSearchResults(criteria);
		assertThat(list.size()).isEqualTo(1);

	}
	
	/**
	 *  Test for Issue #PII-307. i.e Not able to search Claim Identifier in ClaimantClaimId Table.   
	 *  
	 *  Test to select {@link Claimant} based on claimant claim Identifier search criteria.
	 * 	 
	 * @throws IOException
	 * @throws NoSuchColumnException
	 * @throws UnsupportedTypeException
	 * @throws GarbageLineException
	 */
	@Test
	public void getClaimantByClaimIdentifier() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setClaimIdentifier("60000001");
		
		List list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 1);
		criteria.setClaimIdentifier("60000002");
		list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 1);
		
		criteria.setClaimIdentifier("60000005");
		list = claimantDao.getClaimantSearchResults(criteria);
		assertTrue(list.size() == 0);
		
		
	}
	
	@Test
	public void getClaimantSortedByClaimIdentifier() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		
		ClaimantSearchCriteriaDto criteria = new ClaimantSearchCriteriaDto();
		criteria.setSortField("claimIdentifier");
		criteria.setSortOrder("desc");
		List<ClaimantSearchRecordDto> list = claimantDao
				.getClaimantSearchResults(criteria);
		assertThat(list.size()).isEqualTo(4);
		assertThat(list.get(0).getClaimIdentifier()).isEqualTo("60000004");
		assertThat(list.get(1).getClaimIdentifier()).isEqualTo("60000003");
		assertThat(list.get(2).getClaimIdentifier()).isEqualTo("60000002");
		assertThat(list.get(3).getClaimIdentifier()).isEqualTo("60000001");
	
	}
	

}