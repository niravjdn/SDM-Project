package com.sdmproject;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.repository.ReservationHistoryRepository;

@SpringBootApplication
public class VehicleRentingApplication extends SpringBootServletInitializer implements CommandLineRunner {


	@Bean
	public HttpSessionEventPublisher sessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(VehicleRentingApplication.class, args);
		
		Runtime.getRuntime().addShutdownHook(new Thread() 
	    { 
	      public void run() 
	      { 
	        System.err.println("Shutdown Hook is running !"); 
	      } 
	    }); 
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(VehicleRentingApplication.class);
	}

	@Override
	public void run(String... args) throws ParseException, DuplicateEntryException {
		
	}
}
