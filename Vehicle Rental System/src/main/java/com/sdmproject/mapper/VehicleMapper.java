package com.sdmproject.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sdmproject.model.Vehicle;
import com.sdmproject.orm.Table;

public class VehicleMapper {
	private Table<Vehicle, Integer> DAO;
	private List<Vehicle> vRec;

	public VehicleMapper() {
		this.DAO = new Table<Vehicle, Integer>(Vehicle.class);
		this.vRec = new ArrayList<Vehicle>();
	}
	
	
	public Vehicle findByPlateNo(String plateNO) {
		Vehicle vehicleRecord;
		if (!vRec.isEmpty()) {
			for (Vehicle cr : this.vRec) {
				if (cr.getPlateNo() == plateNO)
					return cr;
			}
		}
		vehicleRecord = DAO.queryForParamsEquality(new String[] {"plateNO"}, new Object[] {plateNO}).get(0);
		vRec.add(vehicleRecord);
		return vehicleRecord;
	}
	
	public Vehicle findById(int id) {
		Vehicle vehicleRecord;
		if (!vRec.isEmpty()) {
			for (Vehicle cr : this.vRec) {
				if (cr.getId() == id)
					return cr;
			}
		}
		vehicleRecord = DAO.queryById(id);
		vRec.add(vehicleRecord);
		return vehicleRecord;
	}
	
	public List<Vehicle> findAll() {
		if (!vRec.isEmpty()) {
			return vRec;
		}
		vRec = DAO.queryForAll();
		return vRec;
	}

}
