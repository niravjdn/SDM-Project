package com.sdmproject.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.sdmproject.model.VehicleType;
import com.sdmproject.model.Role;

public class VehicleTypeRowMapper implements RowMapper<VehicleType> {

	@Override
	public VehicleType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		try {
			final VehicleType vehicle = new VehicleType();

			vehicle.setId(rs.getInt("vehicletype_id"));
			vehicle.setType(rs.getString("type"));
			
			return vehicle;

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
