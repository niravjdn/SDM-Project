package com.sdmproject.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.model.User;
import com.sdmproject.orm.Table;

@Repository
public class UserRepository {

	Logger logger = LoggerFactory.getLogger(UserRepository.class);

	public Table<User, Integer> DAO;
	public UserRepository() {
		this.DAO = new Table<User, Integer>(User.class);
	}

	public User findByEmail(String email) {
		return DAO.queryForEq("email", email).get(0);
	}
	
}
