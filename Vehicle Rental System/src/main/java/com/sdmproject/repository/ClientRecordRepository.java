package com.sdmproject.repository;

import java.sql.SQLException;
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
import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.helper.QueryBuilder;
import com.sdmproject.model.Vehicle;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.rowmapper.VehicleRowMapper;
import com.sdmproject.rowmapper.ClientRecordRowMapper;

@Repository
public class ClientRecordRepository {

	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcInsert simpleJdbcInsert;

	private SimpleJdbcCall simpleJdbcCall;

	Logger logger = LoggerFactory.getLogger(ClientRecordRepository.class);

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator = new CustomSQLErrorCodeTranslator();
		jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("client_record")
				.usingGeneratedKeyColumns("id");
		;

		// Commented as the database is H2, change the database and create procedure
		// READ_EMPLOYEE before calling getEmployeeUsingSimpleJdbcCall
		// simpleJdbcCall = new
		// SimpleJdbcCall(dataSource).withProcedureName("READ_EMPLOYEE");
	}

	public int getCountOfVehicles() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM client_record", Integer.class);
	}

	public boolean isClientExist(String driverLicienceNo) {
		String query = "SELECT count(*) FROM client_record WHERE driverLicienceNo = ?";
		int count = jdbcTemplate.queryForObject(query, new Object[] { driverLicienceNo }, Integer.class);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ClientRecord findByLicienceNo(String driverLicienceNo) {
		// String query = "SELECT * FROM user WHERE email = ?";
		String query = "SELECT * FROM client_record WHERE driverLicienceNo = ?";
		ClientRecord record = jdbcTemplate.queryForObject(query, new Object[] { driverLicienceNo },
				new ClientRecordRowMapper());
		return record;
	}

	public ClientRecord save(ClientRecord record) throws DuplicateEntryException {
		String updateQuery = "UPDATE `client_record` SET `firstName`=?,`lastName`=?,`driverLicienceNo`=?,`expiryDate`=? WHERE id=?";

		int updated = 0;
		if (isClientExist(record.getDriverLicienceNo())) {
			throw new DuplicateEntryException("The Client with same licience number already exist.", null);
		}

		logger.trace("Updated rows for saving user " + updated);
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("firstName", record.getFirstName());
		parameters.put("lastName", record.getLastName());
		parameters.put("driverLicienceNo", record.getDriverLicienceNo());
		parameters.put("expiryDate", record.getExpiryDate());
		parameters.put("phoneNo", record.getPhoneNo());

		Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
		// convert Number to Int using ((Number) key).intValue()

		record.setId(key.intValue());

		logger.trace("Updated rows for saving user " + updated);
		return record;

	}

	public ClientRecord update(ClientRecord record) throws DuplicateEntryException {
		String updateQuery = "UPDATE `client_record` SET `firstName`=?,`lastName`=?,phoneNo = ?,`expiryDate`=? WHERE id=?";

		logger.trace("Updated rows for saving user -----");
		int updated = 0;
		try {
			jdbcTemplate.update(updateQuery,
					new Object[] { record.getFirstName(), record.getLastName(), record.getPhoneNo(),
							record.getExpiryDate(), record.getId() },
					new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.INTEGER });

		} catch (Exception e) {
			throw new DuplicateEntryException("The Client with same licience number already exist.", null);
		}

		logger.trace("Updated rows for saving user " + updated);
		return record;
	}

	public void deleteClientRecordByID(int id) {
		jdbcTemplate.update("Delete from client_record where id = ?", new Object[] { id }, new int[] { Types.INTEGER });
	}

	public ClientRecord findById(int id) {
		String query = "SELECT * FROM client_record WHERE id = ?";
		ClientRecord record = jdbcTemplate.queryForObject(query, new Object[] { id }, new ClientRecordRowMapper());
		return record;
	}

	public List<ClientRecord> findAll() {
		String query = "SELECT * FROM client_record";
		List<ClientRecord> record = jdbcTemplate.query(query, new Object[] {}, new ClientRecordRowMapper());
		return record;
	}

	public List<Integer> findAllIDsAndSortByID() {
		String query = "SELECT id FROM client_record order by id";
		List<Integer> record = jdbcTemplate.queryForList(query, new Object[] {}, Integer.class);
		return record;
	}

	public List<ClientRecord> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		QueryBuilder builder = new QueryBuilder();
		builder.setTableName("client_record");
		if (sortProperty.isPresent())
			builder.setOrderBy(sortProperty.get(), sortOrder.get());
		List<ClientRecord> record = jdbcTemplate.query(builder.getSQLQuery(), new Object[] { }, new ClientRecordRowMapper());
		return record;
	}

	public List<Integer> findIDWithSort(String sortProperty,String sortOrder) {
		QueryBuilder builder = new QueryBuilder();
		builder.setTableName("client_record");
		builder.setFields("id");
		builder.setOrderBy(sortProperty, sortOrder);
		List<Integer> record = jdbcTemplate.queryForList(builder.getSQLQuery(), new Object[] {}, Integer.class);
		return record;
	}

}
