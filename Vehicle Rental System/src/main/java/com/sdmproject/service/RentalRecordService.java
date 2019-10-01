package com.sdmproject.service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.helper.QueryBuilder;
import com.sdmproject.model.Vehicle;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.RentalRecord;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.repository.VehicleRepository;
import com.sdmproject.repository.ClientRecordRepository;
import com.sdmproject.repository.RentalRecordRepository;
import com.sdmproject.repository.RoleRepository;
import com.sdmproject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service("rentalRecordService")
public class RentalRecordService {

	@Autowired
	private RentalRecordRepository rentalRecordRepository;


	public boolean isClientExist(String licience_no) {
		return rentalRecordRepository.isClientExist(licience_no);
	}

	public RentalRecord save(RentalRecord rentalRecord) throws DuplicateEntryException {
		return rentalRecordRepository.save(rentalRecord);
	}

	public void deleteRentalByID(int id) {
		rentalRecordRepository.deleteRentalRecordByID(id);
	}
	
	public List<RentalRecord> findAllWithSort(Optional<String> sort, Optional<String> order) {
		return rentalRecordRepository.findAllWithSort(sort, order);
	}
	

	public List<RentalRecord> findAll() {
		return rentalRecordRepository.findAll();
	}
}