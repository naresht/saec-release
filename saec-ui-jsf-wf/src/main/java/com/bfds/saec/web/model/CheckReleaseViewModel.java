package com.bfds.saec.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.component.dialog.Dialog;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.dao.PaymentDaoImpl;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.CheckSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.ReleaseRejectResponseCode;
import com.bfds.saec.web.ui.components.BaseLazyDataModel;
import com.bfds.saec.web.util.JsfUtils;

public class CheckReleaseViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TITLE_MANUAL_CHECK_RELEASE = "Check Release - Manual";
	private static final String TITLE_BULK_CHECK_RELEASE = "Check Release - Bulk";
	private static final String TITLE_MANUAL_WIRE_RELEASE = "Wire Release";
	private static final String CHECK_RELEASE_SUCCESS_MSG = "Selected checks processed.";
	private static final String WIRE_RELEASE_SUCCESS_MSG = "Selected wires processed.";
	private LazyDataModel<CheckSearchRecordDto> checkList;
	private List<CheckSearchRecordDto> currentPageList = new ArrayList<CheckSearchRecordDto>();
	/*
	 * 
	 */
	private Map<Long, CheckSearchRecordDto> releasedRecordsMap = new HashMap<Long, CheckSearchRecordDto>();
	private CheckReleaseSearchCriteriaDto prevCriteria;
	@Autowired
	private transient PaymentDao paymentDao = new PaymentDaoImpl();
	
	private boolean includeAllOnPage;

	CheckReleaseSearchCriteriaDto criteria;

	public LazyDataModel<CheckSearchRecordDto> loadChecksForRelease(
			final CheckReleaseSearchCriteriaDto criteria,
			final MessageContext messageContext) {
		checkList = new BaseLazyDataModel<CheckSearchRecordDto>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<CheckSearchRecordDto> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				criteria.setFirstResult(first);
				criteria.setMaxResults(pageSize);
				criteria.setSortField(sortField);
				criteria.setSortOrder(SortOrder.ASCENDING ==  sortOrder ? CheckReleaseSearchCriteriaDto.SORT_ORDER_ASC
						: CheckReleaseSearchCriteriaDto.SORT_ORDER_DESC);
				criteria.setFilters(filters);
				if (!criteria.equals(prevCriteria) || currentPageList.isEmpty()) {
					prevCriteria = criteria.clone();
					updateReleasedRecordsMap(criteria.isBulk(), criteria.isExcludeAllByDefault());
					currentPageList = paymentDao.getChecksForRelease(criteria);
					updateCurrentPageList();
				}
				return currentPageList;
			}
		};

		checkList.setRowCount((int) paymentDao
				.getCheckCountForRelease(criteria).longValue());
		return checkList;
	}

	public void validateCheckRelease(ComponentSystemEvent event) {

		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();

		UIInput fromDate = (UIInput) components.findComponent("fromDate");
		UIInput toDate = (UIInput) components.findComponent("toDate");

		UIInput fromAmount = (UIInput) components.findComponent("fromAmount");
		UIInput toAmount = (UIInput) components.findComponent("toAmount");

		Date frmDate = (Date) fromDate.getLocalValue();
		Date toDt = (Date) toDate.getLocalValue();
		Number frmAmount = (Number) fromAmount.getLocalValue();
		Number tAmount = (Number) toAmount.getLocalValue();
		StringBuilder errors = new StringBuilder();
		if (frmDate != null && toDt != null && frmDate.compareTo(toDt) > 0) {
			errors.append("To date cannot be less than from date");
		}

		if (frmAmount != null && tAmount != null
				&& (frmAmount.doubleValue() > tAmount.doubleValue())) {
			errors.append("<br/>To Amount cannot be less than from Amount");
		}
		if (errors.length() > 0) {
			FacesMessage msg = new FacesMessage(errors.toString());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(components.getClientId(), msg);
			fc.renderResponse();
		}

	}

	private void updateCurrentPageList() {
		for (CheckSearchRecordDto vo : currentPageList) {
			CheckSearchRecordDto cacheVo = releasedRecordsMap.get(vo.getId());
			if (cacheVo != null) {
				vo.setApproved(cacheVo.isApproved());
				vo.setRejected(cacheVo.isRejected());
				vo.setComments(cacheVo.getComments());
				vo.setRejectReasonCode(cacheVo.getRejectReasonCode());
			}
		}
	}

	private void updateReleasedRecordsMap(final boolean isBulk, final boolean excludeAllByDefault) {
		for (CheckSearchRecordDto vo : currentPageList) {
			if (isBulk && !excludeAllByDefault) {
				if (!vo.isApproved()) {
					releasedRecordsMap.put(vo.getId(), vo);
				} else {
					releasedRecordsMap.remove(vo.getId());
				}

			} else {
				if (vo.isApproved() || vo.isRejected()) {
					releasedRecordsMap.put(vo.getId(), vo);
				} else {
					releasedRecordsMap.remove(vo.getId());
				}
			}
		}
	}

	public LazyDataModel<CheckSearchRecordDto> getCheckList() {
		return checkList;
	}

	public void selectCheckForApproval(final Long id) {
		resetPopups();
		for (Iterator<CheckSearchRecordDto> itr = currentPageList.iterator(); itr
				.hasNext();) {
			CheckSearchRecordDto checkSearchRecordVo = itr.next();
			if (checkSearchRecordVo.getId().equals(id)) {
				if (checkSearchRecordVo.isApproved()) {
					checkSearchRecordVo.setApproved(false);
				} else {
					checkSearchRecordVo.setRejected(false);
					checkSearchRecordVo.setApproved(true);
					checkSearchRecordVo.setRejectReasonCode(null);
				}
			}
		}
	}

	public void selectCheckForRejection(final Long id) {
		resetPopups();
		for (Iterator<CheckSearchRecordDto> itr = currentPageList.iterator(); itr
				.hasNext();) {
			CheckSearchRecordDto checkSearchRecordVo = itr.next();
			if (checkSearchRecordVo.getId().equals(id)) {
				if (checkSearchRecordVo.isRejected()) {
					checkSearchRecordVo.setRejected(false);
					checkSearchRecordVo.setRejectReasonCode(null);
				} else {
					checkSearchRecordVo.setApproved(false);
					checkSearchRecordVo.setRejected(true);
				}
			}
		}

	}
	
	public boolean validateSelectedCheck()
	{
		resetPopups();
		resetPopups();
		int approveCount = 0;
		int rejectCount = 0;
		int count = 0;
		Dialog approveCheckDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "approveCheckDialog");
		Dialog rejectCheckDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "rejectCheckDialog");
		Dialog approveRejectCheckDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "approveRejectCheckDialog");
		for (CheckSearchRecordDto check : currentPageList) {
			if(check.isApproved()){
				approveCount++;
				count++;
			}else if(check.isRejected()){
				rejectCount++;
				count++;
			}
		}
		for (CheckSearchRecordDto check : currentPageList) {
			if(count!=approveCount && count!=rejectCount){
				approveRejectCheckDialog.setVisible(true);
				return false;
			}
			else if( check.isApproved() ) { 				
				approveCheckDialog.setVisible(true);
				return false;
			}else if( check.isRejected() ){
				rejectCheckDialog.setVisible(true);
				return false;
			}
		}
		return true;
	}

	public boolean saveManual(final MessageContext messageContext,
			final CheckReleaseSearchCriteriaDto criteria,
			final PaymentDao paymentDao) {
		updateReleasedRecordsMap(false, false);
		if (releasedRecordsMap.size() == 0) {
			messageContext.addMessage(new MessageBuilder().info()
					.defaultText("Select one or more Items.").build());
			return false;
		}
		final List<CheckSearchRecordDto> list = new ArrayList<CheckSearchRecordDto>();

		for (CheckSearchRecordDto checkSearchRecordVo : releasedRecordsMap
				.values()) {
			if (!validCheckManualSave(checkSearchRecordVo, criteria,
					messageContext)) {
			} else if (checkSearchRecordVo.isApproved()
					|| checkSearchRecordVo.isRejected()) {
				list.add(checkSearchRecordVo);
			}
		}
		if (list.size() > 0) {
			paymentDao.processManualRelease(list, criteria.getPaymentType());
			if (criteria.getPaymentType() == PaymentType.CHECK) {
				messageContext.addMessage(new MessageBuilder().info()
						.defaultText(CHECK_RELEASE_SUCCESS_MSG).build());
			} else {
				messageContext.addMessage(new MessageBuilder().info()
						.defaultText(WIRE_RELEASE_SUCCESS_MSG).build());
			}
		}
		clearCurrentPage();
		return true;
	}

	public void clearCurrentPage() {
		currentPageList.clear();
		releasedRecordsMap.clear();
	}

	public boolean saveBulk(final CheckReleaseSearchCriteriaDto criteria,
			final MessageContext messageContext, final PaymentDao paymentDao) {
		updateReleasedRecordsMap(criteria.isBulk(), criteria.isExcludeAllByDefault());
		if(!criteria.isExcludeAllByDefault()) {
			paymentDao.processBulkRelease(criteria, releasedRecordsMap.keySet(), null);
		} else {
			final List<CheckSearchRecordDto> list = new ArrayList<CheckSearchRecordDto>();
			list.addAll(this.releasedRecordsMap.values());
			paymentDao.processManualRelease(list, criteria.getPaymentType());
		}
		
		if (criteria.getPaymentType() == PaymentType.CHECK) {
			messageContext.addMessage(new MessageBuilder().info()
					.defaultText(CHECK_RELEASE_SUCCESS_MSG).build());
		} else {
			messageContext.addMessage(new MessageBuilder().info()
					.defaultText(WIRE_RELEASE_SUCCESS_MSG).build());
		}
		clearCurrentPage();
		return true;
	}

	private boolean validCheckManualSave(
			final CheckSearchRecordDto checkSearchRecordVo,
			final CheckReleaseSearchCriteriaDto criteria,
			final MessageContext messageContext) {
		final StringBuilder sb = new StringBuilder();
		if (checkSearchRecordVo.isRejected()
				&& !(StringUtils.hasText(checkSearchRecordVo.getComments()) && StringUtils
						.hasText(checkSearchRecordVo.getRejectReasonCode()))) {
			if (criteria.getPaymentType() == PaymentType.CHECK) {
				sb.append("Please enter comments/reason for Check:").append(
						checkSearchRecordVo.getCheckNo());
			} else {
				sb.append(
						"Please enter comments/reason for Wrie with Reference#:")
						.append(checkSearchRecordVo.getReferenceNo());
			}
		}
		return true;  
	}

	public void clearAllManualSections() {
		resetPopups();
		for (CheckSearchRecordDto checkSearchRecordVo : currentPageList) {
			checkSearchRecordVo.setApproved(false);
			checkSearchRecordVo.setRejected(false);
			checkSearchRecordVo.setRejectReasonCode(null);
			checkSearchRecordVo.setComments(null);
		}
	}

	public void clearBulkSections() {
		for (CheckSearchRecordDto checkSearchRecordVo : currentPageList) {
			checkSearchRecordVo.setApproved(false);
		}
	}

	public void clearSearchForm(CheckReleaseSearchCriteriaDto criteria) {
		JsfUtils.clearValues(JsfUtils.getUIViewRoot(), "fromAmount",
				"toAmount", "fromDate", "toDate");
		criteria.setFromAmount(0d);
		criteria.setToAmount(0d);
		criteria.setFromDate(null);
		criteria.setToDate(null);
	}

	public PaymentDao getPaymentDao() {
		return paymentDao;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public List<CheckSearchRecordDto> getCurrentPageList() {
		return currentPageList;
	}

	public void setCurrentPageList(List<CheckSearchRecordDto> currentPageList) {
		this.currentPageList = currentPageList;
	}

	public void setSelectedItem(CheckSearchRecordDto selectedCar) {
		UIViewRoot v = FacesContext.getCurrentInstance().getViewRoot();
		UIComponent summaryPanel = findComponent(v, "summary_c");
		summaryPanel.setRendered(true);
	}

	public static UIComponent findComponent(UIComponent c, String id) {
		if (id.equals(c.getId())) {
			return c;
		}
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public String getFormTitle(CheckReleaseSearchCriteriaDto criteria) {
		if (criteria.getPaymentType() == PaymentType.CHECK) {
			if (criteria.isBulk()) {
				return TITLE_BULK_CHECK_RELEASE;
			} else {
				return TITLE_MANUAL_CHECK_RELEASE;
			}
		} else if (criteria.getPaymentType() == PaymentType.WIRE) {
			return TITLE_MANUAL_WIRE_RELEASE;
		}
		return null;
	}

	public String getNameCorrectionNeeded() {
		return ReleaseRejectResponseCode.NAME_CORRECTION_NEEDED.toString();
	}

	public String getAddressCorrectionNeeded() {
		return ReleaseRejectResponseCode.ADDRESS_CORRECTION_NEEDED.toString();
	}

	public String getCheckReissuedInError() {
		return ReleaseRejectResponseCode.CHECK_REISSUED_IN_ERROR.toString();
	}
	
	private void resetPopups()
	{
		Dialog approveCheckDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "approveCheckDialog");
		Dialog rejectCheckDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "rejectCheckDialog");
		Dialog approveRejectCheckDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "approveRejectCheckDialog");
		if (approveCheckDialog != null)
			approveCheckDialog.setVisible(false);
		if (rejectCheckDialog != null)
			rejectCheckDialog.setVisible(false);
		if (approveRejectCheckDialog != null)
			approveRejectCheckDialog.setVisible(false);
	}
	
	public void togglePaymentsForbulkApproval(final boolean val){
		for(final CheckSearchRecordDto checkSearchRecordDto : currentPageList) {
			checkSearchRecordDto.setApproved(val);
		}
	}

	public boolean isIncludeAllOnPage() {
		return includeAllOnPage;
	}

	public void setIncludeAllOnPage(boolean includeAllOnPage) {
		this.includeAllOnPage = includeAllOnPage;
	}
	
	public void togglePaymentsOnPageForBulk(){
		if(this.includeAllOnPage == true){
			includeChecksOnPage(false);
		}else{
			includeChecksOnPage(true);
		}
	}

	private void includeChecksOnPage(boolean val) {
		for(final CheckSearchRecordDto checkSearchRecordDto : currentPageList) {
			checkSearchRecordDto.setApproved(val);
		}
	}

}
