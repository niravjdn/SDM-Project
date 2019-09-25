package com.sdmproject;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.repository.RoleRepository;
import com.sdmproject.service.UserService;

@SpringBootApplication
public class VehicleRentingApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentingApplication.class, args);
	}

	@Override
	public void run(String... args) throws ParseException {
		boolean isAdminExist = userService.isUserExist("admin@niravjdn.xyz");
		if (!isAdminExist) {
			User admin = new User();
			admin.setEmail("admin@niravjdn.xyz");
			admin.setPassword("admin");
			admin.setFirstName("admin");
			admin.setLastName("admin");

			Role userRole = roleRepository.findByRole("ADMIN");
			admin.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

			userService.saveUser(admin);
		}

		boolean isUserExist = userService.isUserExist("user@niravjdn.xyz");
		if (!isUserExist) {
			User user = new User();
			user.setEmail("user@niravjdn.xyz");
			user.setPassword("user");
			user.setFirstName("user");
			user.setLastName("user");
			Role userRole = roleRepository.findByRole("USER");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

		}
	}
}
