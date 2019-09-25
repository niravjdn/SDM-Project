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
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.rowmapper.UserRowMapper;

@Repository
public class UserRepository {

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
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("user").usingGeneratedKeyColumns("user_id");;

		// Commented as the database is H2, change the database and create procedure
		// READ_EMPLOYEE before calling getEmployeeUsingSimpleJdbcCall
		// simpleJdbcCall = new
		// SimpleJdbcCall(dataSource).withProcedureName("READ_EMPLOYEE");
	}

	public int getCountOfUsers() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
	}

	public boolean isUserExist(String email) {
		String query = "SELECT count(*) FROM user WHERE email = ?";
		int count = jdbcTemplate.queryForObject(query, new Object[] { email }, Integer.class);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public User findByEmail(String email) {
		//String query = "SELECT * FROM user WHERE email = ?";
		String query = "SELECT * \n" + 
				"FROM user u \n" + 
				"LEFT OUTER JOIN user_role ur ON (u.user_id = ur.user_id)\n" + 
				"LEFT OUTER JOIN role r ON (ur.role_id = r.role_id) where email = ? \n" + 
				"GROUP BY u.user_id ";
		User user = jdbcTemplate.queryForObject(query, new Object[] { email }, new UserRowMapper());
		
		return user;
	}

	public User save(User user) {
		String updateQuery = "UPDATE user SET active=?,lastName=?,firstName=?, password=? where email = ?";
		String updateQueryWithoutPassword = "UPDATE user SET active=?,lastName=?,firstName=? where email = ?";
		
		String insertRoleQuery = "insert into user_role (user_id, role_id) values (?, ?)";
		int updated = 0;
		if(isUserExist(user.getEmail())) {
			//update
			String query = user.getPassword()!= null ? updateQuery : updateQueryWithoutPassword;
			logger.info(query);
			updated = jdbcTemplate.update(query, new Object[] { user.getActive(), user.getLastName(), user.getFirstName(), user.getPassword(),user.getEmail() },
					new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });

		}else {
			logger.trace("Updated rows for saving user " + updated);
			System.out.println("Here");
//			updated = simpleJdbcInsert.update(insertQuery, new Object[] {  user.getActive(), user.getEmail(), user.getLastName(), user.getName(), user.getPassword() },
//					new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR ,Types.VARCHAR, Types.VARCHAR });
//		
			Map<String, Object> parameters = new HashMap<String, Object>();
		    
		    parameters.put("active", user.getActive());
		    parameters.put("lastName", user.getLastName());
		    parameters.put("firstName", user.getFirstName());
		    parameters.put("password", user.getPassword());
		    parameters.put("email", user.getEmail());
		    
		    Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(
	                parameters));
	           // convert Number to Int using ((Number) key).intValue()
	        
	        System.out.println("Here2");
	        for(Role r : user.getRoles()) {
	        	updated = jdbcTemplate.update(insertRoleQuery, new Object[] { key.intValue(), r.getId()},
						new int[] {Types.INTEGER, Types.INTEGER});
	        }
	        
	        user.setId(key.intValue());
		}
		
		logger.trace("Updated rows for saving user " + updated);
		return user;
	}

	public void deleteByEmail(String email) {
		jdbcTemplate.update("Delete from user where email = ?", new Object[] { email },
				new int[] {Types.VARCHAR});
		
	}

}
