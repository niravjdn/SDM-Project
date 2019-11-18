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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service("reservationService")
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	public void save(Reservation reservationRecord) {
		reservationRepository.save(reservationRecord);
	}

	public void deleteReservationByID(int id) {
		reservationRepository.deleteReservationByID(id);
	}

	public void returnReservationByID(int id) {
		reservationRepository.returnReservationByID(id);
	}

	public List<Reservation> findReservationWithDateRange(int vehicleId, Date fromDate, Date toDate) {
		return reservationRepository.findReservationWithDateRange(vehicleId, fromDate, toDate);
	}

	public Reservation findByID(int id) {
		return reservationRepository.findByID(id);
	}

	public List<Reservation> findAllRentals(Optional<String> sort, Optional<String> order) {
		return reservationRepository.findAllRental(sort.orElse(""), order.orElse("asc"));
	}

	public List<Reservation> findAllRentalsWithVehicle(Optional<String> sort, Optional<String> order, Vehicle vehicle) {
		return reservationRepository.findAllRentalWithVehicle(sort.orElse(""), order.orElse("asc"), vehicle.getId());
	}
	
	public List<Reservation> findAllRentalsOnDueDate(Date dueDate) {
		return reservationRepository.findAllRentalsOnDueDate(dueDate);
	}

	public List<Reservation> findAllReservations(Optional<String> sort, Optional<String> order) {
		return reservationRepository.findAllReservations(sort.orElse(""), order.orElse("asc"));
	}

	public List<Reservation> findAllResvationOnDueDate(Date dueDate) {
		return reservationRepository.findAllReservationOnDueDate(dueDate);
	}

	public List<Reservation> findAllWithVehiclID(int vehicleId) {
		return reservationRepository.findAllWithVehiclID(vehicleId);
	}
}