package com.bfds.saec.infrastructure.build;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Configurable
@Component
public class BuildInfo {
	
	@Value("${build.number}")
	private String buildNo;

	@Value("${application.version}")
	private String applicationVersion;

	public String getBuildNo() {
		return valid(buildNo) ? buildNo : "NA";
	}

	public String getApplicationVersion() {
		return valid(applicationVersion) ? applicationVersion : "NA";
	}

	private boolean valid(String val) {
		return StringUtils.hasText(val) && !(val.contains("$") || val.contains("{") || val.contains("}"));
	}

}
