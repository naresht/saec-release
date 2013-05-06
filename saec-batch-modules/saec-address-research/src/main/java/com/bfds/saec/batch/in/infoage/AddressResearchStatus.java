package com.bfds.saec.batch.in.infoage;

import java.util.Date;

import com.bfds.saec.batch.file.domain.in.infoage.AddressResearchUpdate;
import com.bfds.saec.batch.file.domain.in.infoage_corporate.CorporateAddressResearch;
import com.bfds.saec.batch.file.domain.in.infoage_individual.IndividualAddressResearch;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


/**
 * Overall status of an account as a result of Address Research (is the account identified as address changed?)
 * <p/>
 * This class has both individual and corproate research data rather than use another inheritance class to avoid
 * further inheritance nightmare in InfoAgeAddressResearchService and IndividualXXX and CorporateXXX
 */
@RooJavaBean
@RooToString
public class AddressResearchStatus {

    private boolean addressUpdated;

    private boolean rpoObjectUpdated;

    private AddressResearchUpdate addressResearchUpdate;
}
