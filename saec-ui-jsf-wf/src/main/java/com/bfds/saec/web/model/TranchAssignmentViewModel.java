package com.bfds.saec.web.model;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bfds.saec.domain.PaymentLetterCode;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import com.bfds.saec.domain.LetterCode;
import com.bfds.saec.domain.Tranch;
import com.bfds.saec.domain.dto.CheckReleaseSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchCriteriaDto;
import com.bfds.saec.domain.dto.TranchAssignmentSearchRecordDto;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.tranch.service.TranchException;
import com.bfds.saec.tranch.service.TranchService;
import com.bfds.saec.web.ui.components.BaseLazyDataModel;
import com.bfds.saec.web.util.JsfUtils;
import com.google.common.collect.Sets;


public class TranchAssignmentViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(TranchAssignmentViewModel.class);

	@Autowired
	private transient TranchService tranchService;

	private TranchAssignmentSearchRecordDtoLazyDataModel checkList;
	
	private TranchAssignmentSearchRecordDtoLazyDataModel wireList;
	
	private BigDecimal checkTotal;
	
	private BigDecimal wireTotal;
	
	private List<Tranch> tranchList;
	
	private String newTranchCode;
	
	private PaymentLetterCode letterCode;
	
	public boolean validateSearchCriteria(
			final TranchAssignmentSearchCriteriaDto criteria,
			final MessageContext messageContext) {
		
		resetPopups();
		List<String> errors = criteria.validate();
		
		if(errors.size() > 0) {
			for(final String error : errors) {
				messageContext.addMessage(new MessageBuilder()
				.error()
				.defaultText(error)
				.build());
				setTranchDialogVisibility(false);
			}
			return false;
		}
		if(log.isInfoEnabled())
			log.info(String.format("Validated search criteria for Tranch assignment."));
		return true;
	}
	
	public boolean loadChecksForTranchAssignment(
			final TranchAssignmentSearchCriteriaDto criteria_,
			final MessageContext messageContext) {
		resetPopups();
		final TranchAssignmentSearchCriteriaDto criteria = (TranchAssignmentSearchCriteriaDto) criteria_.clone();
		criteria.setPaymentType(PaymentType.CHECK);		
		try {
			checkList = newSearchResultsLazyDataModel(criteria);
			checkTotal = tranchService.getpaymentDistributionTotalForTranchAssignment(criteria);
			if(log.isInfoEnabled())
				log.info(String.format("Loaded checks for Tranch assignment."));
		} catch (TranchException e) {
			displayTrachAssignmentErrors(messageContext, e);
			if(log.isErrorEnabled())
				log.error(String.format("Checks not Loaded for Tranch assignment."));
			return false;
		}
		return true;
	}
	
	public boolean loadWiresForTranchAssignment(
			final TranchAssignmentSearchCriteriaDto criteria_,
			final MessageContext messageContext) {
		resetPopups();
		final TranchAssignmentSearchCriteriaDto criteria = (TranchAssignmentSearchCriteriaDto) criteria_.clone();
		criteria.setPaymentType(PaymentType.WIRE);		
		try {
			wireList = newSearchResultsLazyDataModel(criteria);
			wireTotal = tranchService.getpaymentDistributionTotalForTranchAssignment(criteria);
			if(log.isInfoEnabled())
				log.info(String.format("Loaded wires for Tranch assignment."));
		} catch (TranchException e) {
			displayTrachAssignmentErrors(messageContext, e);
			if(log.isErrorEnabled())
				log.error(String.format("Checks not Loaded for Tranch assignment."));
			return false;
		}
		return true;
	}
	
	public boolean hasResults() {
		resetPopups();
		return !wireList.isEmpty() || !checkList.isEmpty();
	}

	private TranchAssignmentSearchRecordDtoLazyDataModel newSearchResultsLazyDataModel(
			final TranchAssignmentSearchCriteriaDto criteria_) {
		final TranchAssignmentSearchRecordDtoLazyDataModel ret = 	new TranchAssignmentSearchRecordDtoLazyDataModel(criteria_);
		return ret;
	}
	
	public boolean refreshResults(final MessageContext messageContext) {
		resetPopups();
		boolean checksValid = checkList.refreshResults(messageContext, "formCheckResults");
		boolean wiresValid = wireList.refreshResults(messageContext, "formWireResults");		
		return checksValid && wiresValid;
	}
	
	 public boolean loadAssignableTranches(final MessageContext messageContext) {
		 resetPopups();
         this.tranchList = Tranch.findAllTranches();
         return true;
	 }
	
	public boolean showTranchSelection(final MessageContext messageContext) {
		final boolean checksValid = checkList.refreshResults(messageContext, "formCheckResults");
		final boolean wiresValid = wireList.refreshResults(messageContext, "formWireResults");
		if(checksValid || wiresValid) {
			setTranchDialogVisibility(true);
			return true;
		}else {
			setTranchDialogVisibility(false);
			return false;
		}
	}

	private void setTranchDialogVisibility(final boolean visible) {
		final Dialog tranchSelectionDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "tranchDialog");
		tranchSelectionDialog.setVisible(visible);
	}
	
	public boolean addToTranch(final TranchAssignmentSearchCriteriaDto criteria, 
			final MessageContext messageContext) {
		if(!validTranchCodeSelection(criteria, messageContext)) {
			return false;
		}
		Tranch tranch = new Tranch();
		tranch.setCode(StringUtils.hasText(criteria.getTranchCode()) ? criteria.getTranchCode() : newTranchCode);
		final boolean checksValid = checkList.refreshResults(messageContext, "formCheckResults");
		final boolean wiresValid = wireList.refreshResults(messageContext, "formWireResults");
		try {
			if(checksValid && wiresValid) {
				tranchService.executePaymentTranchAssignment(this.getLetterCode(),tranch, checkList.getCriteria(), wireList.getCriteria());
			} else if(checksValid) {
				tranchService.executePaymentTranchAssignment(this.getLetterCode(),tranch, checkList.getCriteria());
			} else if(wiresValid) {
				tranchService.executePaymentTranchAssignment(this.getLetterCode(),tranch, wireList.getCriteria());
			} else {
				return false;
			}
		} catch (final TranchException e) {
			displayTrachAssignmentErrors(messageContext, e);			
			return false;
		}
		messageContext.addMessage(new MessageBuilder()
		.info()
		.defaultText(
				"Tranch request submitted.")
		.build());
		setTranchDialogVisibility(false);
		return true;
	}
	
	public boolean removeFromTranch(final TranchAssignmentSearchCriteriaDto criteria, 
			final MessageContext messageContext) {
		
		try {
			final Set<Long> paymentsToRemove = Sets.newHashSet();
			paymentsToRemove.addAll(checkList.getInverseSet());
			paymentsToRemove.addAll(wireList.getInverseSet());
			tranchService.removePaymentsFromTranch(criteria.getTranchCode(), paymentsToRemove);
		} catch (final TranchException e) {
			displayTrachAssignmentErrors(messageContext, e);			
			return false;
		}
		messageContext.addMessage(new MessageBuilder()
		.info()
		.defaultText(
				"Tranch request submitted.")
		.build());
		setTranchDialogVisibility(false);
		return true;
	}

	private void displayTrachAssignmentErrors(
			final MessageContext messageContext, final TranchException e) {
		if(e.getDetailMsgs() != null && e.getDetailMsgs().length > 0) {
			for(final String detailMsg : e.getDetailMsgs()) {
				messageContext.addMessage(new MessageBuilder()
				.error()
				.defaultText(
						detailMsg)
				.build());
			}
		}
	}

	private boolean validTranchCodeSelection(TranchAssignmentSearchCriteriaDto criteria,
			MessageContext messageContext) {
		if(StringUtils.hasText(criteria.getTranchCode()) && StringUtils.hasText(newTranchCode)) {
			messageContext
			.addMessage(new MessageBuilder()
					.error()
					.defaultText(
							"Enter/select only one of 'New Tranch Code' and 'Tranch Code'.")
					.build());
			return false;
		} else if(!StringUtils.hasText(criteria.getTranchCode()) && !StringUtils.hasText(newTranchCode)) {
			messageContext
			.addMessage(new MessageBuilder()
					.error()
					.defaultText(
							"Enter/select one of 'New Tranch Code' and 'Tranch Code'.")
					.build());
			return false;
		} else if(StringUtils.hasText(newTranchCode) && Tranch.findByCode(newTranchCode) != null) {
			 
			messageContext
			.addMessage(new MessageBuilder()
					.error()
					.defaultText(
							"'New Tranch Code' exists. Select it form the 'Tranch Code' list.")
					.build());
			return false;
		}
		return true;
	}

	public BigDecimal getTranchTotal() {
		if(checkTotal == null) {
			return wireTotal;
		} else if(wireTotal == null) {
			return checkTotal;
		} else {
			return checkTotal.add(wireTotal);
		}
	}
	
	public void clearResults() {
		checkList = dummyLazydataModel();
		wireList = dummyLazydataModel();
		this.letterCode = null;
		this.newTranchCode = null;
		//this.tranchCode = null;
	}

	private TranchAssignmentSearchRecordDtoLazyDataModel dummyLazydataModel() {
		return new DummyTranchAssignmentSearchRecordDtoLazyDataModel();
	}

	public class DummyTranchAssignmentSearchRecordDtoLazyDataModel extends TranchAssignmentSearchRecordDtoLazyDataModel {
		private static final long serialVersionUID = 1L;

		public DummyTranchAssignmentSearchRecordDtoLazyDataModel() {
			super();
		}
		
		@Override
		public List<TranchAssignmentSearchRecordDto> load(int first, int pageSize,
				String sortField, SortOrder sortOrder,
				Map<String, String> filters) {
			return java.util.Collections.<TranchAssignmentSearchRecordDto>emptyList();
		}
	}
	
	public class TranchAssignmentSearchRecordDtoLazyDataModel extends BaseLazyDataModel<TranchAssignmentSearchRecordDto> {
		
		private static final long serialVersionUID = 1L;
		
		private final TranchAssignmentSearchCriteriaDto criteria;
		private TranchAssignmentSearchCriteriaDto prevCriteria;
		private List<TranchAssignmentSearchRecordDto> currentPageList = new ArrayList<TranchAssignmentSearchRecordDto>();
		private boolean includeAllOnPage;
		private boolean includeAll;
		private final Set<Long> inverseSet;
		
		TranchAssignmentSearchRecordDtoLazyDataModel() {
			criteria = null;
			inverseSet = null;
		}
		
		public TranchAssignmentSearchRecordDtoLazyDataModel(final TranchAssignmentSearchCriteriaDto criteria_) {
			criteria = (TranchAssignmentSearchCriteriaDto) criteria_.clone();
			includeAllOnPage = true;
			includeAll = true;
			inverseSet = new HashSet<Long>();
			this.setRowCount(tranchService.getPaymentCountForTranchAssignment(criteria).intValue());
		}
		
		@Override
		public List<TranchAssignmentSearchRecordDto> load(int first, int pageSize,
				String sortField, SortOrder sortOrder,
				Map<String, String> filters) {
			criteria.setFirstResult(first);
			criteria.setMaxResults(pageSize);
			criteria.setSortField(sortField);
			criteria.setSortOrder(SortOrder.ASCENDING ==  sortOrder ? CheckReleaseSearchCriteriaDto.SORT_ORDER_ASC
					: CheckReleaseSearchCriteriaDto.SORT_ORDER_DESC);
			criteria.setFilters(filters);
			if (!canShowResultsFromCache()) {
				prevCriteria = criteria.clone();
				currentPageList = tranchService.getpaymentsForTranchAssignment(criteria);
				togglePaymentsForTranch(isIncludeAll());
				this.setRowCount(tranchService.getPaymentCountForTranchAssignment(criteria).intValue());
				applyInverseSet();
			}			
			
			return currentPageList;
		}
		
		@Override
		public Object getRowKey(TranchAssignmentSearchRecordDto object) {
			final List<TranchAssignmentSearchRecordDto> payments = (List<TranchAssignmentSearchRecordDto>) getWrappedData();
	        for(TranchAssignmentSearchRecordDto payment : payments) {
	            if(payment.getId().equals(object.getId()))
	                return payment.getId();
	        }
		    return null;
		}

		@Override
		public TranchAssignmentSearchRecordDto getRowData(String rowKey) {
			final List<TranchAssignmentSearchRecordDto> payments = (List<TranchAssignmentSearchRecordDto>) getWrappedData();
	        for(TranchAssignmentSearchRecordDto payment : payments) {
	            if(payment.getId().equals(Long.parseLong(rowKey)))
	                return payment;
	        }
			return null;
		}   


		private boolean canShowResultsFromCache() {
			return criteria.equals(prevCriteria) && !currentPageList.isEmpty();
		}
		
		private void applyInverseSet() {
			for(final TranchAssignmentSearchRecordDto tranchAssignmentSearchRecordDto : currentPageList) {
				if(inverseSet.contains(tranchAssignmentSearchRecordDto.getId())) {
					tranchAssignmentSearchRecordDto.setInclude(!isIncludeAll()) ;
				}				
			}			
		}

		public void selectPaymentForTranch(final Long id) {
			final List<TranchAssignmentSearchRecordDto> currentPageList = (List<TranchAssignmentSearchRecordDto>) this.getWrappedData();
			for(final TranchAssignmentSearchRecordDto tranchAssignmentSearchRecordDto : currentPageList) {
				if (tranchAssignmentSearchRecordDto.getId().equals(id)) {
					inverseSet.add(id);
				}
			}
		}
		
		public void togglePaymentsForTranch() {
			resetPopups();
			togglePaymentsForTranch(!this.isIncludeAll());
			inverseSet.clear();
		}
		
		public void togglePaymentsOnPageForTranch() {
			resetPopups();
			togglePaymentsForTranch(!this.isIncludeAllOnPage());
			for(final TranchAssignmentSearchRecordDto tranchAssignmentSearchRecordDto : currentPageList) {
				inverseSet.add(tranchAssignmentSearchRecordDto.getId());
			}
		}

		public void togglePaymentsForTranch(boolean flag) {
			for(final TranchAssignmentSearchRecordDto tranchAssignmentSearchRecordDto : currentPageList) {
				tranchAssignmentSearchRecordDto.setInclude(flag) ;
			}
		}
		
		/**
		 * Include manual/bulk includes/excluded performed by the user in the search criteria and re-execute the search. 
		 * Effectively this will remove all unchecked(excluded) items form the search result. 
		 * 
		 * @param messageContext
		 * @return true if refresh was successful. 
		 */
		public boolean refreshResults(final MessageContext messageContext, final String targetComponentd) {
			final TranchAssignmentSearchCriteriaDto criteriaTestClone = this.criteria.clone();
			applyRefresh(criteriaTestClone);
			if(!validCriteria(criteriaTestClone, messageContext, targetComponentd)) {
				return false;
			}
			applyRefresh(this.criteria);
			//Clear page cache.
			this.currentPageList.clear();
			// Re-load table data. This is required to clear paging info - firResult. See GitHub #563
			if(criteria.getPaymentType() == PaymentType.CHECK) {
				loadChecksForTranchAssignment(criteria, messageContext); 
			} else if(criteria.getPaymentType() == PaymentType.WIRE) {
				loadWiresForTranchAssignment(criteria, messageContext); 
			} else { 
				throw new IllegalStateException("Unsupported paymentType "+criteria.getPaymentType());
			}
			return true;
		}

		private boolean validCriteria(final TranchAssignmentSearchCriteriaDto criteriaTestClone, final MessageContext messageContext, final String targetComponentd) {
			List<String> errors = criteriaTestClone.validate();
			if(!errors.isEmpty()) {
				for(String error : errors) {
					messageContext.addMessage(new MessageBuilder().error()
							.source(JsfUtils.getClientId(targetComponentd))
							.defaultText(error).build());
				}
				return false;
			}
			return true;
		}

		private void applyRefresh( final TranchAssignmentSearchCriteriaDto criteria) {
			if(!this.includeAll) { 
				// All records have been excluded. So include only the ones that have been manually checked.
				criteria.setExcludeAll(true);
				criteria.getIncludes().addAll(this.inverseSet);
			} else {
				criteria.getExcludes().addAll(this.inverseSet);
			}
		}
		
		public boolean isEmpty() {
			return this.getRowCount() == 0;
		}
		
		public void clearResults() {
			checkList = dummyLazydataModel();
			wireList = dummyLazydataModel();
		}
		/**
		 * @return the excludeAllOnPage
		 */
		public boolean isIncludeAllOnPage() {
			return includeAllOnPage;
		}

		/**
		 * @param excludeAllOnPage the excludeAllOnPage to set
		 */
		public void setIncludeAllOnPage(boolean includeAllOnPage) {
			this.includeAllOnPage = includeAllOnPage;
		}
		
		/**
		 * @return the excludeAll
		 */
		public boolean isIncludeAll() {
			return includeAll;
		}

		/**
		 * @param excludeAll the excludeAll to set
		 */
		public void setIncludeAll(boolean includeAll) {
			setIncludeAllOnPage(includeAll);
			this.includeAll = includeAll;
		}

		/**
		 * @return the criteria
		 */
		public TranchAssignmentSearchCriteriaDto getCriteria() {
			return criteria;
		}

		public Set<Long> getInverseSet() {
			return inverseSet;
		}		
				
	}
	
	public TranchAssignmentSearchRecordDtoLazyDataModel getCheckList() {
		return checkList;
	}

	public void setCheckList(
			TranchAssignmentSearchRecordDtoLazyDataModel checkList) {
		this.checkList = checkList;
	}

	public TranchAssignmentSearchRecordDtoLazyDataModel getWireList() {
		return wireList;
	}

	public void setWireList(TranchAssignmentSearchRecordDtoLazyDataModel wireList) {
		this.wireList = wireList;
	}

	public BigDecimal getCheckTotal() {
		return checkTotal;
	}

	public void setCheckTotal(BigDecimal checkTotal) {
		this.checkTotal = checkTotal;
	}

	public BigDecimal getWireTotal() {
		return wireTotal;
	}

	public void setWireTotal(BigDecimal wireTotal) {
		this.wireTotal = wireTotal;
	}
		
	/**
	 * @return the newTranchCode
	 */
	public String getNewTranchCode() {
		return newTranchCode;
	}

	/**
	 * @param newTranchCode the newTranchCode to set
	 */
	public void setNewTranchCode(String newTranchCode) {
		this.newTranchCode = newTranchCode;
	}

	/**
	 * @return the tranchList
	 */
	public List<Tranch> getTranchList() {
		return tranchList;
	}

	/**
	 * @param tranchList the tranchList to set
	 */
	public void setTranchList(List<Tranch> tranchList) {
		this.tranchList = tranchList;
	}


	public PaymentLetterCode getLetterCode() {
		return letterCode;
	}

	public void setLetterCode(PaymentLetterCode letterCode) {
		this.letterCode = letterCode;
	}
	
	private void resetPopups(){
		Dialog tranchDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "tranchDialog");
		tranchDialog.setVisible(false);
	}

}
