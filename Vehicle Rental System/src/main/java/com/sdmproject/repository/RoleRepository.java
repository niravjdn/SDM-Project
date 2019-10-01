package com.sdmproject.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.model.Role;

@Repository
public class RoleRepository {

	Logger logger = LoggerFactory.getLogger(UserRepository.class);

	private List<Role> records = new ArrayList<Role>(); 

	public Role findByRole(String roleName) {
		List<Role> result = records.stream().filter(record -> (record.getRole().equals(roleName)))
				.collect(Collectors.toList());
		return result.get(0);
	}

	public void save(Role role) {
		records.add(role);
	}

}