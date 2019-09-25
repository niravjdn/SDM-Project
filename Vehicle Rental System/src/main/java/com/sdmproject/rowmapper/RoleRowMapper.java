package com.sdmproject.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.sdmproject.model.Role;

public class RoleRowMapper implements RowMapper<Role> {

	@Override
	public Role mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		try {
			final Role role = new Role();

			role.setId(rs.getInt("role_id"));
			role.setRole(rs.getString("role"));
			
			return role;

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
