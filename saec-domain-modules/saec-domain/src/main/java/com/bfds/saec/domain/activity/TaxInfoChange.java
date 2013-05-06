package com.bfds.saec.domain.activity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.ClaimantTaxInfo;

@Configurable
@Entity
@RooJavaBean
public class TaxInfoChange extends Activity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static final String ACTIVITY_TYPE_DESCRIPTION = "Tax Info Change";
	@Embedded
	private ClaimantTaxInfo from;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "ssn", column = @Column(name = "to_ssn")),
			@AttributeOverride(name = "ein", column = @Column(name = "to_ein")),
			@AttributeOverride(name = "tin", column = @Column(name = "to_tin")),
			@AttributeOverride(name = "certificationStatus", column = @Column(name = "to_certificationStatus")),
			@AttributeOverride(name = "certificationType", column = @Column(name = "to_certificationType")),
			@AttributeOverride(name = "taxCountryCode", column = @Column(name = "to_taxCountryCode")),
			@AttributeOverride(name = "usCitizen", column = @Column(name = "to_usCitizen")),
			@AttributeOverride(name = "certificationDate", column = @Column(name = "to_certificationDate")),
			@AttributeOverride(name = "foreignTax", column = @Column(name = "to_foreignTax")),
			@AttributeOverride(name = "foreignTaxClassification", column = @Column(name = "to_foreignTaxClassification")),
			@AttributeOverride(name = "dateSolicitationReceived", column = @Column(name = "to_dateSolicitationReceived"))})
	private ClaimantTaxInfo to;

	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		if (from != null) {
			sb.append(from.getAsFormattedText());
		}
		if (to != null) {
			sb.append(" To ");
			sb.append(to.getAsFormattedText());
		}
		return sb.toString();
	}

	@Override
	public String getActivityTypeDescription() {
		return ACTIVITY_TYPE_DESCRIPTION;
	}
}
