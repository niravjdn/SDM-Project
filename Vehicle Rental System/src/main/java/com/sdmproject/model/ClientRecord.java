package com.sdmproject.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@DatabaseTable("client_record")
public class ClientRecord {
	
	@DatabaseField(id = true, ai = true)
	private int id;
	
	@DatabaseField
	private String firstName;
	
	@DatabaseField
	private String lastName;
	
	@DatabaseField
	private String driverLicienceNo;
	
	@DatabaseField
	private String phoneNo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@DatabaseField(dateFormat = "yyyy-MM-dd")
	private Date expiryDate;
	
	@DatabaseField
	int version;	

	@Override
    public boolean equals(Object obj) 
    {
		return this.driverLicienceNo.equals(((ClientRecord)obj).getDriverLicienceNo());
    }
}
