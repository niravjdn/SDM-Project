package com.sdmproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ReservationHistory;
import com.sdmproject.model.Vehicle;
import com.sdmproject.repository.ReservationHistoryRepository;

@Service("reservationHistoryService")
public class ReservationHistoryService {

	@Autowired
	private ReservationHistoryRepository reservationHistoryRepository;

	public void save(ReservationHistory reservationRecord)  {
		reservationHistoryRepository.save(reservationRecord);
	}

	public List<ReservationHistory> findAllWithSort(Optional<String> sort, Optional<String> order) {
		return reservationHistoryRepository.findAllWithSort(sort, order);
	}
	
	public List<ReservationHistory> filterMultipleAttribute(ArrayList<String> param, ArrayList<String> operator, ArrayList<Object> values,
			Optional<String> sortProperty, Optional<String> order) {
		String prop = "";
		boolean isDesc = false;
		if (sortProperty.isPresent()) {
			prop = sortProperty.get();
		}
		if (order.isPresent()) {
			isDesc = order.get().equals("desc");
		}
		return reservationHistoryRepository.filterMultipleAttribute(param.toArray(new String[param.size()]), operator.toArray(new String[operator.size()]),
				values.toArray(new Object[values.size()]), prop, isDesc);
	}
	public List<ReservationHistory> findAll() {
		return reservationHistoryRepository.findAll();
	}
}