package com.sdmproject.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.exceptions.ValidationException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Vehicle;
import com.sdmproject.repository.VehicleRepository;

@Service("vehicleService")
public class VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	public Vehicle findUserByPlateNo(String plateNo) {
		return vehicleRepository.findByPlateNo(plateNo);
	}

	public boolean isVehicleExist(String plateNo) {
		return vehicleRepository.isVehicleExist(plateNo);
	}

	public List<Vehicle> findAllWithSort(Optional<String> sort, Optional<String> order) {
		return vehicleRepository.findAllWithSort(sort, order);
	}

	public void deleteVehicleByID(int id) {
		vehicleRepository.deleteVehicleByID(id);
	}

	public Vehicle findByID(int id) {
		return vehicleRepository.findById(id);
	}

	public List<Integer> findIDWithSort(String sortProperty, String sortOrder) {
		return vehicleRepository.findIDWithSort(sortProperty, sortOrder);
	}

	public List<Vehicle> findAll() {
		return vehicleRepository.findAll();
	}

	public void save(Vehicle vehicle) throws DuplicateEntryException, ValidationException {
		vehicleRepository.save(vehicle);
	}

	public void update(Vehicle vehicle) throws SQLException {
		vehicleRepository.update(vehicle);
	}

	public void deleteById(int id) {
		vehicleRepository.deleteVehicleByID(id);
	}

	public List<Vehicle> filterMultipleAttribute(ArrayList<String> param, ArrayList<String> operator, ArrayList<Object> values,
			Optional<String> sortProperty, Optional<String> order) {
		String prop = "";
		boolean isDesc = false;
		if (sortProperty.isPresent()) {
			prop = sortProperty.get();
		}
		if (order.isPresent()) {
			isDesc = order.get().equals("desc");
		}
		return vehicleRepository.filterMultipleAttribute(param.toArray(new String[param.size()]), operator.toArray(new String[operator.size()]),
				values.toArray(new Object[values.size()]), prop, isDesc);
	}
	
	public List<Integer> filterMultipleAttributeAndGetId(ArrayList<String> param, ArrayList<String> operator, ArrayList<Object> values,
			Optional<String> sortProperty, Optional<String> order) {
		String prop = "";
		boolean isDesc = false;
		if (sortProperty.isPresent()) {
			prop = sortProperty.get();
		}
		if (order.isPresent()) {
			isDesc = order.get().equals("desc");
		}

		return vehicleRepository.queryIDParamsForDifferentOperationsWithSort(param.toArray(new String[param.size()]), operator.toArray(new String[operator.size()]),
				values.toArray(new Object[values.size()]), prop, isDesc);
	}

}