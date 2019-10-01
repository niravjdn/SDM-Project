package com.sdmproject.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.User;

@Repository
public class UserRepository {

	Logger logger = LoggerFactory.getLogger(UserRepository.class);

	private List<User> records = new ArrayList<User>();
	private static int id = 1;
	
	
	public int getCountOfUsers() {
		return records.size();
	}

	public boolean isUserExist(String email) {
		User result = records.stream().filter(record -> record.getEmail().equals(email))
				.findAny().orElse(null);
		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	public User findByEmail(String email) {
		List<User> result = records.stream()
				.filter(record -> (record.getEmail().equals(email))).collect(Collectors.toList());
		return result.get(0);
	}

	public User save(User user) {
		user.setId(id++);
		records.add(user);
		return user;
	}

	public void deleteByEmail(String email) {
		for (Iterator<User> iterator = records.iterator(); iterator.hasNext();) {
			if (iterator.next().getEmail().equals(email))
				iterator.remove();
		}
	}
}
