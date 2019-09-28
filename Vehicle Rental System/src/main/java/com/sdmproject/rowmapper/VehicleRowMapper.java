package com.sdmproject.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.sdmproject.model.Vehicle;
import com.sdmproject.model.VehicleType;

public class VehicleRowMapper implements RowMapper<Vehicle> {

	@Override
	public Vehicle mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		try {
			final Vehicle vehicle = new Vehicle();

			vehicle.setId(rs.getInt("id"));
			vehicle.setColor(rs.getString("color"));
			vehicle.setPlateNo(rs.getString("plateNo"));
			vehicle.setMaker(rs.getString("make"));
			vehicle.setModel(rs.getString("model"));
			vehicle.setType(new VehicleType());
			vehicle.setYear(rs.getInt("year"));
			
			return vehicle;

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
