package com.bfds.saec.web.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchRecordDto;
import com.bfds.saec.web.service.ClaimantService;
import com.bfds.saec.web.ui.components.BaseLazyDataModel;
import com.bfds.saec.web.util.JsfUtils;

@Component
@Scope("session")
public class ClaimantSearchViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private transient ClaimantService claimantService;

	private ClaimantSearchCriteriaDto param = new ClaimantSearchCriteriaDto();

	private transient LazyDataModel<ClaimantSearchRecordDto> claimants;
	
	/**
	 * Prepare the search parameters and call the claimantService finder. Called
	 * from webflow script.
	 */
	public LazyDataModel<ClaimantSearchRecordDto> find() {
		LazyDataModel<ClaimantSearchRecordDto> claimantListModel = new BaseLazyDataModel<ClaimantSearchRecordDto>()  {
			private static final long serialVersionUID = 1L;

			@Override
			public List<ClaimantSearchRecordDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
				param.setFirstResult(first);
				param.setMaxResults(pageSize);
				param.setSortField(sortField);
				param.setSortOrder(SortOrder.ASCENDING ==  sortOrder ? ClaimantSearchCriteriaDto.SORT_ORDER_ASC
						: ClaimantSearchCriteriaDto.SORT_ORDER_DESC);
				param.setFilters(filters);
				return claimantService.getClaimantSearchResults(param);
			}
		};
		Long l = claimantService.getClaimantSearchResultsCount(param) ;
		if (l != null) {
			int i = (int) l.longValue() ;
			claimantListModel.setRowCount(i);
		}
		final boolean searchResultsEmpty = !(claimantListModel.getRowCount() > 0);
		showEmptyResultsMessage(searchResultsEmpty);
		hideSearchFieldSet(searchResultsEmpty);

		return claimants = claimantListModel;
	}

	private void hideSearchFieldSet(final boolean searchResultsEmpty) {
		final Fieldset searchFieldSet = (Fieldset) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "searchFormFieldSet");	
		searchFieldSet.setCollapsed(!searchResultsEmpty);
	}

	private void showEmptyResultsMessage(final boolean searchResultsEmpty) {
		final Dialog refineSearchDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "refineSearchDialog");
		refineSearchDialog.setVisible(searchResultsEmpty);
	}

	public ClaimantSearchCriteriaDto getParam() {
		return param;
	}

	public ClaimantViewModel getClaimantByPrimayKey(final Long id) {
		Claimant claimant = Claimant.findClaimant(id, Claimant.ASSOCIATION_ALL);
		return new ClaimantViewModel(claimant == null ? new Claimant()
				: claimant);
	}

	public void save(final ClaimantViewModel model) {
		model.setClaimant(model.getClaimant().merge());
	}	

	public LazyDataModel<ClaimantSearchRecordDto> getClaimants() {
		return claimants;
	}

	public void setClaimants(LazyDataModel<ClaimantSearchRecordDto> claimants) {
		this.claimants = claimants;
		DataTable searchResultsTable = (DataTable) JsfUtils.findComponent(
				JsfUtils.getUIViewRoot(), "formList");
		searchResultsTable.reset();
	}

	public void reset() {
		param = new ClaimantSearchCriteriaDto();
		claimants = new BaseLazyDataModel<ClaimantSearchRecordDto>()  {
			private static final long serialVersionUID = 1L;

			@Override
			public List<ClaimantSearchRecordDto> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				return java.util.Collections.<ClaimantSearchRecordDto>emptyList();
			}
		};
		if(JsfUtils.getUIViewRoot() != null){
			Dialog refineSearchDialog = (Dialog) JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "refineSearchDialog");
			refineSearchDialog.setVisible(false);
		}
	}

	public List emptyResultsList() {
		return java.util.Collections.EMPTY_LIST;
	}

	public void setSelectedItem(ClaimantSearchRecordDto selectedCar) {
		UIComponent summaryPanel = JsfUtils.findComponent(JsfUtils.getUIViewRoot(), "summary_c");
		summaryPanel.setRendered(true);
	}
}