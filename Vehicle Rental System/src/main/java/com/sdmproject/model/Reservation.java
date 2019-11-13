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
@DatabaseTable("reservation")
public class Reservation {
	
	@DatabaseField(id = true, ai = true)
	private int id;
	
	@DatabaseField(otherClassReference = true, columnName = "vehicleID")
	private Vehicle vehicle;
	
	@DatabaseField(otherClassReference = true, columnName = "clientId")
	private ClientRecord client;
	
	@DatabaseField(dateFormat = "yyyy-MM-dd hh:mm")
	private Date fromDateTime;
	
	@DatabaseField(dateFormat = "yyyy-MM-dd hh:mm")
	private Date toDateTime;
	
	@DatabaseField
	private TypeOfReservation typeOfReservation;
	
	@DatabaseField(dateFormat = "yyyy-MM-dd hh:mm")
	private Date createdOn;
	
	@DatabaseField
	private int version;
}
