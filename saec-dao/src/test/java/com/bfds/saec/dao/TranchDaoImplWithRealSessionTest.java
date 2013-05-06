package com.bfds.saec.dao;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.Tranch;
import com.bfds.saec.domain.dto.TranchAssignmentSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.util.CsvReader.GarbageLineException;
import com.bfds.saec.util.CsvReader.NoSuchColumnException;
import com.bfds.saec.util.CsvReader.UnsupportedTypeException;
import com.bfds.saec.util.DataGenerator;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-dao-test.xml" })

public class TranchDaoImplWithRealSessionTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory
			.getLogger(ClaimantDaoImplWithRealSessionTest.class);

	@Autowired
	private TranchDao tranchDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

	@Before
	public void onSetUp() throws Exception {
		DataGenerator.deleteData();
	}

	@Test
    @Transactional
	public void getAllChecksForTranchWithNoSearchCriteria() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
				
		assertThat(list).onProperty("checkNo").containsOnly("1000010", "1000021", "2000010", "2000021");
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchWithOrderByCriteria() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_PAYMENT_AMOUNT);
		criteria.setPaymentType(PaymentType.CHECK);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("checkNo").containsExactly("1000010", "2000010", "1000021", "2000021");
	}


	@Test
    @Transactional
	public void getAllChecksForTranchWithDollarRangerCriteria() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromAmount(100d);
		criteria.setToAmount(150d);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("checkNo").containsExactly("1000010", "2000010");
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchWithCheckNumberRangerCriteria() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromCheckNo("1000010");
		criteria.setToCheckNo("1000010");
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("checkNo").containsExactly("1000010");
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchWithAccountNumberRangerCriteria() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		final String accountNo = Claimant.findAllClaimants().get(0).getReferenceNo();
		criteria.setFromAccountNo(accountNo);
		criteria.setToAccountNo(accountNo);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("referenceNo").containsOnly(String.valueOf(accountNo));
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchByState() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setStateCode(new String[]{"MI"});
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("checkNo").containsOnly("1000010", "1000021");
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchByAccountNo() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		final String accountNo = Claimant.findAllClaimants().get(0).getReferenceNo();
		criteria.setAccountNo(accountNo);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("referenceNo").containsOnly(String.valueOf(accountNo));
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchByBrokerId() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		
		criteria.setBrokerId("30001");
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("checkNo").containsOnly("1000010", "1000021");
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchWithManualExcludes() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		final Long paymentId = Payment.findCheckByNumberAndAmount("1000010", 100d).getId();
		criteria.getExcludes().add(paymentId);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("checkNo").satisfies(new org.fest.assertions.Condition<List<?>>() {
			@Override
			public boolean matches(List<?> value) {
				final Set set = Sets.newHashSet();
				set.addAll(value);
				return !set.contains("1000010");
			}			
		});
	}
	
	@Test
    @Transactional
	public void getAllChecksForTranchAllExcludedAndManualIncludes() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		final Long paymentId = Payment.findCheckByNumberAndAmount("1000010", 100d).getId();
		criteria.getIncludes().add(paymentId);
		criteria.setExcludeAll(true);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).onProperty("checkNo").containsOnly("1000010");
	}

	@Test
    @Transactional
	public void getAllChecksForTranchWithComplexSearch() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromAmount(10d);
		criteria.setToAmount(50d);
		
		Map<String, String> filters = new HashMap<String, String>();
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_REFERENCE_NO, "1");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_NAME, "A");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_ADDRESS, "A");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_PAYMENT_AMOUNT, "10");
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_CHECK_NO, "1");

		criteria.setFilters(filters);
		criteria.setSortField(TranchAssignmentSearchCriteriaDto.COLUMN_ADDRESS);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
		assertThat(list).isNotNull();
	}
	
	@Test
    @Transactional
	public void getPaymentCountForTranchAssignment() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		Long ret = tranchDao.getPaymentCountForTranchAssignment(criteria);
		assertThat(ret).isEqualTo(4);
	}
	
	@Test
    @Transactional
	public void getpaymentDistributionTotalForTranchAssignment() {
		DataGenerator.generateTranchDaoImplTestData();
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		BigDecimal ret = tranchDao.getpaymentDistributionTotalForTranchAssignment(criteria);
		assertThat(ret.doubleValue()).isEqualTo(630.00D);
	}
	
	@Test
	public void executePaymentTranchAssignment() {
        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        final Tranch tranch = tt.execute(new TransactionCallback<Tranch>() {
            @Override
            public Tranch doInTransaction(TransactionStatus status) {
                DataGenerator.generateTranchDaoImplTestData();
                Tranch tranch = new Tranch();
                tranch.setCode("Tranch-001");
                tranch = tranch.merge();
                tranch.flush();
                tranch.clear();
                return tranch;
            }
        });

		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
				
		assertThat(list).onProperty("checkNo").containsOnly("1000010", "1000021", "2000010", "2000021");
		

		tranchDao.executePaymentTranchAssignmentAsync(null, tranch, criteria);

		list = tranchDao.getpaymentsForTranchAssignment(criteria);
		
		assertThat(list).isEmpty();
		tt.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus status) {
            	List<Payment> tranchPaymentList = Payment.findAllPaymentsAssignedToTranch(tranch.getCode());
		
            	assertThat(tranchPaymentList).onProperty("identificatonNo").containsOnly("1000010", "1000021", "2000010", "2000021");
            	assertThat(tranchPaymentList).onProperty("tranch").onProperty("code").containsOnly("Tranch-001");
            	return null;
            }
        });
		DataGenerator.deleteData();
	}
	
	@Test
	public void executePaymentTranchAssignmentWithManualExcludes() {
        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        final Tranch tranch = tt.execute(new TransactionCallback<Tranch>() {
            @Override
            public Tranch doInTransaction(TransactionStatus status) {
                DataGenerator.generateTranchDaoImplTestData();
                Tranch tranch = new Tranch();
                tranch.setCode("Tranch-002");
                tranch = tranch.merge();
                tranch.flush();
                tranch.clear();
                return tranch;
            }
        });
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
				
		assertThat(list).onProperty("checkNo").containsOnly("1000010", "1000021", "2000010", "2000021");
		

		criteria.getExcludes().add(Payment.findPaymentIdentificationNo("1000010").getId());
		tranchDao.executePaymentTranchAssignmentAsync(null, tranch.merge(), criteria);

		
		criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		list = tranchDao.getpaymentsForTranchAssignment(criteria);		
		assertThat(list).onProperty("checkNo").containsOnly("1000010");
		
		tt.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus status) {		
            	List<Payment> tranchPaymentList = Payment.findAllPaymentsAssignedToTranch(tranch.getCode());
		
            	assertThat(tranchPaymentList).onProperty("identificatonNo").containsOnly("1000021", "2000010", "2000021");		
            	assertThat(tranchPaymentList).onProperty("tranch.code").containsOnly("Tranch-002");
            	return null;
            }
        });
		
	}
	
	@Test
	public void checkTranchLocking() {
        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        final Tranch tranch = tt.execute(new TransactionCallback<Tranch>() {
            @Override
            public Tranch doInTransaction(TransactionStatus status) {
                DataGenerator.generateTranchDaoImplTestData();
                Tranch tranch = new Tranch();
                tranch.setCode("Tranch-004");
                tranch = tranch.merge();
                tranch.flush();
                tranch.clear();
                return tranch;
            }
        });
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		Map<String, String> filters = new HashMap<String, String>();
		filters.put(TranchAssignmentSearchCriteriaDto.COLUMN_CHECK_NO, "1000010");

		criteria.setFilters(filters);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
				
		assertThat(list).onProperty("checkNo").containsOnly("1000010");

		tranchDao.executePaymentTranchAssignmentAsync(null, tranch.merge(), criteria);
		
		assertThat(Tranch.anyTranchAssignmentInProgress()).isFalse();		
		
		
	}
	
	@Test
	public void executePaymentTranchAssignmentWithManualIncludes() {
        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        final Tranch tranch = tt.execute(new TransactionCallback<Tranch>() {
            @Override
            public Tranch doInTransaction(TransactionStatus status) {
                DataGenerator.generateTranchDaoImplTestData();
                Tranch tranch = new Tranch();
                tranch.setCode("Tranch-003");
                tranch = tranch.merge();
                tranch.flush();
                tranch.clear();
                return tranch;
            }
        });
		TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
		criteria.setPaymentType(PaymentType.CHECK);
		List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);
				
		assertThat(list).onProperty("checkNo").containsOnly("1000010", "1000021", "2000010", "2000021");

		criteria.setExcludeAll(true);
		criteria.getIncludes().add(Payment.findPaymentIdentificationNo("1000010").getId());
		tranchDao.executePaymentTranchAssignmentAsync(null, tranch.merge(), criteria);
		
		
		tt.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus status) {
            	TranchAssignmentSearchCriteriaDto criteria = new TranchAssignmentSearchCriteriaDto("add");
        		criteria.setPaymentType(PaymentType.CHECK);
            	List<TranchAssignmentSearchRecordDto> list = tranchDao.getpaymentsForTranchAssignment(criteria);		
				assertThat(list).onProperty("checkNo").containsOnly("1000021", "2000010", "2000021");
				
				List<Payment> tranchPaymentList = Payment.findAllPaymentsAssignedToTranch(tranch.getCode());
				
				assertThat(tranchPaymentList).onProperty("identificatonNo").containsOnly("1000010");
				assertThat(tranchPaymentList).onProperty("tranch.code").containsOnly("Tranch-003");
		    	return null;
		    }
		});
	}
	
}
