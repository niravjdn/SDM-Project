package com.sdmproject.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.orm.Table;

@Repository
public class RoleRepository {

	Logger logger = LoggerFactory.getLogger(UserRepository.class);

	public Table<Role, Integer> DAO;
	public RoleRepository() {
		this.DAO = new Table<Role, Integer>(Role.class);
	}
	
	public Role findByRole(String roleName) {
		return DAO.queryForEq("role",roleName).get(0);
	}

}