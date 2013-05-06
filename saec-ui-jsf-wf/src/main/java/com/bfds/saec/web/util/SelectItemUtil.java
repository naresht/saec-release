package com.bfds.saec.web.util;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.bfds.saec.domain.PaymentLetterCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bfds.saec.domain.GroupMailCode;
import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.wss.domain.reference.ProofType;
import com.bfds.wss.domain.reference.ReminderType;
import com.bfds.wss.domain.reference.SecurityFund;
import com.bfds.wss.domain.reference.SecurityRef;
import com.bfds.wss.domain.reference.TransactionType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * A Provider for {@link SelectItem}s for use in JSF views.
 *
 */
@Component("selectItemUtil")
@Scope("session") // We want to clear the caches.
public class SelectItemUtil {
	
	private Map<LetterType, List<SelectItem>> letterCodesForLetterTypes = Maps.newHashMap();
	
	/**
	 * @param letterType - The {@link LetterType} for which to fetch the {@link LetterCode}s. letterType can be null.
	 * @return - {@link List} of {@link SelectItem}s where {@link SelectItem#getLabel()} returns the text representation of the {@link LetterCode}
	 * and {@link SelectItem#getValue()} returns the {@link LetterCode}. If {@link LetterType} is null all {@link LetterCode}s are returned.
	 */
	public List<SelectItem> getLetterCodeSelectItems(final LetterType letterType) {
		if(letterType == null) {
			return java.util.Collections.emptyList();
			}
			List<SelectItem> ret = letterCodesForLetterTypes.get(letterType);
			if(ret == null) {
			ret = Lists.newArrayList();                       ;
			for(LetterCode letterCode : LetterCode.findByLetterType(letterType)) {
			ret.add(new SelectItem(letterCode, letterCode.getCode() + " - " + letterCode.getDescription()));
			}
			letterCodesForLetterTypes.put(letterType, ret);
			}
			return ret;

	}


    /**
     *  @return - {@link List} of {@link SelectItem}s where {@link SelectItem#getLabel()} returns the text representation of the {@link com.bfds.saec.domain.PaymentLetterCode}
     * and {@link SelectItem#getValue()} returns the {@link com.bfds.saec.domain.PaymentLetterCode}.
     */
    public List<SelectItem> getPaymentLetterCodeSelectItems() {
        List<SelectItem> ret = null;
        if(ret == null) {
            ret = Lists.newArrayList();
            final List<PaymentLetterCode> list = PaymentLetterCode.findAllPaymentLetterCodes();
            for(PaymentLetterCode paymentLetterCode : list) {
                ret.add(new SelectItem(paymentLetterCode, paymentLetterCode.getCode()));
            }
        }
        return ret;
    }
	
	/**
	 *  @return - {@link List} of {@link SelectItem}s where {@link SelectItem#getLabel()} returns the text representation of the {@link GroupMailCode}
	 * and {@link SelectItem#getValue()} returns the {@link GroupMailCode}. 
	 */
	public List<SelectItem> getGroupMailCodeSelectItems() {
		List<SelectItem> ret = null;
		if(ret == null) {
			ret = Lists.newArrayList();
			final List<GroupMailCode> list = GroupMailCode.findAllGroupMailCodes() ;
			for(GroupMailCode groupMailCode : list) {
				ret.add(new SelectItem(groupMailCode, groupMailCode.getCode()));
			}
		}
		return ret;
	}
	
	public List<SelectItem> getReminderTypeSelectItems() {
		List<SelectItem> ret = null;
		if(ret == null) {
			ret = Lists.newArrayList();
			final List<ReminderType> list = ReminderType.findAllReminderTypes() ;
			for(ReminderType reminderType : list) {
				ret.add(new SelectItem(reminderType, reminderType.getDescription()));
			}
		}
		return ret;
	}
	
	/**
	 * @return - {@link List} of {@link SelectItem}s where {@link SelectItem#getLabel()} returns the text representation of the {@link ProofType}
	 * and {@link SelectItem#getValue()} returns the {@link ProofType}. 
	 */
	public List<SelectItem> getAllProofTypeSelectItems() {
		List<SelectItem> ret = null;
		if(ret == null) {
			ret = Lists.newArrayList();
			final List<ProofType> list = ProofType.findAllProofTypes();
			for(ProofType proofType : list) {
				ret.add(new SelectItem(proofType, proofType.getCode()));
			}
		}
		return ret;
	}
	
	
	/**
	 *@return - {@link List} of {@link SelectItem}s where {@link SelectItem#getLabel()} returns the text representation of the {@link SecurityFund} fund
	 *{@link SecurityRef} Name , Ticker and Cusip
	 * and {@link SelectItem#getValue()} returns the {@link SecurityFund}. 
	 */
	public List<SelectItem> getAllSecurityFundAssetsSelectItems() {
		List<SelectItem> ret = null;
		if(ret == null) {
			ret = Lists.newArrayList();
			final List<SecurityFund> list = SecurityFund.findAllSecurityFunds();
			for(SecurityFund securityFund : list) {
				ret.add(new SelectItem(securityFund, securityFund.getSecurityRef().getName()+" "+securityFund.getFund()+" " 
						+securityFund.getSecurityRef().getTicker()+" "+securityFund.getSecurityRef().getCusip()));
			}
		}
		return ret;
	}
	
	
	/**
	 * @return - {@link List} of {@link SelectItem}s where {@link SelectItem#getLabel()} returns the text representation of the {@link TransactionType}
	 * and {@link SelectItem#getValue()} returns the {@link TransactionType}. 
	 */
	public List<SelectItem> getTransactionTypeSelectItems() {
		List<SelectItem> ret = null;
		if(ret == null) {
			ret = Lists.newArrayList();
			final List<TransactionType> list = TransactionType.findAllTransactionTypes();
			for(TransactionType transType : list) {
				ret.add(new SelectItem(transType,transType.getCode()));
			}
		}
		return ret;
	}
	
	
}
