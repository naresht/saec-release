package com.bfds.saec.dao;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.CheckSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.util.CsvReader.GarbageLineException;
import com.bfds.saec.util.CsvReader.NoSuchColumnException;
import com.bfds.saec.util.CsvReader.UnsupportedTypeException;
import com.bfds.saec.util.DataGenerator;
import org.fest.assertions.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-dao-test.xml" })
@Transactional
public class PaymentDaoImplWithRealSessionTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory
			.getLogger(ClaimantDaoImplWithRealSessionTest.class);

	@Autowired
	private PaymentDao paymentDao;

	@Before
	public void onSetUp() throws Exception {
		DataGenerator.deleteData();
	}

	@Test
	public void getAllChecksForReleaseWithNoSearchCriteria()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllWiresForReleaseWithNoSearchCriteria()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplWireData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.WIRE);
		List<CheckSearchRecordDto> list = paymentDao.getChecksForRelease(criteria);
		assertThat(list.size()).isEqualTo(1);
		assertThat(list.get(0).getCheckNo()).isEqualTo("100001");
		assertThat(list.get(0).getPaymentCode()).isEqualTo(PaymentCode.WIRE_REQUESTED_W_W);	
	}
	
	@Test
	@ExpectedException(IllegalArgumentException.class)
	public void getAllChecksForReleaseWithInvalidSearchCriteria()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setFromAmount(100d);
		criteria.setToAmount(50d);
		criteria.setPaymentType(PaymentType.CHECK);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithOrderByCriteria_1()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_NAME);
		criteria.setPaymentType(PaymentType.CHECK);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithOrderByCriteria_2()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_ADDRESS);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithDollarRangerCriteria_1()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromAmount(10d);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithDollarRangerCriteria_2()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setToAmount(10d);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithDollarRangerCriteria_3()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromAmount(10d);
		criteria.setToAmount(50d);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithDateRangerCriteria_1()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromDate(new Date());
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithDateRangerCriteria_2()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setToDate(new Date());
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getAllChecksForReleaseWithDateRangerCriteria_3()
			throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromDate(new Date(2011, 7, 1));
		criteria.setToDate(new Date(2011, 7, 10));
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void getCheckCountForRelease() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		Long ret = paymentDao.getCheckCountForRelease(criteria);
		assertThat(ret).isNotNull();
	}

	//@Test
	public void executeManualRelease() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		List<CheckSearchRecordDto> list = new ArrayList<CheckSearchRecordDto>();
		CheckSearchRecordDto vo = new CheckSearchRecordDto();
		vo.setApproved(true);
		vo.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		vo.setId(Claimant.findAllClaimants().get(0).getPayments().iterator()
				.next().getId());
		
		list.add(vo);
		paymentDao.processManualRelease(list, criteria.getPaymentType());
	}

	@Test
	public void executeManualReleaseCheckUpdateTest() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		DataGenerator.paymentDaoProcessManualChecksReleaseTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		
		List<CheckSearchRecordDto> list = new ArrayList<CheckSearchRecordDto>();
		
		CheckSearchRecordDto vo = new CheckSearchRecordDto();		
		vo.setId(Payment.findCheckByNumberAndAmount("100111", 100).getId());
		vo.setComments("Test123");
		vo.setApproved(true);
		vo.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		list.add(vo);
		
		vo = new CheckSearchRecordDto();		
		vo.setId(Payment.findCheckByNumberAndAmount("100112", 200).getId());
		vo.setRejected(true);
		vo.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		vo.setRejectReasonCode("NameCorrectionNeeded");
		vo.setComments("Test123");
		list.add(vo);
		
		vo = new CheckSearchRecordDto();		
		vo.setId(Payment.findCheckByNumberAndAmount("100113", 300).getId());
		vo.setRejected(true);
		vo.setPaymentCode(PaymentCode.VOID_RE_ISSUE_REQUESTED_I_VR);
		vo.setRejectReasonCode("NameCorrectionNeeded");
		list.add(vo);
//		
		paymentDao.processManualRelease(list, criteria.getPaymentType());
		(Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI)).flush();
		(Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI)).clear();
		
		Payment p = Payment.findCheckByNumberAndAmount("100111", 100);	
		assertThat(p.getPaymentStatus()).isEqualTo(PaymentStatus.REISSUE_APPROVED);
		assertThat(p.getPaymentCode()).isEqualTo(PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA);
		assertThat(p.getComments()).isEqualTo("Test123");
		
		p = Payment.findCheckByNumberAndAmount("100112", 200);		
		assertThat(p.getComments()).isEqualTo("Test123");
		
		p = Payment.findCheckByNumberAndAmount("100113", 300);		
		assertThat(p.getComments()).isEqualTo("NameCorrectionNeeded");
	}

	@Test
	public void executeManualWireRelease() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		DataGenerator.deleteData();
		DataGenerator.paymentDaoProcessManualWireReleaseTestData();
		final Claimant c = Claimant.findAllClaimants().get(0);
		List<Payment> wires = new ArrayList<Payment>(c.getPayments());
		Payment w1 = wires.get(0);
		Payment w2 = wires.get(1);
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.WIRE);
		
		List<CheckSearchRecordDto> list = new ArrayList<CheckSearchRecordDto>();
		
		CheckSearchRecordDto vo = new CheckSearchRecordDto();		
		vo.setId(w1.getId());
		vo.setComments("Test123");
		vo.setApproved(true);
		vo.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
		list.add(vo);
		
		vo = new CheckSearchRecordDto();		
		vo.setId(w2.getId());
		vo.setRejected(true);
		vo.setPaymentCode(PaymentCode.WIRE_REQUESTED_W_W);
		vo.setRejectReasonCode("NameCorrectionNeeded");
		vo.setComments("Test123");
		list.add(vo);
			
		paymentDao.processManualRelease(list, criteria.getPaymentType());
		(new Payment()).flush();
		(new Payment()).clear();
		
		w1 = Payment.findPayment(w1.getId());
		w2 = Payment.findPayment(w2.getId());
		assertThat(w1.getPaymentStatus()).isEqualTo(PaymentStatus.WIRE_APPROVED);
		assertThat(w1.getPaymentCode()).isEqualTo(PaymentCode.WIRE_APPROVED_WA_WA);
		assertThat(w1.getComments()).isEqualTo("Test123");
		
		assertThat(w2.getPaymentStatus()).isEqualTo(PaymentStatus.WIRE_REJECTED);
		assertThat(w2.getPaymentCode()).isEqualTo(PaymentCode.WIRE_REJECTED_WR_WR);
		assertThat(w2.getComments()).isEqualTo("Test123");
		
		
	}
	
	@Test
	public void executeBatchRelease() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromAmount(10d);
		criteria.setToAmount(50d);
		List<Long> excludes = new ArrayList<Long>();
		excludes.add(1L);
		excludes.add(2L);
		paymentDao.processBulkRelease(criteria, excludes, null);
	}

	@Test
	public void getAllChecksForReleaseComplexSearch() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		DataGenerator.generatePaymentDaoImplTestData();
		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.CHECK);
		criteria.setFromAmount(10d);
		criteria.setToAmount(50d);
		criteria.setFromDate(new Date(2011, 7, 1));
		criteria.setToDate(new Date(2011, 7, 10));
		Map<String, String> filters = new HashMap<String, String>();
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_REFERENCE_NO, "1");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_NAME, "A");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_ADDRESS, "A");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_PAYMENT_AMOUNT, "10");
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_CHECK_NO, "1");
		// 101, 7, 14
		filters.put(CheckReleaseSearchCriteriaDto.COLUMN_STATUS_DATE,
				"08/14/2001");
		criteria.setFilters(filters);
		criteria.setSortField(CheckReleaseSearchCriteriaDto.COLUMN_ADDRESS);
		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
	}

	@Test
	public void loadPaymentsForReturnOfFundingByPaymentNumber() {
		DataGenerator.generatePaymentDaoImplTestData();
		Claimant claimant = Claimant.findAllClaimants().get(0);
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CASHED_C_FIC);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setIdentificatonNo("TEST-ROF-1234568");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();
		List<Payment> checks = paymentDao.loadPaymentsForReturnOfFunding(null,
				"TEST-ROF-1234568");
		assertThat(checks.size()).isGreaterThan(0);
		// Only Cashed checks are candidates for ROF.
		assertThat(checks).onProperty("paymentCode").containsOnly(
				PaymentCode.FIRST_ISSUE_CASHED_C_FIC);

	}

	@Test
	public void loadPaymentsForReturnOfFundingByReferenceNumber() {
		DataGenerator.generatePaymentDaoImplTestData();
		Claimant claimant = Claimant.findAllClaimants().get(0);
		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setPaymentCode(PaymentCode.ISSUANCE_CASHED_C_ISC);
		c.setIdentificatonNo("TEST-ROF-1234568");
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		claimant.addCheck(c);
		claimant.persist();
		claimant.flush();
		List<Payment> checks = paymentDao.loadPaymentsForReturnOfFunding(
				claimant.getReferenceNo(), "TEST-ROF-1234568fdg");
		assertThat(checks.size()).isGreaterThan(0);
		// Only Cashed checks are candidates for ROF.
		assertThat(checks).onProperty("paymentCode").is(new Condition<List<?>>() {
			
			@Override
			public boolean matches(List<?> value) {
				for(Object pc : value) {
				if(! PaymentCodeUtil.getCashedCodes().contains(pc)) {
					return false;
				}
				}
				return true;
			}
		});
	}

	@Test
	public void loadChecksForVoidReversal() {
		DataGenerator.generateStopVoidReversalTestData();
		assertThat(paymentDao.getCheckForStopVoidReversal("1001")).isNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1002")).isNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1003")).isNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1010")).isNull();

		//assertThat(paymentDao.getCheckForStopVoidReversal("1004")).isNotNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1005")).isNotNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1006")).isNotNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1007")).isNotNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1008")).isNotNull();
		assertThat(paymentDao.getCheckForStopVoidReversal("1009")).isNotNull();
	}

	@Test
	public void getCheckCoutWithPaymentStatus() {
		DataGenerator.generatePaymentDaoImplTestData();
		assertThat(paymentDao
				.getOutstandingCheckCount() > 0);
	}

	/**
	 * Updated test-method to insure GitHub Issue #729,Enhancement: user is able
	 * to lift the stop on the Stop Confirmed check
	 */
	@Test
	public void getCheckForStopLift() {
		DataGenerator.generateCheckForStopLiftData();
		assertThat(paymentDao.getCheckForStopLift("11111111")).isNotNull();
		assertThat(paymentDao.getCheckForStopLift("11111112")).isNotNull();
		assertThat(paymentDao.getCheckForStopLift("11111113")).isNotNull();

	}
	
	/**
	 * Test for issue #705.The newly created wire objects are not getting picked
	 * for Wire Release.
	 */
	@Test
	public void loadWiresCreatedForPayementReIssue() {

		DataGenerator.createWireToRelease();

		CheckReleaseSearchCriteriaDto criteria = new CheckReleaseSearchCriteriaDto();
		criteria.setPaymentType(PaymentType.WIRE);

		List<CheckSearchRecordDto> list = paymentDao
				.getChecksForRelease(criteria);
		assertThat(list).isNotNull();
		assertThat(list.size()).isEqualTo(2);

	}
}
