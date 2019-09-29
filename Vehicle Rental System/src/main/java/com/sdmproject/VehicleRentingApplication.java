package com.sdmproject;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.model.Vehicle;
import com.sdmproject.model.VehicleType;
import com.sdmproject.repository.RoleRepository;
import com.sdmproject.repository.VehicleTypeRepository;
import com.sdmproject.service.UserService;
import com.sdmproject.service.VehicleService;

@SpringBootApplication
public class VehicleRentingApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private VehicleTypeRepository carTypeRepository;

	
	public static void main(String[] args) {
		SpringApplication.run(VehicleRentingApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VehicleRentingApplication.class);
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
			Role userRole = roleRepository.findByRole("CLERK");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userService.saveUser(user);
		}
		
		boolean isCarExist = vehicleService.isVehicleExist("GJ 02 BQ 4273");
		if (!isCarExist) {
			Vehicle vehicle = new Vehicle();
			vehicle.setColor("Blue");
			vehicle.setModel("S");
			VehicleType type = carTypeRepository.findByType("SEADEN");
			vehicle.setType(type);
			vehicle.setPlateNo("XY BHS");
			vehicle.setMaker("Tesla");
			vehicle.setYear(2019);
			vehicleService.saveVehicle(vehicle);
		}
	}
}
