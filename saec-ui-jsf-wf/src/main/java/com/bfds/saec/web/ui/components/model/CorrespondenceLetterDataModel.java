package com.bfds.saec.web.ui.components.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.bfds.saec.domain.CorrespondenceDocument;
import com.bfds.saec.web.model.CorrespondenceLetterViewModel;

/**
 * A {@link SelectableDataModel} implementation for {@link CorrespondenceDocument}s
 *
 */
public class CorrespondenceLetterDataModel extends ListDataModel<CorrespondenceLetterViewModel> implements SelectableDataModel<CorrespondenceLetterViewModel> {

	public CorrespondenceLetterDataModel(final List<CorrespondenceLetterViewModel> letterCodes) {
		super(letterCodes);

	}

	@Override
	public Object getRowKey(CorrespondenceLetterViewModel object) {
		final List<CorrespondenceLetterViewModel> incomingNIGOs = (List<CorrespondenceLetterViewModel>) getWrappedData();
        for(CorrespondenceLetterViewModel obj : incomingNIGOs) {
            if(obj.equals(object))
                return obj.getLetter().getId();
        }
		 return null;
	}

	@Override
	public CorrespondenceLetterViewModel getRowData(String rowKey) {
		final List<CorrespondenceLetterViewModel> letterCodes = (List<CorrespondenceLetterViewModel>) getWrappedData();
        for(CorrespondenceLetterViewModel letterCode : letterCodes) {
            if(letterCode.getLetter().getId().toString().equals(rowKey))
                return letterCode;
        }
		return null;
	}   
	
   
}