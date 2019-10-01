package com.sdmproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

	private int id;

	private String type;

	private String maker;

	private String model;

	private int year;

	private String color;

	private String plateNo;

	@Override
	public boolean equals(Object obj) {
		return this.plateNo.equals(((Vehicle) obj).getPlateNo());
	}
}
