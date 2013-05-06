/**
 * 
 */
package com.bfds.saec.web.ui.components;

import org.primefaces.model.LazyDataModel;

/**
 * See http://code.google.com/p/primefaces/issues/detail?id=1544
 *
 * @param <T>
 */
public abstract class BaseLazyDataModel<T> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	@Override
    public void setRowIndex(int rowIndex) {
        /*
         * The following is in ancestor (LazyDataModel):
         * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
         */
        if (rowIndex == -1 || getPageSize() == 0) {
            super.setRowIndex(-1);
        }
        else
            super.setRowIndex(rowIndex % getPageSize());
    }
	
	/*
	 * There is a bug in DataTableRenderer.handlePreselection with lazy datamodel and single selection(I suppose, multiple selection can have a problem too). 
	 * It iterate over table.getRowCount() while lazy datamodel table contains only current page.
	 * Workaround is to override getRowData in datamodel but of course it's a bug that needs fixing in Primcefaces.
	 * Ref link : http://forum.primefaces.org/viewtopic.php?f=3&t=11353
	 */
	
	@Override
    public T getRowData() {
        if (isRowAvailable()) {
            return super.getRowData();
        } else {
            return null;
        }

    }
}