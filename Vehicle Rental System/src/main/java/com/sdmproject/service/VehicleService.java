package com.sdmproject.service;

import com.sdmproject.model.Vehicle;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.repository.VehicleRepository;
import com.sdmproject.repository.RoleRepository;
import com.sdmproject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

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

    public Vehicle saveVehicle(Vehicle vehicle) {
        return carRepository.save(vehicle);
    }
    
    public void deleteVehicleByID(int id) {
    	carRepository.deleteVehicleByID(id);
    }
}