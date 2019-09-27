package com.sdmproject.model;

import com.sdmproject.model.Role.RoleBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleType {
	private int id;
	private String type;
}
