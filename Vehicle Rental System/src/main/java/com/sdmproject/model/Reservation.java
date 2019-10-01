package com.sdmproject.model;

import java.util.Date;

import com.sdmproject.model.Role.RoleBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
	private int id;
	
	private int carId;
	
	private int clientId;
	
	private Date fromDateTime;
	
	private Date toDateTime;
	
	private String createdBy;
	
}
