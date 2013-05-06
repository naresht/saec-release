package com.bfds.saec.batch.in.infoage_corporate;

import com.bfds.saec.batch.file.domain.in.infoage_corporate.HitIndicatorCorpType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class CorporateAddressResearch implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String ofacIndicator;
    private HitIndicatorCorpType hitIndicator;
    private String partialAddressIndicator;
    private String matchAnalysisTag;
    private String userRef;
    private String companyName;
    private String fein;
    private String phoneAreaCode;
    private String phone;
    private String phoneExt;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String addressDateReported;
    
    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Partial Address Indicator: ").append(this.getPartialAddressIndicator()).append("\n");
        sb.append("Match Analysis Tag: ").append(this.getMatchAnalysisTag()).append("\n");
        sb.append("Company Name: ").append(getCompanyName()).append("\n");       
        sb.append("FEIN: ").append(getFein()).append("\n");
        sb.append("Phone Area Code: ").append(getPhoneAreaCode()).append("\n");
        sb.append("Phone: ").append(getPhone()).append("\n");
        sb.append("Phone Ext: ").append(getPhoneExt()).append("\n");
        sb.append("Address: ").append(getAddress()).append("\n");
        sb.append("City: ").append(getCity()).append("\n");
        sb.append("State: ").append(getState()).append("\n");
        sb.append("Zip: ").append(getZipCode()).append("\n");
        sb.append("Address Date Reported: ").append(getAddressDateReported()).append("\n");
        return sb.toString();
     }
    
    
}
