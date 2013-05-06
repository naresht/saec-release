package com.bfds.saec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;


@Repository
public class AssociateDaoImpl implements AssociateDao {

	@Autowired
	private DataSource universalDbDataSource;
	
	@Override
	public List<String> getAllAssociateNames() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> ret = Lists.newArrayList();
		try {
			con = universalDbDataSource.getConnection();
			ps = con.prepareStatement("select NT_ID from Associates where status = 'A' and NT_ID <>'' order by NT_ID");
			rs = ps.executeQuery();
			while(rs.next()) {
				ret.add(trimDomianName(rs.getString(1)));
			}
			
		}catch(SQLException e ) {
			throw new IllegalStateException(e);
		}finally {
			try {
				
				if(rs != null) { rs.close();}
				if(ps != null) { ps.close();}
				if(con != null) { con.close();}
			}catch(SQLException e) {
				throw new IllegalStateException(e);
			}
		}
		return ret;
	}

	private String trimDomianName(String string) {
		if(StringUtils.hasText(string) && string.indexOf('\\') >= 0 ) {
			string = string.substring(string.indexOf('\\') + 1, string.length());
		}
		return string;
	}

}
