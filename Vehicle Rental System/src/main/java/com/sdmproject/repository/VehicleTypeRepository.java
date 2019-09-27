package com.sdmproject.repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.sdmproject.Config.CustomSQLErrorCodeTranslator;
import com.sdmproject.model.VehicleType;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.rowmapper.VehicleTypeRowMapper;
import com.sdmproject.rowmapper.RoleRowMapper;
import com.sdmproject.rowmapper.UserRowMapper;

@Repository
public class VehicleTypeRepository {

	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcInsert simpleJdbcInsert;

	private SimpleJdbcCall simpleJdbcCall;

	Logger logger = LoggerFactory.getLogger(UserRepository.class);

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator = new CustomSQLErrorCodeTranslator();
		jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("vehicletype").usingGeneratedKeyColumns("user_id");;

		// Commented as the database is H2, change the database and create procedure
		// READ_EMPLOYEE before calling getEmployeeUsingSimpleJdbcCall
		// simpleJdbcCall = new
		// SimpleJdbcCall(dataSource).withProcedureName("READ_EMPLOYEE");
	}

	public int getCountOFVehicleTypes() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vehicletype", Integer.class);
	}


	public VehicleType findByType(String type) {
		//String query = "SELECT * FROM user WHERE email = ?";
		String query = "SELECT * FROM `vehicletype` WHERE type=?";
		VehicleType vehicleType = jdbcTemplate.queryForObject(query, new Object[] { type }, new VehicleTypeRowMapper());
		
		return vehicleType;
	}

}