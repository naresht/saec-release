package com.bfds.saec.rpo.dto;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class AwdRpoRowMapper implements RowMapper<RpoItem> {

	@Override
	public RpoItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		final RpoItem rpoItem = new RpoItem();
		rpoItem.setBusinessArea(rs.getString(1));
		rpoItem.setWorkType(rs.getString(2));
		rpoItem.setReferenceNo(rs.getString(3));
		rpoItem.setSsn(rs.getString(4));
		rpoItem.setCheckAmount(new BigDecimal(rs.getDouble(5)));
		rpoItem.setCheckNo(rs.getString(6));
		rpoItem.setMailControlNo(rs.getString(7));
		return rpoItem;
	}
	
}
