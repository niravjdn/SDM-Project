package com.sdmproject;

import java.text.ParseException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class VehicleRentingApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentingApplication.class, args);
	}

	@Override
	public void run(String... args) throws ParseException {
	}
}
