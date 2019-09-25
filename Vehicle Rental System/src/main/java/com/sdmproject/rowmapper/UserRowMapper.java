package com.sdmproject.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.sdmproject.model.Role;
import com.sdmproject.model.User;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		try {
			final User user = new User();

			user.setId(rs.getInt("user_id"));
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setEmail(rs.getString("email"));
			
			Role r = new Role();
			r.setId(rs.getInt("role_id"));
			r.setRole(rs.getString("role"));
			
			Set<Role> roles = new HashSet<Role>();
			roles.add(r);
			user.setRoles(roles);
			
			return user;

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
