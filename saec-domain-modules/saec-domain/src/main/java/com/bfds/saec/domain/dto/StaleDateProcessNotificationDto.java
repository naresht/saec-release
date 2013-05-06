package com.bfds.saec.domain.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Notification Bean to hold Stale date processing Statistics
 */

@RooJavaBean
@RooToString
public class StaleDateProcessNotificationDto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String businessArea;

	/**
	 * Date of scan
	 */
	private Date scanDate;
	/**
	 * total # of check objects in an outstanding status for that event
	 */
	private Long totalOutstandingChecks;
	/**
	 * total # of objects where status was changed to stale date as a result of
	 * the scan
	 */
	private int checksStaleDated;
}
