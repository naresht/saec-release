package com.bfds.saec.web.model;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.bfds.saec.domain.Claimant;
import com.bfds.saec.domain.ClaimantCalculationAttributes;
import com.bfds.saec.domain.dto.ClaimantPositionCriteriaDto;
import com.bfds.saec.domain.dto.ClaimantSearchCriteriaDto;
import com.bfds.saec.web.ui.components.BaseLazyDataModel;
import com.bfds.wss.domain.ClaimantPosition;

public class DailyPositionViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long claimantId;

	private ClaimantPositionCriteriaDto param = new ClaimantPositionCriteriaDto();

	public ClaimantClaimHeader getClaimantClaimHeader() {
		return new ClaimantClaimHeader(this.claimantId);
	}

	public BaseLazyDataModel<ClaimantPosition> getClaimantPositions() {

		param.setClaimantClaim(Claimant.findClaimant(this.claimantId).getSingleClaimantClaim());

		final ClaimantPosition claimantPosition = new ClaimantPosition();

		BaseLazyDataModel<ClaimantPosition> model = new BaseLazyDataModel<ClaimantPosition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ClaimantPosition> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {

				param.setFirstResult(first);
				param.setMaxResults(pageSize);
				param.setSortField(sortField);
				param.setSortOrder(SortOrder.ASCENDING == sortOrder ? ClaimantSearchCriteriaDto.SORT_ORDER_ASC
						: ClaimantSearchCriteriaDto.SORT_ORDER_DESC);
				param.setFilters(filters);
				List<ClaimantPosition> list = claimantPosition
						.findClaimantPositionByClaimant(param);
				return list;
			}

		};

		Long l = claimantPosition.getClaimantPositionCountForClaimant(param);
		if (l != null) {
			int i = (int) l.longValue();
			model.setRowCount(i);
		}

		return model;
	}

	public boolean isReCalcRequired() {
		return ClaimantCalculationAttributes.isReCalcRequired(Claimant
				.findClaimant(claimantId));
	}

	public void setClaimantId(final Long claimantId) {
		this.claimantId = claimantId;
	}
}