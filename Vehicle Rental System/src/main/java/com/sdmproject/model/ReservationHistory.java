package com.sdmproject.model;

import java.util.Date;

import com.sdmproject.orm.DatabaseField;
import com.sdmproject.orm.DatabaseTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable("reservation_history")
public class ReservationHistory {

	@DatabaseField(id = true)
	private int id;

	// 0 for return 1 for canclled
	
	@DatabaseField
	private Status status;
	

	@DatabaseField
	private String firstName;

	@DatabaseField
	private String lastName;

	@DatabaseField
	private String driverLicienceNo;

	@DatabaseField(dateFormat = "yyyy-MM-dd")
	private Date expiryDate;

	@DatabaseField
	private String phoneNo;

	@DatabaseField
	private String color;
	@DatabaseField
	private String plateNo;

	@DatabaseField
	private String make;

	@DatabaseField
	private String model;

	@DatabaseField
	private int year;

	@DatabaseField(dateFormat = "yyyy-MM-dd hh:mm")
	private Date fromDateTime;

	@DatabaseField(dateFormat = "yyyy-MM-dd hh:mm")
	private Date toDateTime;

	@DatabaseField(dateFormat = "yyyy-MM-dd hh:mm")
	private Date updatedOn;
}
