package com.sdmproject.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Vehicle;
import com.sdmproject.orm.Table;

@Repository
public class VehicleRepository {

	public Table<Vehicle, Integer> DAO;

	public VehicleRepository() {
		this.DAO = new Table<Vehicle, Integer>(Vehicle.class);
	}

	Logger logger = LoggerFactory.getLogger(VehicleRepository.class);

	public int getCountOfVehicles() {
		return DAO.queryForTotalCount();
	}

	public boolean isVehicleExist(String plateNo) {
		return DAO.countForParam(new String[] {"plateNo"}, new Object[] {plateNo}) > 0;	}

	public Vehicle findByPlateNo(String plateNO) {
		List<Vehicle> result = DAO.queryForParamsEquality(new String[] {"plateNO"}, new Object[] {plateNO});
		return result.get(0);
	}

	public void save(Vehicle vehicle) throws DuplicateEntryException {
		if (isVehicleExist(vehicle.getPlateNo())) {
			throw new DuplicateEntryException("The Vehicle with same licience number already exist.", null);
		}
		DAO.create(vehicle);
	}

	public void deleteVehicleByID(int id) {
		DAO.deleteById(id);
	}

	public List<Vehicle> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<Vehicle> result;
		if (sortProperty.isPresent()) {
			result = DAO.queryForAllWithSort(sortProperty.get(), sortOrder.get());
		}else {
			result = DAO.queryForAll();
		}
		System.out.println(result);
		return result;
	}

	public Vehicle findById(int id) {
		return DAO.queryById(id);
	}

	public List<Integer> findIDWithSort(String sortProperty, String sortOrder) {
		List<Integer> result = DAO.queryForIdWithSort(sortProperty, sortOrder);
		System.out.println(result);
		return result;
	}

	public List<Vehicle> findAll() {
		return DAO.queryForAll();
	}

	public Vehicle update(Vehicle record) throws SQLException {
		logger.trace("Updated rows for saving user -----");
		DAO.update(record);
		return record;
	}

	public List<Vehicle> filterMultipleAttribute(String[] param, String[] operator, Object[] values,
			String sortProperty, boolean order) {
		return DAO.queryForParamsForDifferentOperationsWithSort(param, operator, values, sortProperty, order);
	}

	public List<Integer> queryIDParamsForDifferentOperationsWithSort(String[] param, String[] operator, Object[] values,
			String sortProperty, boolean isDesc) {
		return DAO.queryIDParamsForDifferentOperationsWithSort(param, operator, values, sortProperty, isDesc);
	}

}
