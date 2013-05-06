package com.bfds.saec.web.ui.components.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.bfds.saec.domain.LetterCode;

/**
 * A {@link SelectableDataModel} implementation for {@link LetterCode}s
 *
 */
public class LetterCodeDataModel extends ListDataModel<LetterCode> implements SelectableDataModel<LetterCode> {

	public LetterCodeDataModel(final List<LetterCode> letterCodes) {
		super(letterCodes);

	}

	@Override
	public Object getRowKey(LetterCode object) {
		final List<LetterCode> letterCodes = (List<LetterCode>) getWrappedData();
        for(LetterCode letterCode : letterCodes) {
            if(letterCode.equals(object))
                return letterCode.getCode();
        }
	    return null;
	}

	@Override
	public LetterCode getRowData(String rowKey) {
		final List<LetterCode> letterCodes = (List<LetterCode>) getWrappedData();
        for(LetterCode letterCode : letterCodes) {
            if(letterCode.getCode().equals(rowKey))
                return letterCode;
        }
		return null;
	}   

}
