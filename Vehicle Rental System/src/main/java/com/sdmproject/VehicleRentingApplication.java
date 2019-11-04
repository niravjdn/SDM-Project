package com.sdmproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.ReservationHistory;
import com.sdmproject.model.Role;
import com.sdmproject.model.TypeOfCar;
import com.sdmproject.model.User;
import com.sdmproject.model.Vehicle;
import com.sdmproject.repository.ClientRecordRepository;
import com.sdmproject.repository.ReservationRepository;
import com.sdmproject.repository.ReservationHistoryRepository;
import com.sdmproject.repository.RoleRepository;
import com.sdmproject.service.UserService;
import com.sdmproject.service.VehicleService;

@SpringBootApplication
public class VehicleRentingApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ClientRecordRepository clientRecordRepository;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private ReservationRepository reservationService;

	@Autowired
	private ReservationHistoryRepository reservationHistoryService;

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentingApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(VehicleRentingApplication.class);
	}

	@Override
	public void run(String... args) throws ParseException, DuplicateEntryException {

		Role roleClerk = new Role(1, "CLERK");
		roleRepository.save(roleClerk);
		Role roleAdmin = new Role(2, "ADMIN");
		roleRepository.save(roleAdmin);

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
	}
}
