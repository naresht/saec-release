package com.bfds.saec.addressresearch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * Handler to handle errors during Jaxb/Xml marshalling
 * TODO: Need a strategy to handle validation errors as single records should be allowed to fail
 */
public class AddressResearchValidationHandler implements ValidationEventHandler {

    final Logger log = LoggerFactory.getLogger(AddressResearchValidationHandler.class);

    @Override
    public boolean handleEvent(ValidationEvent event) {
        if (event.getSeverity() == ValidationEvent.ERROR || event.getSeverity() == ValidationEvent.FATAL_ERROR) {
            log.error(String.format("Error [%s] occured during Schema Validation. Please refer to address-research-error.log file\n" +
                    "Original Object = ", event, event.getLocator().getObject()));

            return false;
        }
        return true;
    }
}
