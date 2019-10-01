package com.sdmproject.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalRecord {
	private int id;
	private String clientName;
	private String driverLicienceNo;
	private String vehicleName;
	private String plateNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date bookingDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;

	public boolean duplicateClient(Object obj) 
    {
		return this.driverLicienceNo.equals(((RentalRecord)obj).getDriverLicienceNo());
    }
	
	public boolean duplicateVehicle(Object obj) 
    {
		return this.plateNo.equals(((RentalRecord)obj).getDriverLicienceNo());
    }
}
