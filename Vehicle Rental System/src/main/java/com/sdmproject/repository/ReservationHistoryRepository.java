package com.sdmproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.sdmproject.model.ReservationHistory;
import com.sdmproject.orm.Table;

@Repository
public class ReservationHistoryRepository {
	
	Table<ReservationHistory, Integer> DAO;

	public ReservationHistoryRepository() {
		this.DAO = new Table<ReservationHistory, Integer>(ReservationHistory.class);
	}

	public void save(ReservationHistory record) {
		DAO.create(record);
	}

	public List<ReservationHistory> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<ReservationHistory> result;
		if (sortProperty.isPresent()) {
			result = DAO.queryForAllWithSort(sortProperty.get(), sortOrder.get());
		} else {
			result = DAO.queryForAll();
		}
		System.out.println(result);
		return result;
	}

	public List<ReservationHistory> filterMultipleAttribute(String[] param, String[] operator, Object[] values,
			String sortProperty, boolean order) {
		return DAO.queryForParamsForDifferentOperationsWithSort(param, operator, values, sortProperty, order);
	}

	public List<ReservationHistory> findAll() {
		return DAO.queryForAll();
	}
}
