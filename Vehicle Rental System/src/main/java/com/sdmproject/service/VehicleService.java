package com.sdmproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.Vehicle;
import com.sdmproject.repository.VehicleRepository;

@Service("vehicleService")
public class VehicleService {

	@Autowired
    private VehicleRepository carRepository;

    public Vehicle findUserByPlateNo(String plateNo) {
        return carRepository.findByPlateNo(plateNo);
    }
    
    public boolean isVehicleExist(String plateNo) {
        return carRepository.isVehicleExist(plateNo);
    }

    public Vehicle saveVehicle(Vehicle vehicle) throws DuplicateEntryException {
        return carRepository.save(vehicle);
    }
    
    public List<Vehicle> findAllWithSort(Optional<String> sort, Optional<String> order) {
		return carRepository.findAllWithSort(sort, order);
	}
    
    public void deleteVehicleByID(int id) {
    	carRepository.deleteVehicleByID(id);
    }
}