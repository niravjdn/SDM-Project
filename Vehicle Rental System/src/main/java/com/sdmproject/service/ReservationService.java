package com.sdmproject.service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.helper.QueryBuilder;
import com.sdmproject.model.Vehicle;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.repository.VehicleRepository;
import com.sdmproject.repository.ClientRecordRepository;
import com.sdmproject.repository.ReservationRepository;
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

@Service("reservationService")
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	public Reservation save(Reservation reservationRecord) {
		return reservationRepository.save(reservationRecord);
	}

	public void deleteReservationByID(int id) {
		reservationRepository.deleteReservationByID(id);
	}
	
	public void returnReservationByID(int id) {
		reservationRepository.returnReservationByID(id);
	}
	
	
	public List<Reservation> findAllWithSort(Optional<String> sort, Optional<String> order) {
		return reservationRepository.findAllWithSort(sort, order);
	}
	
	public List<Reservation> findAllOutReservationSort(Optional<String> sort, Optional<String> order) {
		return reservationRepository.findAllOutReservationSort(sort, order);
	}
	

	public List<Reservation> findAll() {
		return reservationRepository.findAll();
	}

	public Reservation findByID(int id) {
		return reservationRepository.findByID(id);
	}
}