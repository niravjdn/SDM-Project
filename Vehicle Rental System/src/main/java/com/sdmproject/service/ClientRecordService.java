package com.sdmproject.service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.helper.QueryBuilder;
import com.sdmproject.model.Vehicle;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.repository.VehicleRepository;
import com.sdmproject.repository.ClientRecordRepository;
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

@Service("clientRecordService")
public class ClientRecordService {

	@Autowired
	private ClientRecordRepository clientRecordRepository;

	public ClientRecord findUserByLicienceNo(String licience_no) {
		return clientRecordRepository.findByLicienceNo(licience_no);
	}

	public boolean isClientExist(String licience_no) {
		return clientRecordRepository.isClientExist(licience_no);
	}

	public void save(ClientRecord clientRecord) throws DuplicateEntryException {
		clientRecordRepository.save(clientRecord);
	}

	public ClientRecord update(ClientRecord clientRecord) throws DuplicateEntryException, SQLException {
		return clientRecordRepository.update(clientRecord);
	}

	public void deleteClientByID(int id) {
		clientRecordRepository.deleteClientRecordByID(id);
	}

	public ClientRecord findByID(int id) {
		return clientRecordRepository.findById(id);
	}

	public List<ClientRecord> findAll() {
		return clientRecordRepository.findAll();
	}
	
	public List<ClientRecord> findAllWithSort(Optional<String> sort, Optional<String> order) {
		return clientRecordRepository.findAllWithSort(sort, order);
	}
	
	public List<Integer> findIDWithSort(String sortProperty, String sortOrder) {
		return clientRecordRepository.findIDWithSort(sortProperty, sortOrder);
	}
}