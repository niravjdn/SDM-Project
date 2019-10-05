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
public class Reservation {
	private int id;
	
	private Vehicle vehicle;
	
	private ClientRecord client;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
	private Date fromDateTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
	private Date toDateTime;
	
	private Date createdOn;
	
}
