package com.sdmproject.model;

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
@DatabaseTable("vehicle")
public class Vehicle {

	@DatabaseField(id = true, ai = true)
	int id;

	@DatabaseField
	TypeOfCar type;
	
	@DatabaseField
	String make;

	@DatabaseField
	String model;

	@DatabaseField
	int year;

	@DatabaseField
	String color;

	@DatabaseField
	String plateNo;	
	
	@Override
    public boolean equals(Object obj) 
    {
		return this.plateNo.equals(((Vehicle)obj).getPlateNo());
    }
}
