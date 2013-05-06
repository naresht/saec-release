package com.bfds.saec.domain.dto;

import java.util.Collections;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

@RooJavaBean
public abstract class AbstractSearchCriteriaDto implements java.io.Serializable,
		Cloneable {
	private static final long serialVersionUID = 1L;
	
	public static final String SORT_ORDER_ASC = "asc";
	public static final String SORT_ORDER_DESC = "desc";
	public static final String DEFAULT_SORT_ORDER = SORT_ORDER_ASC;
	public static final int DEFAULT_PAGE_SIZE = 25;

	private static final Double DOUBLE_ZERO = new Double(0);
	
	protected int firstResult;
	protected int maxResults;	
	protected String sortField;
	protected String sortOrder;
	protected Map<String, String> filters;

	public int getMaxResults() {
		return maxResults == 0 ? DEFAULT_PAGE_SIZE : maxResults;
	}

	public boolean hasSortFields() {
		return StringUtils.hasText(sortField);
	}

	public abstract boolean isValid() ;
		
	public String getSortOrder() {
		return sortOrder == null ? DEFAULT_SORT_ORDER : sortOrder;
	}

	public Map<String, String> getFilters() {
		return filters == null ? Collections.<String, String>emptyMap() : filters;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + firstResult;
		result = prime * result + maxResults;
		result = prime * result
				+ ((sortField == null) ? 0 : sortField.hashCode());
		result = prime * result
				+ ((sortOrder == null) ? 0 : sortOrder.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractSearchCriteriaDto other = (AbstractSearchCriteriaDto) obj;
		if (filters == null) {
			if (other.filters != null) {
				return false;
			}
		}
		else if (!filters.equals(other.filters)) {
			return false;
		}
		if (firstResult != other.firstResult) {
			return false;
		}
		if (maxResults != other.maxResults) {
			return false;
		}
		if (sortField == null) {
			if (other.sortField != null) {
				return false;
			}
		}
		else if (!sortField.equals(other.sortField)) {
			return false;
		}
		if (sortOrder == null) {
			if (other.sortOrder != null) {
				return false;
			}
		}
		else if (!sortOrder.equals(other.sortOrder)) {
			return false;
		}
		return true;
	}	
	
}