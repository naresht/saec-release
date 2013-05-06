package com.bfds.saec.domain.activity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
public class GeneralAccountMaintenanceChange extends Activity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	private static final String ACTIVITY_TYPE_DESCRIPTION = "GeneralAccountMaintenance Change";

	@Embedded
	private GeneralAccountMaintenance from;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "fundAccountNo", column = @Column(name = "to_fundAccountNo")),
			@AttributeOverride(name = "accountStatus", column = @Column(name = "to_accountStatus")),
			@AttributeOverride(name = "accountType", column = @Column(name = "to_accountType")),
			@AttributeOverride(name = "brokerId", column = @Column(name = "to_brokerId")),
			@AttributeOverride(name = "bin", column = @Column(name = "to_bin")) })
	private GeneralAccountMaintenance to;

	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		if (from != null) {
			sb.append(from.getAsString());
		}
		if (to != null) {
			sb.append(" To ");
			sb.append(to.getAsString());
		}
		return sb.toString();
	}

	public String getActivityTypeDescription() {
		return ACTIVITY_TYPE_DESCRIPTION;
	}

	public GeneralAccountMaintenance getFrom() {
		return from;
	}

	public void setFrom(GeneralAccountMaintenance from) {
		this.from = from;
	}

	public GeneralAccountMaintenance getTo() {
		return to;
	}

	public void setTo(GeneralAccountMaintenance to) {
		this.to = to;
	}

}
