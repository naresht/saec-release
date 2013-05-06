package com.bfds.saec.batch.out.ifds_check_status;

import static org.fest.assertions.Assertions.assertThat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bfds.saec.batch.util.TestDataUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
public class IFDSCheckStatusQueryProviderTest {

	@Autowired
	IfdscheckStatusQueryProvider ifdscheckStatusQueryProvider;

	@PersistenceContext(unitName="entityManagerFactory")
	EntityManager entityManager;

	@org.junit.After
	public void after() {
        (new TestDataUtil()).deleteData();
	}

	@Test
	public void runIfdscheckStatusQueryProviderTest() throws Exception {
		DataGenerator
				.createTestStopChecksArePickedAfterTheyAreSentAsOutstangingItems();

		ifdscheckStatusQueryProvider.setEntityManager(entityManager);
		List<Payment> ifdsCheckFileList = ifdscheckStatusQueryProvider.createQuery().getResultList();

		assertThat(ifdsCheckFileList.size()).isEqualTo(26);
		assertThat(ifdsCheckFileList).onProperty("paymentType").containsOnly(
				PaymentType.CHECK, PaymentType.WIRE, PaymentType.ROF);
		assertThat(ifdsCheckFileList).onProperty("paymentCode").contains(
				PaymentCode.STOP_STOP_REQUESTED_S_SR,
				PaymentCode.STOP_DAMASCO_STOP_REQUESTED_P_PR,
				PaymentCode.VOID_LIFT_OUTSTANDING_VL_VL,
				PaymentCode.STOP_LIFT_OUTSTANDING_L_L,
				PaymentCode.VOID_DAMASCO_RE_ISSUE_APPROVED_I_VDA,
				PaymentCode.VOID_RPO_FORWARDABLE_RE_ISSUE_APPROVED_I_VFA,
				PaymentCode.VOID_RPO_NON_FORWARDABLE_RE_ISSUE_APPROVED_I_VNA,
				PaymentCode.VOID_HOLD_RE_ISSUE_APPROVED_I_VHA,
				PaymentCode.VOID_RE_ISSUE_APPROVED_I_VA,
				PaymentCode.VOID_VOID_TO_WIRE_CONFIRMED_VW_VW,
				PaymentCode.STOP_WIRE_STOP_TO_WIRE_CONFIRMED_SW_SW,
				PaymentCode.ROF_FULL_RESIDUAL_PROCESSED_RFR_RFR,
				PaymentCode.ROF_FULL_PROCESSED_RF_RF,
				PaymentCode.ROF_PARTIAL_RESIDUAL_PROCESSED_RPR_RPR,
				PaymentCode.ROF_PARTIAL_PROCESSED_RP_RP,
				PaymentCode.STALE_DATE_OUTSTANDING_X_X,
				PaymentCode.WIRE_REQUESTED_W_W,
				PaymentCode.WIRE_APPROVED_WA_WA,
				PaymentCode.WIRE_REJECTED_WR_WR,
				PaymentCode.STOP_REPLACE_REQUESTED_R_SRR,
				PaymentCode.STOP_STOP_REJECTED_SR_SJ,
				PaymentCode.STOP_DAMASCO_REPLACE_REQUESTED_R_PRR,
				PaymentCode.STOP_DAMASCO_STOP_REJECTED_SR_PJ,
				PaymentCode.STOP_DAMASCO_REPLACE_APPROVED_PRA,
				PaymentCode.ROF_INTEREST_PROCESSED_INT_INT);
	}
}
