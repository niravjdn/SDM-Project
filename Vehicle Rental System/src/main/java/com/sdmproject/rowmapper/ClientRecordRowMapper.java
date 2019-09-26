package com.sdmproject.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.sdmproject.model.VehicleType;
import com.sdmproject.model.ClientRecord;

public class ClientRecordRowMapper implements RowMapper<ClientRecord> {

	@Override
	public ClientRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		try {
			ClientRecord record = new ClientRecord();

			record.setId(rs.getInt("id"));
			record.setFirstName(rs.getString("firstName"));
			record.setLastName(rs.getString("lastName"));
			record.setDriverLicienceNo(rs.getString("driverLicienceNo"));
			record.setExpiryDate(rs.getDate("expiryDate"));
			record.setPhoneNo(rs.getString("phoneNo"));
			
			return record;

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
