package com.bfds.saec.external.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.PaymentCalc;
import com.bfds.saec.domain.reference.PaymentStatus;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentCodeUtil;
import com.bfds.saec.external.service.dto.PaymentDto;
import com.google.common.base.Preconditions;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@PersistenceContext(unitName="entityManagerFactory")
	private EntityManager entityManager;
	
	@Override
	public List<PaymentDto> findPaymentsOfClaimant(String claimantReferenceNo, PaymentSearchFilter filter) {
		Preconditions.checkArgument(StringUtils.hasText(claimantReferenceNo), "claimantReferenceNo is null or empty");
		List<PaymentDto> ret = new ArrayList<PaymentDto>();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Payment> check = cq.from(Payment.class);
		cq.select(getChecksForReleaseSelection(check, entityManager));
		filter.setClaimantReferenceNo(claimantReferenceNo);
		buildChecksForReleaseWhereClause(cb, cq, check, filter);		
		List<Tuple> results = entityManager.createQuery(cq).getResultList();
		for (Tuple t : results) {
			PaymentDto paymentDto = new PaymentDto();
			paymentDto.setCheckNo(t.get("identificatonNo", String.class));
			paymentDto.setPaymentStatus(t.get("paymentStatus", PaymentStatus.class));
			paymentDto.setPaymentType(t.get("paymentType", PaymentType.class));
			paymentDto.setPaymentDate(t.get("paymentDate", Date.class));
			paymentDto.setPaymentAmount(t.get("nettAmount", BigDecimal.class));
			ret.add(paymentDto);
		}
		return ret;
	}

	
	private <T> void buildChecksForReleaseWhereClause(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<Payment> check, PaymentSearchFilter filter) {
		final List<Predicate> expressions = new ArrayList<Predicate>();
		
		if(Boolean.TRUE.equals(filter.getReIssuable())) {			
			expressions.add(check.get("paymentCode").in(PaymentCodeUtil.getOutstandingCodes()));
		}
		if(StringUtils.hasText(filter.getClaimantReferenceNo())) {			
			expressions.add(cb.equal(check.get("payTo").get("referenceNo"), filter.getClaimantReferenceNo()));
		}
				
		if (!expressions.isEmpty()) {
			cq.where((Predicate[]) expressions.toArray(new Predicate[expressions.size()]));
		}
	}
	
	private CompoundSelection<Tuple> getChecksForReleaseSelection(Root<Payment> check, EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		return cb.tuple(check.get("identificatonNo").alias("identificatonNo"), 
				check.<PaymentType> get("paymentType").alias("paymentType"),
				check.<PaymentStatus> get("paymentStatus").alias("paymentStatus"),
				check.<Date> get("paymentDate").alias("paymentDate"),
				check.<PaymentCalc> get("paymentCalc").<BigDecimal>get("nettAmount").alias("nettAmount"));
	}

}
