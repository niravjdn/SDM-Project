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
@DatabaseTable("role")
public class Role {
	
	@DatabaseField(id = true)
    private int id;
    
    @DatabaseField
    private String role;
}
