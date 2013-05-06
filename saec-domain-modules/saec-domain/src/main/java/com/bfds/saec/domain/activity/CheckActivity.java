package com.bfds.saec.domain.activity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.util.PaymentStatusCodesUtil;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 public class CheckActivity extends Activity implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    private static final String ACTIVITY_TYPE_DESCRIPTION = "Payment Management";

    private String identificationNo;
	
	private PaymentType fromPaymentType;
	
	private PaymentType toPaymentType;
	
	private PaymentCode fromPaymentCode;
	
	private PaymentCode toPaymentCode;
	
	private String comments;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_fk")
	private Payment payment;

	public String getDescription() {
		String desc = super.getDescription();
		if(!StringUtils.hasText(desc)) {
			desc = generateDescription();
		}
		return desc;
	}

	private String generateDescription() {
		StringBuilder sb = new StringBuilder();
		generateDescriptionHeader(sb);
		sb.append(" ");
		generateDescriptionDetails(sb);
		return sb.toString();
	}

	private void generateDescriptionDetails(StringBuilder sb) {
		sb.append(PaymentStatusCodesUtil.getDescription(this.getToPaymentCode()));
		if(StringUtils.hasText(comments)) {
			sb.append(" - ");
			sb.append(comments);
		}
	}

	private void generateDescriptionHeader(StringBuilder sb) {
		if(this.getToPaymentType() == PaymentType.CHECK) {
			sb.append("<b>Check</b>");
		} else if(this.getToPaymentType() == PaymentType.WIRE) {
			sb.append("<b>Wire</b>");			
		} else {
			sb.append("<b>Payment</b>");
		}
		if(StringUtils.hasText(identificationNo)) {
			sb.append("#").append(identificationNo);
		}
	}	
	
	public String getActivityTypeDescription() {
		return ACTIVITY_TYPE_DESCRIPTION;
	}		
		
}
