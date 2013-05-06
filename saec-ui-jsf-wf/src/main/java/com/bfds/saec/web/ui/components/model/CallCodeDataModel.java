package com.bfds.saec.web.ui.components.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.bfds.saec.domain.reference.CallCode;

public class CallCodeDataModel extends ListDataModel<CallCode> implements SelectableDataModel<CallCode> {

	public CallCodeDataModel(final List<CallCode> callCodes) {
		super(callCodes);

	}

	@Override
	public Object getRowKey(CallCode object) {
		final List<CallCode> callCodes = (List<CallCode>) getWrappedData();
        for(CallCode callCode : callCodes) {
            if(callCode.equals(object))
                return callCode.getCode();
        }
	    return null;
	}

	@Override
	public CallCode getRowData(String rowKey) {
		final List<CallCode> callCodes = (List<CallCode>) getWrappedData();
        for(CallCode callCode : callCodes) {
            if(callCode.getCode().equals(rowKey))
                return callCode;
        }
		return null;
	}   

}
