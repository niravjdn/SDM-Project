package com.sdmproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ReservationHistory;
import com.sdmproject.repository.ReservationHistoryRepository;

@Service("reservationHistoryService")
public class ReservationHistoryService {

	@Autowired
	private ReservationHistoryRepository reservationHistoryRepository;

	public ReservationHistory save(ReservationHistory reservationRecord)  {
		return reservationHistoryRepository.save(reservationRecord);
	}

	public List<ReservationHistory> findAllWithSort(Optional<String> sort, Optional<String> order) {
		return reservationHistoryRepository.findAllWithSort(sort, order);
	}

	public List<ReservationHistory> findAll() {
		return reservationHistoryRepository.findAll();
	}
}