package com.bfds.saec.rpo.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.bfds.saec.domain.Event;

@RooJavaBean
public class RpoPreparedStatementSetter implements PreparedStatementSetter {

	private static final int DEFAULT_DAYS_TO_LOOK_BACK = 30;
	private int daysToLookBack;

	/**
	 * This function fills the query with parameters attribute values.
	 */
	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
		ps.setString(1, Event.getCurrentEvent().getCode());
		ps.setDate(2, getDateThirtyDaysBefore());
	}

	private java.sql.Date getDateThirtyDaysBefore() {
		java.util.Date today = calculateDueDateFrom(new java.util.Date());
		return new java.sql.Date(today.getTime());
	}

	private Date calculateDueDateFrom(Date checkoutDate) {
		final Calendar c = Calendar.getInstance();
		c.setTime(checkoutDate);
		c.add(Calendar.DATE, getDaysToLookBack() > 0 ? -getDaysToLookBack()
				: -DEFAULT_DAYS_TO_LOOK_BACK);
		return c.getTime();
	}

	public int getDaysToLookBack() {
		return daysToLookBack;
	}

	public void setDaysToLookBack(int daysToLookBack) {
		this.daysToLookBack = daysToLookBack;
	}
}
