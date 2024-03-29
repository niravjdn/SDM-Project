package com.sdmproject.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.TypeOfReservation;
import com.sdmproject.model.Vehicle;
import com.sdmproject.orm.Table;

@Repository
public class ReservationRepository {
	Logger logger = LoggerFactory.getLogger(ReservationRepository.class);

	Table<Reservation, Integer> DAO;

	public ReservationRepository() {
		this.DAO = new Table<Reservation, Integer>(Reservation.class);
	}

	public void save(Reservation record) {
		DAO.create(record);
	}

	public void deleteReservationByID(int id) {
		DAO.deleteById(id);
	}

	public void returnReservationByID(int id) {
		DAO.deleteById(id);
	}

	public Reservation findByID(int id) {
		return DAO.queryById(id);
	}

	public List<Reservation> findReservationWithDateRange(int vehicleId, Date fromDate, Date toDate) {
		return DAO.queryForParamsForDifferentOperations(new String[] { "DATE(fromDateTime)", "DATE(toDateTime)", "vehicleId" },
				new String[] { "<=", ">=" , "="}, new Object[] { new SimpleDateFormat("yyyy-MM-dd hh:mm").format(toDate),
						new SimpleDateFormat("yyyy-MM-dd hh:mm").format(fromDate), vehicleId });
	}

	public List<Reservation> findAllRental(String sortProperty, String sortOrder) {
		return DAO.queryForParamsForDifferentOperationsWithSort(new String[] { "typeOfReservation" },
				new String[] { "="}, new String[] { "RENTAL"}, sortProperty, sortOrder.equals("desc"));
	}

	public List<Reservation> findAllRentalsOnDueDate(Date dueDate) {
		return DAO.queryForParamsForDifferentOperations(new String[] { "DATE(toDateTime)", "typeOfReservation" },
				new String[] { "=", "=" },
				new String[] { new SimpleDateFormat("yyyy-MM-dd").format(dueDate), "RENTAL" });
	}

	public List<Reservation> findAllReservations(String sortProp, String order) {
		return DAO.queryForParamsForDifferentOperations(new String[] { "typeOfReservation" },
				new String[] { "="},
				new String[] { "RESERVATION" });
	}

	public List<Reservation> findAllReservationOnDueDate(Date dueDate) {
		return DAO.queryForParamsForDifferentOperations(new String[] { "DATE(toDateTime)", "typeOfReservation" },
				new String[] { "=", "=" },
				new String[] { new SimpleDateFormat("yyyy-MM-dd").format(dueDate), "RESERVATION" });
	}

	public List<Reservation> findAllRentalWithVehicle(String sortProperty, String sortOrder, int id) {
		return DAO.queryForParamsForDifferentOperationsWithSort(new String[] { "typeOfReservation", "vehicleID" },
				new String[] { "=", "="}, new Object[] { "RENTAL", id}, sortProperty, sortOrder.equals("desc"));
	}

	public List<Reservation> findAllWithVehiclID(int vehicleId) {
		return DAO.queryForParamsForDifferentOperations(new String[] { "vehicleId" },
				new String[] { "="}, new Object[] {vehicleId });
	}
}
