package com.bfds.saec.web.ui.components.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.bfds.saec.domain.CorrespondenceDocument;

/**
 * A {@link SelectableDataModel} implementation for {@link CorrespondenceDocument}s
 *
 */
public class CorrespondenceDocumentDataModel extends ListDataModel<CorrespondenceDocument> implements SelectableDataModel<CorrespondenceDocument> {

	public CorrespondenceDocumentDataModel(final List<CorrespondenceDocument> letterCodes) {
		super(letterCodes);

	}

	@Override
	public Object getRowKey(CorrespondenceDocument object) {
		final List<CorrespondenceDocument> letterCodes = (List<CorrespondenceDocument>) getWrappedData();
        for(CorrespondenceDocument letterCode : letterCodes) {
            if(letterCode.equals(object))
                return letterCode.getName();
        }
	    return null;
	}

	@Override
	public CorrespondenceDocument getRowData(String rowKey) {
		final List<CorrespondenceDocument> letterCodes = (List<CorrespondenceDocument>) getWrappedData();
        for(CorrespondenceDocument letterCode : letterCodes) {
            if(letterCode.getName().equals(rowKey))
                return letterCode;
        }
		return null;
	}   

}