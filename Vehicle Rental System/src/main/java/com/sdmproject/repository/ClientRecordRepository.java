package com.sdmproject.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.orm.Table;

@Repository
public class ClientRecordRepository {
	Logger logger = LoggerFactory.getLogger(ClientRecordRepository.class);

	public Table<ClientRecord, Integer> DAO;

	public ClientRecordRepository() {
		this.DAO = new Table<ClientRecord, Integer>(ClientRecord.class);
	}

	public int getCountOfVehicles() {
		return DAO.queryForTotalCount();
	}

	public boolean isClientExist(String driverLicienceNo) {
		return DAO.countForParam(new String[] { "driverLicienceNo" }, new Object[] { driverLicienceNo }) > 0;
	}

	public ClientRecord findByLicienceNo(String driverLicienceNo) {
		List<ClientRecord> result = DAO.queryForParamsEquality(new String[] { "driverLicienceNo" },
				new Object[] { driverLicienceNo });
		return result.get(0);
	}

	public void save(ClientRecord record) throws DuplicateEntryException {
		if (isClientExist(record.getDriverLicienceNo())) {
			throw new DuplicateEntryException("The Client with same licience number already exist.", null);
		}
		System.out.println(record.getExpiryDate());
		DAO.create(record);
	}

	public ClientRecord update(ClientRecord record) throws DuplicateEntryException, SQLException {
		logger.trace("Updated rows for saving user -----");
		DAO.update(record);
		return record;
	}

	public void deleteClientRecordByID(int id) {
		DAO.deleteById(id);
	}

	public ClientRecord findById(int id) {
		return DAO.queryById(id);
	}

	public List<ClientRecord> findAll() {
		return DAO.queryForAll();
	}

	public List<ClientRecord> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<ClientRecord> result;
		if (sortProperty.isPresent()) {
			result = DAO.queryForAllWithSort(sortProperty.get(), sortOrder.get());
		} else {
			result = DAO.queryForAll();
		}
		System.out.println(result);
		return result;
	}

	public List<Integer> findIDWithSort(String sortProperty, String sortOrder) {
		List<Integer> result = DAO.queryForIdWithSort(sortProperty, sortOrder);
		System.out.println(result);
		return result;
	}
}
