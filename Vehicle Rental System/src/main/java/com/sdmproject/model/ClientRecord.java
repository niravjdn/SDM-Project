package com.sdmproject.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.sdmproject.model.Role.RoleBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRecord {
	
	private int id;
	private String firstName;
	private String lastName;
	private String driverLicienceNo;
	private String phoneNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expiryDate;
	
}
