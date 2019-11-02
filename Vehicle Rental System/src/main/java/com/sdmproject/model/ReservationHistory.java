package com.sdmproject.model;

import java.util.Date;

import com.sdmproject.model.Reservation.ReservationBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationHistory {

	private int id;
	
	//0 for return 1 for canclled
	private int typeOfReservation;
	
	private String firstName;

	private String lastName;

	private String driverLicienceNo;

	private Date expiryDate;

	private String phoneNo;

	private String color;

	private String plateNo;

	private String make;

	private String model;

	private int year;

	private String returnedBy;

	private Date fromDateTime;

	private Date toDateTime;

	private Date updatedDateTime;
}
