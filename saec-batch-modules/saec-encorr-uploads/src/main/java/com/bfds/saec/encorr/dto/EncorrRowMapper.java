package com.bfds.saec.encorr.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bfds.saec.encorr.dao.EncorrItem;

public class EncorrRowMapper implements RowMapper<EncorrItem> {

	@Override
	public EncorrItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		final EncorrItem encorrItem = new EncorrItem();
		encorrItem.setBusinessArea(rs.getString(1));
		encorrItem.setWorkType(rs.getString(2));
		encorrItem.setReferenceNo(rs.getString(3));
		encorrItem.setSsn(rs.getString(4));
		encorrItem.setMailControlNo(rs.getString(7));
		return encorrItem;
	}

}
