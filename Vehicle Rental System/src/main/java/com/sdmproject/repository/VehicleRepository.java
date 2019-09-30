package com.sdmproject.repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.sdmproject.Config.CustomSQLErrorCodeTranslator;
import com.sdmproject.helper.QueryBuilder;
import com.sdmproject.model.Vehicle;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.rowmapper.ClientRecordRowMapper;
import com.sdmproject.rowmapper.VehicleRowMapper;

@Repository
public class VehicleRepository {

	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcInsert simpleJdbcInsert;

	private SimpleJdbcCall simpleJdbcCall;

	Logger logger = LoggerFactory.getLogger(VehicleRepository.class);

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator = new CustomSQLErrorCodeTranslator();
		jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("vehicle").usingGeneratedKeyColumns("id");;

		// Commented as the database is H2, change the database and create procedure
		// READ_EMPLOYEE before calling getEmployeeUsingSimpleJdbcCall
		// simpleJdbcCall = new
		// SimpleJdbcCall(dataSource).withProcedureName("READ_EMPLOYEE");
	}

	public int getCountOfVehicles() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vehicle", Integer.class);
	}

	public boolean isVehicleExist(String plateNo) {
		String query = "SELECT count(*) FROM vehicle WHERE plateNo = ?";
		int count = jdbcTemplate.queryForObject(query, new Object[] { plateNo }, Integer.class);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Vehicle findByPlateNo(String email) {
		//String query = "SELECT * FROM user WHERE email = ?";
		String query = "SELECT * FROM vehicle WHERE plateNo = ?";
		Vehicle vehicle = jdbcTemplate.queryForObject(query, new Object[] { email }, new VehicleRowMapper());
		return vehicle;
	}

	public Vehicle save(Vehicle vehicle) {
		String updateQuery = "UPDATE `vehicle` SET color=?, plateNo = ?,maker=?,model=?,year=? WHERE id=?";
		String updateVehicleTypeQuery = "UPDATE `vehicle_vehicletype` `vehicletype_id`=? WHERE id=?";
		
		String insertVehicleTypeQuery = "INSERT INTO `vehicle_vehicletype` VALUES (?,?)";
		
		int updated = 0;
		if(isVehicleExist(vehicle.getPlateNo())) {
			//update
			logger.info(updateQuery);
			updated = jdbcTemplate.update(updateQuery, new Object[] { vehicle.getColor(),  vehicle.getMaker(), vehicle.getModel(), vehicle.getType(), vehicle.getYear(), vehicle.getId()},
					new int[] {  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR , Types.VARCHAR, Types.VARCHAR, Types.INTEGER});

			updated = jdbcTemplate.update(updateVehicleTypeQuery, new Object[] { vehicle.getType().getId(), vehicle.getId()},
					new int[] {  Types.INTEGER, Types.INTEGER});

		}else {
			logger.trace("Updated rows for saving user " + updated);
			System.out.println("Here");
//			updated = simpleJdbcInsert.update(insertQuery, new Object[] {  user.getActive(), user.getEmail(), user.getLastName(), user.getName(), user.getPassword() },
//					new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR ,Types.VARCHAR, Types.VARCHAR });
//		
			Map<String, Object> parameters = new HashMap<String, Object>();
		    
		    parameters.put("color", vehicle.getColor());
		    parameters.put("plateNo", vehicle.getPlateNo());
		    parameters.put("maker", vehicle.getMaker());
		    parameters.put("model", vehicle.getModel());
		    parameters.put("type", vehicle.getType());
		    parameters.put("year", vehicle.getYear());
		    
		    
		    Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(
	                parameters));
	           // convert Number to Int using ((Number) key).intValue()
	        
		    updated = jdbcTemplate.update(insertVehicleTypeQuery, new Object[] { key.intValue(), vehicle.getType().getId() },
					new int[] {Types.INTEGER, Types.INTEGER});
	        
	        vehicle.setId(key.intValue());
		}
		
		logger.trace("Updated rows for saving user " + updated);
		return vehicle;
	}

	public void deleteVehicleByID(int id) {
		jdbcTemplate.update("Delete from vehicle where id = ?", new Object[] { id },
				new int[] {Types.INTEGER});
	}
	
	public List<Vehicle> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		QueryBuilder builder = new QueryBuilder();
		builder.setTableName("vehicle");
		if (sortProperty.isPresent())
			builder.setOrderBy(sortProperty.get(), sortOrder.get());
		List<Vehicle> record = jdbcTemplate.query(builder.getSQLQuery(), new Object[] { }, new VehicleRowMapper());
		return record;
	}

}
